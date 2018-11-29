package com.hdvon.sip.app;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.ListeningPoint;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipFactory;
import javax.sip.TimeoutEvent;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.SubjectHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.exception.SipSystemException;
import com.hdvon.sip.utils.CommonUtil;
import com.hdvon.sip.utils.SipConstants;
import com.hdvon.sip.vo.MediaDownloadQuery;
import com.hdvon.sip.vo.MediaDownloadVo;
/**
 * 视频下载业务处理
 * @author wanshaojian
 *
 */
public class MediaDownloadClient extends SipAdapter{

	public Logger logger = LoggerFactory.getLogger(getClass());

	private volatile static MediaDownloadClient instance;
	
	/**
	 * 保存当前请求call-ID与MediaPlayVo对象关系
	 */
	private ConcurrentHashMap<String, MediaDownloadVo> mediaPlayMap = new ConcurrentHashMap<>();
	
	
	/**
	 * 保存当前请求call-ID与MediaPlayVo对象关系
	 */
	private ConcurrentHashMap<String, ClientTransaction> clientTranMap = new ConcurrentHashMap<>();
	
	private AtomicLong snCounter = new AtomicLong(1);
	
	private MediaDownloadClient(SipConfig sipConfig) {
		this.sipConfig = sipConfig;
		
		try {
			String host = sipConfig.getClientIp();
			String ipAddress = sipConfig.getClientIp();
			
			Properties properties = new Properties();
			properties.setProperty("javax.sip.IP_ADDRESS", ipAddress);
			properties.setProperty("javax.sip.STACK_NAME", "hdvon-download-stack");
			properties.setProperty("gov.nist.javax.sip.MAX_MESSAGE_SIZE", "1048576");
			properties.setProperty("gov.nist.javax.sip.DEBUG_LOG", "sipAuthdebug.txt");
			properties.setProperty("gov.nist.javax.sip.SERVER_LOG", "sipAuthlog.txt");
			properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", new Integer(logLevel).toString());
			// Drop the client connection after we are done with the transaction.
			properties.setProperty("gov.nist.javax.sip.CACHE_CLIENT_CONNECTIONS", "false");
			
			properties.setProperty("gov.nist.javax.sip.READ_TIMEOUT", "1000");
	        properties.setProperty("gov.nist.javax.sip.NIO_BLOCKING_MODE", "NONBLOCKING");
	        
			// Create SipStack object
			this.sipFactory = SipFactory.getInstance();
            this.sipFactory.setPathName(SipConstants.SIP_DOMAIN);
            this.sipStack = this.sipFactory.createSipStack(properties);
            this.sipStack.start();

			// 获取端口
			int port = PortPoolManager.getInstance().getPool();
					
			ListeningPoint lp = this.sipStack.createListeningPoint(host, port, SipConstants.TRANSPORT_UDP);
			        
			this.sipProvider = this.sipStack.createSipProvider(lp);
			headerFactory = this.sipFactory.createHeaderFactory();
			messageFactory = this.sipFactory.createMessageFactory();
			addressFactory = this.sipFactory.createAddressFactory();
			
			this.sipProvider.addSipListener(this);
		} catch (Exception e) {
			// could not find gov.nist.jain.protocol.ip.sip.SipStackImpl in the
			// classpath
			e.printStackTrace();
			logger.error(e.getMessage());
		}

	}
	
	public static MediaDownloadClient getInstance(SipConfig sipConfig){
        if(instance==null){
            synchronized (MediaDownloadClient.class){
                if(instance==null){
                    instance=new MediaDownloadClient(sipConfig);
                }
            }
        }
        return instance;
    }


