package com.hdvon.sip.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hdvon.nmp.common.SipConstants;
import com.hdvon.nmp.common.SipLogBean;
import com.hdvon.nmp.enums.CruiseTypeEnum;
import com.hdvon.nmp.enums.MethodEum;
import com.hdvon.nmp.enums.PresetTypeEnum;
import com.hdvon.sip.app.BaseSipManage;
import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.config.kafka.Sender;
import com.hdvon.sip.entity.ByeBean;
import com.hdvon.sip.entity.ErrorBean;
import com.hdvon.sip.entity.ErrorMsg;
import com.hdvon.sip.entity.ErrorMsg.ResponseCodeEnum;
import com.hdvon.sip.entity.InviteBean;
import com.hdvon.sip.entity.MessageBean;
import com.hdvon.sip.entity.RegisterBean;
import com.hdvon.sip.entity.RequestBean;
import com.hdvon.sip.entity.ResponseBean;
import com.hdvon.sip.entity.SipResultBean;
import com.hdvon.sip.entity.param.CallParam;
import com.hdvon.sip.entity.param.CloudParam;
import com.hdvon.sip.entity.param.CruiseParam;
import com.hdvon.sip.entity.param.DeviceParam;
import com.hdvon.sip.entity.param.DownloadParam;
import com.hdvon.sip.entity.param.DragZoomParam;
import com.hdvon.sip.entity.param.HomePositionParam;
import com.hdvon.sip.entity.param.InviteParam;
import com.hdvon.sip.entity.param.PlaybackControlParam;
import com.hdvon.sip.entity.param.PlaybackParam;
import com.hdvon.sip.entity.param.PresetParam;
import com.hdvon.sip.entity.param.RecordParam;
import com.hdvon.sip.entity.param.VideotapeParam;
import com.hdvon.sip.enums.CloudControlTypeEnum;
import com.hdvon.sip.enums.DirectionEnum;
import com.hdvon.sip.enums.DownloadSpeedEnum;
import com.hdvon.sip.enums.FocusEnum;
import com.hdvon.sip.enums.IrisEnum;
import com.hdvon.sip.enums.MessageTypeEnum;
import com.hdvon.sip.enums.PlaybackEnum;
import com.hdvon.sip.enums.ScaleEnum;
import com.hdvon.sip.enums.TapeTypeEnum;
import com.hdvon.sip.enums.ZoomEnum;
import com.hdvon.sip.exception.SipSystemException;
import com.hdvon.sip.utils.SipMsgUtils;
import com.hdvon.sip.websocket.WebSocketManager;
import com.hdvon.sip.websocket.WsCacheBean;

@Service
public class SipService {
	private static final Logger LOG = LoggerFactory.getLogger(SipService.class);
	
	@Resource
	SipConfig sipConfig;
	@Resource
	WebSocketService socketService;
	@Resource
	Sender sender;
	
	private static final String SPLIT_CHAR = ",";
	
	private AtomicLong snCounter = new AtomicLong(1);
	/**
	 * sip请求接口
	 * @param request
	 * @param host
	 * @return
	 */
	public ResponseBean<SipResultBean> sendMsg(String message,String host) {
		
		RequestBean request = null;
		try {
			request = JSON.parseObject(message,RequestBean.class);
			request.setReqTime(System.currentTimeMillis());
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("参数解析异常{}",e.getMessage());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}
		String method = request.getMethod();
		MethodEum em = MethodEum.getObjectByKey(method);
		if(em == null) {
			ResponseBean<SipResultBean> respData = new ResponseBean<SipResultBean>();
			BeanUtils.copyProperties(request, respData);
			LOG.error("请求{}异常,原因:{}",JSON.toJSON(request),ResponseCodeEnum.METHOD_ERROR.getValue());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.METHOD_ERROR);
		}
		ResponseBean<SipResultBean> response = null;
		if(MethodEum.PLAY.equals(em)) {
			response = play(request, host);
		}else if(MethodEum.PLAYBACK.equals(em)) {
			response = playback(request, host);
		}else if(MethodEum.TERMINATE.equals(em)) {
			response = bye(request, host);
		}else if(MethodEum.CLOUD.equals(em)) {
			//云台控制
			response = cloudControl(request);
		}else if(MethodEum.CRUISE.equals(em)) {
			//巡航预案控制
			response = cruiseControl(request);
		}else if(MethodEum.PRESET.equals(em)) {
			//预置位设置
			response = presetControl(request);
		}else if(MethodEum.QUERYPRESET.equals(em)) {
			//预置位查询
			response = presetQuery(request);
		}else if(MethodEum.WIPER.equals(em)) {
			//雨刷控制
			
		}else if(MethodEum.RECORD.equals(em)) {
			//录像控制
			response = record(request);
		}else if(MethodEum.QUERYRECORD.equals(em)) {
			//录像查询
			response = queryRecord(request);
		}else if(MethodEum.PLAYBACK_CONTROL.equals(em)) {
			//回看控制
			response = playbackControl(request);
		}else if(MethodEum.DOWNLOAD.equals(em)) {
			//录像下载
			response = download(request, host);
		}else if(MethodEum.HOMEPOSITION.equals(em)) {
			//看守位控制
			response = homePosition(request);
		}else if(MethodEum.DRAGZOOM.equals(em)) {
			//3D拖放控制
			response = dragZoom(request);
		}
		WsCacheBean cacheBean = WebSocketManager.get(request.getWsId());
		SipLogBean sipLog = null;
		if(cacheBean != null) {
			JSONObject json = JSON.parseObject(request.getParam().toString());
			String deviceID = json.getString("deviceID");
			
			sipLog = SipLogBean.builder().transactionID(request.getTransactionID()).userId(cacheBean.getUserId()).reqIp(cacheBean.getReqIp()).token(request.getToken()).
					deviceID(deviceID).method(request.getMethod()).reqDate(new Date()).param(request.getParam().toString()).build();

		}
		/**
		 * @author 		huanhongliang
		 * @date		2018/11/7
		 * @description	调用接口时需要拿到当前会话id并将其会话id更新到对应的sip操作日志中
		 */
		if (null != sipLog) {
			
			if (null != response) {
				
				SipResultBean result = (SipResultBean) response.getResult();
				
				if (null != result) {
					
					String callId = result.getCallId();
					//设置当前会话id
					sipLog.setCallID(callId);
					
				}
				
			}
			sender.sendLog(sipLog);
			
		}
		
