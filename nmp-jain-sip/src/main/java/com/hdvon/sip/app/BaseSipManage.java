package com.hdvon.sip.app;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
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
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.SubjectHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.hdvon.nmp.common.CruiseBean;
import com.hdvon.nmp.common.PresentBean;
import com.hdvon.nmp.common.PresentListBean;
import com.hdvon.nmp.common.SipConstants;
import com.hdvon.nmp.common.SipLogBean;
import com.hdvon.nmp.enums.CruiseTypeEnum;
import com.hdvon.nmp.enums.MethodEum;
import com.hdvon.nmp.enums.MsgTypeEnum;
import com.hdvon.nmp.enums.PresetTypeEnum;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.config.kafka.Sender;
import com.hdvon.sip.entity.ByeBean;
import com.hdvon.sip.entity.DeviceStatusBean;
import com.hdvon.sip.entity.ErrorBean;
import com.hdvon.sip.entity.ErrorMsg;
import com.hdvon.sip.entity.ErrorMsg.ResponseCodeEnum;
import com.hdvon.sip.entity.InviteBean;
import com.hdvon.sip.entity.InviteEntity;
import com.hdvon.sip.entity.MessageBean;
import com.hdvon.sip.entity.PresetQueryBean;
import com.hdvon.sip.entity.PresetQueryBean.PresetItemBean;
import com.hdvon.sip.entity.RecordBean;
import com.hdvon.sip.entity.RecordBean.RecordItemBean;
import com.hdvon.sip.entity.RegisterBean;
import com.hdvon.sip.entity.ResponseBean;
import com.hdvon.sip.entity.SipResultBean;
import com.hdvon.sip.entity.StatusBean;
import com.hdvon.sip.enums.MessageTypeEnum;
import com.hdvon.sip.exception.SipParamException;
import com.hdvon.sip.exception.SipSystemException;
import com.hdvon.sip.service.WebSocketService;
import com.hdvon.sip.utils.CommonUtil;
import com.hdvon.sip.utils.SipMsgUtils;
import com.hdvon.sip.utils.XmlUtil;
import com.hdvon.sip.websocket.WebSocketManager;
import com.hdvon.sip.websocket.WsCacheBean;

import gov.nist.javax.sip.SipStackExt;
import gov.nist.javax.sip.clientauthutils.AuthenticationHelper;
/**
 * sip客户端处理类
 * @author wanshaojian
 *
 */
public class BaseSipManage implements SipListener {
	private static final Logger LOG = LoggerFactory.getLogger(BaseSipManage.class);
	
	private static final int logLevel = 32;

	private SipStack sipStack;
	private SipFactory sipFactory;
	private SipProvider sipProvider;
    private ListeningPoint lp;
    
	private AddressFactory addressFactory;
	private MessageFactory messageFactory;
	private HeaderFactory headerFactory;

	private boolean byeTaskRunning;
	private Timer timer;

	private static final int DEFAULT_PORT = 5060;

	private SipConfig sipConfig;
	
	private WebSocketService socketService;
	/**
	 * kafka发送消息类
	 */
	private Sender sender;
	
	private AtomicLong cSeqCounter = new AtomicLong(1);

	private volatile static BaseSipManage instance;
	
	private static final String URI_CHAR = ":255";
	
	private String regCallId = null;
	
	/**
	 * 绑定请求call-ID与响应对象对应关系
	 * 点播和回看 特殊处理
	 *  	一个callID对应用户多个请求，数据结构为ConcurrentHashMap<String, Map<String,Object>> 对应关系    {callId:[{消息类型:请求对象}]}
	 *  
	 */
	private ConcurrentHashMap<String, Object> callMap = new ConcurrentHashMap<>();

	
	
	/**
	 * 保存当前请求call-ID与事务对应关系
	 */
	private ConcurrentHashMap<String, ClientTransaction> tranMap = new ConcurrentHashMap<>();
	
	/** 
	  * 绑定客户端回话与信令服务器端回话关系
	  */ 
	private ConcurrentHashMap<String, String> relationMap = new ConcurrentHashMap<>(); 
	
	/**
	 * 保存当前会话返回的录像记录
	 */
	private ConcurrentHashMap<String, List<RecordItemBean>> callMediaRecordMap = new ConcurrentHashMap<>();
	

	private volatile static Logger logger = LoggerFactory.getLogger(BaseSipManage.class);

	
	private BaseSipManage(final SipConfig sipConfig,final WebSocketService socketService,Sender sender) {
		this.sipConfig = sipConfig;
		this.socketService = socketService;
		this.sender = sender;
		init();
	}



	public static BaseSipManage getInstance(SipConfig sipConfig,WebSocketService socketService,	Sender sender) {
		if (instance == null) {
			synchronized (BaseSipManage.class) {
				if (instance == null) {
					instance = new BaseSipManage(sipConfig,socketService,sender);
				}
			}
		}
		return instance;
	}
	
	/**
	 * 初始化创建sip客户端
	 */
	private void init() {
		try {
			String ipAddress = sipConfig.getClientIp();

			Properties properties = new Properties();
			properties.setProperty("javax.sip.IP_ADDRESS", ipAddress);
			properties.setProperty("javax.sip.STACK_NAME", "hdvon-register-stack");
			properties.setProperty("gov.nist.javax.sip.MAX_MESSAGE_SIZE", "1048576");
//			properties.setProperty("gov.nist.javax.sip.DEBUG_LOG", "sipAuthdebug.txt");
//			properties.setProperty("gov.nist.javax.sip.SERVER_LOG", "sipAuthlog.txt");
			properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", new Integer(logLevel).toString());
			// Drop the client connection after we are done with the transaction.
			properties.setProperty("gov.nist.javax.sip.CACHE_CLIENT_CONNECTIONS", "false");

			// properties.setProperty("gov.nist.javax.sip.READ_TIMEOUT", "1000");
			// 连接超时时间
			properties.setProperty("gov.nist.javax.sip.CONNECTION_TIMEOUT", "3000");
			properties.setProperty("gov.nist.javax.sip.CONGESTION_CONTROL_TIMEOUT", "3000");

			properties.setProperty("gov.nist.javax.sip.NIO_BLOCKING_MODE", "NONBLOCKING");

			// Create SipStack object
			this.sipFactory = SipFactory.getInstance();
			this.sipFactory.setPathName(SipConstants.SIP_DOMAIN);
			this.sipStack = this.sipFactory.createSipStack(properties);
			this.sipStack.start();

			this.lp = this.sipStack.createListeningPoint(ipAddress, DEFAULT_PORT, SipConstants.TRANSPORT_UDP);
			this.sipProvider = this.sipStack.createSipProvider(lp);
            this.sipProvider.addSipListener(this);
            
            this.headerFactory = this.sipFactory.createHeaderFactory();
            this.messageFactory = this.sipFactory.createMessageFactory();
            this.addressFactory = this.sipFactory.createAddressFactory();
            

		} catch (Exception e) {
//			e.printStackTrace();
			logger.error("sip 客户端初始化加载失败，失败原因：{}",e.getMessage());
		}
	}

