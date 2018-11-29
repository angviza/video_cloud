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
import javax.sip.header.ContactHeader;
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
import com.hdvon.sip.entity.RegisterBean;
import com.hdvon.sip.exception.SipSystemException;
import com.hdvon.sip.utils.CommonUtil;
import com.hdvon.sip.utils.SipConstants;

import gov.nist.javax.sip.SipStackExt;
import gov.nist.javax.sip.clientauthutils.AuthenticationHelper;
/**
 * 用户注册处理类
 * 作用：1：用户注册 expires 
 *     2：用户注销 expires 不为0
 * @author wanshaojian
 *
 */
public class RegisterClient  implements SipListener,AutoCloseable{
	public Logger logger = LoggerFactory.getLogger(getClass());
	
	 /** 
	  * 绑定请求call-ID与注册用户对应关系
	  */ 
	private ConcurrentHashMap<String, RegisterBean> regUserMap = new ConcurrentHashMap<>(); 

	private volatile static RegisterClient instance;
	
	private SipConfig sipConfig;
	
	private SipProvider sipProvider;

	private AddressFactory addressFactory;

	private MessageFactory messageFactory;

	private HeaderFactory headerFactory;
	
	private AtomicLong cSeqCounter = new AtomicLong(1);
	
	private RegisterClient(SipConfig sipConfig) {
		this.sipConfig = sipConfig;
	}
	
	public static RegisterClient getInstance(SipConfig sipConfig){
        if(instance==null){
            synchronized (RegisterClient.class){
                if(instance==null){
                    instance=new RegisterClient(sipConfig);
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
	 * 用户注册
	 * @param model 注册对象
	 * @throws SipSystemException
	 */
	public void registerProcess(RegisterBean model) throws SipSystemException {
		
		try {
			// 获取端口
			int clientPort = PortPoolManager.getInstance().getPool();
			
			createSipProvider(clientPort);
			
			String registerCode = model.getRegisterCode();
			int expires = model.getExpires();
	        String domain = sipConfig.getDomain();
	        
	        String clientIp = sipConfig.getClientIp();
	        
	        //创建form头
			SipURI from = addressFactory.createSipURI(registerCode, domain);
			Address fromNameAddress = addressFactory.createAddress(from);
			FromHeader fromHeader = headerFactory.createFromHeader(fromNameAddress, CommonUtil.getTags());

	        //创建to头
			SipURI toAddress = addressFactory.createSipURI(registerCode, domain);
			Address toNameAddress = addressFactory.createAddress(toAddress);
			ToHeader toHeader = headerFactory.createToHeader(toNameAddress, null);

			//创建请求 URI
			String serverAddress = sipConfig.getServerIp() + ":" + sipConfig.getServerPort();
			SipURI requestURI = addressFactory.createSipURI(registerCode, serverAddress);
			requestURI.setTransportParam(SipConstants.TRANSPORT_UDP);

			//创建Via 头数组
			ArrayList<ViaHeader> viaHeaders = new ArrayList<>();
			ViaHeader viaHeader = headerFactory.createViaHeader(clientIp,clientPort, SipConstants.TRANSPORT_UDP, null);
			viaHeaders.add(viaHeader);

			//创建Call-ID 头
			CallIdHeader callIdHeader = sipProvider.getNewCallId();
			//创建CSeq 头
			CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(cSeqCounter.getAndIncrement(), Request.REGISTER);
			//Max-forwards 头
			MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70);
			//实例化实际的 SIP 消息本身
			Request request = messageFactory.createRequest(requestURI, Request.REGISTER, callIdHeader, cSeqHeader,
					fromHeader, toHeader, viaHeaders, maxForwards);
			//消息添加Contact 头
			SipURI contactURI = addressFactory.createSipURI(registerCode, clientIp);
			contactURI.setPort(clientPort);
			
			Address contactAddress = addressFactory.createAddress(contactURI);
			ContactHeader contactHeader = headerFactory.createContactHeader(contactAddress);
			request.addHeader(contactHeader);
			
			//消息添加Expires
			request.setExpires(headerFactory.createExpiresHeader(expires));

			//发送消息
			String callId = callIdHeader.getCallId();
			
			CountDownLatch latch = new CountDownLatch(1);
			model.setLatch(latch);
			
			//保存事务与注册用户对应关系
	        regUserMap.put(callId,model);
	        
			ClientTransaction clientTran = sipProvider.getNewClientTransaction(request);
			clientTran.sendRequest();
			
			try {
				latch.await(); //阻塞等待计数为0
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			model = regUserMap.get(callId);
			//清除map
			regUserMap.remove(callId);
			//回收端口
			PortPoolManager.getInstance().destroy(clientPort);
		} catch ( Exception e) {
			// TODO: handle exception
			throw new SipSystemException(e);
		}


	}
	
	@Override
	public void processRequest(RequestEvent requestEvent) {
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>"+requestEvent.getRequest().getContent());
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

		int status = resp.getStatusCode();

		CallIdHeader callIdHeader = (CallIdHeader) resp.getHeader(CallIdHeader.NAME);
		String callId = callIdHeader.getCallId();
		RegisterBean register = regUserMap.get(callId);
		String registerCode = register.getRegisterCode();
		int registerStatus = SipConstants.FAIL;
		
		if (status == Response.OK) {
			logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>账号{}注册成功",registerCode);
			registerStatus = SipConstants.SUCCESS;
			register.setStatus(registerStatus);
			
			regUserMap.put(callId, register);
			
			register.getLatch().countDown(); //计数减1
		} else if (status == Response.PROXY_AUTHENTICATION_REQUIRED || status == Response.UNAUTHORIZED) {
			try {
				String registerPwd = sipConfig.getDefaultPassword();
				/**
				 * 发送认证信息
				 */
				AuthenticationHelper authenticationHelper = ((SipStackExt) SipStackManage.getSipStack())
						.getAuthenticationHelper(new AccountManagerImpl(registerCode,registerPwd), headerFactory);
				ClientTransaction inviteTid = authenticationHelper.handleChallenge(resp, tid, sipProvider, 5 ,true);
				
				
				inviteTid.sendRequest();
			} catch (Exception ex) {
				logger.error(ex.getMessage());
				ex.printStackTrace();
			}
		}else {
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>账号{}注册失败",registerCode);
			
			register.setStatus(registerStatus);
			regUserMap.put(callId, register);
			
			register.getLatch().countDown(); //计数减1


			
		}


	}
	@Override
	public void close() throws IOException, ObjectInUseException {
		// TODO Auto-generated method stub
		logger.info(">>>>>>>>>>>>>>>>>>>RegisterClient关闭注册资源");
		
		//删除当前sip
		sipProvider.removeSipListener(this);
		SipStackManage.getSipStack().deleteSipProvider(sipProvider);
		

	}



	
	@Override
	public void processTimeout(TimeoutEvent event) {
		CallIdHeader callIdHeader = (CallIdHeader) event.getClientTransaction().getRequest().getHeader(CallIdHeader.NAME);
		String callId = callIdHeader.getCallId();
		RegisterBean register = regUserMap.get(callId);
		register.setStatus(SipConstants.TIMEOUT_FAIL);
		regUserMap.put(callId, register);
		
		
		register.getLatch().countDown(); //计数减1
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
