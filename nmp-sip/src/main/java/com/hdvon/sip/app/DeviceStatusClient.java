package com.hdvon.sip.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import javax.sip.ClientTransaction;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.ListeningPoint;
import javax.sip.ObjectInUseException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.entity.DeviceStatusRecord;
import com.hdvon.sip.exception.SipSystemException;
import com.hdvon.sip.utils.CommonUtil;
import com.hdvon.sip.utils.SipConstants;
import com.hdvon.sip.utils.XmlUtil;
import com.hdvon.sip.vo.DeviceStatusVo;
/**
 * 设备状态查询
 *  流程：1：客户端向信令服务器发送message消息
 *      2：信令服务器回复message消息
 *      3：客户端发送200消息确认
 * @author wanshaojian
 *
 */
public class DeviceStatusClient implements SipListener,AutoCloseable{
	public Logger logger = LoggerFactory.getLogger(getClass());
	
	
	private static final String SPLIT_CHAR = "@";
	/** 
	  * 绑定callId与检测用户对应关系
	  */ 
	private ConcurrentHashMap<String, DeviceStatusVo> statusMap = new ConcurrentHashMap<>(); 
	
	/** 
	  * 绑定客户端回话与信令服务器端回话关系
	  */ 
	private ConcurrentHashMap<String, String> relationMap = new ConcurrentHashMap<>(); 
	
	private volatile static DeviceStatusClient instance;
	
	private SipConfig sipConfig;
	
	private SipProvider sipProvider;

	private AddressFactory addressFactory;

	private MessageFactory messageFactory;

	private HeaderFactory headerFactory;
	
	private AtomicLong cSeqCounter = new AtomicLong(1);
	
	
	private DeviceStatusClient(SipConfig sipConfig) {
		this.sipConfig = sipConfig;
	}

	public static DeviceStatusClient getInstance(SipConfig sipConfig){
        if(instance==null){
            synchronized (DeviceStatusClient.class){
                if(instance==null){
                    instance=new DeviceStatusClient(sipConfig);
                }
            }
        }
        return instance;
    }
	private void createSipProvider(int port) {

		try {
			String host = sipConfig.getClientIp();
			
            SipStack sipStack = SipStackManage.getSipStack();
            SipFactory sipFactory = SipStackManage.getSipFactory();
			
            ListeningPoint lp = sipStack.createListeningPoint(host, port, SipConstants.TRANSPORT_UDP);
            
            this.sipProvider = sipStack.createSipProvider(lp);
            headerFactory = sipFactory.createHeaderFactory();
            messageFactory = sipFactory.createMessageFactory();
            addressFactory = sipFactory.createAddressFactory();
            
            this.sipProvider.addSipListener(this);
             
		} catch (Exception e) {
			// could not find gov.nist.jain.protocol.ip.sip.SipStackImpl in the
			logger.error(e.getMessage());
			throw new SipSystemException(e);
		}
	}
	