	@Override
	public void processRequest(RequestEvent requestEvent) {
		// TODO Auto-generated method stub
		Request request = requestEvent.getRequest();
		LOG.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\r\nRequest: " + request.getMethod() + " received at");
		ServerTransaction serverTransactionId = requestEvent.getServerTransaction();


		if (request.getMethod().equals(Request.BYE)) {
			processBye(request, serverTransactionId);
		} else if(request.getMethod().equals(Request.MESSAGE)){
			String xml = new String ((byte[]) request.getContent());

			MessageTypeEnum typeEm = XmlUtil.getMessageType(xml);

			if(MessageTypeEnum.RECORD_INFO.equals(typeEm)) {
				//录像查询响应处理
				requestRecordInfo(xml);
			}else if(MessageTypeEnum.DEVICE_STATUS.equals(typeEm)){
				//设备查询响应处理
				requestDeviceStatus(xml);
			}else if(MessageTypeEnum.PRESET_QUERY.equals(typeEm)) {
				//预置位查询响应处理
				requestPresetQuery(xml);
			}else if(MessageTypeEnum.MEDIA_STATUS.equals(typeEm)) {
				//录像结束响应处理
				//获取会话id
				CallIdHeader callHeader = (CallIdHeader) request.getHeader(CallIdHeader.NAME);
				String callId = callHeader.getCallId();
				requestTerminated(callId,xml);
			}

		}else if(request.getMethod().equals(Request.INVITE)) {
			LOG.info(">>>>>>>>>>>>>>>>>>>>>>INVITE");
		}

		try {
	    	Response response = messageFactory.createResponse(Response.OK, request);
	        if(serverTransactionId == null) {
	        	serverTransactionId = sipProvider.getNewServerTransaction(request);
	        }
            serverTransactionId.sendResponse(response);
		} catch (SipException | InvalidArgumentException | ParseException e) {
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>processRequest异常，原因：{}",e.getMessage());
		} 

	}

	
	@Override
	public void processResponse(ResponseEvent responseEvent) {
		// TODO Auto-generated method stub
		Response resp = responseEvent.getResponse();
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Response received : Status Code = " + resp.getStatusCode());
		ClientTransaction tid = responseEvent.getClientTransaction();
		CSeqHeader cseq = (CSeqHeader) resp.getHeader(CSeqHeader.NAME);

		if (tid == null) {
			logger.info("Stray response -- dropping ");
			return;
		}
		int status = resp.getStatusCode();

		if (status == Response.OK) {
			processResponseOk(responseEvent);
		} else if (status == Response.PROXY_AUTHENTICATION_REQUIRED || status == Response.UNAUTHORIZED) {
			processResponseAuth(responseEvent);
		} else if (status == Response.TRYING) {//try
			
		}else{
			CallIdHeader callHeader = (CallIdHeader) resp.getHeader(CallIdHeader.NAME);
			String callId = callHeader.getCallId();

			String method = cseq.getMethod();
			
			ResponseBean<ErrorBean> respData = new ResponseBean<>();
			if (method.equals(Request.INVITE)) {
				Map<String,Object> dataMap = (Map<String, Object>) callMap.get(callId);
				//InviteBean对象不存在，则直接返回
				if(dataMap == null || !dataMap.containsKey(Request.INVITE)) {
					return;
				}
				InviteBean inviteData = (InviteBean) dataMap.get(Request.INVITE);
				
				BeanUtils.copyProperties(inviteData, respData);
			} else if (method.equals(Request.MESSAGE)) {
				MessageBean msgData = (MessageBean) callMap.get(callId);
				String msgMethod = msgData.getMethod();
				ResponseCodeEnum respCodeEm = ResponseCodeEnum.getObjectByKey(String.valueOf(status));

				if(MethodEum.KEEPLIVE.getValue().equals(msgMethod)) {
					if(ResponseCodeEnum.NOT_REGISTER.equals(respCodeEm)) {
						//用户发送失败，且失败原因为用户未注册
						logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>用户:{}发送心跳失败,用户未注册",JSON.toJSONString(msgData.getTargetDeviceCode()));
						//重新用户注册
						RegisterBean registerBean = new RegisterBean(msgData.getTargetDeviceCode(), sipConfig.getExpire());
						processRegister(registerBean);
					}
					//删除callId
					callMap.remove(callId);

					return;
				}else{
					BeanUtils.copyProperties(msgData, respData);
				}
				
			} else if (method.equals(Request.REGISTER)) {
				RegisterBean  regData = (RegisterBean) callMap.get(callId);
				BeanUtils.copyProperties(regData, respData);
			} else if(method.equals(Request.BYE)) {
				Map<String,Object> dataMap = (Map<String, Object>) callMap.get(callId);
				//InviteBean对象不存在，则直接返回
				if(dataMap == null || !dataMap.containsKey(Request.BYE)) {
					return;
				}
				ByeBean byeData = (ByeBean) dataMap.get(Request.BYE);
				
				BeanUtils.copyProperties(byeData, respData);
			}
			ErrorMsg.resultErrorMsg(respData, ResponseCodeEnum.getObjectByKey(String.valueOf(status)));
			
			logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>callId:{}发送message消息失败,原因：{}",callId,JSON.toJSONString(respData));
			/**
			 * @TODO websockt发送消息 待完成
			 */
			socketService.sendUser(respData.getWsId(), respData);
			

			//删除callId
			callMap.remove(callId);
		}

	}



	@Override
	public void processTimeout(TimeoutEvent timeoutEvent) {
		// TODO Auto-generated method stub
		ClientTransaction tran = timeoutEvent.getClientTransaction();
		CallIdHeader callIdHeader = (CallIdHeader) tran.getRequest().getHeader(CallIdHeader.NAME);
		String callId = callIdHeader.getCallId();
		CSeqHeader cseqHeader = (CSeqHeader) tran.getRequest().getHeader(CSeqHeader.NAME);
		String method = cseqHeader.getMethod();
		ResponseBean<ErrorBean> respData = new ResponseBean<>();
		
		boolean websocketFlag = false;
		if (method.equals(Request.INVITE)) {
			InviteBean bean = (InviteBean) callMap.get(callId);
			BeanUtils.copyProperties(bean, respData);
			LOG.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>callId:{}请求：{}响应超时",callId,bean);
			websocketFlag = true;
		}else if (method.equals(Request.MESSAGE)) {
			MessageBean bean = (MessageBean) callMap.get(callId);
			BeanUtils.copyProperties(bean, respData);
			
			if(MethodEum.KEEPLIVE.getValue().equals(bean.getMethod())) {
				//用户发送失败，且失败原因为用户未注册
				logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>用户:{}发送心跳失败,用户未注册",JSON.toJSONString(bean.getTargetDeviceCode()));
				//重新用户注册
				RegisterBean registerBean = new RegisterBean(bean.getTargetDeviceCode(), sipConfig.getExpire());
				processRegister(registerBean);
			}
			
			LOG.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>callId:{}请求：{}响应超时",callId,bean);
			websocketFlag = true;
		}else if (method.equals(Request.REGISTER)) {
			RegisterBean bean = (RegisterBean) callMap.get(callId);
			LOG.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>用户:{} callId:{} 注册响应响应超时",bean.getRegisterCode(),callId);
		}else if (method.equals(Request.BYE)) {
			ByeBean bean = (ByeBean) callMap.get(callId);
			BeanUtils.copyProperties(bean, respData);
			websocketFlag = true;
			LOG.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>callId:{}请求：{}响应超时",callId,bean);

		}else{
			LOG.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>callId:{}请求响应超时",callId);
		}
		if(websocketFlag) {
			ErrorMsg.resultErrorMsg(respData, ResponseCodeEnum.getObjectByKey(ResponseCodeEnum.REQUEST_TIMEOUT.getCode()));
			/**
			 * @TODO websockt发送消息 待完成
			 */
			socketService.sendUser(respData.getWsId(), respData);
		}
		//清除当前callId相关信息
		callMap.remove(callId);
		//删除会话对象
		tranMap.remove(callId);
		
	}

