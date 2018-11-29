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
import com.hdvon.sip.exception.SipSystemException;
import com.hdvon.sip.utils.CommonUtil;
import com.hdvon.sip.utils.SipConstants;
import com.hdvon.sip.vo.CloudControlQuery;
import com.hdvon.sip.vo.CloudControlQuery.CloudControlTypeEnum;
import com.hdvon.sip.vo.CloudControlQuery.DirectionEnum;
import com.hdvon.sip.vo.CloudControlQuery.FocusEnum;
import com.hdvon.sip.vo.CloudControlQuery.IrisEnum;
import com.hdvon.sip.vo.CloudControlQuery.ZoomEnum;
import com.hdvon.sip.vo.CloudControlVo;
/**
 * 云台控制处理
 * 	包括 1：云台方向控制
 *     2：摄像机变焦控制
 *     3：摄像机光圈控制
 *     4：摄像机调焦控制
 * @author wanshaojian
 *
 */
public class CloudControlClient implements SipListener,AutoCloseable{

	public Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 保存当前请求call-ID与CloudControlVo对象关系
	 */
	private ConcurrentHashMap<String, CloudControlVo> cloudMap = new ConcurrentHashMap<>();
	
	private volatile static CloudControlClient instance;
	
	private SipConfig sipConfig;
	
	private SipProvider sipProvider;

	private AddressFactory addressFactory;

	private MessageFactory messageFactory;

	private HeaderFactory headerFactory;
	
	private AtomicLong cSeqCounter = new AtomicLong(1);
	
	
	private CloudControlClient(SipConfig sipConfig) {
		this.sipConfig = sipConfig;
	}

