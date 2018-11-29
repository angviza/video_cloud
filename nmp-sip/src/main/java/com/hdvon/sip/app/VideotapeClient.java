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
import com.hdvon.sip.entity.VideotapeRecord;
import com.hdvon.sip.exception.SipSystemException;
import com.hdvon.sip.utils.CommonUtil;
import com.hdvon.sip.utils.SipConstants;
import com.hdvon.sip.utils.XmlUtil;
import com.hdvon.sip.vo.VideotapeQuery;
import com.hdvon.sip.vo.VideotapeVo;
/**
 * 媒体录像处理
 * 	 包括 录像开始和暂停处理
 *   主要流程  1：客户端向信令服务器发送录像控制报文【message】
 *         2： 信令服务器向sip客户端发送message
 *         3：sip客户端向信令服务器发送200确认消息
 * @author wanshaojian
 *
 */
public class VideotapeClient implements SipListener,AutoCloseable{

	private Logger logger = LoggerFactory.getLogger(getClass());

	//录像返回结果
	private static final String SUCCESS_RESULT = "OK";
	
	private static final String SPLIT_CHAR = "@";
	/**
	 * 保存当前请求request与VideotapeVo对象关系
	 */
	private ConcurrentHashMap<String, VideotapeVo> tapeMap = new ConcurrentHashMap<>();
	
	/** 
	  * 绑定客户端回话与信令服务器端回话关系
	  */ 
	private ConcurrentHashMap<String, String> relationMap = new ConcurrentHashMap<>(); 
	
	private volatile static VideotapeClient instance;
	
	private SipConfig sipConfig;
	
	private SipProvider sipProvider;

	private AddressFactory addressFactory;

	private MessageFactory messageFactory;

	private HeaderFactory headerFactory;
	
	private AtomicLong cSeqCounter = new AtomicLong(1);
	
	
	private VideotapeClient(SipConfig sipConfig) {
		this.sipConfig = sipConfig;
	}