	@Override
	public void processIOException(IOExceptionEvent exceptionEvent) {
		// TODO Auto-generated method stub
		LOG.info(">>>>>>>>>>>>>>>>>>>processIOException");
	}

	@Override
	public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
		// TODO Auto-generated method stub
		LOG.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>processTransactionTerminated");
		ClientTransaction clientTran = transactionTerminatedEvent.getClientTransaction();
		

	}

	@Override
	public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
		// TODO Auto-generated method stub
		LOG.info(">>>>>>>>>>>>>>>>>>>processDialogTerminated");
		Dialog dialog = dialogTerminatedEvent.getDialog();
		String callId = dialog.getCallId().getCallId();
//		//删除当前对象
//		tranMap.remove(callId);
//		

	}
	/**
	 * 发送message消息
	 * @param bean message对象
	 * @param registerCode 注册用户
	 * @return typeEm message类型
	 * @throws Exception
	 */
	public SipResultBean processMessage(MessageBean bean,String registerCode,MessageTypeEnum typeEm) throws Exception {
		if (bean == null || StringUtils.isEmpty(bean.getSdpData()) || bean.getSn() == null || StringUtils.isEmpty(bean.getTargetDeviceCode())) {
			throw new SipParamException("发送Message消息异常，targetDeviceCode，sdpData和sn不能为空！");
		}
		try {
			String domian = sipConfig.getDomain();

			// 创建From 头
			SipURI from = addressFactory.createSipURI(registerCode, domian);

			Address fromNameAddress = addressFactory.createAddress(from);
			FromHeader fromHeader = headerFactory.createFromHeader(fromNameAddress, CommonUtil.getTags());

			// 媒体流发送者设备编码
			String targetDeviceCode = bean.getTargetDeviceCode();			
			// 创建to 头
			SipURI toAddress = addressFactory.createSipURI(targetDeviceCode, domian);
			Address toNameAddress = addressFactory.createAddress(toAddress);
			ToHeader toHeader = headerFactory.createToHeader(toNameAddress, null);

			// 创建请求 URI
			String serverAddress = sipConfig.getServerIp() + ":" + sipConfig.getServerPort();
			SipURI requestURI = addressFactory.createSipURI(registerCode, serverAddress);

			// sip客户端的ip地址
			String clientIp = sipConfig.getClientIp();

			// 创建Via 头数组
			ArrayList<ViaHeader> viaHeaders = new ArrayList<>();
			ViaHeader viaHeader = headerFactory.createViaHeader(clientIp, DEFAULT_PORT, SipConstants.TRANSPORT_UDP,
					null);
			viaHeaders.add(viaHeader);

			// 创建Call-ID 头
			CallIdHeader callIdHeader = sipProvider.getNewCallId();

			Long invco = cSeqCounter.getAndIncrement();

			// 创建CSeq 头
			CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(invco, Request.MESSAGE);
			// Max-forwards 头
			MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70);

			// 实例化实际的 SIP 消息本身
			Request request = messageFactory.createRequest(requestURI, Request.MESSAGE, callIdHeader, cSeqHeader,
					fromHeader, toHeader, viaHeaders, maxForwards);

			byte[] contents = bean.getSdpData().getBytes();
			// 添加消息的内容
			ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("Application", "MANSCDP+xml");

			request.setContent(contents, contentTypeHeader);

			String callId = callIdHeader.getCallId();
			bean.setCallId(callId);
			callMap.put(callId, bean);
			
			// 发送消息
			ClientTransaction inviteTid = sipProvider.getNewClientTransaction(request);
			inviteTid.sendRequest();
			
			if(MessageTypeEnum.RECORD_INFO.equals(typeEm) || MessageTypeEnum.DEVICE_STATUS.equals(typeEm) || MessageTypeEnum.PRESET_QUERY.equals(typeEm)) {
				String relationKey = XmlUtil.genRelationKey(bean.getTargetDeviceCode(), bean.getSn());
				relationMap.put(relationKey, callId);
			}
			
			return new SipResultBean(callId,SipConstants.STATE_CONTINUE);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>processMessage异常，原因：{}",e.getMessage());
			throw new SipSystemException(e.getMessage());
		}

	}

	/**
	 * 发送register消息
	 * 
	 * @param model
	 *            注册对象
	 * @throws SipSystemException
	 */
	public SipResultBean processRegister(RegisterBean bean) {
		try {
			String registerCode = bean.getRegisterCode();
			int expires = bean.getExpires();
			String domain = sipConfig.getDomain();

			String clientIp = sipConfig.getClientIp();

			// 创建form头
			SipURI from = addressFactory.createSipURI(registerCode, domain);
			Address fromNameAddress = addressFactory.createAddress(from);
			FromHeader fromHeader = headerFactory.createFromHeader(fromNameAddress, CommonUtil.getTags());

			// 创建to头
			SipURI toAddress = addressFactory.createSipURI(registerCode, domain);
			Address toNameAddress = addressFactory.createAddress(toAddress);
			ToHeader toHeader = headerFactory.createToHeader(toNameAddress, null);

			// 创建请求 URI
			String serverAddress = sipConfig.getServerIp() + ":" + sipConfig.getServerPort();
			SipURI requestURI = addressFactory.createSipURI(registerCode, serverAddress);
			requestURI.setTransportParam(SipConstants.TRANSPORT_UDP);

			// 创建Via 头数组
			ArrayList<ViaHeader> viaHeaders = new ArrayList<>();
			ViaHeader viaHeader = headerFactory.createViaHeader(clientIp, DEFAULT_PORT, SipConstants.TRANSPORT_UDP,
					null);
			viaHeaders.add(viaHeader);

			// 创建Call-ID 头
			CallIdHeader callIdHeader = sipProvider.getNewCallId();
			if(StringUtils.isEmpty(regCallId)) {
				regCallId = callIdHeader.getCallId();
			}else {
				callIdHeader.setCallId(regCallId);
			}
			// 创建CSeq 头
			CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(cSeqCounter.getAndIncrement(), Request.REGISTER);
			// Max-forwards 头
			MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70);
			// 实例化实际的 SIP 消息本身
			Request request = messageFactory.createRequest(requestURI, Request.REGISTER, callIdHeader, cSeqHeader,
					fromHeader, toHeader, viaHeaders, maxForwards);
			// 消息添加Contact 头
			SipURI contactURI = addressFactory.createSipURI(registerCode, clientIp);
			contactURI.setPort(DEFAULT_PORT);

			Address contactAddress = addressFactory.createAddress(contactURI);
			ContactHeader contactHeader = headerFactory.createContactHeader(contactAddress);
			request.addHeader(contactHeader);

			// 消息添加Expires
			request.setExpires(headerFactory.createExpiresHeader(expires));

			// 发送消息
			String callId = callIdHeader.getCallId();
			// 保存事务与注册用户对应关系
			callMap.put(callId, bean);

			ClientTransaction inviteTid = sipProvider.getNewClientTransaction(request);
			inviteTid.sendRequest();
			
			//重新获取对象
			return new SipResultBean(callId,SipConstants.STATE_OK);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>processRegister异常，原因：{}",e.getMessage());
			throw new SipSystemException(e);
		}

	}

	/**
	 * 发送Invite消息
	 * @param param invite请求参数
	 * @param sdpData sdp报文       
	 * @param registerCode 注册用户
	 * @param port 接受流端口
	 */
	public SipResultBean processInvite(InviteBean bean,String registerCode,String sdpData, int port) throws Exception {
		if (bean == null || StringUtils.isEmpty(sdpData) || StringUtils.isEmpty(bean.getDeviceID())) {
			throw new SipParamException("发送Invite消息异常，deviceCode和sdpData不能为空！");
		}
		try {

			String domian = sipConfig.getDomain();
			String transportType = SipConstants.TRANSPORT_UDP;
			// 创建From 头
			SipURI from = addressFactory.createSipURI(registerCode, domian);

			Address fromNameAddress = addressFactory.createAddress(from);
			FromHeader fromHeader = headerFactory.createFromHeader(fromNameAddress, CommonUtil.getTags());

			// 媒体流发送者设备编码
			String targetDeviceCode = bean.getDeviceID();
			// 创建to 头
			SipURI toAddress = addressFactory.createSipURI(targetDeviceCode, domian);
			Address toNameAddress = addressFactory.createAddress(toAddress);
			ToHeader toHeader = headerFactory.createToHeader(toNameAddress, null);

			
			// 创建请求 URI
			String serverAddress = sipConfig.getServerIp() + ":" + sipConfig.getServerPort();

			SipURI requestURI = addressFactory.createSipURI(targetDeviceCode, serverAddress);
			requestURI.setTransportParam(transportType);

			// sip客户端的ip地址
			String clientIp = sipConfig.getClientIp();

			// 创建Via 头数组
			ArrayList<ViaHeader> viaHeaders = new ArrayList<>();
			ViaHeader viaHeader = headerFactory.createViaHeader(clientIp, DEFAULT_PORT, transportType, null);
			viaHeaders.add(viaHeader);

			// 创建Call-ID 头
			CallIdHeader callIdHeader = sipProvider.getNewCallId();
			// 创建CSeq 头
			CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(cSeqCounter.getAndIncrement(), Request.INVITE);
			// Max-forwards 头
			MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70);

			// 实例化实际的 SIP 消息本身
			Request request = messageFactory.createRequest(requestURI, Request.INVITE, callIdHeader, cSeqHeader,
					fromHeader, toHeader, viaHeaders, maxForwards);

			byte[] contents = sdpData.getBytes();
			// 添加消息的内容
			ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("Application", "sdp");

			request.setContent(contents, contentTypeHeader);

			// 消息添加Contact 头
			SipURI contactURI = addressFactory.createSipURI(registerCode, clientIp);
			contactURI.setPort(DEFAULT_PORT);

			Address contactAddress = addressFactory.createAddress(contactURI);
			ContactHeader contactHeader = headerFactory.createContactHeader(contactAddress);
			request.addHeader(contactHeader);

			// 消息添加subject
			StringBuffer subject = new StringBuffer();
			subject.append(targetDeviceCode).append(":").append(getSendMediaNo(bean.getMethod()));
			subject.append(",").append(registerCode).append(":").append(getReceiveMediaNo());
			SubjectHeader subjectHeader = headerFactory.createSubjectHeader(subject.toString());
			request.addHeader(subjectHeader);

			// 保存事务CallId与注册用户对应关系
			String callId = callIdHeader.getCallId();
			bean.setCallId(callId);
			
			

			/**
			 * 一个callId会关联多个请求消息，通过消息类型来区分
			 */
			Map<String,Object> dataMap = new HashMap<>();
			dataMap.put(Request.INVITE, bean);
			
			callMap.put(callId, dataMap);
			
			// 发送消息
			ClientTransaction inviteTid = sipProvider.getNewClientTransaction(request);
			inviteTid.sendRequest();

			bean.setState(SipConstants.STATE_CONTINUE);
		    
			tranMap.put(callId, inviteTid);
			
			logger.info(">>>>>>>>>>>>>>>>发起callId:{}对象:{}invite请求",callId,JSON.toJSONString(bean));
			
			return new SipResultBean(callId,SipConstants.STATE_CONTINUE);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>processInvite异常，原因：{}",e.getMessage());
			throw new SipSystemException(e.getMessage());
		}
	}

	/**
	 * 发送停止命令
	 * 
	 * @param model
	 */
	public void processBye(Request request, ServerTransaction serverTransactionId) throws SipSystemException {
		try {
			if (serverTransactionId == null) {
				logger.info("null TID.");
				return;
			}
			Dialog dialog = serverTransactionId.getDialog();
			logger.info("Dialog State = " + dialog.getState());

			Response response = messageFactory.createResponse(200, request);
			serverTransactionId.sendResponse(response);
			
			String callId = dialog.getCallId().getCallId();
			//根据callId获取对象
			
			Map<String,Object> dataMap = (Map<String, Object>) callMap.get(callId);
			
			InviteBean inviteBean = (InviteBean) dataMap.get(Request.INVITE);
			
			ResponseBean<ErrorBean> respData = new ResponseBean<>();
			BeanUtils.copyProperties(inviteBean, respData);
			
			
			
			
			ErrorMsg.resultErrorMsg(respData, ResponseCodeEnum.VIDEO_PLAY_ERROR);

			/**
			 * @TODO websockt发送消息 待完成
			 */
			socketService.sendUser(respData.getWsId(), respData);
			
			//清除ws与callID关系
			delCacheInvite(respData.getWsId(), callId,inviteBean.getPort());
			
			//清除当前callId相关信息
			callMap.remove(callId);
			//删除会话对象
			tranMap.remove(callId);

			logger.info(">>>>>>>>>>>>>>>>信令关闭视频流{}请求",JSON.toJSONString(inviteBean));
			
		} catch (Exception e) {
			// TODO: handle exception
			throw new SipSystemException(e);
		}
	}
	/**
	 * Info消息处理
	 * @param callId 回话id
	 * @param sdpData 报文内容
	 */
	public SipResultBean processInfo(String callId,String deviceID,String sdpData) {
		try {
			//获取当前播放的dialog
			ClientTransaction clientTran = tranMap.get(callId);
			Dialog dialog = clientTran.getDialog();
	        
			// 创建请求 URI
			String serverAddress = sipConfig.getServerIp() + ":" + sipConfig.getServerPort();
			SipURI requestURI = addressFactory.createSipURI(deviceID, serverAddress);
			
			Request infoRequest = dialog.createRequest(Request.INFO);
			infoRequest.setRequestURI(requestURI);
			// 添加消息的内容
			ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("Application",
					"MANSRTSP");
			
			byte[] contents = sdpData.getBytes();
			infoRequest.setContent(contents, contentTypeHeader);
			
			ClientTransaction ct = sipProvider.getNewClientTransaction(infoRequest);
			dialog.sendRequest(ct);
			
			//重新获取对象
			return new SipResultBean(callId,SipConstants.STATE_OK);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>processInfo异常，原因：{}",e.getMessage());
			throw new SipSystemException(e);
		}

	}
	/**
	 * 发送停止命令
	 * 
	 * @param model
	 */
	public SipResultBean processTerminate(ByeBean bye) throws SipSystemException {
		try {
			String callId = bye.getCallId();


			if(!tranMap.containsKey(callId) || !callMap.containsKey(callId)) {
				return new SipResultBean(callId,SipConstants.STATE_OK);
			}
			ClientTransaction clientTran = tranMap.get(callId);
			
			Dialog dialog =  clientTran.getDialog();
			Map<String,Object> dataMap = (Map<String, Object>) callMap.get(callId);
			//添加bye请求
			dataMap.put(Request.BYE, bye);
			
			callMap.put(callId, dataMap);
			
			Request byeRequest = dialog.createRequest(Request.BYE);
			ClientTransaction ct = sipProvider.getNewClientTransaction(byeRequest);
			dialog.sendRequest(ct);
			
			logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>关流callId:{} ",bye.getCallId());
			return new SipResultBean(callId,SipConstants.STATE_CONTINUE);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>processTerminate异常，原因：{}",e.getMessage());
			throw new SipSystemException(e);
		}
	}
	/**
	 * 取消发送
	 * callId 会话id
	 */
    public SipResultBean processCancel(String callId) {
        try {
			if(!tranMap.containsKey(callId)) {
				return new SipResultBean(callId,SipConstants.STATE_TERMINATED);
			}
			ClientTransaction inviteTid = tranMap.get(callId);
            Request cancelRequest = inviteTid.createCancel();
            ClientTransaction cancelTid = sipProvider
                    .getNewClientTransaction(cancelRequest);
            cancelTid.sendRequest();
                        
            return new SipResultBean(callId,SipConstants.STATE_CONTINUE);
        } catch (Exception ex) {
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>processCancel异常，原因：{}",ex.getMessage());
			throw new SipSystemException(ex);
        }
    }
    /**
     * 结束处理
     * @param callId
     * @param xml
     */
    public void requestTerminated(String callId,String xml) {
    	StatusBean recordData = XmlUtil.getStatusBean(xml);
		
		/**
		 * 判断录像结束，自动通知前端
		 * 	notifyType = 121,录像结束
		 */
		if(SipConstants.NOTIFY_TYPE.equals(recordData.getNotifyType())) {

			/**
			 * 视频点播、录像回看、下载
			 * 一个callId会绑定多个请求  如invite  bye
			 */
			Map<String,Object> dataMap = (Map<String, Object>) callMap.get(callId);

			//InviteBean对象不存在，则直接返回
			if(dataMap == null || !dataMap.containsKey(Request.INVITE)) {
				return;
			}
			InviteBean inviteData = (InviteBean) dataMap.get(Request.INVITE);

			ResponseBean<SipResultBean> respData = new ResponseBean<>();
			BeanUtils.copyProperties(inviteData, respData);
			
			SipResultBean resultBean = new SipResultBean(callId,SipConstants.STATE_TERMINATED);
			respData.setResult(resultBean);
			
			/**
			 * @TODO websockt发送消息 待完成
			 */
			socketService.sendUser(respData.getWsId(), respData);
			
			
			WsCacheBean cacheBean = WebSocketManager.get(respData.getWsId());
			if(cacheBean != null) {
				String deviceID = inviteData.getDeviceID();
				
				SipLogBean sipLog  = SipLogBean.builder().transactionID(null).userId(null).reqIp(cacheBean.getReqIp()).token(respData.getToken()).
						deviceID(deviceID).callID(inviteData.getCallId()).method(MethodEum.TERMINATE.getValue()).reqDate(new Date()).param(null).build();
				if(LOG.isDebugEnabled()) {
					LOG.debug(">>>>>>>>>>>>>>>>>>>发送kafka消息,自动关闭发送消息内容sipLog:{}",JSON.toJSONString(sipLog));
				}
				sender.sendLog(sipLog);
			}
			
			//清除ws与callID关系
			delCacheInvite(respData.getWsId(), callId,inviteData.getPort());
			
			//清除当前callId相关信息
			callMap.remove(callId);
			//删除会话对象
			tranMap.remove(callId);

			logger.info(">>>>>>>>>>>>>>>>结束处理callId:{}的结果：{}",callId,JSON.toJSONString(recordData));
		}
    }
	/**
	 * 录像查询接受信令服务器消息处理
	 * @param xml
	 */
	private void requestRecordInfo(String xml) {
		//将xml转化为对象
		RecordBean recordData = XmlUtil.toBean(RecordBean.class, xml);
		//获取会话id
		String reqKey = XmlUtil.genRelationKey(recordData.getDeviceID(), recordData.getSn());
		if(callMap.isEmpty() || relationMap.isEmpty()) {
			return;
		}
		String callId = relationMap.get(reqKey);
		//获取当前请求信息
		MessageBean msgData = (MessageBean) callMap.get(callId);

		//录像类型 time或alarm或manual或all
		//获取的当前保存的录像记录
		List<RecordItemBean>  saveList = null;
		if(callMediaRecordMap.containsKey(callId)) {
			//获取的当前保存的录像记录
			saveList = callMediaRecordMap.get(callId);
		}else {
			saveList = new ArrayList<>();
		}
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>recordData：{}",JSON.toJSONString(recordData));
//		if(logger.isDebugEnabled()) {
//			logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>recordData：{}",JSON.toJSONString(recordData));
//		}
		if(recordData.getRecordList()==null || recordData.getRecordList().isEmpty()) {
			//不存在录像，直接返回结果
			sendRecordMsg(msgData, recordData, Collections.EMPTY_LIST, callId, reqKey);
			return;
		}
		List<RecordItemBean> recordList = recordData.getRecordList();

		for(RecordItemBean item:recordList){
			String uri = item.getDeviceID()+URI_CHAR;
			item.setUri(uri);
			//保存到汇总集合中
			saveList.add(item);
		}
//		if(logger.isDebugEnabled()) {
//			logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>当前录像记录：{}",JSON.toJSONString(saveList));
//		}
		
		//跟新内存中的录像记录
		callMediaRecordMap.put(callId,saveList);
		
		boolean flag = false;
		//获取当前返回记录的段数
		int index = saveList.size();
		if(index == recordData.getSumNum()) {
			flag = true;
		}
		//判断亲求是否超时,则其他未返回片段丢失，直接返回当前结果
		long d1 = System.currentTimeMillis();
		int time = (int) ((d1-msgData.getReqTime())/1000);
//		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>当前录像记录时间：{}",msgData.getReqTime());
		if(time>sipConfig.getConnectionTimeout()) {
			flag = true;
		}
		if(flag) {
			//排序
			Collections.sort(saveList, (s1, s2) -> (CommonUtil.parseDate(s1.getStartTime())).compareTo(CommonUtil.parseDate(s2.getStartTime())));
			int k = 0;
			//保存录像集合
			List<RecordItemBean> itemList = new ArrayList<>();
			//跳槽循环标志
			boolean isFlag = false;
			logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>返回录像翻查过滤前前结果集：{}",JSON.toJSON(saveList));
			for(RecordItemBean bean:saveList){
				//判断第一段时间与查询时间对比,查询起始时间大于录像起始时间，则已查询起始时间开始
				if(CommonUtil.parseDate1(msgData.getStartTime()).getTime()>CommonUtil.parseDate(bean.getStartTime()).getTime()) {
					bean.setStartTime(msgData.getStartTime().replace(" ", "T"));
				}
				//录像结束时间大于查询结束时间
				if(CommonUtil.parseDate(bean.getEndTime()).getTime()>CommonUtil.parseDate1(msgData.getEndTime()).getTime()) {
					bean.setEndTime(msgData.getEndTime().replace(" ", "T"));
					isFlag = true;
				}
				if(k>0) {
					//获取前一段路线记录
					RecordItemBean previousReocrd = saveList.get(k-1);
					/**
					 * 当前段结束时间小于上一段结束时间,则调到下一次循环
					 */
					if(CommonUtil.parseDate(bean.getEndTime()).getTime() < CommonUtil.parseDate(previousReocrd.getEndTime()).getTime() ) {
						continue;
					}
					//比较当前段录像是否与上一段一段录像重合
					if(CommonUtil.parseDate(bean.getStartTime()).getTime() < CommonUtil.parseDate(previousReocrd.getEndTime()).getTime() ) {
						bean.setStartTime(previousReocrd.getEndTime());
					}

				}
				itemList.add(bean);
				//录像时间大于查询结束时间跳出循环
				if(isFlag) {
					break;
				}

				k++;
			}
			logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>返回录像翻查过滤后前结果集：{}",JSON.toJSON(itemList));
			recordData.setSumNum(itemList.size());
			//录像翻查发送websockete消息
			sendRecordMsg(msgData, recordData, itemList, callId, reqKey);
		}

	}
	/**
	 * 录像翻查发送websockete消息
	 * @param msgData
	 * @param recordData
	 * @param result
	 * @param callId
	 * @param reqKey
	 */
	private void sendRecordMsg(MessageBean msgData,RecordBean recordData,List result,String callId,String reqKey) {
		
		ResponseBean<RecordBean> respData = new ResponseBean<>();
		
		BeanUtils.copyProperties(msgData, respData);
		
		recordData.setStartTime(msgData.getStartTime().replace(" ", "T"));
		recordData.setEndTime(msgData.getEndTime().replace(" ", "T"));
		
		recordData.setState(SipConstants.STATE_OK);
		recordData.setCallId(callId);
		recordData.setIndex(result==null?0:result.size());
		recordData.setRecordList(result);
		respData.setResult(recordData);

		/**
		 * @TODO websockt发送消息 待完成
		 */
		socketService.sendUser(respData.getWsId(), respData);
		
		logger.info(">>>>>>>>>>>>>>>>录像查询callId:{}的结果：{}",callId,JSON.toJSONString(respData));
		
		callMap.remove(callId);
		relationMap.remove(reqKey);
		callMediaRecordMap.remove(callId);
	}
	/**
	 * 设备状态查询接受信令服务器消息处理
	 * @param xml
	 */
	private void requestDeviceStatus(String xml) {
		//将xml转化为对象
		DeviceStatusBean recordData = XmlUtil.getDeviceStatusBean(xml);
		
		//获取会话id
		String reqKey = XmlUtil.genRelationKey(recordData.getDeviceID(), recordData.getSn());
		String callId = relationMap.get(reqKey);
		//获取当前请求信息
		MessageBean msgData = (MessageBean) callMap.get(callId);
		
		ResponseBean<DeviceStatusBean> respData = new ResponseBean<>();
		BeanUtils.copyProperties(msgData, respData);
		respData.setResult(recordData);

		/**
		 * @TODO websockt发送消息 待完成
		 */
		socketService.sendUser(respData.getWsId(), respData);
		
		//清除当前callId相关信息
		callMap.remove(callId);
		relationMap.remove(reqKey);
		
		logger.info(">>>>>>>>>>>>>>>>设备查询callId:{}的结果：{}",callId,JSON.toJSONString(recordData));
	}
	
	/**
	 * 预置位查询接受信令服务器消息处理
	 * @param xml
	 */
	private void requestPresetQuery(String xml) {
		//将xml转化为对象
		PresetQueryBean recordData = XmlUtil.getPresetQueryBean(xml);
		
		//获取会话id
		String reqKey = XmlUtil.genRelationKey(recordData.getDeviceID(), recordData.getSn());
		if(callMap.isEmpty() || relationMap.isEmpty()) {
			return;
		}
		String callId = relationMap.get(reqKey);
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>callMap:{}",JSON.toJSONString(callMap));

		//获取当前请求信息
		MessageBean msgData = (MessageBean) callMap.get(callId);

		if(SipConstants.IS_WEBSOCKET) {
			requestPresetQueryByWS(recordData, msgData, callId);
		}else {
			requestPresetQueryByKafka(recordData, msgData, callId);
		}
		
		//清除当前callId相关信息
		callMap.remove(callId);
		relationMap.remove(reqKey);
		
		logger.info(">>>>>>>>>>>>>>>>设备预置位查询callId:{}的结果：{}",callId,JSON.toJSONString(recordData));
	}
	/**
	 * 预置位列表kafka消息类型处理
	 * @param recordData 返回结果
	 * @param msgData 请求参数
	 * @param callId 会话id
	 * 
	 */
	private void requestPresetQueryByKafka(PresetQueryBean recordData,MessageBean msgData,String callId) {

		List<PresetItemBean> recordList = recordData.getRecordList();
		//设备不存在预置位列表，则不发送消息，已联网平台数据为准
		if(recordList == null || recordList.isEmpty()) {
			return;
		}
		PresentListBean content = new  PresentListBean();
		//消息id
		String msgID = IdGenerator.nextId();
		content.setMsgID(msgID);
		content.setDeviceID(recordData.getDeviceID());
		//设备存在预置位列表，则发送kafak消息，联网平台增量更新预置位信息
		List<PresentBean> itemList = new ArrayList<>(recordList.size());
		recordList.stream().forEach(t->{
			PresentBean preset = new PresentBean();
			preset.setPresetNum(Integer.parseInt(t.getPresetID()));
			preset.setPresetName(t.getPresetName());
			itemList.add(preset);
		});
		content.setItemList(itemList);
		recordList = null;
		
		/**
		 * 推送消息到kafka,联网获取结果并检测
		 */
		sender.sendMsg(MsgTypeEnum.PRESET_QUERY_MSG.getValue(), JSON.toJSONString(content));
	}
	/**
	 * 预置位列表websocket消息类型处理
	 * @param recordData 返回结果
	 * @param msgData 请求参数
	 * @param callId 会话id
	 * 
	 */
	private void requestPresetQueryByWS(PresetQueryBean recordData,MessageBean msgData,String callId) {
		ResponseBean<PresetQueryBean> respData = new ResponseBean<>();
		BeanUtils.copyProperties(msgData, respData);
		
		List<PresetItemBean> recordList = recordData.getRecordList();
		if(recordList==null || recordList.isEmpty()) {
			recordData.setRecordList(Collections.emptyList());
		}else {
			Collections.sort(recordData.getRecordList(), (s1, s2) -> s1.getPresetID().compareTo(s2.getPresetID()));
			recordData.setRecordList(recordList);
		}
		recordData.setRecordList(recordList);
		recordData.setState(SipConstants.STATE_OK);
		recordData.setCallId(callId);
		
		respData.setResult(recordData);

		/**
		 * @TODO websockt发送消息 待完成
		 */
		socketService.sendUser(respData.getWsId(), respData);
	}
	/**
	 * 消息确认
	 * 
	 * @param responseEvent
	 */
	private void processResponseOk(ResponseEvent responseEvent) {
		try {
			Response resp = responseEvent.getResponse();
			ClientTransaction tid = responseEvent.getClientTransaction();
			CSeqHeader cseq = (CSeqHeader) resp.getHeader(CSeqHeader.NAME);
			CallIdHeader callIdHeader = (CallIdHeader) resp.getHeader(CallIdHeader.NAME);
			String callId = callIdHeader.getCallId();

			Dialog dialog = tid.getDialog();
			String method = cseq.getMethod();
			if (method.equals(Request.INVITE)) {
				
				Request ackRequest = dialog.createAck(cseq.getSeqNumber());
				dialog.sendAck(ackRequest);
//				LOG.info(">>>>>>>>>>>>>>>>>>>>>>callId:{}发送ack消息",callId);
				
				String sdp = new String(resp.getRawContent());
				
				Map<String,Object> dataMap = (Map<String, Object>) callMap.get(callId);
				//InviteBean对象不存在，则直接返回
				if(dataMap == null || !dataMap.containsKey(Request.INVITE)) {
					return;
				}
				InviteBean inviteData = (InviteBean) dataMap.get(Request.INVITE);
				//获取流媒体服务器地址
				String mediaIp = inviteData.getHost();
				int port = inviteData.getPort();
//				LOG.info(">>>>>>>>>>>>>>>>>>>>>>port:{}",port);
				if(SipConstants.TRANSPORT_TCP.equals(inviteData.getTransport().toLowerCase())) {
					int index1= sdp.indexOf("IP4 ");
					mediaIp = sdp.substring(index1+4);
					index1 = mediaIp.indexOf("\r\n");
					mediaIp = mediaIp.substring(0,index1);
					
					index1= sdp.indexOf("video ");
					String portStr = sdp.substring(index1+6);
					index1 = portStr.indexOf(" TCP/RTP/AVP");
					portStr = portStr.substring(0,index1);
					port = Integer.parseInt(portStr);
				}
				ResponseBean<InviteEntity> respData = new ResponseBean<>();
				BeanUtils.copyProperties(inviteData, respData);
				
				InviteEntity invite = new InviteEntity();
				BeanUtils.copyProperties(inviteData, invite);
				invite.setState(SipConstants.STATE_OK);
				invite.setSdp(sdp);
				invite.setPort(port);
				invite.setHost(mediaIp);
				
				respData.setResult(invite);
				
				/**
				 * @TODO websockt发送消息 待完成
				 */
				socketService.sendUser(respData.getWsId(), respData);
				
				/**
				 * 保存wsID与callID对应关系，用途  websocket自动断开连接时，根据callID自动关流
				 */
				addCacheInvite(respData.getWsId(), callId);
				
				LOG.info(">>>>>>>>>>>>>>>>>>>>>>发送实时视频callId:{}播放成功通知{}",callId,JSON.toJSONString(respData));
			} else if (method.equals(Request.MESSAGE)) {
				processMessageResponse(callId);
				
			} else if (method.equals(Request.REGISTER)) {
				callMap.remove(callId);
				LOG.info(">>>>>>>>>>>>>>>>>>>>>>注册成功，删除会话callId:{}成功",callId);
			} else if (method.equals(Request.BYE)) {
				processByeResponse(callId);
			} else if (method.equals(Request.CANCEL)) {
				logger.info("Sending BYE for late CANCEL");
				Request byeRequest = dialog.createRequest(Request.BYE);
				ClientTransaction ct = sipProvider.getNewClientTransaction(byeRequest);
				dialog.sendRequest(ct);
			}
		} catch (Exception ex) {
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>processResponseOk异常，原因：{}",ex.getMessage());
		}
	}
	/**
	 * bye消息接受处理
	 * @param callId
	 */
	private void processByeResponse(String callId) {
		
		Map<String,Object> dataMap = (Map<String, Object>) callMap.get(callId);
		
		//BYE对象不存在，则直接返回
		if(dataMap == null || !dataMap.containsKey(Request.BYE) || !dataMap.containsKey(Request.INVITE)) {
			return;
		}
		InviteBean inviteBean = (InviteBean) dataMap.get(Request.INVITE);
		ByeBean byeData = (ByeBean) dataMap.get(Request.BYE);
		
		ResponseBean<SipResultBean> respData = new ResponseBean<SipResultBean>();
		BeanUtils.copyProperties(byeData, respData);
		
		SipResultBean resultBean = new SipResultBean(callId, SipConstants.STATE_OK);
		respData.setResult(resultBean);
		
		/**
		 * @TODO websockt发送消息 待完成
		 */
		socketService.sendUser(respData.getWsId(), respData);
		
		
		/**
		 * 删除wsID与callID对应关系
		 */
		delCacheInvite(respData.getWsId(), callId,inviteBean.getPort());
		
		//清除当前callId相关信息
		callMap.remove(callId);
		//删除会话对象
		tranMap.remove(callId);
		
		LOG.info(">>>>>>>>>>>>>>>>>>>>>>发送停止实时视频callId:{}成功通知",callId);
	}
	/**
	 * message 消息接受处理
	 * @param callId 会话id
	 */
	private void processMessageResponse(String callId) {
		MessageBean msgData = (MessageBean) callMap.get(callId);
		String msgMethod = msgData.getMethod();

		if(MethodEum.PRESET.getValue().equals(msgMethod)) {
			//获取操作类型
			PresetTypeEnum typeEm = PresetTypeEnum.getObjectByKey(msgData.getType());
			boolean flag = false;
			//预置位操作为调用和删除，发送消息到kafka,通知联网平台同步预置位数据
			if(!PresetTypeEnum.TRANSFER.equals(typeEm)) {
				PresentBean presetBean = new PresentBean();
				BeanUtils.copyProperties(msgData, presetBean);
				presetBean.setDeviceID(msgData.getTargetDeviceCode());
				String msgID = IdGenerator.nextId();
				presetBean.setMsgID(msgID);
				//获取用户信息
				WsCacheBean wsBean =WebSocketManager.get(msgData.getWsId());
				presetBean.setUserId(wsBean.getUserId());
				
				sender.sendMsg(MsgTypeEnum.PRESET_MSG.getValue(), JSON.toJSONString(presetBean));
				
				LOG.info(">>>>>>>>>>>>>>>>>>>发送kafka消息,消息内容presetBean:{}",JSON.toJSONString(presetBean));
				
				flag = true;
			}
			
			ResponseBean<SipResultBean> respData = new ResponseBean<SipResultBean>();
			BeanUtils.copyProperties(msgData, respData);
			
			SipResultBean resultBean = new SipResultBean(callId, SipConstants.STATE_OK);
			respData.setResult(resultBean);
			
			/**
			 * @TODO websockt发送消息 待完成
			 */
			socketService.sendUser(respData.getWsId(), respData);
			
			if(flag) {
				/**
				 * 然后调用预置位列表接口
				 * 延时N秒执行
				 */
				new Timer().schedule(new PresetQueryTask(msgData.getRegisterCode(),msgData.getTargetDeviceCode(),msgData.getSn()), sipConfig.getPresetListInvokeInterval()) ;
			}
		}else if(MethodEum.CRUISE.getValue().equals(msgMethod)) {
			//获取操作类型
			CruiseTypeEnum typeEm = CruiseTypeEnum.getObjectByKey(msgData.getType());
			//球机巡航预案控制操作不为启动和停止，则发送kafka消息,通知联网平台同步巡航预案数据
			if(!CruiseTypeEnum.START.equals(typeEm) && !CruiseTypeEnum.STOP.equals(typeEm)) {
				CruiseBean cruiseBean = new CruiseBean();
				BeanUtils.copyProperties(msgData, cruiseBean);
				cruiseBean.setDeviceID(msgData.getTargetDeviceCode());
				String msgID = IdGenerator.nextId();
				cruiseBean.setMsgID(msgID);
				//获取用户信息
				WsCacheBean wsBean =WebSocketManager.get(msgData.getWsId());
				cruiseBean.setUserId(wsBean.getUserId());
				
				sender.sendMsg(MsgTypeEnum.CRUISE_MSG.getValue(), JSON.toJSONString(cruiseBean));
				
				LOG.info(">>>>>>>>>>>>>>>>>>>发送kafka消息,消息内容cruiseBean:{}",JSON.toJSONString(cruiseBean));
			}
			
			ResponseBean<SipResultBean> respData = new ResponseBean<SipResultBean>();
			BeanUtils.copyProperties(msgData, respData);
			
			SipResultBean resultBean = new SipResultBean(callId, SipConstants.STATE_OK);
			respData.setResult(resultBean);
			
			/**
			 * @TODO websockt发送消息 待完成
			 */
			socketService.sendUser(respData.getWsId(), respData);
		}else if(MethodEum.HOMEPOSITION.getValue().equals(msgMethod)) {
			ResponseBean<SipResultBean> respData = new ResponseBean<SipResultBean>();
			BeanUtils.copyProperties(msgData, respData);
			
			SipResultBean resultBean = new SipResultBean(callId, SipConstants.STATE_OK);
			respData.setResult(resultBean);
			
			/**
			 * @TODO websockt发送消息 待完成
			 */
			socketService.sendUser(respData.getWsId(), respData);
		}
	}
	/**
	 * 设备注册发送认证信息
	 * 
	 * @param responseEvent
	 */
	private void processResponseAuth(ResponseEvent responseEvent) {
		try {
			Response resp = responseEvent.getResponse();
			ClientTransaction tid = responseEvent.getClientTransaction();

			CallIdHeader callIdHeader = (CallIdHeader) resp.getHeader(CallIdHeader.NAME);
			String callId = callIdHeader.getCallId();
			RegisterBean model = (RegisterBean) callMap.get(callId);
			String registerCode = model.getRegisterCode();

			String registerPwd = sipConfig.getDefaultPassword();

			/**
			 * 发送认证信息
			 */
			AuthenticationHelper authenticationHelper = ((SipStackExt) sipStack)
					.getAuthenticationHelper(new AccountManagerImpl(registerCode, registerPwd), headerFactory);
			ClientTransaction inviteTid = authenticationHelper.handleChallenge(resp, tid, sipProvider, 5, true);

			inviteTid.sendRequest();
		} catch (Exception ex) {
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>processResponseAuth异常，原因：{}",ex.getMessage());
		}
	}
	
	class ByeTask extends TimerTask {
		Dialog dialog;

		public ByeTask(Dialog dialog) {
			this.dialog = dialog;
		}

		public void run() {
			byeTaskRunning = false;
			timer = null;

			try {
				Request byeRequest = this.dialog.createRequest(Request.BYE);

				ClientTransaction ct = sipProvider.getNewClientTransaction(byeRequest);

				dialog.sendRequest(ct);
			} catch (Exception ex) {
			}
		}
	}
	/**
	 * 预置位查询定时任务
	 * @author Administrator
	 *
	 */
	class PresetQueryTask extends TimerTask {
		String deviceID;
		Long invco;
		String registerCode;
		
		public PresetQueryTask(String registerCode,String deviceID,Long invco) {
			this.deviceID = deviceID;
			this.registerCode = registerCode;
			this.invco = invco;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				invco += 1;
				String sdpData = SipMsgUtils.presetQueryMsg(invco, deviceID);
				
				MessageBean msgBean = new MessageBean();
				msgBean.setMethod(MethodEum.QUERYPRESET.getValue());
				
				msgBean.setTargetDeviceCode(deviceID);
				msgBean.setSdpData(sdpData);
				msgBean.setSn(invco);
				msgBean.setIsWebsocket(false);
				processMessage(msgBean, registerCode, MessageTypeEnum.PRESET_QUERY);
			} catch (Exception ex) {
				logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>PresetQueryTask异常，原因：{}",ex.getMessage());
			}
		}
	}
	/**
	 * 获取发送端媒体流序列号
	 * 
	 * @return
	 */
	private String getSendMediaNo(String method) {
		/**
		 * 当请求为实时视频时，首位取值为0，对于相同的实时视频取值唯一；当请求的媒体流为历史视频时，首位取值为1，对于每一路历史视频取值唯一。
		 */
		String no = "00";
		if(MethodEum.PLAYBACK.getValue().equals(method)) {
			no = "10";
		}
		return no + CommonUtil.getTags();
	}


	/**
	 * 获取发送端媒体流序列号
	 * 
	 * @return
	 */
	private String getReceiveMediaNo() {
		return "001";
	}
	/**
	 * 保存用户视频点播  录像回看  录像下载 视频流的会话callId
	 *    用于websocket断开连接，自动关流
	 * @param wsID
	 * @param callID 会话id
	 */
	private void addCacheInvite(String wsID,String callID) {
		WsCacheBean wsBean = WebSocketManager.get(wsID);
		if(wsBean == null) {
			return;
		}
		Map<String, Object> sipMap = wsBean.getSipMap();
		sipMap.put(callID, callID);
		
	}

	/**
	 * 删除Invtive流与websocket对应关系
	 * @param wsID
	 * @param callID 会话id
	 * @param port 端口
	 */
	private void delCacheInvite(String wsID,String callID,int port) {
		WsCacheBean wsBean = WebSocketManager.get(wsID);
		if(wsBean==null) {
			return;
		}
		Map<String, Object> sipMap = wsBean.getSipMap();
		sipMap.remove(callID, callID);
		
		wsBean.setSipMap(sipMap);
		
		LOG.info("-------------------------callId:{}的Invtive流关闭删除wsBean对象中sipMap:{}",callID);
		
		WebSocketManager.put(wsID, wsBean);
	}
}