		return response;
	}
	/**
	 * 停止流播放
	 * @param request
	 * @return
	 */
	public void terminate(ByeBean byeModel){
		//停止
		BaseSipManage sipManage =BaseSipManage.getInstance(sipConfig,socketService,sender);
		
		sipManage.processTerminate(byeModel);
		
		//存在会话ID，需要发送kafka发送消息，让消费端监听并处理
		if (null != byeModel.getCallId()) {
			
			SipLogBean sipLog = SipLogBean.builder().transactionID(byeModel.getTransactionID()).userId(byeModel.getUserId()).
					reqIp(byeModel.getReqIp()).token(byeModel.getToken()).method(byeModel.getMethod()).reqDate(new Date()).
					param(byeModel.getParam()).callID(byeModel.getCallId()).build();
			
			sender.sendLog(sipLog);
		}
		
	}
	
	
	/**
	 * 云台控制
	 * @param request 请求参数
	 * @return
	 */
	public ErrorBean cloudControl(CloudParam param){
		String deviceID = param.getDeviceID();
		
		
		//获取云台控制类型
		CloudControlTypeEnum em = CloudControlTypeEnum.getObjectByKey(param.getType());
		if(em == null) {
			LOG.error("请求{}异常,原因:{}",JSON.toJSON(param));
			return ErrorMsg.resultMsg(ResponseCodeEnum.CLOUD_TYPE_ERROR);
		}

		Long invco = snCounter.getAndIncrement();
		String ptzCmd = null;
		if(CloudControlTypeEnum.DIRECTION.equals(em)) {
			//方向控制
			//获取方向控制类型
			DirectionEnum directionEm = DirectionEnum.getObjectByKey(param.getDirection());
			if(directionEm == null) {
				LOG.error("云台控制请求{}异常,原因:{}",JSON.toJSON(param),ResponseCodeEnum.DIRECTION_TYPE_ERROR.getValue());
				return ErrorMsg.resultMsg(ResponseCodeEnum.DIRECTION_TYPE_ERROR);
			}
			ptzCmd = SipMsgUtils.genDirectionPTZCmd(directionEm.getKey(), param.getSpeed());
		}else if(CloudControlTypeEnum.ZOOM.equals(em)) {
			//镜头变倍控制
			//获取方向控制类型
			ZoomEnum zoomEm = ZoomEnum.getObjectByKey(param.getZoom());
			if(zoomEm == null) {
				LOG.error("云台控制请求{}异常,原因:{}",JSON.toJSON(param),ResponseCodeEnum.ZOOM_TYPE_ERROR.getValue());
				return ErrorMsg.resultMsg(ResponseCodeEnum.ZOOM_TYPE_ERROR);
			}
			ptzCmd = SipMsgUtils.genZoomPTZCmd(zoomEm.getKey(), param.getSpeed());
		}else if(CloudControlTypeEnum.IRIS.equals(em)) {
			//光圈控制
			//获取方向控制类型
			IrisEnum irisEm = IrisEnum.getObjectByKey(param.getIris());
			if(irisEm == null) {
				LOG.error("云台控制请求{}异常,原因:{}",JSON.toJSON(param),ResponseCodeEnum.IRIS_TYPE_ERROR.getValue());
				return ErrorMsg.resultMsg(ResponseCodeEnum.IRIS_TYPE_ERROR);
			}
			ptzCmd = SipMsgUtils.genIrisPTZCmd(irisEm.getKey(), param.getSpeed());
		}else {
			//焦距控制
			//获取方向控制类型
			FocusEnum focusEm = FocusEnum.getObjectByKey(param.getFocus());
			if(focusEm == null) {
				LOG.error("云台控制请求{}异常,原因:{}",JSON.toJSON(param),ResponseCodeEnum.FOCUS_TYPE_ERROR.getValue());
				return ErrorMsg.resultMsg(ResponseCodeEnum.FOCUS_TYPE_ERROR);
			}
			ptzCmd = SipMsgUtils.genFocusPTZCmd(focusEm.getKey(), param.getSpeed());
		}
		String sdpData  = SipMsgUtils.cloudControlMsg(invco, ptzCmd, deviceID);
		
		
		processMessage(deviceID, sdpData, invco, MessageTypeEnum.DEVICE_CONTROL);
		
		return ErrorMsg.resultMsg(ResponseCodeEnum.SUCCESS);
	}
	/**
	 * 实时点播
	 * @param request
	 * @param host
	 * @return
	 */
	private ResponseBean<SipResultBean> play(RequestBean request,String host){
		String registerCode = getRegisterCode();
		InviteParam param = null;
		ResponseBean<SipResultBean> respData = new ResponseBean<SipResultBean>();
		BeanUtils.copyProperties(request, respData);
		try {
			param = JSON.parseObject(request.getParam().toString(),InviteParam.class);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("参数解析异常{}",e.getMessage());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}
		//参数验证
		if(StringUtils.isEmpty(param.getDeviceID()) || StringUtils.isEmpty(param.getProtocol()) || StringUtils.isEmpty(host) 
				|| param.getPort() == null ||  StringUtils.isEmpty(param.getTransport())) {
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.REQ_PARAM_ERROR);
		}
		int port = param.getPort();
		String sdpData = SipMsgUtils.videoPlayMsg(registerCode, host,port,param.getTransport());
		
		return processInvite(request, sdpData, host, port, registerCode, MethodEum.PLAY);
	}
	
	/**
	 * 录像回看
	 * @param request
	 * @param host
	 * @return
	 */
	private ResponseBean<SipResultBean> playback(RequestBean request,String host){
		//录像回看
		PlaybackParam param = null;
		ResponseBean<SipResultBean> respData = new ResponseBean<SipResultBean>();
		BeanUtils.copyProperties(request, respData);

		try {
			param = JSON.parseObject(request.getParam().toString(),PlaybackParam.class);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("参数解析异常{}",e.getMessage());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}
		//参数验证
		if(StringUtils.isEmpty(param.getDeviceID()) || StringUtils.isEmpty(param.getProtocol()) || StringUtils.isEmpty(host) 
				|| param.getPort() == null ||  StringUtils.isEmpty(param.getTransport()) || StringUtils.isEmpty(param.getUri())
				|| StringUtils.isEmpty(param.getStartTime()) || StringUtils.isEmpty(param.getEndTime())) {
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.REQ_PARAM_ERROR);
		}
		
		String registerCode = getRegisterCode();
		int port = param.getPort();
		
		String sdpData = SipMsgUtils.videoBackMsg(registerCode, host, port, param.getUri(), param.getStartTime(), param.getEndTime(),param.getTransport());
		
		return processInvite(request, sdpData, host, port, registerCode, MethodEum.PLAYBACK);
	}
	/**
	 * 停止流播放
	 * @param request
	 * @param host
	 * @return
	 */
	private ResponseBean<SipResultBean> bye(RequestBean request,String host){ 
		//停止
		BaseSipManage sipManage =BaseSipManage.getInstance(sipConfig,socketService,sender);
		ResponseBean<SipResultBean> respData = new ResponseBean<SipResultBean>();
		BeanUtils.copyProperties(request, respData);
		
		CallParam param = null;
		try {
			param = JSON.parseObject(request.getParam().toString(),CallParam.class);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("参数解析异常{}",e.getMessage());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}
//		LOG.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>关流："+param.getCallId());
		//参数验证
		if(StringUtils.isEmpty(param.getDeviceID()) || StringUtils.isEmpty(param.getCallId())) {
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.REQ_PARAM_ERROR);
		}
		String callId = param.getCallId();
		ByeBean byeModel = new ByeBean();
		BeanUtils.copyProperties(request, byeModel);
		byeModel.setCallId(callId);
		SipResultBean resultData = null;
		try {
			resultData = sipManage.processTerminate(byeModel);
		} catch (SipSystemException e) {
			// TODO: handle exception
			LOG.error("sip发送bye异常{}",e.getMessage());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.NOT_CALL);
		}
		ResponseBean<SipResultBean> response = new ResponseBean<SipResultBean>();
		BeanUtils.copyProperties(request, response);
		response.setResult(resultData);
		return response;
	}
	/**
	 * 录像下载
	 * @param request
	 * @param host
	 * @return
	 */
	private ResponseBean<SipResultBean> download(RequestBean request,String host) {
		//下载控制
		DownloadParam param = null;
		ResponseBean<SipResultBean> respData = new ResponseBean<SipResultBean>();
		BeanUtils.copyProperties(request, respData);

		try {
			param = JSON.parseObject(request.getParam().toString(),DownloadParam.class);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("参数解析异常{}",e.getMessage());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}
		//参数验证
		if(StringUtils.isEmpty(param.getDeviceID()) || StringUtils.isEmpty(param.getProtocol()) || StringUtils.isEmpty(host) 
				|| param.getPort() == null ||  StringUtils.isEmpty(param.getTransport()) || StringUtils.isEmpty(param.getUri())
				|| StringUtils.isEmpty(param.getStartTime()) || StringUtils.isEmpty(param.getEndTime()) || param.getSpeed() == null ) {
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.REQ_PARAM_ERROR);
		}
		
		String registerCode = getRegisterCode();
		//生成接受流端口
		int port = param.getPort();
		DownloadSpeedEnum speedEm =  DownloadSpeedEnum.getObjectByKey(param.getSpeed());
		//参数验证
		if(speedEm == null ) {
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.DOWNLOAD_SPEED_ERROR);
		}
		String sdpData = SipMsgUtils.downloadMsg(registerCode, host,port, param.getUri(), param.getStartTime(), param.getEndTime(),speedEm.getValue(),param.getTransport());
		
		return processInvite(request, sdpData, host, port, registerCode, MethodEum.DOWNLOAD);
	}


	
	/**
	 * 云台控制
	 * @param request 请求参数
	 * @return
	 */
	private ResponseBean<SipResultBean> cloudControl(RequestBean request){
		CloudParam param = null ;
		ResponseBean<SipResultBean> respData = new ResponseBean<SipResultBean>();
		BeanUtils.copyProperties(request, respData);
		try {
			param = JSONObject.parseObject(request.getParam().toString(),CloudParam.class) ;
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("参数解析异常{}",e.getMessage());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}
		String deviceID = param.getDeviceID();
		
		//获取云台控制类型
		CloudControlTypeEnum em = CloudControlTypeEnum.getObjectByKey(param.getType());
		if(em == null) {
			LOG.error("请求{}异常,原因:{}",JSON.toJSON(request),ResponseCodeEnum.CLOUD_TYPE_ERROR.getValue());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.CLOUD_TYPE_ERROR);
		}
		if(sipConfig.isDefaultFlag()) {
			deviceID = sipConfig.getDefaultDeviceCode();
		}
		Long invco = snCounter.getAndIncrement();
		String ptzCmd = null;
		if(CloudControlTypeEnum.DIRECTION.equals(em)) {
			//方向控制
			//获取方向控制类型
			DirectionEnum directionEm = DirectionEnum.getObjectByKey(param.getDirection());
			if(directionEm == null) {
				LOG.error("云台控制请求{}异常,原因:{}",JSON.toJSON(request),ResponseCodeEnum.DIRECTION_TYPE_ERROR.getValue());
				return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.DIRECTION_TYPE_ERROR);
			}
			ptzCmd = SipMsgUtils.genDirectionPTZCmd(directionEm.getKey(), param.getSpeed());
		}else if(CloudControlTypeEnum.ZOOM.equals(em)) {
			//镜头变倍控制
			//获取方向控制类型
			ZoomEnum zoomEm = ZoomEnum.getObjectByKey(param.getZoom());
			if(zoomEm == null) {
				LOG.error("云台控制请求{}异常,原因:{}",JSON.toJSON(request),ResponseCodeEnum.ZOOM_TYPE_ERROR.getValue());
				return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.ZOOM_TYPE_ERROR);
			}
			ptzCmd = SipMsgUtils.genZoomPTZCmd(zoomEm.getKey(), param.getSpeed());
		}else if(CloudControlTypeEnum.IRIS.equals(em)) {
			//光圈控制
			//获取方向控制类型
			IrisEnum irisEm = IrisEnum.getObjectByKey(param.getIris());
			if(irisEm == null) {
				LOG.error("云台控制请求{}异常,原因:{}",JSON.toJSON(request),ResponseCodeEnum.IRIS_TYPE_ERROR.getValue());
				return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.IRIS_TYPE_ERROR);
			}
			ptzCmd = SipMsgUtils.genIrisPTZCmd(irisEm.getKey(), param.getSpeed());
		}else {
			//焦距控制
			//获取方向控制类型
			FocusEnum focusEm = FocusEnum.getObjectByKey(param.getFocus());
			if(focusEm == null) {
				LOG.error("云台控制请求{}异常,原因:{}",JSON.toJSON(request),ResponseCodeEnum.FOCUS_TYPE_ERROR.getValue());
				return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.FOCUS_TYPE_ERROR);
			}
			ptzCmd = SipMsgUtils.genFocusPTZCmd(focusEm.getKey(), param.getSpeed());
		}
		String sdpData  = SipMsgUtils.cloudControlMsg(invco, ptzCmd, deviceID);
		
		
		return processMessage(deviceID, sdpData, invco, request,MessageTypeEnum.DEVICE_CONTROL,SipConstants.STATE_OK);
	}
	
	/**
	 * 巡航预案控制
	 * @param request 请求参数
	 * @return
	 */
	private ResponseBean<SipResultBean> cruiseControl(RequestBean request){
		CruiseParam param = null;

		try {
			param = JSON.parseObject(request.getParam().toString(),CruiseParam.class);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("参数解析异常{}",e.getMessage());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}
		ResponseBean<SipResultBean> respData = new ResponseBean<SipResultBean>();
		BeanUtils.copyProperties(request, respData);
		CruiseTypeEnum em = CruiseTypeEnum.getObjectByKey(param.getType());
		if(em == null) {
			LOG.error("巡航预案控制请求{}异常,原因:{}",JSON.toJSON(request),ResponseCodeEnum.CRUISE_TYPE_ERROR.getValue());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.CRUISE_TYPE_ERROR);
		}
		Long invco = snCounter.getAndIncrement();
		String deviceID = param.getDeviceID();
		String ptzCmd = SipMsgUtils.genCruisePTZCmd(param.getType(), param.getGroupNum(),param.getPresetNum(),param.getStayTime(),param.getSpeed());
		
		if(sipConfig.isDefaultFlag()) {
			deviceID = sipConfig.getDefaultDeviceCode();
		}
		
		String sdpData = SipMsgUtils.cloudControlMsg(invco,ptzCmd, deviceID);
		

		try {

			BaseSipManage sipManage =BaseSipManage.getInstance(sipConfig,socketService,sender);
			
			String registerCode = getRegisterCode();
			
			MessageBean msgBean = new MessageBean();
			BeanUtils.copyProperties(request, msgBean);
			
			msgBean.setTargetDeviceCode(deviceID);
			msgBean.setSdpData(sdpData);
			msgBean.setSn(invco);
			msgBean.setPresetNum(param.getPresetNum());
			msgBean.setGroupNum(param.getGroupNum());
			msgBean.setType(param.getType());
			msgBean.setStayTime(param.getStayTime());
			msgBean.setSpeed(param.getSpeed());
			
			SipResultBean sipBean = sipManage.processMessage(msgBean, registerCode, MessageTypeEnum.DEVICE_CONTROL);
			sipBean.setState(SipConstants.STATE_CONTINUE);
			respData.setResult(sipBean);
			
			return respData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("{}请求{}异常,原因:{}",MessageTypeEnum.DEVICE_CONTROL.getValue(),JSON.toJSONString(request),ResponseCodeEnum.PARAM_ERROR.getValue());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}

	}
	
	/**
	 * 预置位控制
	 * @param request 请求参数
	 * @return
	 */
	private ResponseBean<SipResultBean> presetControl(RequestBean request){
		PresetParam param = null;
		try {
			param = JSON.parseObject(request.getParam().toString(),PresetParam.class);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("参数解析异常{}",e.getMessage());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}
		ResponseBean<SipResultBean> respData = new ResponseBean<SipResultBean>();
		BeanUtils.copyProperties(request, respData);
		//参数验证
		if(StringUtils.isEmpty(param.getDeviceID()) || StringUtils.isEmpty(param.getType()) || param.getPresetNum()== null) {
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.REQ_PARAM_ERROR);
		}
		PresetTypeEnum em = PresetTypeEnum.getObjectByKey(param.getType());
		if(em == null) {
			LOG.error("预置位控制请求{}异常,原因:{}",JSON.toJSON(request),ResponseCodeEnum.PRESET_TYPE_ERROR.getValue());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PRESET_TYPE_ERROR);
		}
		if(PresetTypeEnum.SET.equals(em) && StringUtils.isEmpty(param.getPresetName())) {
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.REQ_PARAM_ERROR);
		}
		Long invco = snCounter.getAndIncrement();
		String deviceID = param.getDeviceID();
		String ptzCmd = SipMsgUtils.genPresetPTZCmd(em.getKey(), param.getPresetNum());
		
		if(sipConfig.isDefaultFlag()) {
			deviceID = sipConfig.getDefaultDeviceCode();
		}
		
		String sdpData = SipMsgUtils.cloudControlMsg(invco,ptzCmd, deviceID);
		
		try {

			BaseSipManage sipManage =BaseSipManage.getInstance(sipConfig,socketService,sender);
			
			String registerCode = getRegisterCode();
			
			MessageBean msgBean = new MessageBean();
			BeanUtils.copyProperties(request, msgBean);
			
			msgBean.setTargetDeviceCode(deviceID);
			msgBean.setSdpData(sdpData);
			msgBean.setSn(invco);
			msgBean.setPresetNum(param.getPresetNum());
			msgBean.setPresetName(param.getPresetName());
			msgBean.setType(param.getType());
			msgBean.setRegisterCode(registerCode);
			
			SipResultBean sipBean = sipManage.processMessage(msgBean, registerCode, MessageTypeEnum.DEVICE_CONTROL);
			sipBean.setState(SipConstants.STATE_CONTINUE);
			respData.setResult(sipBean);
			
			return respData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("{}请求{}异常,原因:{}",MessageTypeEnum.DEVICE_CONTROL.getValue(),JSON.toJSONString(request),ResponseCodeEnum.PARAM_ERROR.getValue());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}
	}
	
	/**
	 * 录像控制
	 * @param request 请求参数
	 * @return
	 */
	private ResponseBean<SipResultBean> record(RequestBean request){
		VideotapeParam param = null;
		try {
			param = JSON.parseObject(request.getParam().toString(),VideotapeParam.class);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("参数解析异常{}",e.getMessage());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}
		ResponseBean<SipResultBean> respData = new ResponseBean<SipResultBean>();
		BeanUtils.copyProperties(request, respData);
		//参数验证
		if(StringUtils.isEmpty(param.getDeviceID()) || StringUtils.isEmpty(param.getType())) {
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.REQ_PARAM_ERROR);
		}
		TapeTypeEnum em = TapeTypeEnum.getObjectByKey(param.getType());
		if(em == null) {
			LOG.error("实时录像控制请求{}异常,原因:{}",JSON.toJSON(request),ResponseCodeEnum.RECORD_TYPE_ERROR.getValue());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.RECORD_TYPE_ERROR);
		}
		Long invco = snCounter.getAndIncrement();
		String deviceID = param.getDeviceID();
		if(sipConfig.isDefaultFlag()) {
			deviceID = sipConfig.getDefaultDeviceCode();
		}
		String sdpData = SipMsgUtils.startVideotapeMsg(invco, deviceID);
		if(TapeTypeEnum.START.equals(em)) {
			sdpData = SipMsgUtils.stopVideotapeMsg(invco, deviceID);
		}
		return processMessage(deviceID, sdpData, invco, request,MessageTypeEnum.DEVICE_CONTROL,SipConstants.STATE_CONTINUE);

	}
	/**
	 * 设备预置位查询
	 * @param request 请求参数
	 * @return
	 */
	private ResponseBean<SipResultBean> presetQuery(RequestBean request){
		DeviceParam param = null;

		try {
			param = JSON.parseObject(request.getParam().toString(),DeviceParam.class);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("参数解析异常{}",e.getMessage());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}
		ResponseBean<SipResultBean> respData = new ResponseBean<SipResultBean>();
		BeanUtils.copyProperties(request, respData);
		//参数验证
		if(StringUtils.isEmpty(param.getDeviceID())) {
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.REQ_PARAM_ERROR);
		}
		Long invco = snCounter.getAndIncrement();
		String deviceID = param.getDeviceID();
		if(sipConfig.isDefaultFlag()) {
			deviceID = sipConfig.getDefaultDeviceCode();
		}
		String sdpData = SipMsgUtils.presetQueryMsg(invco, deviceID);

		try {
			BaseSipManage sipManage =BaseSipManage.getInstance(sipConfig,socketService,sender);
			
			String registerCode = getRegisterCode();
			
			MessageBean msgBean = new MessageBean();
			BeanUtils.copyProperties(request, msgBean);
			
			msgBean.setTargetDeviceCode(deviceID);
			msgBean.setSdpData(sdpData);
			msgBean.setSn(invco);
			msgBean.setIsWebsocket(true);
			
			SipResultBean sipBean = sipManage.processMessage(msgBean, registerCode, MessageTypeEnum.PRESET_QUERY);
			sipBean.setState(SipConstants.STATE_CONTINUE);
			respData.setResult(sipBean);
			
			return respData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("{}请求{}异常,原因:{}",MessageTypeEnum.PRESET_QUERY,JSON.toJSONString(request),ResponseCodeEnum.PARAM_ERROR.getValue());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}

	}
	

	

	
	
	/**
	 * 录像控制
	 * @param request 请求参数
	 * @return
	 */
	private ResponseBean<SipResultBean> playbackControl(RequestBean request){
		BaseSipManage sipManage =BaseSipManage.getInstance(sipConfig,socketService,sender);
		
		String registerCode = getRegisterCode();
		
		PlaybackControlParam param = null;
		try {
			param = JSON.parseObject(request.getParam().toString(),PlaybackControlParam.class);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("参数解析异常{}",e.getMessage());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}
		ResponseBean<SipResultBean> respData = new ResponseBean<SipResultBean>();
		BeanUtils.copyProperties(request, respData);

		Long invco = snCounter.getAndIncrement();
		
		PlaybackEnum em = PlaybackEnum.getObjectByKey(param.getType());
		if(em == null) {
			LOG.error("回看控制请求{}异常,原因:{}",JSON.toJSON(request),ResponseCodeEnum.PLAYBACK_TYPE_ERROR.getValue());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PLAYBACK_TYPE_ERROR);
		}
		String sdpData = null;
		String callId = param.getCallId();
		String deviceID = param.getDeviceID();
		if(sipConfig.isDefaultFlag()) {
			deviceID = sipConfig.getDefaultDeviceCode();
		}
		if(PlaybackEnum.TEARDOWN.equals(em)) {
			//参数验证
			if(StringUtils.isEmpty(param.getDeviceID()) || StringUtils.isEmpty(param.getCallId())|| StringUtils.isEmpty(param.getType())) {
				return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.REQ_PARAM_ERROR);
			}
			//回看停止
			ByeBean byeModel = new ByeBean();
			BeanUtils.copyProperties(request, byeModel);
			byeModel.setCallId(callId);
			
			SipResultBean data = sipManage.processTerminate(byeModel);
			respData.setResult(data);
			return respData;
		}else if(PlaybackEnum.PLAY.equals(em)) {
			//参数验证
			if(StringUtils.isEmpty(param.getDeviceID()) || StringUtils.isEmpty(param.getCallId())|| StringUtils.isEmpty(param.getType())
					|| StringUtils.isEmpty(param.getScale())) {
				return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.REQ_PARAM_ERROR);
			}
			//回看播放
			sdpData = SipMsgUtils.playbackBroadcastMsg(invco);
		}else if(PlaybackEnum.MULTIPLE.equals(em)) {
			//参数验证
			if(StringUtils.isEmpty(param.getDeviceID()) || StringUtils.isEmpty(param.getCallId())|| StringUtils.isEmpty(param.getType())
					|| StringUtils.isEmpty(param.getScale())) {
				return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.REQ_PARAM_ERROR);
			}
			//回看快进
			ScaleEnum scaleEm = ScaleEnum.getObjectByKey(param.getScale());
			if(scaleEm == null) {
				scaleEm = ScaleEnum.ONE_TIMES;
			}
			sdpData = SipMsgUtils.playbackFastForwardMsg(invco,scaleEm.getValue());
		}else if(PlaybackEnum.RANDOM_PLAY.equals(em)){
			//参数验证
			if(StringUtils.isEmpty(param.getDeviceID()) || StringUtils.isEmpty(param.getCallId())|| StringUtils.isEmpty(param.getType())
					|| StringUtils.isEmpty(param.getScale()) || param.getRange() == null) {
				return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.REQ_PARAM_ERROR);
			}
			//回看随机播放
			sdpData = SipMsgUtils.playbackRandomBroadcastMsg(invco,param.getRange());
		}else {
			//参数验证
			if(StringUtils.isEmpty(param.getDeviceID()) || StringUtils.isEmpty(param.getCallId())|| StringUtils.isEmpty(param.getType())) {
				return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.REQ_PARAM_ERROR);
			}
			//回看播放暂停
			sdpData = SipMsgUtils.playbackPauseMsg(invco);
		}
		try {
			SipResultBean data = sipManage.processInfo(callId,deviceID, sdpData);
			respData.setResult(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("录像控制请求{}异常,原因:{}",registerCode,ResponseCodeEnum.PARAM_ERROR.getValue());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}

		return respData;
	}
	
	
	/**
	 * 看守位控制
	 * @param request
	 * @return
	 */
	private ResponseBean<SipResultBean> homePosition(RequestBean request){
		BaseSipManage sipManage =BaseSipManage.getInstance(sipConfig,socketService,sender);
		HomePositionParam param = null;
		try {
			param = JSON.parseObject(request.getParam().toString(),HomePositionParam.class);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("参数解析异常{}",e.getMessage());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}
		ResponseBean<SipResultBean> respData = new ResponseBean<SipResultBean>();
		BeanUtils.copyProperties(request, respData);
		//参数验证
		if(StringUtils.isEmpty(param.getDeviceID()) || param.getEnabled() == null) {
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.REQ_PARAM_ERROR);
		}
		String deviceID = param.getDeviceID();
		if(sipConfig.isDefaultFlag()) {
			deviceID = sipConfig.getDefaultDeviceCode();
		}
		Long invco = snCounter.getAndIncrement();
		try {
			String registerCode = getRegisterCode();
			//生成接受流端口			
			String sdpData = SipMsgUtils.homePositionMsg(deviceID, param.getEnabled(),param.getResetTime(),param.getPresetNum(), invco);
			
			MessageBean msgBean = new MessageBean();
			BeanUtils.copyProperties(request, msgBean);
			
			msgBean.setTargetDeviceCode(deviceID);
			msgBean.setSdpData(sdpData);
			msgBean.setSn(invco);

			SipResultBean bean = sipManage.processMessage(msgBean, registerCode, MessageTypeEnum.HOME_POSITION);
			respData.setResult(bean);
			
			return respData;
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("看守位控制请求{}异常,原因:{}",JSON.toJSON(request),ResponseCodeEnum.PARAM_ERROR.getValue());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}
	}
	
	/**
	 * 3D拖放控制
	 * @param request
	 * @return
	 */
	private ResponseBean<SipResultBean> dragZoom(RequestBean request){
		BaseSipManage sipManage = BaseSipManage.getInstance(sipConfig,socketService,sender);
		DragZoomParam param = null;
		try {
			param = JSON.parseObject(request.getParam().toString(),DragZoomParam.class);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("参数解析异常{}",e.getMessage());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}
		ResponseBean<SipResultBean> respData = new ResponseBean<SipResultBean>();
		BeanUtils.copyProperties(request, respData);
		//参数验证
		if(StringUtils.isEmpty(param.getDeviceID()) || param.getLength() == null || param.getWidth()==null || param.getLengthX()==null
				|| param.getLengthY()== null || param.getMidPointX()== null || param.getMidPointY() == null || param.getLength() ==null) {
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.REQ_PARAM_ERROR);
		}
		String deviceID = param.getDeviceID();
		if(sipConfig.isDefaultFlag()) {
			deviceID = sipConfig.getDefaultDeviceCode();
			param.setDeviceID(deviceID);
		}
		Long invco = snCounter.getAndIncrement();
		try {
			String registerCode = getRegisterCode();
			//生成接受流端口			
			String sdpData = SipMsgUtils.dragZoomMsg(param, invco);
			
			MessageBean msgBean = new MessageBean();
			BeanUtils.copyProperties(request, msgBean);
			
			msgBean.setTargetDeviceCode(deviceID);
			msgBean.setSdpData(sdpData);
			msgBean.setSn(invco);

			SipResultBean bean = sipManage.processMessage(msgBean, registerCode, MessageTypeEnum.DRAG_ZOOM);
			bean.setState(SipConstants.STATE_OK);
			respData.setResult(bean);
			
			return respData;
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("3D拖放控制请求{}异常,原因:{}",JSON.toJSON(request),ResponseCodeEnum.PARAM_ERROR.getValue());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}
	}
	/**
	 * 录像查询
	 * @param request 请求参数
	 * @return
	 */
	private ResponseBean<SipResultBean> queryRecord(RequestBean request){
		BaseSipManage sipManage =BaseSipManage.getInstance(sipConfig,socketService,sender);
		
		RecordParam param = null;
		try {
			param = JSON.parseObject(request.getParam().toString(),RecordParam.class);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("参数解析异常{}",e.getMessage());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}
		ResponseBean<SipResultBean> respData = new ResponseBean<SipResultBean>();
		BeanUtils.copyProperties(request, respData);
		//参数验证
		if(StringUtils.isEmpty(param.getDeviceID()) || StringUtils.isEmpty(param.getStartTime())|| StringUtils.isEmpty(param.getEndTime())) {
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.REQ_PARAM_ERROR);
		}
		String deviceID = param.getDeviceID();
		if(sipConfig.isDefaultFlag()) {
			deviceID = sipConfig.getDefaultDeviceCode();
		}
		Long invco = snCounter.getAndIncrement();
		try {
			String registerCode = getRegisterCode();
			//生成接受流端口			
			String sdpData = SipMsgUtils.searchMediaMsg(deviceID, param.getStartTime(), param.getEndTime(), invco);
			
			MessageBean msgBean = new MessageBean();
			BeanUtils.copyProperties(request, msgBean);
			
			msgBean.setTargetDeviceCode(deviceID);
			msgBean.setSdpData(sdpData);
			msgBean.setSn(invco);
			msgBean.setStartTime(param.getStartTime());
			msgBean.setEndTime(param.getEndTime());

			SipResultBean bean = sipManage.processMessage(msgBean, registerCode, MessageTypeEnum.RECORD_INFO);
			respData.setResult(bean);
			
			return respData;
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("录像查询请求{}异常,原因:{}",JSON.toJSON(request),ResponseCodeEnum.PARAM_ERROR.getValue());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}
	}
	
	/**
	 * 设备状态查询
	 * @param deviceCode
	 * @return
	 */
	public ResponseBean<SipResultBean> searchDeviceStatus(RequestBean<DeviceParam> request){
		
		Long invco = snCounter.getAndIncrement();
		DeviceParam param = null;
		ResponseBean<SipResultBean> respData = new ResponseBean<SipResultBean>();
		BeanUtils.copyProperties(request, respData);
		try {
			param = JSON.parseObject(request.getParam().toString(),DeviceParam.class);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("参数解析异常{}",e.getMessage());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}
		String deviceID = param.getDeviceID();

		//sdp报文
		String sdpData = SipMsgUtils.deviceStatusMsg(invco, deviceID);

		return processMessage(deviceID, sdpData, invco, request, MessageTypeEnum.DEVICE_STATUS,SipConstants.STATE_CONTINUE);		
	}
	/**
	 * 用户注册
	 * @return
	 */
	public void register(){
		String[] userArr = sipConfig.getUserlist().split(",");
		List<String> userList = new ArrayList<>(Arrays.asList(userArr));
		
		for(String registerCode:userList) {
			register(registerCode);
		}
	}
	/**
	 * 单个用户注册
	 * @return
	 */
	public SipResultBean register(String registerCode){
		BaseSipManage sipManage =BaseSipManage.getInstance(sipConfig,socketService,sender);
		RegisterBean registerBean = new RegisterBean(registerCode, sipConfig.getExpire());
		//用户注册
		return sipManage.processRegister(registerBean);
	}
	/**
	 * 用户发送心跳
	 * @param request
	 * @return
	 */
	public void sendHeartbeat(){
		String[] userArr = sipConfig.getUserlist().split(",");
		List<String> userList = new ArrayList<>(Arrays.asList(userArr));
		
		for(String registerCode:userList) {
			sendHeartbeat(registerCode);
		}
	}
	/**
	 * 用户单个发送心跳
	 * @param registerCode
	 * @return
	 */
	public void sendHeartbeat(String registerCode){
		Long invco = snCounter.getAndIncrement();
		//sdp报文
		String sdpData = SipMsgUtils.keepaliveMsg(invco, registerCode);
		
		processMessage(registerCode, sdpData, invco,  MessageTypeEnum.KEEPALIVE);	
	}
	/**
	 * invite消息处理通用方法
	 * @param request 请求参数
	 * @param sdpData sdp报文
	 * @param host 接受端口
	 * @param port 接受流端口
	 * @param registerCode 注册用户
	 * @param em
	 * @return
	 */
	private ResponseBean<SipResultBean> processInvite(RequestBean request,String sdpData,String host,int port,String registerCode,MethodEum em){
		BaseSipManage sipManage =BaseSipManage.getInstance(sipConfig,socketService,sender);
		
		InviteBean inviteBean = new InviteBean();
		if(MethodEum.PLAY.equals(em)) {
			InviteParam param = JSON.parseObject(request.getParam().toString(),InviteParam.class);
			BeanUtils.copyProperties(param, inviteBean);
		}else if(MethodEum.PLAYBACK.equals(em)) {
			PlaybackParam param = JSON.parseObject(request.getParam().toString(),PlaybackParam.class);
			BeanUtils.copyProperties(param, inviteBean);
		}else if(MethodEum.DOWNLOAD.equals(em)) {
			DownloadParam param = JSON.parseObject(request.getParam().toString(),DownloadParam.class);
			BeanUtils.copyProperties(param, inviteBean);
		}
		if(sipConfig.isDefaultFlag()) {
			inviteBean.setDeviceID(sipConfig.getDefaultDeviceCode());
		}
		inviteBean.setHost(host);
		inviteBean.setPort(port);
		inviteBean.setTransactionID(request.getTransactionID());
		inviteBean.setToken(request.getToken());
		inviteBean.setVersion(request.getVersion());
		inviteBean.setMethod(request.getMethod());
		inviteBean.setWsId(request.getWsId());
		inviteBean.setReqTime(request.getReqTime());
		
		ResponseBean<SipResultBean> respData = new ResponseBean<SipResultBean>();
		BeanUtils.copyProperties(request, respData);
		try {
			SipResultBean result = sipManage.processInvite(inviteBean, registerCode,sdpData,port);

			respData.setResult(result);
			return respData;
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("【{}】请求{}异常,原因:{}",em.getValue(),JSON.toJSON(request),ResponseCodeEnum.PARAM_ERROR.getValue());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}
	}
	
	/**
	 * message消息处理通用方法
	 * @param deviceID 设备id
	 * @param sdpData sdp报文
	 * @param sn 编码
	 * @param request 用户请求
	 * @param message 消息类型 
	 * @return
	 * @throws SipSystemException
	 */
	private void processMessage(String deviceID,String sdpData,Long sn,MessageTypeEnum msgEm) throws SipSystemException {
		try {
			BaseSipManage sipManage =BaseSipManage.getInstance(sipConfig,socketService,sender);
			
			String registerCode = getRegisterCode();
			
			MessageBean msgBean = new MessageBean();
			msgBean.setTargetDeviceCode(deviceID);
			msgBean.setSdpData(sdpData);
			msgBean.setSn(sn);
			msgBean.setMethod(msgEm.getKey());
			
			sipManage.processMessage(msgBean, registerCode, msgEm);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("{}请求{}异常,原因:{}",msgEm.getValue(),deviceID,ResponseCodeEnum.PARAM_ERROR.getValue());
		}
	}
	/**
	 * message消息处理通用方法
	 * @param deviceID 设备id
	 * @param sdpData sdp报文
	 * @param sn 编码
	 * @param request 用户请求
	 * @param message 消息类型
	 * @param state 返回状态 
	 * @return
	 * @throws SipSystemException
	 */
	private <T> ResponseBean<SipResultBean> processMessage(String deviceID,String sdpData,Long sn,RequestBean<T> request,MessageTypeEnum msgEm,String state) throws SipSystemException {
		ResponseBean<SipResultBean> respData = new ResponseBean<SipResultBean>();
		BeanUtils.copyProperties(request, respData);
		try {
			
			BaseSipManage sipManage =BaseSipManage.getInstance(sipConfig,socketService,sender);
			
			String registerCode = getRegisterCode();
			
			MessageBean msgBean = new MessageBean();
			BeanUtils.copyProperties(request, msgBean);
			
			msgBean.setTargetDeviceCode(deviceID);
			msgBean.setSdpData(sdpData);
			msgBean.setSn(sn);
			
			SipResultBean sipBean = sipManage.processMessage(msgBean, registerCode, msgEm);
			sipBean.setState(state);
			respData.setResult(sipBean);
			
			return respData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("{}请求{}异常,原因:{}",msgEm.getValue(),JSON.toJSONString(request),ResponseCodeEnum.PARAM_ERROR.getValue());
			return ErrorMsg.resultErrorMsg(request, ResponseCodeEnum.PARAM_ERROR);
		}
	}
	

	private String getRegisterCode() {
		String[] userArr = sipConfig.getUserlist().split(",");
		List<String> userList = new ArrayList<>(Arrays.asList(userArr));
		String registerCode = userList.get(new Random().nextInt(userArr.length));
		return registerCode;
	}
}