	public static VideotapeClient getInstance(SipConfig sipConfig){
        if(instance==null){
            synchronized (VideotapeClient.class){
                if(instance==null){
                    instance=new VideotapeClient(sipConfig);
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
	 * 开始录像请求
	 * 
	 * @param model
	 * 			录像对象
	 * @param registerCode
	 * 			注册用户
	 * @return
	 * @throws SipSystemException
	 */
	public VideotapeVo videotapeProcess(VideotapeQuery model ,String registerCode) throws SipSystemException {
		try {
			// 获取端口
			int clientPort = PortPoolManager.getInstance().getPool();
			createSipProvider(clientPort);
			
			String deviceCode = model.getDeviceCode();
			String storageServerCode= model.getStorageServerCode();
			String domian = sipConfig.getDomain();

			//创建From 头
			SipURI from = addressFactory.createSipURI(registerCode, domian);

			Address fromNameAddress = addressFactory.createAddress(from);
			FromHeader fromHeader = headerFactory.createFromHeader(fromNameAddress,
					CommonUtil.getTags());

			//创建to 头
			SipURI toAddress = addressFactory.createSipURI(storageServerCode, domian);
			Address toNameAddress = addressFactory.createAddress(toAddress);
			ToHeader toHeader = headerFactory.createToHeader(toNameAddress, null);

			//创建请求 URI
			String serverAddress = sipConfig.getServerIp() + ":" + sipConfig.getServerPort();
			
			SipURI requestURI = addressFactory.createSipURI(storageServerCode, serverAddress);
			
			//sip客户端的ip地址
			String clientIp = sipConfig.getClientIp();
			
			//创建Via 头数组
			ArrayList<ViaHeader> viaHeaders = new ArrayList<>();
			ViaHeader viaHeader = headerFactory.createViaHeader(clientIp,
					clientPort, SipConstants.TRANSPORT_UDP, null);
			viaHeaders.add(viaHeader);
            
			Long invo = cSeqCounter.getAndIncrement();
			
            //创建Call-ID 头
			CallIdHeader callIdHeader = sipProvider.getNewCallId();
			//创建CSeq 头
			CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(invo, Request.MESSAGE);
			//Max-forwards 头
			MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70);
			
			//实例化实际的 SIP 消息本身
			Request request = messageFactory.createRequest(requestURI, Request.MESSAGE, callIdHeader,
					cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
			
			
			String udpDate = MediaMsgUtil.startVideotapeMsg(invo, deviceCode);
			if(SipConstants.TAPE_TYPE_STOP == model.getCmdType()) {
				udpDate = MediaMsgUtil.stopVideotapeMsg(invo, deviceCode);
			}
			
			byte[] contents = udpDate.getBytes();
			// 添加消息的内容
			ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("Application",
					"MANSCDP+xml");

			request.setContent(contents, contentTypeHeader);
			

	        //保存事务CallId与注册用户对应关系
			String callId = callIdHeader.getCallId();
			VideotapeVo vo = new VideotapeVo();
			BeanUtils.copyProperties(model, vo);
			vo.setCallId(callId);
			CountDownLatch latch = new CountDownLatch(2);
			vo.setLatch(latch);
			
			tapeMap.put(callId,vo);
			
			String reqkey = deviceCode + SPLIT_CHAR + invo;
			relationMap.put(reqkey, callId);
			
			//发送消息
			ClientTransaction inviteTid = sipProvider.getNewClientTransaction(request);
			inviteTid.sendRequest();
			
			//阻塞方法
			try {
				latch.await(); //阻塞等待计数为0
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			VideotapeVo record = tapeMap.get(reqkey);
			
			//清除内存数据
			tapeMap.remove(reqkey);
			//回收端口
			PortPoolManager.getInstance().destroy(clientPort);
			
			return record;
		} catch (Exception e) {
			// TODO: handle exception
			throw new SipSystemException(e.getMessage());
		} 
		

		
	}
	


	
	@Override
	public void processRequest(RequestEvent requestEvent) {
		// TODO Auto-generated method stub
		Request request = requestEvent.getRequest();
		String xml = new String ((byte[]) request.getContent());
		//将xml转化为对象
		VideotapeRecord recordData = XmlUtil.toBean(VideotapeRecord.class, xml);
		//获取对应请求对象
		String reqKey = recordData.getDeviceCode() + SPLIT_CHAR + recordData.getSn();
		
		String callId = relationMap.get(reqKey);
		VideotapeVo model = tapeMap.get(callId);
		
		int status = SipConstants.FAIL;
		if(SUCCESS_RESULT.equals(recordData.getResult())) {
			status = SipConstants.SUCCESS;
		}
		model.setStatus(status);
		
		tapeMap.put(reqKey, model);
		try {
			//发送确认请求
	    	SipProvider sipProvider = (SipProvider) requestEvent.getSource();
	    	Response response = messageFactory.createResponse(Response.OK, request);
	        ServerTransaction serverTransactionId = requestEvent.getServerTransaction();
	        if(serverTransactionId == null) {
	        	serverTransactionId = sipProvider.getNewServerTransaction(request);
	        }
            serverTransactionId.sendResponse(response);
            
    		model.getLatch().countDown(); //计数减1
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } 
	}

	
	@Override
	public void processResponse(ResponseEvent responseEvent) {
		// TODO Auto-generated method stub
		Response resp = responseEvent.getResponse();
		ClientTransaction tid = responseEvent.getClientTransaction();
		if (tid == null) {
			logger.info("Stray response -- dropping ");
			return;
		}
		CallIdHeader callIdHeader = (CallIdHeader) resp.getHeader(CallIdHeader.NAME);
		String callId = callIdHeader.getCallId();

		VideotapeVo model = tapeMap.get(callId);
		
		int checkStatus = SipConstants.FAIL;
		if (resp.getStatusCode() == Response.OK) {
			checkStatus = SipConstants.SUCCESS;
		}
		model.setStatus(checkStatus);
		model.getLatch().countDown(); //计数减1
		
		tapeMap.put(callId, model);
	}
	@Override
	public void processTimeout(TimeoutEvent event) {
		CallIdHeader callIdHeader = (CallIdHeader) event.getClientTransaction().getRequest().getHeader(CallIdHeader.NAME);
		String callId = callIdHeader.getCallId();
		VideotapeVo model = tapeMap.get(callId);
		model.setStatus(SipConstants.TIMEOUT_FAIL);
		
		tapeMap.put(callId, model);
		
		model.getLatch().countDown(); //计数减1
		model.getLatch().countDown(); //计数减1
	}
	
	@Override
	public void close() throws IOException, ObjectInUseException {
		// TODO Auto-generated method stub
		logger.info(">>>>>>>>>>>>>>>>>>>VideotapeClient关闭注册资源");
		
		//删除当前sip
		sipProvider.removeSipListener(this);
		SipStackManage.getSipStack().deleteSipProvider(sipProvider);
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