	/**
	 * 发送视频下载请求
	 * 
	 * @param model
	 * 			 视频请求对象
	 * @param registerCode
	 *          注册用户对象
	 * @return
	 * @throws SipSystemException
	 */
	public MediaDownloadVo videoDownlaod(MediaDownloadQuery model,String registerCode) throws SipSystemException {
		MediaDownloadVo bean = new MediaDownloadVo();
		BeanUtils.copyProperties(model, bean);
		
		/**
		 * 视频下载
		 */
		long startTime = model.getStartDate().getTime()/1000;
		long endTime = model.getEndDate().getTime()/1000;
		
		//自动分配媒体流接受端端口
		Integer receivePort = ReceivePortManager.getInstance().getPool();
		bean.setReceivePort(receivePort);
		
		String sdpData = MediaMsgUtil.downloadMsg(registerCode, model.getReceiveIp(),receivePort, model.getUri(), startTime, endTime);
		
		mediaProcess(bean, registerCode, sdpData);
		
		return bean;

	}
	/**
	 * 媒体资源处理
	 * 
	 * @param model
	 * 			 视频请求对象
	 * @param registerCode
	 *          注册用户对象
	 * @param sdpData
	 * 			请求报文
	 * @return
	 * @throws SipSystemException
	 */
	private void mediaProcess(MediaDownloadVo model,String registerCode,String sdpData) throws SipSystemException {
		String callID = null;
		try {
			//获取端口
			int clientPort = sipProvider.getListeningPoint(model.getTransportType()).getPort();
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
			String transportType = model.getTransportType();
			SipURI requestURI = addressFactory.createSipURI(deviceCode, serverAddress);
			requestURI.setTransportParam(transportType);
			
			//sip客户端的ip地址
			String clientIp = sipConfig.getClientIp();
			
			//创建Via 头数组
			ArrayList<ViaHeader> viaHeaders = new ArrayList<>();
			ViaHeader viaHeader = headerFactory.createViaHeader(clientIp,
					clientPort, transportType, null);
			viaHeaders.add(viaHeader);
            
            //创建Call-ID 头
			CallIdHeader callIdHeader = sipProvider.getNewCallId();
			//创建CSeq 头
			CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(cSeqCounter.getAndIncrement(), Request.INVITE);
			//Max-forwards 头
			MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70);
			
			//实例化实际的 SIP 消息本身
			Request request = messageFactory.createRequest(requestURI, Request.INVITE, callIdHeader,
					cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
			
			byte[] contents = sdpData.getBytes();
			// 添加消息的内容
			ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("Application",
					"sdp");

			request.setContent(contents, contentTypeHeader);
			
			//消息添加Contact 头
			SipURI contactURI = addressFactory.createSipURI(registerCode, clientIp);
			contactURI.setPort(clientPort);

			Address contactAddress = addressFactory.createAddress(contactURI);
			ContactHeader contactHeader = headerFactory.createContactHeader(contactAddress);
			request.addHeader(contactHeader);

			//消息添加subject
			StringBuffer subject = new StringBuffer();
			subject.append(deviceCode).append(":").append(getSendMediaNo());
			subject.append(",").append(registerCode).append(":").append(getReceiveMediaNo());
			SubjectHeader subjectHeader = headerFactory.createSubjectHeader(subject.toString());
			request.addHeader(subjectHeader);

	        //保存事务CallId与注册用户对应关系
			callID = callIdHeader.getCallId();
			model.setCallId(callID);
			mediaPlayMap.put(callID,model);
			
			//发送消息
			ClientTransaction inviteTid = sipProvider.getNewClientTransaction(request);
			clientTranMap.put(callID, inviteTid);
			inviteTid.sendRequest();
			
			CountDownLatch latch = new CountDownLatch(1);
			model.setLatch(latch);
			
			try {
				latch.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			model = mediaPlayMap.get(callID);
		} catch (Exception e) {
			// TODO: handle exception
			throw new SipSystemException(e.getMessage());
		} 
		
		
	}
	

	
	
	

	/**
	 * 发送停止命令
	 * @param model
	 */
	public void sendBye(MediaDownloadVo model) {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>stop bye");
		
		try {
    		ClientTransaction inviteTid = clientTranMap.get(model.getCallId());
            Dialog dialog = inviteTid.getDialog();
            
			CountDownLatch latch = new CountDownLatch(1);
			model.setLatch(latch);
			
			Request byeRequest = dialog.createRequest(Request.BYE);
			ClientTransaction ct = sipProvider.getNewClientTransaction(byeRequest);
			dialog.sendRequest(ct);
			
			try {
				latch.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//删除map数据
			clientTranMap.remove(model.getCallId());
			
			model.setStatus(SipConstants.SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>stop bye error{}",e);
			throw new SipSystemException(e);
		}

	}


	/**
	 * 获取发送端媒体流序列号
	 * 
	 * @return
	 */
	private String getSendMediaNo() {
		/**
		 * 当请求为实时视频时，首位取值为0，对于相同的实时视频取值唯一；当请求的媒体流为历史视频时，首位取值为1，对于每一路历史视频取值唯一。
		 */
		String no = "00";
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

	@Override
	public void processRequest(RequestEvent requestEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processResponse(ResponseEvent responseEvent) {
		// TODO Auto-generated method stub
		logger.info("Got a response");
		Response response = (Response) responseEvent.getResponse();
		ClientTransaction tid = responseEvent.getClientTransaction();
		CSeqHeader cseq = (CSeqHeader) response.getHeader(CSeqHeader.NAME);

		logger.info("Response received : Status Code = " + response.getStatusCode() + " " + cseq);
		if (tid == null) {
			logger.info("Stray response -- dropping ");
			return;
		}

		
		
		logger.info("transaction state is " + tid.getState());
		logger.info("Dialog = " + tid.getDialog());
		logger.info("Dialog State is " + tid.getDialog().getState());
		try {
			CallIdHeader callIdHeader = (CallIdHeader) response.getHeader(CallIdHeader.NAME);
	        String callId = callIdHeader.getCallId();
	        
			int playStatus = SipConstants.FAIL;
			if (response.getStatusCode() == Response.OK) {
				MediaDownloadVo model = mediaPlayMap.get(callId);
		        
				ClientTransaction inviteTid = clientTranMap.get(callId);
				Dialog dialog = inviteTid.getDialog();
				
				playStatus = SipConstants.SUCCESS;
				model.setStatus(playStatus);
				mediaPlayMap.put(callId,model);
				
				if (cseq.getMethod().equals(Request.INVITE)) {
					model.getLatch().countDown(); //计数减1
					
					Request ackRequest = dialog.createAck(cseq.getSeqNumber());
					logger.info("Sending ACK");
					dialog.sendAck(ackRequest);
				}else if (cseq.getMethod().equals(Request.BYE)) {
					model.getLatch().countDown(); //计数减1
                }

			}else if(response.getStatusCode() == Response.TRYING) {
				
			} else {
				logger.info("response = " + response);
				MediaDownloadVo model = mediaPlayMap.get(callId);
				
				model.getLatch().countDown(); //计数减1
				
				mediaPlayMap.put(callId,model);
				logger.error(">>>>>>>>>>>>>>>>>>>>>>>>点播{}视频失败！",JSON.toJSONString(model));
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	@Override
	public void processTimeout(TimeoutEvent event) {
		CallIdHeader callIdHeader = event.getClientTransaction().getDialog().getCallId();
		String callId = callIdHeader.getCallId();
		MediaDownloadVo mediaVo = mediaPlayMap.get(callId);
		
		mediaVo.getLatch().countDown(); //计数减1
		
	}
	
}
