package com.hdvon.sip.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
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
import org.springframework.util.StringUtils;

import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.entity.VideoRecord;
import com.hdvon.sip.entity.VideoRecord.VideoItem;
import com.hdvon.sip.exception.SipSystemException;
import com.hdvon.sip.utils.CommonUtil;
import com.hdvon.sip.utils.SipConstants;
import com.hdvon.sip.utils.XmlUtil;
import com.hdvon.sip.vo.MediaDirectoryVo;
import com.hdvon.sip.vo.MediaItemVo;
import com.hdvon.sip.vo.MediaQuery;
/**
 * 录像查询
 *     流程 1：sip客户端向信令服务器发送message消息
 *        2： 信令服务器向sip客户端回复message消息
 *        3： sip客户端向信令服务器回复200确认消息
 * @author wanshaojian
 *
 */
public class MediaSearchClient implements SipListener, AutoCloseable{
	public Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 保存当前请求与请求对象值关系
	 */
	private ConcurrentHashMap<String, MediaDirectoryVo> queryVideoMap = new ConcurrentHashMap<>();
	/** 
	  * 绑定客户端回话与信令服务器端回话关系
	  */ 
	private ConcurrentHashMap<String, String> relationMap = new ConcurrentHashMap<>(); 

	private static final String URI_CHAR = ":";
	
	private static final String KEY_CHAR = "@";
    
	
	private volatile static MediaSearchClient instance;
	
	private SipConfig sipConfig;
	
	private SipProvider sipProvider;

	private AddressFactory addressFactory;

	private MessageFactory messageFactory;

	private HeaderFactory headerFactory;
	
	private AtomicLong cSeqCounter = new AtomicLong(1);
	
	
	private MediaSearchClient(SipConfig sipConfig) {
		this.sipConfig = sipConfig;
	}

