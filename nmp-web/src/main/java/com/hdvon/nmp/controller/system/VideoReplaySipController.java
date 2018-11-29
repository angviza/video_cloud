package com.hdvon.nmp.controller.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.enums.DevcOperTypeEnums;
import com.hdvon.nmp.enums.MenuTypeEnums;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.service.ICameraLabelService;
import com.hdvon.nmp.service.ICameraLogService;
import com.hdvon.nmp.service.ICameraService;
import com.hdvon.nmp.service.IUserFilepathService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.ClientUtil;
import com.hdvon.nmp.vo.CameraLabelVo;
import com.hdvon.nmp.vo.UserVo;
import com.hdvon.nmp.vo.video.UserOperLogVo;
import com.hdvon.sip.video.service.IVideoSipService;
import com.hdvon.sip.video.vo.CallbackResponseVo;
import com.hdvon.sip.video.vo.FileResponseVo;
import com.hdvon.sip.video.vo.InviteOptionInputVo;
import com.hdvon.sip.video.vo.QueryRecOptionInputVo;
import com.hdvon.sip.video.vo.RecordCtrlOptionSipVo;
import com.hdvon.sip.video.vo.ResponseVideoVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/videoReplay",tags="线索翻查功能模块",description="针对线索翻查录像回看，下载操作")
@RestController
@RequestMapping("/videoReplay")
@Slf4j
public class VideoReplaySipController extends BaseController {
	
	@Reference
	private ICameraService cameraService;
	
	@Reference
	private IUserFilepathService userFilepathService;
	
	@Reference
	private ICameraLabelService cameraLabelService;
	
	@Reference
	private IVideoSipService videoSipService;
	
	@Reference
	private ICameraLogService cameraLogService;
	
	@ApiOperation(value="视频录像回放")
	@GetMapping(value = "/cameraPlayback") 
    public ApiResponse<CallbackResponseVo> cameraPlayback(InviteOptionInputVo vo) {
		
		long start = Calendar.getInstance().getTime().getTime();
		String error = checkParam(vo);
		String deviceCode = vo.getDeviceCode();
		
		if(StrUtil.isNotBlank(error)) {
			return new ApiResponse().error(error);
		}
		
		getUserPermission(deviceCode, "2");//查看录像视频
		
		String clientIp = ClientUtil.getClientIp(request);
		
		List<Integer> prots= ClientUtil.creatProts(clientIp, 1);
		if(prots.size()> 0) {
			vo.setPort(prots.get(0));
		} else {
			return new ApiResponse().error("系统内部错误");
		}
		
		vo.setClientIp(clientIp);
		
		//调用视频播放接口
		CallbackResponseVo responseVo = videoSipService.invite(vo);
		
		UserVo userVo = getLoginUser();
		UserOperLogVo log = new UserOperLogVo();
		log.setUserId(userVo.getId());
		log.setCallId(responseVo.getCallId());
		log.setLocalIp(responseVo.getLocalIp());
		log.setDeviceCode(deviceCode);
		log.setPlayType(vo.getPlayType());
		
		log.setStart(start);
		log.setMenuType(MenuTypeEnums.线索翻查.getValue());
		log.setOperType(DevcOperTypeEnums.录像回放.getValue());
		log.setContent("视频录像回放");
		log.setToken(this.token);
		cameraLogService.saveCameraLog(log, userVo);
		
		//this.inviteBiz.saveCallId(responseVo, token);
		return new ApiResponse().ok().setData(responseVo);
    }
	
