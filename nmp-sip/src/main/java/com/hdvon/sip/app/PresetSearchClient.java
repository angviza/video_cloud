package com.hdvon.sip.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.exception.SipSystemException;
import com.hdvon.sip.utils.CommonUtil;
import com.hdvon.sip.utils.SipConstants;
import com.hdvon.sip.vo.DevicePresetVo;
/**
 * 预置位查询
 * @author wanshaojian
 *
 */
public class PresetSearchClient implements SipListener, AutoCloseable{
	public Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final String SPLIT_CHAR = "@";
	 /** 
	  * 绑定callId与请求对象对应关系
	  */ 
	private ConcurrentHashMap<String, DevicePresetVo> deviceMap = new ConcurrentHashMap<>(); 
	
	private volatile static PresetSearchClient instance;
	
	private SipConfig sipConfig;
	
	private SipProvider sipProvider;

	private AddressFactory addressFactory;

	private MessageFactory messageFactory;

	private HeaderFactory headerFactory;
	
	private AtomicLong cSeqCounter = new AtomicLong(1);
	
	
	private PresetSearchClient(SipConfig sipConfig) {
		this.sipConfig = sipConfig;
	}

	public static PresetSearchClient getInstance(SipConfig sipConfig){
        if(instance==null){
            synchronized (PresetSearchClient.class){
                if(instance==null){
                    instance=new PresetSearchClient(sipConfig);
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
	 * 预置位查询
	 * @param deviceCode 检测用户编码
	 * @param registerCode 注册用户
	 * @return
	 * @throws SipSystemException
	 */
	public DevicePresetVo searchPreset(String deviceCode,String registerCode) throws SipSystemException {
		try {
			// 获取端口
			int clientPort = PortPoolManager.getInstance().getPool();
			createSipProvider(clientPort);
			
			String domain = sipConfig.getDomain();
			
			String clientIp = sipConfig.getClientIp();
			String fromDeviceCode = registerCode;
			
			SipURI from = addressFactory.createSipURI(fromDeviceCode, domain);
			Address fromNameAddress = addressFactory.createAddress(from);
			FromHeader fromHeader = headerFactory.createFromHeader(fromNameAddress, CommonUtil.getTags());

			SipURI toAddress = addressFactory.createSipURI(deviceCode, domain);
			Address toNameAddress = addressFactory.createAddress(toAddress);
			ToHeader toHeader = headerFactory.createToHeader(toNameAddress, null);

			String serverAddress = sipConfig.getServerIp() + ":" + sipConfig.getServerPort();
			SipURI requestURI = addressFactory.createSipURI(deviceCode, serverAddress);

			ArrayList<ViaHeader> viaHeaders = new ArrayList<>(16);
			ViaHeader viaHeader = headerFactory.createViaHeader(clientIp,clientPort, SipConstants.TRANSPORT_UDP, null);
			viaHeaders.add(viaHeader);

			CallIdHeader callIdHeader = sipProvider.getNewCallId();

	        //生成发送序列号
	        Long invco = cSeqCounter.getAndIncrement();
	        
			CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(invco, Request.MESSAGE);

			MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70);

			Request request = messageFactory.createRequest(requestURI, Request.MESSAGE, callIdHeader, cSeqHeader,
					fromHeader, toHeader, viaHeaders, maxForwards);
			
			
	        // Create ContentTypeHeader
	        ContentTypeHeader contentTypeHeader = headerFactory
	                .createContentTypeHeader("Application", "MANSCDP+xml");
	        
	        
	        String contents = MediaMsgUtil.searchPresetMsg(invco, deviceCode);
	        request.setContent(contents, contentTypeHeader);
	        
	        DevicePresetVo data = new DevicePresetVo();
	        data.setDeviceCode(fromDeviceCode);
	        
			String reqkey = deviceCode + SPLIT_CHAR + invco;
	        deviceMap.put(reqkey, data);
	        
	        ClientTransaction inviteTid = sipProvider.getNewClientTransaction(request);	        
			inviteTid.sendRequest();

            Thread.sleep(1000);
            
            DevicePresetVo dataRecord = deviceMap.get(reqkey);
			
            //清除map数据
            deviceMap.remove(reqkey);
			//回收端口
			PortPoolManager.getInstance().destroy(clientPort);
			
			return dataRecord;
		} catch ( Exception e) {
			// TODO: handle exception
			throw new SipSystemException(e.getMessage());
		}
		
	}
	

	@Override
	public void processRequest(RequestEvent requestEvent) {
		// TODO Auto-generated method stub
		Request request = requestEvent.getRequest();
		
		String xml = new String ((byte[]) request.getContent());
		System.out.println(">>>>>>>>>>>>>>>>>>>>>"+xml);

	}

	
	@Override
	public void processResponse(ResponseEvent responseEvent) {
		// TODO Auto-generated method stub		

	}


	@Override
	public void close() throws IOException, ObjectInUseException {
		// TODO Auto-generated method stub
		logger.info(">>>>>>>>>>>>>>>>>>>PresetSearchClient关闭资源");
		
		//删除当前sip
		sipProvider.removeSipListener(this);
		SipStackManage.getSipStack().deleteSipProvider(sipProvider);
	}
	

	@Override
	public void processTimeout(TimeoutEvent event) {
		CallIdHeader callIdHeader = (CallIdHeader) event.getClientTransaction().getRequest().getHeader(CallIdHeader.NAME);
		String callId = callIdHeader.getCallId();
		DevicePresetVo bean = deviceMap.get(callId);
		bean.setStatus(SipConstants.TIMEOUT_FAIL);
		deviceMap.put(callId, bean);
		
//		bean.getLatch().countDown(); //计数减1
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