	/**
	 * 设备状态查询
	 * @param deviceCode
	 * @return
	 * @throws SipSystemException
	 */
	public DeviceStatusVo searchDeviceStatus(String deviceCode,String registerCode) throws SipSystemException {
		try {
			
			// 获取端口
			int clientPort = PortPoolManager.getInstance().getPool();
			createSipProvider(clientPort);
			
			String domian = sipConfig.getDomain();

			//创建From 头
			SipURI from = addressFactory.createSipURI(registerCode, domian);

			Address fromNameAddress = addressFactory.createAddress(from);
			FromHeader fromHeader = headerFactory.createFromHeader(fromNameAddress,
					CommonUtil.getTags());

			//创建to 头
			SipURI toAddress = addressFactory.createSipURI(deviceCode, domian);
			Address toNameAddress = addressFactory.createAddress(toAddress);
			ToHeader toHeader = headerFactory.createToHeader(toNameAddress, null);

			//创建请求 URI
			String serverAddress = sipConfig.getServerIp() + ":" + sipConfig.getServerPort();
			SipURI requestURI = addressFactory.createSipURI(registerCode, serverAddress);
			
			//sip客户端的ip地址
			String clientIp = sipConfig.getClientIp();
			
			//创建Via 头数组
			ArrayList<ViaHeader> viaHeaders = new ArrayList<>();
			ViaHeader viaHeader = headerFactory.createViaHeader(clientIp,
					clientPort, SipConstants.TRANSPORT_UDP, null);
			viaHeaders.add(viaHeader);
            
            //创建Call-ID 头
			CallIdHeader callIdHeader = sipProvider.getNewCallId();
			
			Long invco = cSeqCounter.getAndIncrement();
			
			//创建CSeq 头
			CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(invco, Request.MESSAGE);
			//Max-forwards 头
			MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70);
			
			//实例化实际的 SIP 消息本身
			Request request = messageFactory.createRequest(requestURI, Request.MESSAGE, callIdHeader,
					cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
			
			
			String sdpData = getXmlQueryParam(invco, deviceCode);
			byte[] contents = sdpData.getBytes();
			// 添加消息的内容
			ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("Application",
					"MANSCDP+xml");

			request.setContent(contents, contentTypeHeader);
			
			DeviceStatusVo vo = new DeviceStatusVo();
			vo.setDeviceCode(deviceCode);
			
	        //将异步请求转换为同步请求
			CountDownLatch latch = new CountDownLatch(2);
			vo.setLatch(latch);
			
			String callId = callIdHeader.getCallId();
			statusMap.put(callId, vo);
			
			
	        String reqKey = deviceCode + SPLIT_CHAR + invco;
	        relationMap.put(reqKey, callId);
			
			//发送消息
			ClientTransaction inviteTid = sipProvider.getNewClientTransaction(request);
			inviteTid.sendRequest();
			
			//阻塞方法
			try {
				latch.await(); //阻塞等待计数为0
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			DeviceStatusVo dataRecord = statusMap.get(callId);
			
            //清除map数据
			statusMap.remove(callId);
			relationMap.remove(reqKey);
			
			//回收端口
			PortPoolManager.getInstance().destroy(clientPort);
			
			return dataRecord;
		} catch (Exception e) {
			// TODO: handle exception
			throw new SipSystemException(e.getMessage());
		}
	}
	
	/**
	 * 获取xml参数
	 * @param deviceCode
	 * @return
	 */
	private String getXmlQueryParam(Long sn,String deviceCode) {
		String contents = "<?xml version=\"1.0\"?>\r\n"+ 
				"<Query>\r\n" + 
				"<CmdType>DeviceStatus</CmdType>\r\n" + 
				"<SN>"+sn+"</SN>\r\n" + 
				"<DeviceID>"+deviceCode+"</DeviceID>\r\n" + 
				"</Query>";
		return contents;
	}
	
	@Override
	public void processRequest(RequestEvent requestEvent) {
		// TODO Auto-generated method stub
		Request request = requestEvent.getRequest();
		
		String xml = new String ((byte[]) request.getContent());
		DeviceStatusRecord recordData = XmlUtil.toBean(DeviceStatusRecord.class, xml);

		String reqKey = recordData.getDeviceCode() + SPLIT_CHAR + recordData.getSn();
		//获取会话id
		String callId = relationMap.get(reqKey);
		
		DeviceStatusVo record = statusMap.get(callId);
		
		DeviceStatusVo model = new DeviceStatusVo();
		BeanUtils.copyProperties(recordData, model);
		model.setDeviceDate(CommonUtil.convertDate(recordData.getDeviceTime()));
		model.setLatch(record.getLatch());
		
		statusMap.put(callId, model);
	    try {
	    	SipProvider sipProvider = (SipProvider) requestEvent.getSource();
	    	Response response = messageFactory.createResponse(Response.OK, request);
	        ServerTransaction serverTransactionId = requestEvent.getServerTransaction();
	        if(serverTransactionId == null) {
	        	serverTransactionId = sipProvider.getNewServerTransaction(request);
	        }
            serverTransactionId.sendResponse(response);
            
            record.getLatch().countDown(); //计数减1
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } 



		

	}
	@Override
	public void processResponse(ResponseEvent responseEvent) {
		// TODO Auto-generated method stub		
		ClientTransaction tid = responseEvent.getClientTransaction();
		Response resp = responseEvent.getResponse();
		CSeqHeader cseq = (CSeqHeader) resp.getHeader(CSeqHeader.NAME);

		logger.info("Response received : Status Code = " + resp.getStatusCode() + " " + cseq);
		if (tid == null) {
			logger.info("Stray response -- dropping ");
			return;
		}
		CallIdHeader callIdHeader = (CallIdHeader) resp.getHeader(CallIdHeader.NAME);
		String callId = callIdHeader.getCallId();

		DeviceStatusVo model = statusMap.get(callId);
		
		int checkStatus = SipConstants.FAIL;
		if (resp.getStatusCode() == Response.OK) {
			checkStatus = SipConstants.SUCCESS;
		}
		model.setRespStatus(checkStatus);
		model.getLatch().countDown(); //计数减1
		
		statusMap.put(callId, model);
		
		
	}


	@Override
	public void close() throws IOException, ObjectInUseException {
		// TODO Auto-generated method stub
		logger.info(">>>>>>>>>>>>>>>>>>>DeviceStatusClient关闭资源");
		
		//删除当前sip
		sipProvider.removeSipListener(this);
		SipStackManage.getSipStack().deleteSipProvider(sipProvider);
	}

	@Override
	public void processTimeout(TimeoutEvent event) {
		CallIdHeader callIdHeader = (CallIdHeader) event.getClientTransaction().getRequest().getHeader(CallIdHeader.NAME);
		String callId = callIdHeader.getCallId();
		DeviceStatusVo bean = statusMap.get(callId);
		bean.setRespStatus(SipConstants.TIMEOUT_FAIL);
		statusMap.put(callId, bean);
		
		bean.getLatch().countDown(); //计数减1
		bean.getLatch().countDown(); //计数减1
	}

	@Override
	public void processIOException(IOExceptionEvent exceptionEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
		// TODO Auto-generated method stub
		
	}
	

    
}
