//package com.hdvon.nmp.controller.system;
//
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.List;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.alibaba.dubbo.config.annotation.Reference;
//import com.hdvon.nmp.common.WebConstant;
//import com.hdvon.nmp.controller.BaseController;
//import com.hdvon.nmp.service.ICameraLogService;
//import com.hdvon.nmp.util.ApiResponse;
//import com.hdvon.nmp.util.ClientUtil;
//import com.hdvon.nmp.util.PropertiesUtils;
//import com.hdvon.nmp.vo.CameraLogVo;
//import com.hdvon.nmp.vo.UserVo;
//import com.hdvon.sip.enums.MediaOperationType;
//import com.hdvon.sip.service.SipService;
//import com.hdvon.sip.video.vo.CallbackResponseVo;
//import com.hdvon.sip.vo.MediaPlayQuery;
//import com.hdvon.sip.vo.MediaPlayVo;
//
//import cn.hutool.core.util.StrUtil;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiOperation;
//
//@Api(value="/videoPlay",tags="sip视频监控模块",description="针对摄像机播放操作")
//@RestController
//@RequestMapping("/videoPlay")
//public class VideoPlayController extends BaseController{
//	
//	@Reference
//	private ICameraLogService cameraLogService;
//	@Reference
//	private SipService sipService;
//	
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	@ApiOperation(value="视频实时播放")
//	@GetMapping(value = "/mediaPlay") 
//    public ApiResponse<MediaPlayVo> mediaPlay(MediaPlayQuery model) {
//		long start = Calendar.getInstance().getTime().getTime();
//		String deviceCode = model.getDeviceCode();
//		if(StrUtil.isBlank(deviceCode)) {
//			return new ApiResponse().error("设备编码不能为空！");
//		}
//		model.setTransportType("udp");
//		getUserPermission(deviceCode, "2");//检测查看录像视频的权限
//		if(PropertiesUtils.getBooleanProperty("isDEV")) {	//开发环境或测试环境
//			model.setDeviceCode(PropertiesUtils.getProperty("default.deviceCode"));
//		} //生产环境
//		model.setReceiveIp(ClientUtil.getClientIp(request));
//		
//		MediaPlayVo mediaPlay=sipService.mediaPlay(model);
//		
////		UserVo userVo = getLoginUser();
////		
////		CameraLogVo log=new CameraLogVo();
////		log.setUserId(userVo.getId());
////		log.setCallId(mediaPlay.getCallID());
////		log.setUserIp(mediaPlay.getReceiveIp());
////		log.setSbbm(deviceCode);
////		log.setPlayType("");
////		cameraLogService.saveLog(log);
//		
//		//this.videoBiz.saveUserLog(deviceCode, "视频实时播放", "/videoMonitoring/cameraPlay", start, WebConstant.CAMEAR_PALY_1, this.token);
//		
//		return new ApiResponse().ok().setData(mediaPlay);
//    }
//	
//	
//	
//	@ApiOperation(value="视频停止播放")
//    @ApiImplicitParam(name = "callIds", value = "点播返回的callIds", required = true)
//	@GetMapping(value = "/cameraStop")
//    public ApiResponse<Object> cameraStop(String callIds) {
//		
//		if(StrUtil.isBlank(callIds)) {
//			return new ApiResponse().error("参数不能为空！");
//		}
//		String token = request.getHeader(WebConstant.TOKEN_HEADER);
//		UserVo userVo = getLoginUser();
//		List<String> callIdList = Arrays.asList(callIds.split(","));
//		for(String id:callIdList) {
//			sipService.stopMedia(id, MediaOperationType.PLAY);
//		}
//		//sipService.stopMedia(callId, em);
////		List<String> callIdList = Arrays.asList(callIds.split(","));
////		cameraLogService.deleteLog(callIdList);
//		return new ApiResponse().ok().setData("视频停止播放成功");
//    }
//	
//	
//	
//
//}