	@ApiOperation(value="录像回放单路多时")
	@GetMapping(value = "/cameraPlaybackMany") 
    public ApiResponse<List<CallbackResponseVo>> cameraPlaybackMany(InviteOptionInputVo vo) {
		
		long start = Calendar.getInstance().getTime().getTime();
		String deviceCode = vo.getDeviceCode();
		String error = checkParam(vo);
		if(StrUtil.isNotBlank(error)) {
			return new ApiResponse().error(error);
		}
		
		if(vo.getNumber() == 0) {
			return new ApiResponse().error("分段数量不能为空！");
		}
		
		getUserPermission(deviceCode, "2");//查看录像视频
		
		String clientIp = ClientUtil.getClientIp(request);
		
		List<Integer> prots = ClientUtil.creatProts(clientIp, vo.getNumber());
		log.info("this prot is " +prots.toString());
		
		vo.setClientIp(clientIp);
		
		//调用封装的录像回放单路多时业务接口
		List<CallbackResponseVo> list = videoSipService.cameraMutiChannelPlayback(vo, prots);
		
		UserVo userVo = getLoginUser();
		UserOperLogVo log = new UserOperLogVo();
		log.setDeviceCode(vo.getDeviceCodes());
		log.setStart(start);
		log.setMenuType(MenuTypeEnums.线索翻查.getValue());
		log.setOperType(DevcOperTypeEnums.录像回放.getValue());
		log.setContent("视频录像单路多时回放");
		log.setToken(this.token);
		cameraLogService.saveCameraLog(log, userVo);
		
		return new ApiResponse().ok().setData(list);
    }
	
	
	//CmdType 回放控制：播放1    暂停2   快进/慢进 3    随机拖放4
	@ApiOperation(value="回放控制命令")
	@ApiImplicitParam(name="callId",value="CmdType回看控制播放1    暂停2   快进/慢进 3    随机拖放4;播放速度，基本取值：0.25、0.5、1、2、4;随机播播放录像起点的相对值;请求录像回看返回的callID;其余参数 ",required=true)
	@GetMapping(value = "/playbackCtrl") 
    public ApiResponse<List<String>> playbackCtrl(RecordCtrlOptionSipVo vo, String callId) {
		
		if(StrUtil.isBlank(callId)) {
			return new ApiResponse().error("callId不能为空！");
		}
		
		if(vo.getCmdType() == 0) {
			return new ApiResponse().error("回放控制参数不能为空！");
		}
		
		List<String> list = videoSipService.playbackCtrl(vo, callId);
		
		//this.videoBiz.saveUserLog(deviceCode,"视频录像回放","/videoMonitoring/cameraPlayback",start,"playback", this.token);
		return new ApiResponse().ok().setData(list);
    }
	
	
	@ApiOperation(value="视频文件下载")
	@GetMapping(value = "/cameraDownload") 
    public ApiResponse<List<CallbackResponseVo>> cameraDownload(InviteOptionInputVo vo) {
		
		if(StrUtil.isBlank(vo.getDeviceCodes())) {
			return new ApiResponse().error("请选择设备！");
		}
		
		if(vo.getStartTime() == null) {
			throw new ServiceException("视频开始时间不能为空！");
		}
		
		if(vo.getEndTime() == null) {
			throw new ServiceException("视频结束时间不能为空！");
		}
		
		String[] deviceCodes = vo.getDeviceCodes().split(",");
		
		List<CallbackResponseVo> responseVoList = new ArrayList<CallbackResponseVo>();
		String clientIp = ClientUtil.getClientIp(this.request);
		List<Integer> lists = ClientUtil.creatProts(clientIp, deviceCodes.length);
		
		vo.setClientIp(clientIp);
		
		UserVo userVo = getLoginUser();
		UserOperLogVo log = new UserOperLogVo();
		
		for(int i = 0;i < deviceCodes.length;i++) {
			
			if(StrUtil.isNotBlank(deviceCodes[i])) {
				
				getUserPermission(deviceCodes[i], "4");//录像视频下载
				
				long start = Calendar.getInstance().getTime().getTime();
				
				vo.setPort(lists.get(i));
				vo.setDeviceCode(deviceCodes[i]);
				CallbackResponseVo responseVo = videoSipService.invite(vo);
				responseVoList.add(responseVo);
				
				log = new UserOperLogVo();
				log.setDeviceCode(deviceCodes[i]);
				log.setStart(start);
				log.setMenuType(MenuTypeEnums.线索翻查.getValue());
				log.setOperType(DevcOperTypeEnums.录像下载.getValue());
				log.setContent("录像文件下载");
				log.setToken(this.token);
				cameraLogService.saveCameraLog(log, userVo);
			}
		}
		
		return new ApiResponse().ok().setData(responseVoList);
    }
	
	
	@ApiOperation(value="录像查询")
	@GetMapping(value = "/getVideoFile") 
    public ApiResponse<List<ResponseVideoVo>> getVideoFile(QueryRecOptionInputVo vo) {
		
		if(StrUtil.isBlank(vo.getDeviceCode())) {
			return new ApiResponse().error("请选择设备！");
		}
		
		if(vo.getStartTime() == null) {
			return new ApiResponse().error("开始时间不能为空！");
		}
		
		if(vo.getEndTime() == null) {
			return new ApiResponse().error("截止时间不能为空！");
		}
		
		if(vo.getStartTime() > vo.getEndTime()) {
			return new ApiResponse().error("开始时间不能大于截止时间！");
		}
		
//		vo.setStartTime(1536718486);
//		vo.setEndTime("1536718600");
		List<ResponseVideoVo> responseVo = videoSipService.getDownloadVideo(vo);
		
		return new ApiResponse().ok().setData(responseVo);
		
    }
	
	
	@ApiOperation(value="录像回放/下载结束回调状态")
	@ApiImplicitParam(name="callIds",value="录像回放/下载结束返回的callID")
	@GetMapping(value = "/responseStatus") 
    public ApiResponse<List<FileResponseVo>> responseStatus(String callIds) {
		
		if(StrUtil.isBlank(callIds)) {
			return new ApiResponse().error("callId不能为空！");
		}
		
		List<FileResponseVo> responseVo = videoSipService.responseStatus(callIds);
		
		return new ApiResponse().ok().setData(responseVo);
    }
	
	
	@ControllerLog
	@ApiOperation(value="新增摄像机录像标签")
	@PostMapping(value = "/saveCameraLabel")
    public ApiResponse<Object> saveUserFilepath(CameraLabelVo vo) {
 		 UserVo user = getLoginUser();
 		 
		 if(StrUtil.isBlank(vo.getName())) {
			 return new ApiResponse().error("标签名称不能为空！");
		 }
		 
		 if(vo.getStartTime() == null) {
			 return new ApiResponse().error("标签开始时间不能为空！");
		 }
		 
		 if(vo.getEndTime() == null) {
			 return new ApiResponse().error("标签结束不能为空！");
		 }
		 
		 cameraLabelService.saveCameraLabel(user, vo);
		 
		 return new ApiResponse().ok().setData("保存成功！");
    }
	
	
	@ApiOperation(value="查询摄像机录像标签列表")
	@ApiImplicitParams({
		@ApiImplicitParam(name="deviceId",value="设备id",required=false),
		@ApiImplicitParam(name="name",value="录像名称",required=true),
		@ApiImplicitParam(name="startTime",value="录像开始时间",required=false),
		@ApiImplicitParam(name="endTime",value="录像结束时间",required=false)
	})
	@GetMapping(value = "/getCameraLabel")
    public ApiResponse<List<CameraLabelVo>> getCameraLabel(String deviceId,String name,String startTime,String endTime) {
		 Map<String ,Object> map = new HashMap<String ,Object>();
		 UserVo user = getLoginUser();
		 if(user.getAccount().toLowerCase().equals("admin")){//管理员返回所有
			 map.put("isAdmin", "1");
	     }  
		 map.put("userId", user.getId());
		 map.put("deviceId", deviceId);
		 map.put("name", name);
		 map.put("startTime", startTime); 
		 map.put("endTime", endTime); 
		 List<CameraLabelVo> list = cameraLabelService.getCameraLabel(map); 
		return new ApiResponse().ok().setData(list);
    }
	
	@ApiOperation(value="批量删除便签")
    @DeleteMapping(value = "/delete")
    @ApiImplicitParam(name = "ids[]", value = "便签id数组", required = true)
    public ApiResponse<Object> delete(@RequestParam(value="ids[]") String[] ids) {
        List<String> listIds = Arrays.asList(ids);
        cameraLabelService.deleteByIds(listIds);
        return new ApiResponse().ok("便签删除成功");
    }
	
	private String checkParam(InviteOptionInputVo vo) {
		if(StrUtil.isBlank(vo.getDeviceCode())) {
			return "请选择设备！";
		}
		if(vo.getStartTime() ==null) {
			return "视频开始时间不能为空！";
		}
		if(vo.getEndTime() ==null) {
			return "视频结束时间不能为空！";
		}
		if(vo.getStartTime() > vo.getEndTime()) {
			return "开始时间不能大于结束时间！";
		}
		return null;
	}

}
