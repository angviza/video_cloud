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

import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.entity.HearbeatBean;
import com.hdvon.sip.exception.SipSystemException;
import com.hdvon.sip.utils.CommonUtil;
import com.hdvon.sip.utils.SipConstants;
/**
 * 用户注册处理类
 * 作用：注册用户向信令服务器发送心跳
 * 	        发送心跳失败，则自动注册改用户账号，
 * @author wanshaojian
 *
 */
public class HearbeatClient implements SipListener,AutoCloseable{
	public Logger logger = LoggerFactory.getLogger(getClass());
	
	
	 /** 
	  * 绑定请求call-ID与检测用户对应关系
	  */ 
	private ConcurrentHashMap<String, HearbeatBean> checkUserMap = new ConcurrentHashMap<>(); 
	
	private volatile static HearbeatClient instance;
	
	private SipConfig sipConfig;
	
	private SipProvider sipProvider;

	private AddressFactory addressFactory;

	private MessageFactory messageFactory;

	private HeaderFactory headerFactory;
	
	private AtomicLong cSeqCounter = new AtomicLong(1);
	
	private HearbeatClient(SipConfig sipConfig) {
		this.sipConfig = sipConfig;
	}
	
	public static HearbeatClient getInstance(SipConfig sipConfig){
        if(instance==null){
            synchronized (HearbeatClient.class){
                if(instance==null){
                    instance=new HearbeatClient(sipConfig);
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
	 * 发送用户心跳
	 * @param checkUserCode 检测用户编码
	 * @return
	 * @throws SipSystemException
	 */
	public void sendHeartbeat(HearbeatBean model) throws SipSystemException {
		try {
			// 获取端口
			int clientPort = PortPoolManager.getInstance().getPool();
			//创建监听
			createSipProvider(clientPort);
			
			String checkUserCode = model.getCheckUserCode();
			
			String tag = CommonUtil.getTags();
			String domain = sipConfig.getDomain();
			String clientIp = sipConfig.getClientIp();
	        
			SipURI from = addressFactory.createSipURI(checkUserCode, domain);
			Address fromNameAddress = addressFactory.createAddress(from);
			FromHeader fromHeader = headerFactory.createFromHeader(fromNameAddress, tag);

			SipURI toAddress = addressFactory.createSipURI(checkUserCode, domain);
			Address toNameAddress = addressFactory.createAddress(toAddress);
			ToHeader toHeader = headerFactory.createToHeader(toNameAddress, null);

			String serverAddress = sipConfig.getServerIp() + ":" + sipConfig.getServerPort();
			SipURI requestURI = addressFactory.createSipURI(checkUserCode, serverAddress);
			requestURI.setTransportParam(SipConstants.TRANSPORT_UDP);

			ArrayList<ViaHeader> viaHeaders = new ArrayList<>();
			ViaHeader viaHeader = headerFactory.createViaHeader(clientIp,clientPort, SipConstants.TRANSPORT_UDP, null);
			viaHeaders.add(viaHeader);

			CallIdHeader callIdHeader = sipProvider.getNewCallId();

			Long invo = cSeqCounter.getAndIncrement();
			
			CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(invo, Request.MESSAGE);

			MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70);

			Request request = messageFactory.createRequest(requestURI, Request.MESSAGE, callIdHeader, cSeqHeader,
					fromHeader, toHeader, viaHeaders, maxForwards);
			
			String contents = getXmlQueryParam(invo,checkUserCode);

	        // Create ContentTypeHeader
	        ContentTypeHeader contentTypeHeader = headerFactory
	                .createContentTypeHeader("Application", "MANSCDP+xml");
	        request.setContent(contents, contentTypeHeader);
	        
	        //将异步请求转换为同步请求
			CountDownLatch latch = new CountDownLatch(1);
			model.setLatch(latch);
			
	        //保存事务CallId与注册用户对应关系
	        String callId = callIdHeader.getCallId();

			checkUserMap.put(callId,model);
			
	        ClientTransaction inviteTid = sipProvider.getNewClientTransaction(request);
			inviteTid.sendRequest();
			
			//阻塞方法
			try {
				latch.await(); //阻塞等待计数为0
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			model = checkUserMap.get(callId);
			
			//清除map
			checkUserMap.remove(callId);
			//回收端口
			PortPoolManager.getInstance().destroy(clientPort);
		} catch (Exception e) {
			// TODO: handle exception
			throw new SipSystemException(e.getMessage());
		}
	}
	
	/**
	 * 获取xml参数
	 * @param checkUserCode
	 * @return
	 */
	private String getXmlQueryParam(Long sn,String checkUserCode) {
		String contents = "<?xml version=\"1.0\"?>\r\n"+ 
				"<Notify>\r\n" + 
				"<CmdType>Keepalive</CmdType>\r\n" + 
				"<SN>"+sn+"</SN>\r\n" + 
				"<DeviceID>"+checkUserCode+"</DeviceID>\r\n" + 
				"<Status>OK</Status>\r\n" + 
				"</Notify>";
		return contents;
	}
	
	
	@Override
	public void processResponse(ResponseEvent responseEvent) {
		// TODO Auto-generated method stub		
		Response resp = responseEvent.getResponse();
		 
		ClientTransaction tid = responseEvent.getClientTransaction();
		CSeqHeader cseq = (CSeqHeader) resp.getHeader(CSeqHeader.NAME);

		logger.info("Response received : Status Code = " + resp.getStatusCode() + " " + cseq);
		if (tid == null) {
			logger.info("Stray response -- dropping ");
			return;
		}
		logger.info("transaction state is " + tid.getState());
		logger.info("Dialog = " + tid.getDialog());
		
		CallIdHeader callIdHeader = (CallIdHeader) resp.getHeader(CallIdHeader.NAME);
		String callId = callIdHeader.getCallId();
		HearbeatBean model = checkUserMap.get(callId );
		int checkStatus = SipConstants.FAIL;
		if (resp.getStatusCode() == Response.OK) {
			checkStatus = SipConstants.SUCCESS;
		}
		model.setStatus(checkStatus);
		checkUserMap.put(callId, model);
		
		model.getLatch().countDown(); //计数减1
		
		logger.info("用户{}心跳检测状态：{}",model.getCheckUserCode(),checkStatus);
	}


	@Override
	public void close() throws IOException, ObjectInUseException {
		// TODO Auto-generated method stub
		logger.info(">>>>>>>>>>>>>>>>>>>HearbeatClient关闭发送心跳资源");
		
		//删除当前sip
		sipProvider.removeSipListener(this);
		SipStackManage.getSipStack().deleteSipProvider(sipProvider);
	}
	
	
	@Override
	public void processTimeout(TimeoutEvent event) {
		CallIdHeader callIdHeader = (CallIdHeader) event.getClientTransaction().getRequest().getHeader(CallIdHeader.NAME);
		String callId = callIdHeader.getCallId();
		HearbeatBean bean = checkUserMap.get(callId);
		bean.setStatus(SipConstants.TIMEOUT_FAIL);
		checkUserMap.put(callId, bean);
		
		bean.getLatch().countDown(); //计数减1
	}

	@Override
	public void processRequest(RequestEvent requestEvent) {
		// TODO Auto-generated method stub
		
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
