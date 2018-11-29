package com.hdvon.sip.app;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
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

import com.alibaba.fastjson.JSON;
import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.exception.SipSystemException;
import com.hdvon.sip.utils.CommonUtil;
import com.hdvon.sip.utils.SipConstants;
import com.hdvon.sip.vo.MediaPlayQuery;
import com.hdvon.sip.vo.MediaPlayVo;
/**
 *  视频点播业务处理
 * @author wanshaojian
 *
 */
public class MediaClient implements SipListener{

	public Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 保存当前请求call-ID与MediaPlayVo对象关系
	 */
	private ConcurrentHashMap<String, MediaPlayVo> mediaPlayMap = new ConcurrentHashMap<>();
	
	
	/**
	 * 保存当前请求call-ID与MediaPlayVo对象关系
	 */
	private ConcurrentHashMap<String, Dialog> clientTranMap = new ConcurrentHashMap<>();
	
	private volatile static MediaClient instance;
	
	private SipConfig sipConfig;
	
	private SipProvider sipProvider;

	private AddressFactory addressFactory;

	private MessageFactory messageFactory;

	private HeaderFactory headerFactory;
	
	private AtomicLong cSeqCounter = new AtomicLong(1);
	
	
	private MediaClient(SipConfig sipConfig) {
		this.sipConfig = sipConfig;
	}
	
	public static MediaClient getInstance(SipConfig sipConfig){
        if(instance==null){
            synchronized (MediaClient.class){
                if(instance==null){
                    instance=new MediaClient(sipConfig);
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
	 * 发送视频播放请求
	 * 
	 * @param model
	 * 			 视频请求对象
	 * @param registerCode
	 *          注册用户对象
	 * @return MediaBean
	 * @throws SipSystemException
	 */
	public MediaPlayVo videoPlay(MediaPlayQuery model,String registerCode) throws SipSystemException {
		MediaPlayVo bean = new MediaPlayVo();
		BeanUtils.copyProperties(model, bean);
		
		mediaProcess(bean, registerCode);
		if(bean.getStatus() == null) {
			throw new SipSystemException("视频点播["+JSON.toJSONString(model)+"]点播失败！") ;
		}
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
	private void mediaProcess(MediaPlayVo model,String registerCode) throws SipSystemException {
		String callID = null;
		try {
			// 获取端口
			int clientPort = PortPoolManager.getInstance().getPool();
			model.setReceivePort(clientPort);
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
			
			
			String sdpData = MediaMsgUtil.videoPlayMsg(registerCode, model.getReceiveIp(),clientPort);
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
			
			clientTranMap.put(callID, inviteTid.getDialog());
			
			inviteTid.sendRequest();

			Thread.sleep(800);
			
			model = mediaPlayMap.get(callID);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new SipSystemException(e.getMessage());
		} 
		

		
	}
	
	

	/**
	 * 发送停止命令
	 * @param model
	 */
	public void sendBye(MediaPlayVo model) {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>stop bye");
		
		try {
			Dialog dialog = clientTranMap.get(model.getCallId());
			
			Request byeRequest = dialog.createRequest(Request.BYE);
			ClientTransaction ct = sipProvider.getNewClientTransaction(byeRequest);
			dialog.sendRequest(ct);
			
			Thread.sleep(800);
			
			//删除map数据
			clientTranMap.remove(model.getCallId());
			mediaPlayMap.remove(model.getCallId());
			
			//关闭资源
			close();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>stop bye error{}",e);
			throw new SipSystemException(e);
		}finally {
			
			
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
		
		String hashCode = responseEvent.getSource().toString();
		System.out.println(">>>>>>>>>>>>>>>>"+hashCode);
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
		        MediaPlayVo model = mediaPlayMap.get(callId);
		        Dialog dialog = clientTranMap.get(callId);
				
				playStatus = SipConstants.SUCCESS;
				model.setStatus(playStatus);
				mediaPlayMap.put(callId,model);
				
				if (cseq.getMethod().equals(Request.INVITE)) {
					Request ackRequest = dialog.createAck(cseq.getSeqNumber());
					logger.info("Sending ACK");
					dialog.sendAck(ackRequest);
					
				}else if (cseq.getMethod().equals(Request.BYE)) {
					//回收端口
					ReceivePortManager.getInstance().destroy(model.getReceivePort());
                }

			}else if(response.getStatusCode() == Response.TRYING) {
				
			} else {
				logger.info("response = " + response);
				MediaPlayVo model = mediaPlayMap.get(callId);
				model.setStatus(playStatus);
				mediaPlayMap.put(callId,model);
				
				//回收端口
				ReceivePortManager.getInstance().destroy(model.getReceivePort());
				
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
		MediaPlayVo model = mediaPlayMap.get(callId);
		
		//关闭资源
		close();
		//回收端口
		ReceivePortManager.getInstance().destroy(model.getReceivePort());
		
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

	/**
	 * 关闭资源
	 * @throws ObjectInUseException 
	 */
	private void close() {
		logger.info(">>>>>>>>>>>>>>>>>>>MediaClient关闭资源");
		
		//删除当前sip
		sipProvider.removeSipListener(this);
		try {
			SipStackManage.getSipStack().deleteSipProvider(sipProvider);
		} catch (ObjectInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