	public static MediaSearchClient getInstance(SipConfig sipConfig){
        if(instance==null){
            synchronized (MediaSearchClient.class){
                if(instance==null){
                    instance=new MediaSearchClient(sipConfig);
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
	 * 录像目录查找
	 * @param model
	 * @param registerCode
	 * @throws SipSystemException
	 */
	public MediaDirectoryVo queryVideo(MediaQuery model,String registerCode) throws SipSystemException {
		try {
			// 获取端口
			int clientPort = PortPoolManager.getInstance().getPool();
			createSipProvider(clientPort);
			
			
			String domain = sipConfig.getDomain();
			
			String clientIp = sipConfig.getClientIp();
			String fromDeviceCode = registerCode;
			//目的设备编码
			String deviceCode = model.getDeviceCode();
			
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
	        Long incvo = cSeqCounter.getAndIncrement();
	        
			CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(incvo, Request.MESSAGE);

			MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70);

			Request request = messageFactory.createRequest(requestURI, Request.MESSAGE, callIdHeader, cSeqHeader,
					fromHeader, toHeader, viaHeaders, maxForwards);
			
			
	        // Create ContentTypeHeader
	        ContentTypeHeader contentTypeHeader = headerFactory
	                .createContentTypeHeader("Application", "MANSCDP+xml");
	        

	        String contents = getXmlQueryParam(model,incvo);
	        request.setContent(contents, contentTypeHeader);
	        
	        String callId = callIdHeader.getCallId();
	        
	        MediaDirectoryVo data = new MediaDirectoryVo();
	        BeanUtils.copyProperties(model, data);
	        data.setReceivePort(clientPort);
	        data.setCallId(callId);

	        queryVideoMap.put(callId, data);			

	        String reqKey = deviceCode + KEY_CHAR + incvo;
	        relationMap.put(reqKey, callId);
	        
	        ClientTransaction inviteTid = sipProvider.getNewClientTransaction(request);	        
			inviteTid.sendRequest();

			Thread.sleep(800);
            
            MediaDirectoryVo dataRecord = queryVideoMap.get(callId);
			//清除
            queryVideoMap.remove(callId);
            relationMap.remove(reqKey);
            //回收端口
			PortPoolManager.getInstance().destroy(clientPort);
			
            return dataRecord;
            
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
		MediaDirectoryVo record = getMediaData(xml);
		record.setStatus(SipConstants.SUCCESS);
		
		//获取会话id
		String reqKey = record.getDeviceCode() + KEY_CHAR + record.getSn();
		String callId = relationMap.get(reqKey);
		
		MediaDirectoryVo dictRecord = queryVideoMap.get(callId);
		record.setCallId(dictRecord.getCallId());
		
		List<MediaItemVo> dataList = dictRecord.getDataList();
		if(dataList == null || dataList.isEmpty()) {
			dataList = new ArrayList<>();
		}else {
			dataList.addAll(record.getDataList());
			//排序
			Collections.sort(dataList, (s1, s2) -> s1.getStartDate().compareTo(s2.getStartDate()));
		}

		dictRecord.setDataList(dataList);
		queryVideoMap.put(callId, dictRecord);
	    try {
			
	    	SipProvider sipProvider = (SipProvider) requestEvent.getSource();
	    	Response response = messageFactory.createResponse(Response.OK, request);
	        ServerTransaction serverTransactionId = requestEvent.getServerTransaction();
	        if(serverTransactionId == null) {
	        	serverTransactionId = sipProvider.getNewServerTransaction(request);
	        }
            serverTransactionId.sendResponse(response);
            
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

		MediaDirectoryVo model = queryVideoMap.get(callId);
		
		int checkStatus = SipConstants.FAIL;
		if (resp.getStatusCode() == Response.OK) {
			checkStatus = SipConstants.SUCCESS;
		}
		model.setStatus(checkStatus);
		
		queryVideoMap.put(callId, model);
	}



	@Override
	public void close() throws IOException, ObjectInUseException {
		// TODO Auto-generated method stub
		logger.info(">>>>>>>>>>>>>>>>>>>MediaSearchClient关闭资源");
		
		//删除当前sip
		sipProvider.removeSipListener(this);
		SipStackManage.getSipStack().deleteSipProvider(sipProvider);
	}


	@Override
	public void processTimeout(TimeoutEvent event) {
		CallIdHeader callIdHeader = (CallIdHeader) event.getClientTransaction().getRequest().getHeader(CallIdHeader.NAME);
		String callId = callIdHeader.getCallId();
		MediaDirectoryVo model = queryVideoMap.get(callId);
		model.setStatus(SipConstants.TIMEOUT_FAIL);
		queryVideoMap.put(callId, model);
		
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
	 * 将xml转化为对象
	 * @param xml
	 * @return
	 */
	private MediaDirectoryVo getMediaData(String xml){
		VideoRecord recordData = XmlUtil.toBean(VideoRecord.class, xml);
		MediaDirectoryVo model = new MediaDirectoryVo();
		model.setSn(recordData.getSn());
		model.setDeviceCode(recordData.getDeviceId());
		
		List<VideoItem> list = recordData.getRecordList();
		if(list == null || list.isEmpty()) {
			return model;
		}
		
		List<MediaItemVo> dataList = new ArrayList<>();
		int i=1;
		for(VideoItem item:list){
			MediaItemVo vo = new MediaItemVo();
			vo.setDeviceCode(item.getDeviceId());
			vo.setName(item.getName());
			vo.setType(item.getAddress());
			String uri = item.getDeviceId()+URI_CHAR+i;
			vo.setUri(uri);
			Date startDate = CommonUtil.convertDate(item.getStartTime());
			vo.setStartDate(startDate);
			Date endDate = CommonUtil.convertDate(item.getEndTime());
			vo.setEndDate(endDate);
			vo.setReceiveIp(model.getReceiveIp());
			//计算单个视频的播放时长
			long playTime = endDate.getTime()-startDate.getTime();
			vo.setPlayTime(playTime);
			
			dataList.add(vo);			
			i++;
		}
		//排序
		Collections.sort(dataList, (s1, s2) -> s1.getStartDate().compareTo(s2.getStartDate()));
		
		//计算视频的总时长
		long sumPlayTime = dataList.parallelStream().mapToLong(MediaItemVo::getPlayTime).reduce(0, (x, y) -> x + y);
		model.setSumPlayTime(sumPlayTime);
		model.setDataList(dataList);
		
		return model;
	}
	/**
	 * 获取xml参数
	 * @param deviceCode
	 * @return
	 */
	private String getXmlQueryParam(MediaQuery model,Long sendNo) {
		String contents = "<?xml version=\"1.0\"?>\r\n"+ 
				"<Query>\r\n" + 
				"<CmdType>RecordInfo</CmdType>\r\n" + 
				"<SN>"+sendNo+"</SN>\r\n" + 
				"<DeviceID>"+model.getDeviceCode()+"</DeviceID>\r\n" ;
		if(!StringUtils.isEmpty(model.getStartTime())) {
			contents += "<StartTime>"+CommonUtil.convertFormatDate(model.getStartTime())+"</StartTime>\r\n"; 
		}
		if(!StringUtils.isEmpty(model.getEndTime())) {
			contents += "<EndTime>"+CommonUtil.convertFormatDate(model.getEndTime())+"</EndTime>\r\n";
		}				
		contents +="<Type>all</Type>\r\n"  
				 +"<RecordPos>2</RecordPos>\r\n" 
				 + "</Query>";
		
		return contents;
	}


}
