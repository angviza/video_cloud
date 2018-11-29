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
import com.hdvon.sip.exception.SipSystemException;
import com.hdvon.sip.utils.CommonUtil;
import com.hdvon.sip.utils.SipConstants;
import com.hdvon.sip.vo.PresetQuery;
import com.hdvon.sip.vo.PresetVo;
/**
 * 预置位控制
 * @author wanshaojian
 *
 */
public class PresetClient implements SipListener,AutoCloseable{
	public Logger logger = LoggerFactory.getLogger(getClass());

	
	/**
	 * 保存当前请求call-ID与MediaPlayVo对象关系
	 */
	private ConcurrentHashMap<String, PresetVo> presetMap = new ConcurrentHashMap<>();
	
	private volatile static PresetClient instance;
	
	private SipConfig sipConfig;
	
	private SipProvider sipProvider;

	private AddressFactory addressFactory;

	private MessageFactory messageFactory;

	private HeaderFactory headerFactory;
	
	private AtomicLong cSeqCounter = new AtomicLong(1);
	
	
	private PresetClient(SipConfig sipConfig) {
		this.sipConfig = sipConfig;
	}

	public static PresetClient getInstance(SipConfig sipConfig){
        if(instance==null){
            synchronized (PresetClient.class){
                if(instance==null){
                    instance=new PresetClient(sipConfig);
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
	 * 云台控制处理
	 * 
	 * @param model
	 * 			 云台控制对象
	 * @param registerCode
	 *          注册用户对象
	 * @return
	 * @throws SipSystemException
	 */
	public PresetVo preset(PresetQuery model,String registerCode) throws SipSystemException {
		String ptzCmd = MediaMsgUtil.genPresetPTZCmd(model.getTypeEnum(), model.getPresetNum());

		return presetProcess(model, registerCode, ptzCmd);
	}

	
	/**
	 * 媒体资源处理
	 * 
	 * @param model
	 * 			 请求对象
	 * @param registerCode
	 *          注册用户对象
	 * @param ptz
	 * 		    cmd云台控制指令       
	 * @return
	 * @throws SipSystemException
	 */
	private PresetVo presetProcess(PresetQuery model,String registerCode,String ptzCmd) throws SipSystemException {
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

			//媒体流发送者设备编码
			String deviceCode = model.getDeviceCode();
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
			
			
			String sdpData = MediaMsgUtil.cloudControlMsg(invco,ptzCmd, deviceCode);
			byte[] contents = sdpData.getBytes();
			// 添加消息的内容
			ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("Application",
					"MANSCDP+xml");

			request.setContent(contents, contentTypeHeader);
			
			String callId = callIdHeader.getCallId();
			PresetVo vo = new PresetVo();
			BeanUtils.copyProperties(model, vo);
			vo.setType(model.getTypeEnum().getKey());
			vo.setCallId(callId);
			
	        //将异步请求转换为同步请求
			CountDownLatch latch = new CountDownLatch(1);
			vo.setLatch(latch);
			
			presetMap.put(callId, vo);
			
			//发送消息
			ClientTransaction inviteTid = sipProvider.getNewClientTransaction(request);
			inviteTid.sendRequest();
			
			//阻塞方法
			try {
				latch.await(); //阻塞等待计数为0
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			PresetVo data = presetMap.get(callId);
			
            //清除map数据
			presetMap.remove(callId);
			//回收端口
			PortPoolManager.getInstance().destroy(clientPort);
			
			return data;
		} catch (Exception e) {
			// TODO: handle exception
			throw new SipSystemException(e.getMessage());
		} 

		
	}

	@Override
	public void processResponse(ResponseEvent responseEvent) {
		// TODO Auto-generated method stub
		logger.info("Got a response");
		Response response = (Response) responseEvent.getResponse();
		ClientTransaction tid = responseEvent.getClientTransaction();
		CallIdHeader callIdHeader = (CallIdHeader) response.getHeader(CallIdHeader.NAME);

		if (tid == null) {
			logger.info("Stray response -- dropping ");
			return;
		}
		String callId = callIdHeader.getCallId();
		PresetVo model = presetMap.get(callId);
		if(response.getStatusCode() == Response.OK) {
			model.setStatus(SipConstants.SUCCESS);
		}else {
			model.setStatus(SipConstants.FAIL);
		}
		model.getLatch().countDown(); //计数减1
		
		presetMap.put(callId, model);
	}

	@Override
	public void close() throws IOException, ObjectInUseException {
		// TODO Auto-generated method stub
		logger.info(">>>>>>>>>>>>>>>>>>>PresetClient关闭资源");
		
		//删除当前sip
		sipProvider.removeSipListener(this);
		SipStackManage.getSipStack().deleteSipProvider(sipProvider);
	}


	@Override
	public void processRequest(RequestEvent requestEvent) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void processTimeout(TimeoutEvent timeoutEvent) {
		// TODO Auto-generated method stub
		CallIdHeader callIdHeader = (CallIdHeader) timeoutEvent.getClientTransaction().getRequest().getHeader(CallIdHeader.NAME);
		String callId = callIdHeader.getCallId();
		PresetVo bean = presetMap.get(callId);
		bean.setStatus(SipConstants.TIMEOUT_FAIL);
		presetMap.put(callId, bean);
		
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