	public static CloudControlClient getInstance(SipConfig sipConfig){
        if(instance==null){
            synchronized (CloudControlClient.class){
                if(instance==null){
                    instance=new CloudControlClient(sipConfig);
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
	public void cloudControl(CloudControlQuery model,String registerCode) throws SipSystemException {
		String ptzCmd = null;
		if(model.getDirection()!=null) {
			model.setTypeEnum(CloudControlTypeEnum.DIRECTION);
			//发送方向控制指令
			ptzCmd = MediaMsgUtil.genDirectionPTZCmd(model.getDirection(), model.getStepSize());
			cloudControlProcess(model, registerCode, ptzCmd);
			//停止方向控制指令
			ptzCmd = MediaMsgUtil.genDirectionPTZCmd(DirectionEnum.STOP, 0);
			cloudControlProcess(model, registerCode, ptzCmd);
		}else if(model.getZoom()!=null) {
			model.setTypeEnum(CloudControlTypeEnum.ZOOM);
			//发送镜头变倍指令
			ptzCmd = MediaMsgUtil.genZoomPTZCmd(model.getZoom(), model.getStepSize());
			cloudControlProcess(model, registerCode, ptzCmd);
			//停止镜头变倍指令
			ptzCmd = MediaMsgUtil.genZoomPTZCmd(ZoomEnum.STOP, 0);
			cloudControlProcess(model, registerCode, ptzCmd);
		}else if(model.getIris()!=null) {
			model.setTypeEnum(CloudControlTypeEnum.IRIS);
			//发送镜头光圈变动指令
			ptzCmd = MediaMsgUtil.genIrisPTZCmd(model.getIris(), model.getStepSize());
			cloudControlProcess(model, registerCode, ptzCmd);
			//停止镜头光圈变动指令
			ptzCmd = MediaMsgUtil.genIrisPTZCmd(IrisEnum.STOP, 0);
			cloudControlProcess(model, registerCode, ptzCmd);
		}else {
			model.setTypeEnum(CloudControlTypeEnum.FOCUS);
			//发送镜头聚焦变动指令
			ptzCmd = MediaMsgUtil.genFocusPTZCmd(model.getFocus(), model.getStepSize());
			cloudControlProcess(model, registerCode, ptzCmd);
			//停止镜头聚焦变动指令
			ptzCmd = MediaMsgUtil.genFocusPTZCmd(FocusEnum.STOP, 0);
			cloudControlProcess(model, registerCode, ptzCmd);
		}
		
	}

	
	/**
	 * 媒体资源处理
	 * 
	 * @param model
	 * 			 云台控制对象
	 * @param registerCode
	 *          注册用户对象
	 * @param ptz
	 * 		    cmd云台控制指令       
	 * @return
	 * @throws SipSystemException
	 */
	private CloudControlVo cloudControlProcess(CloudControlQuery model,String registerCode,String ptzCmd) throws SipSystemException {
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
			
			CloudControlVo vo = new CloudControlVo();
			vo.setDeviceCode(model.getDeviceCode());
			vo.setType(model.getTypeEnum().getValue());
			vo.setDirection(model.getTypeEnum().equals(CloudControlTypeEnum.DIRECTION) ? model.getDirection().getKey() : null);
			vo.setFocus(model.getTypeEnum().equals(CloudControlTypeEnum.FOCUS) ? model.getFocus().getKey() : null);
			vo.setIris(model.getTypeEnum().equals(CloudControlTypeEnum.IRIS) ? model.getIris().getKey() : null);
			vo.setZoom(model.getTypeEnum().equals(CloudControlTypeEnum.ZOOM) ? model.getZoom().getKey() : null);
			vo.setStepSize(model.getStepSize());
			vo.setCallId(callId);
			
	        //将异步请求转换为同步请求
			CountDownLatch latch = new CountDownLatch(1);
			vo.setLatch(latch);
			
			cloudMap.put(callId, vo);
			//发送消息
			ClientTransaction inviteTid = sipProvider.getNewClientTransaction(request);
			inviteTid.sendRequest();
			
			//阻塞方法
			try {
				latch.await(); //阻塞等待计数为0
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			CloudControlVo data = cloudMap.get(callId);
			
            //清除map数据
			cloudMap.remove(callId);
			//回收端口
			PortPoolManager.getInstance().destroy(clientPort);
			
			return data;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new SipSystemException(e.getMessage());
		} 

		
	}

	@Override
	public void processResponse(ResponseEvent responseEvent) {
		// TODO Auto-generated method stub
		Response resp = (Response) responseEvent.getResponse();
		ClientTransaction tid = responseEvent.getClientTransaction();
		CSeqHeader cseq = (CSeqHeader) resp.getHeader(CSeqHeader.NAME);

		logger.info("Response received : Status Code = " + resp.getStatusCode() + " " + cseq);
		if (tid == null) {
			logger.info("Stray response -- dropping ");
			return;
		}
		CallIdHeader callIdHeader = (CallIdHeader) resp.getHeader(CallIdHeader.NAME);
		String callId = callIdHeader.getCallId();

		CloudControlVo model = cloudMap.get(callId);
		
		int checkStatus = SipConstants.FAIL;
		if (resp.getStatusCode() == Response.OK) {
			checkStatus = SipConstants.SUCCESS;
		}
		model.setStatus(checkStatus);
		model.getLatch().countDown(); //计数减1
		
		cloudMap.put(callId, model);
	}

	@Override
	public void close() throws IOException, ObjectInUseException {
		// TODO Auto-generated method stub
		logger.info(">>>>>>>>>>>>>>>>>>>CloudControlClient关闭资源");
		
		//删除当前sip
		sipProvider.removeSipListener(this);
		SipStackManage.getSipStack().deleteSipProvider(sipProvider);
		
	}

	@Override
	public void processRequest(RequestEvent requestEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processTimeout(TimeoutEvent event) {
		CallIdHeader callIdHeader = (CallIdHeader) event.getClientTransaction().getRequest().getHeader(CallIdHeader.NAME);
		String callId = callIdHeader.getCallId();
		CloudControlVo bean = cloudMap.get(callId);
		bean.setStatus(SipConstants.TIMEOUT_FAIL);
		cloudMap.put(callId, bean);
		
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
