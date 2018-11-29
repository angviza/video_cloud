package com.hdvon.nmp.controller.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.hdvon.nmp.common.WebConstant;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.enums.DevcOperTypeEnums;
import com.hdvon.nmp.enums.MenuTypeEnums;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.service.IBallPlanService;
import com.hdvon.nmp.service.ICameraLogService;
import com.hdvon.nmp.service.ICameraService;
import com.hdvon.nmp.service.IPollingPlanService;
import com.hdvon.nmp.service.IPresentPositionService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.ClientUtil;
import com.hdvon.nmp.util.distributedlock.RedisLock;
import com.hdvon.nmp.vo.BallPlanVo;
import com.hdvon.nmp.vo.CameraLabelVo;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.PlanPresetParamVo;
import com.hdvon.nmp.vo.PresentPositionVo;
import com.hdvon.nmp.vo.UserVo;
import com.hdvon.nmp.vo.sip.PresetOptionVo;
import com.hdvon.nmp.vo.video.UserOperLogVo;
import com.hdvon.sip.video.service.IVideoSipService;
import com.hdvon.sip.video.vo.CallbackResponseVo;
import com.hdvon.sip.video.vo.ControlOptionInputVo;
import com.hdvon.sip.video.vo.CruiseOptionInputVo;
import com.hdvon.sip.video.vo.InviteOptionInputVo;
import com.hdvon.sip.video.vo.PlayDownloadInputVo;
import com.hdvon.sip.video.vo.PresetOptionInputVo;
import com.hdvon.sip.video.vo.QueryPreOptionInputVo;
import com.hdvon.sip.video.vo.WiperOptionInputVo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="/videoMonitoring",tags="视频监控模块",description="针对摄像机播放操作")
@RestController
@RequestMapping("/videoMonitoring")
public class VideoSipController extends BaseController {
	
	//@Reference(version="1.0.0")
	//private IdeviceMonitorService deviceMonitorService;videoReplay
	
	@Reference
	private IPollingPlanService pollingPlanService;
	
	@Reference
	private IPresentPositionService presentPositionService;
	
	@Reference
	private IBallPlanService ballPlanService;
	
	@Reference
	private ICameraService cameraService;
	
	@Reference
	private ICameraLogService cameraLogService;
	
	@Reference
	private IVideoSipService videoSipService;
	
	//redis共享分布式锁
	private RedisLock lock = new RedisLock("REDIS_LOCK", 10000, 20000);
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value="视频实时播放")
	@GetMapping(value = "/cameraPlay") 
    public ApiResponse<CallbackResponseVo> cameraPlay(InviteOptionInputVo vo) {
		
		//String token = request.getHeader(WebConstant.TOKEN_HEADER);
		long start = Calendar.getInstance().getTime().getTime();
		String deviceCode = vo.getDeviceCode();
		
		if(StrUtil.isBlank(deviceCode)) {
			return new ApiResponse().error("设备编码不能为空！");
		}
		
		getUserPermission(deviceCode, "2");//检测查看录像视频的权限
		
		String clientIp = ClientUtil.getClientIp(request);
		
		if(null == vo.getPort()) {
			List<Integer> prots = ClientUtil.creatProts(clientIp, 1);
			if(prots.size()> 0) {
				vo.setPort(prots.get(0));
			} else {
				return new ApiResponse().error("系统内部错误");
			}
		}
		
		vo.setClientIp(clientIp);
		
		//CallbackResponseVo responseVo =null;
		//调用视频播放接口
		CallbackResponseVo responseVo = videoSipService.invite(vo);
		
		//this.inviteBiz.saveCallId(responseVo, token,userVo.getId(),deviceCode);
		UserVo userVo = getLoginUser();
		UserOperLogVo log = new UserOperLogVo();
		log.setUserId(userVo.getId());
		log.setCallId(responseVo.getCallId());
		log.setLocalIp(responseVo.getLocalIp());
		log.setDeviceCode(deviceCode);
		log.setPlayType(vo.getPlayType());
		
		log.setStart(start);
		log.setMenuType(MenuTypeEnums.视频监控.getValue());
		log.setOperType(DevcOperTypeEnums.实时播放.getValue());
		log.setContent("视频实时播放");
		log.setToken(this.token);
		cameraLogService.saveCameraLog(log, userVo);
		
		return new ApiResponse().ok().setData(responseVo);
    }

//	@ApiOperation(value="视频语音对讲")
//	@GetMapping(value = "/cameraTalk") 
//    public ApiResponse<InviteOptionVo> cameraTalk(InviteOptionVo vo) {
//		
//		String deviceCode = vo.getDeviceCode();
//		getUserPermission(deviceCode,"2");//查看录像
//		
//		if(StrUtil.isBlank(vo.getDeviceCode())) {
//			return new ApiResponse().error("设备编码不能为空！");
//		}
//		
//		this.videoBiz.invite(vo, this.request);
//		return new ApiResponse().ok().setData("语音对讲成功！");
//    }
	
//	@ApiOperation(value="视频暂停播放")
//	@ApiImplicitParams({
//		@ApiImplicitParam(name = "deviceCode", value = "设备编码", required = true)
//	})
//	@GetMapping(value = "/cameraSuspend")
//    public ApiResponse<Object> cameraSuspend(String[] deviceCodes) {
//		
//		UserVo userVo = getLoginUser();
//		
//		for(String code:deviceCodes) {
//			String inviteKey = userVo.getId()+"_"+code+"_INVITEID";
//			String inviteId = (String) redisTemplate.opsForValue().get(inviteKey);
//			
//			if(StrUtil.isNotBlank(inviteId)) {
//				deviceMonitorService.callTerminate(inviteId);
//			}
//		}
//		
//		return new ApiResponse().ok().setData("视频暂停成功");
//    }
	
	@ApiOperation(value="视频停止播放")
    @ApiImplicitParam(name = "callIds", value = "点播返回的callIds", required = true)
	@GetMapping(value = "/cameraStop")
    public ApiResponse<Object> cameraStop(String callIds) {
		
		if(StrUtil.isBlank(callIds)) {
			return new ApiResponse().error("参数不能为空！");
		}
		
		//调用视频停止播放接口
		videoSipService.videoStop(callIds);
		
		List<String> callIdList = Arrays.asList(callIds.split(","));
		cameraLogService.deleteLog(callIdList);
		
		return new ApiResponse().ok().setData("视频停止播放成功");
    }
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value="摄像头方向控制")
	@GetMapping(value = "/directionControl")
    public ApiResponse<Object> directionControl(ControlOptionInputVo vo) {
		long start = Calendar.getInstance().getTime().getTime();
		String deviceCode = vo.getDeviceCode();
		
		if(StrUtil.isBlank(deviceCode)) {
			return new ApiResponse().error("设备编码不能为空");
		}
		
		if(null == vo.getDirection()) {
			return new ApiResponse().error("方向控制参数不能为空");
		}
		
		if(null != vo.getIsCancel() && vo.getIsCancel() == 0) {
			return new ApiResponse().error("参数[isCancel]不合法");
		}
		
		getUserPermission(deviceCode, "32");//检测云台方向控制的权限
		
		//调用云台控制接口
		videoSipService.directionControl(vo);
		
		UserVo userVo = getLoginUser();
		UserOperLogVo log = new UserOperLogVo();
		log.setDeviceCode(deviceCode);
		log.setStart(start);
		log.setMenuType(MenuTypeEnums.视频监控.getValue());
		log.setOperType(DevcOperTypeEnums.云台控制.getValue());
		log.setContent("摄像头方向控制");
		log.setToken(this.token);
		cameraLogService.saveCameraLog(log, userVo);
		
		return new ApiResponse().ok().setData("摄像头方向控制成功");
    }
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value="预置位设置控制")
	@GetMapping(value = "/presetControl")
    public ApiResponse<Object> presetControl(PresetOptionInputVo vo) {
		UserVo userVo = getLoginUser();
		String deviceCode = vo.getDeviceCode(); 
		String cameraId = vo.getCameraId();
		long start = Calendar.getInstance().getTime().getTime();
		
		String validMsg = "";
		if (vo.getEntranceType() == null || (vo.getEntranceType() != 1 && vo.getEntranceType() != 2)) {
			validMsg = "调用预置位控制接口入口类型参数不对！";
		}
		
		if (null == vo.getPresetType() || (vo.getPresetType() != 1 && vo.getPresetType() != 2 && vo.getPresetType() != 3)) {
			validMsg = "预置位控制类型参数不对！";
		}
		
		if (vo.getEntranceType() ==1 && vo.getPresetType() == 2 && vo.getPresetNum() ==null) {
			validMsg = "调用预置位，预置位编号参数不能为空！";
		}
		
		if (vo.getPresetType() == 3 && vo.getPresetNum() == null) {
			validMsg = "删除预置位，预置位编号参数不能为空！";
		}
		
		if (vo.getEntranceType() == 1 && StrUtil.isBlank(deviceCode)) {
			validMsg = "设备编码不能为空！";
		}
		
		if (vo.getEntranceType() == 2 && vo.getPresetType() == 1 && StrUtil.isBlank(cameraId)) {
			validMsg = "设备id不能为空！";
		}
		
		if (StrUtil.isNotBlank(validMsg)) {
			return new ApiResponse().error(validMsg);
		}
		
		if (vo.getEntranceType() == 1) {//视屏监控
			getUserPermission(deviceCode,"64");//云台其他控制
		}

		if (vo.getEntranceType() == 1) {//视屏监控 校验摄像机是否存在
			List<CameraVo> cameras = cameraService.getCameraBySbbm(deviceCode);
			//cameraId=presentPositionService.checkParam(vo);
			if(cameras.size() == 0) {
				return new ApiResponse().error("设备编码不存在");
			} else {
				cameraId = cameras.get(0).getId();
			}
		} else if(vo.getEntranceType() == 2 && vo.getPresetType() == 1) {//球机巡航预案 添加预置位的时候校验摄像机是否存在
			CameraVo cameraVo = cameraService.getCameraInfo(cameraId);
			if(cameraVo == null) {
				return new ApiResponse().error("摄像机不存在！");
			}
			vo.setDeviceCode(cameraVo.getSbbm());
		}
		
		if(vo.getPresetType() == 1) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("cameraId", cameraId);
			List<Integer> presentNos = presentPositionService.getEnablePresentPositions(cameraId);
			
			if(presentNos.size() == 0) {
				return new ApiResponse().error("没有可用来设置的预置位编号！");
			}
			
			if(vo.getEntranceType() == 1) {
				vo.setPresetNum(presentNos.get(0));
			}
		}
		
		//调用预置位控制接口
		videoSipService.presetControl(vo);
		
		UserOperLogVo log = new UserOperLogVo();
		log.setDeviceCode(deviceCode);
		log.setStart(start);
		log.setMenuType(MenuTypeEnums.视频监控.getValue());
		log.setOperType(DevcOperTypeEnums.云台控制.getValue());
		log.setContent("预置位设置控制");
		log.setToken(this.token);
		cameraLogService.saveCameraLog(log, userVo);
		
		PresetOptionVo presetVo = Convert.convert(PresetOptionVo.class, vo);
		
		if (vo.getPresetType() == 1) {//设置预置位
			presentPositionService.savePresent(userVo, presetVo, cameraId);
			return new ApiResponse().ok("设置预置位成功");
		} 
		
		if (vo.getPresetType() == 2) {//调用预置位
			return new ApiResponse().ok("调用预置位成功");
		} 
		
		if (vo.getPresetType() == 3) {//删除预置位
			presentPositionService.delPresent(presetVo);
			List<String> idList = new ArrayList<String>();
			idList.add(vo.getPresetId().toString());
	        presentPositionService.delPresentPositionsByIds(idList);
			return new ApiResponse().ok("删除预置位成功");
		}
		
		return new ApiResponse().ok();
    }
	
	
	@ApiOperation(value="预置位查询")
	@GetMapping(value = "/queryPreset")
    public ApiResponse<Object> queryPreset(QueryPreOptionInputVo vo) {
		
		if(StrUtil.isBlank(vo.getDeviceCode())) {
			return new ApiResponse().error("设备编码不能为空");
		}
		
		CallbackResponseVo responseVo = videoSipService.queryPreset(vo);
		
		return new ApiResponse().ok().setData(responseVo);
    }
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value="取消摄像头方向控制")
	@GetMapping(value = "/cancelControl")
    public ApiResponse<Object> cancelControl(ControlOptionInputVo vo) {
		long start = Calendar.getInstance().getTime().getTime();
		String deviceCode = vo.getDeviceCode();
		
		if(StrUtil.isBlank(deviceCode)) {
			return new ApiResponse().error("设备编码不能为空");
		}
		
		if(null == vo.getIsCancel() || vo.getIsCancel() == 1) {
			return new ApiResponse().error("参数[isCancel]不合法");
		}
		
		//调用云台控制接口
		videoSipService.cloudControl(vo);
		
		UserVo userVo = getLoginUser();
		UserOperLogVo log = new UserOperLogVo();
		log.setDeviceCode(deviceCode);
		log.setStart(start);
		log.setMenuType(MenuTypeEnums.视频监控.getValue());
		log.setOperType(DevcOperTypeEnums.云台控制.getValue());
		log.setContent("取消摄像头方向控制");
		log.setToken(this.token);
		cameraLogService.saveCameraLog(log, userVo);
		
		return new ApiResponse().ok().setData("取消摄像头方向控制成功");
    }
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value="摄像头镜头变倍(云台控制微调变焦)")
	@GetMapping(value = "/zoomControl")
    public ApiResponse<Object> zoomControl(ControlOptionInputVo vo) {
		long start = Calendar.getInstance().getTime().getTime();
		String deviceCode = vo.getDeviceCode();
		
		if(StrUtil.isBlank(deviceCode)) {
			return new ApiResponse().error("设备编码不能为空！");
		}
		
		if(null == vo.getZoom()) {
			return new ApiResponse().error("镜头变焦参数不能为空");
		}
		
		if(null == vo.getStep()) {
			return new ApiResponse().error("镜头变焦速度参数不能为空");
		}
		
		getUserPermission(deviceCode, "64");//云台其他控制
		
		//调用云台控制接口
		videoSipService.cloudControl(vo);
		
		UserVo userVo = getLoginUser();
		UserOperLogVo log = new UserOperLogVo();
		log.setDeviceCode(deviceCode);
		log.setStart(start);
		log.setMenuType(MenuTypeEnums.视频监控.getValue());
		log.setOperType(DevcOperTypeEnums.云台控制.getValue());
		log.setContent("摄像头镜头变焦控制");
		log.setToken(this.token);
		cameraLogService.saveCameraLog(log, userVo);
				
		return new ApiResponse().ok().setData("摄像头镜头变焦控制成功");
    }
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value="摄像头镜头光圈(云台控制微调镜头光圈)")
	@GetMapping(value = "/irisControl")
    public ApiResponse<Object> irisControl(ControlOptionInputVo vo) {
		long start = Calendar.getInstance().getTime().getTime();
		String deviceCode = vo.getDeviceCode();
		
		if(StrUtil.isBlank(deviceCode)) {
			return new ApiResponse().error("设备编码不能为空！");
		}
		
		if(null == vo.getIris()) {
			return new ApiResponse().error("镜头光圈参数不能为空");
		}
		
		if(null == vo.getStep()) {
			return new ApiResponse().error("镜头光圈速度参数不能为空");
		}
		
		getUserPermission(deviceCode, "64");//云台其他控制
		
		//调用云台控制接口
		videoSipService.cloudControl(vo);
		
		UserVo userVo = getLoginUser();
		UserOperLogVo log = new UserOperLogVo();
		log.setDeviceCode(deviceCode);
		log.setStart(start);
		log.setMenuType(MenuTypeEnums.视频监控.getValue());
		log.setOperType(DevcOperTypeEnums.云台控制.getValue());
		log.setContent("摄像头镜头光圈控制");
		log.setToken(this.token);
		cameraLogService.saveCameraLog(log, userVo);
		
		return new ApiResponse().ok().setData("摄像头镜头光圈控制成功");
    }
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value="摄像头镜头焦距(云台控制微调调焦)")
	@GetMapping(value = "/focusControl")
    public ApiResponse<Object> focusControl(ControlOptionInputVo vo) {
		long start = Calendar.getInstance().getTime().getTime();
		String deviceCode = vo.getDeviceCode();
		
		if(StrUtil.isBlank(deviceCode)) {
			return new ApiResponse().error("设备编码不能为空！");
		}
		
		if(null == vo.getFocus()) {
			return new ApiResponse().error("镜头焦距参数不能为空");
		}
		
		if(null == vo.getStep()) {
			return new ApiResponse().error("镜头焦距速度参数不能为空");
		}
		
		getUserPermission(deviceCode, "64");//云台其他控制
		
		//调用云台控制接口
		videoSipService.cloudControl(vo);
		
		UserVo userVo = getLoginUser();
		UserOperLogVo log = new UserOperLogVo();
		log.setDeviceCode(deviceCode);
		log.setStart(start);
		log.setMenuType(MenuTypeEnums.视频监控.getValue());
		log.setOperType(DevcOperTypeEnums.云台控制.getValue());
		log.setContent("摄像头镜头调焦控制");
		log.setToken(this.token);
		cameraLogService.saveCameraLog(log, userVo);
		
		return new ApiResponse().ok().setData("摄像头镜头调焦控制成功");
    }
	
	
	@ApiOperation(value="录像控制")
	@GetMapping(value = "/palyDownload")
    public ApiResponse<Object> palyDownload(PlayDownloadInputVo vo) {
		long start = Calendar.getInstance().getTime().getTime();
		if(StrUtil.isBlank(vo.getDeviceCode())) {
			return new ApiResponse().error("设备编码不能为空！");
		}
		
		if(vo.getCmdType()==null) {
			return new ApiResponse().error("控制类型不能为空！");
		}
		
		if(vo.getCmdType() !=1 && vo.getCmdType() !=2) {
			return new ApiResponse().error("控制类型参数不正确！");
		}
		
		getUserPermission(vo.getDeviceCode(),"2");//查看录像视频
		
		//调用录像控制接口
		String callId = videoSipService.playDownload(vo);
		
		UserVo userVo = getLoginUser();
		UserOperLogVo log = new UserOperLogVo();
		log.setDeviceCode(vo.getDeviceCode());
		log.setStart(start);
		log.setMenuType(MenuTypeEnums.视频监控.getValue());
		log.setOperType(DevcOperTypeEnums.录像下载.getValue());
		log.setContent("实时录像控制");
		log.setToken(this.token);
		cameraLogService.saveCameraLog(log, userVo);
		
		return new ApiResponse().ok().setData(callId);
    }
	
	@ApiOperation(value="雨刷开关控制")
	@GetMapping(value = "/wiperControl") 
    public ApiResponse<Object> wiperControl(WiperOptionInputVo vo) {
		long start = Calendar.getInstance().getTime().getTime();
		String deviceCode = vo.getDeviceCode();
		
		if(StrUtil.isBlank(deviceCode)) {
			return new ApiResponse().error("设备编码不能为空！");
		}
		
		if(vo.getCmdType() == null) {
			return new ApiResponse().error("控制类型不能为空！");
		}
		
		if(vo.getCmdType() != 1 && vo.getCmdType() != 2) {
			return new ApiResponse().error("控制类型参数错误！");
		}
		
		getUserPermission(deviceCode, "64");//云台其他控制
		
		//调用雨刷开关接口
		videoSipService.wiperControl(vo);
		
		UserVo userVo = getLoginUser();
		UserOperLogVo log = new UserOperLogVo();
		log.setDeviceCode(vo.getDeviceCode());
		log.setStart(start);
		log.setMenuType(MenuTypeEnums.视频监控.getValue());
		log.setOperType(DevcOperTypeEnums.云台控制.getValue());
		log.setContent("雨刷开关控制");
		log.setToken(this.token);
		cameraLogService.saveCameraLog(log, userVo);
		
		String data = vo.getCmdType()==1?"开启":"关闭";
		return new ApiResponse().ok().setData("雨刷"+data+"成功!");
    }
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value="球机预置位巡航预案控制")
	@GetMapping(value = "/presetCruiseControl")
    public ApiResponse<Object> presetCruiseControl(CruiseOptionInputVo vo) {
		long start = Calendar.getInstance().getTime().getTime();
		String deviceCode = vo.getDeviceCode();
		
		if(StrUtil.isBlank(deviceCode)) {
			return new ApiResponse().error("设备编码不能为空");
		}
		
		if(null == vo.getCmdType()) {
			return new ApiResponse().error("巡航控制类型参数不能为空");
		}
		
		getUserPermission(deviceCode, "64");//云台其他控制
		
		String type = "";
		int cmdType = vo.getCmdType();
		
		if (cmdType == 0) {//调用云台控制的停止命令
			
			ControlOptionInputVo param = new ControlOptionInputVo();
			param.setDeviceCode(vo.getDeviceCode());
			//停止指令
			param.setIsCancel(cmdType);
			
			//调用云台控制的停止接口
			videoSipService.cloudControl(param);
			type = "停止球机预置位巡航预案控制";
			
		} else {
			
			if (null == vo.getGroupNum()) {
				return new ApiResponse().error("巡航组号参数不能为空");
			}
			
			if (vo.getGroupNum() < 0 || vo.getGroupNum() > 255) {
				return new ApiResponse().error("巡航组号取值必须是0-255");
			}
			
			if (null == vo.getPresetNum()) {
				return new ApiResponse().error("预置位号参数不能为空");
			}
			
			if (vo.getCmdType() != 2) {//非删除操作
				if (vo.getPresetNum() < 1 || vo.getPresetNum() > 255) {
					return new ApiResponse().error("非删除操作时，预置位号取值必须是1-255");
				}
			} else {//删除操作
				if (vo.getPresetNum() < 0 || vo.getPresetNum() > 255) {
					return new ApiResponse().error("删除操作时，预置位号取值必须是0-255");
				}
			}
			
			if (null == vo.getStayTime()) {
				return new ApiResponse().error("巡航停留时间参数不能为空");
			}
			
			if (null == vo.getSpeed()) {
				return new ApiResponse().error("巡航速度参数不能为空");
			}
			
			//调用巡航预案控制接口
			videoSipService.cruiseControl(vo);
			
			type = cmdType==1?"加入球机预置位巡航点预案控制":cmdType==2?"删除球机预置位巡航点预案控制":cmdType==3?
					"设置球机预置位巡航速度预案控制":cmdType==4?"设置球机预置位巡航停留时间预案控制":cmdType==5?"开始球机预置位巡航预案控制":"球机预置位巡航预案控制";
		}
		
		UserVo userVo = getLoginUser();
		UserOperLogVo log = new UserOperLogVo();
		log.setDeviceCode(deviceCode);
		log.setStart(start);
		log.setMenuType(MenuTypeEnums.视频监控.getValue());
		log.setOperType(DevcOperTypeEnums.云台控制.getValue());
		log.setContent("球机预置位巡航预案控制");
		log.setToken(this.token);
		log.setContent(type);
		cameraLogService.saveCameraLog(log, userVo);
		
		return new ApiResponse().ok().setData(type+"成功");
    }
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value="球机巡航预案预置位查询列表")
	@GetMapping(value = "/presetCruiseList")
    public ApiResponse<List<PresentPositionVo>> presetCruiseList(PlanPresetParamVo vo) {
		
		if (StrUtil.isBlank(vo.getPlanId())) {
			return new ApiResponse().error("巡航预案id不能为空");
		}
		
		if(StrUtil.isBlank(vo.getCameraId())) {
			return new ApiResponse().error("摄像机id不能为空");
		}
		
		BallPlanVo planVo = ballPlanService.getBallPlanById(vo.getPlanId());
		if (null == planVo) {
			throw new ServiceException("未找到关联的球机巡航预案信息，请联系系统管理员");
		}
		
		if (null == planVo.getStatus() || planVo.getStatus() == 0) {
			throw new ServiceException("巡航预案未启用，请联系系统管理员");
		}
		
		/**
		if (null == planVo.getShareStatus()) {
			throw new ServiceException("巡航预案的共享设置状态未配置，请联系系统管理员");
		}
		**/
		
		if (null == planVo.getBgnTime() || null == planVo.getEndTime()) {
			throw new ServiceException("巡航预案开始时间或结束时间未设置，请联系系统管理员");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cameraId", vo.getCameraId());
		map.put("ballplanId", vo.getPlanId());
		List<PresentPositionVo> list = ballPlanService.getPresentPositionsInBallPlan(map);
		
		return new ApiResponse().ok().setData(list);
    }
	

	/*@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value="摄像头取消控制")
	@GetMapping(value = "/resetControl")
    public ApiResponse<Object> cameraStep(ControlOptionVo vo) {
		ControlOption paramVo = compositeParamsUtils.creatControlOptionParam(vo);
		deviceMonitorService.callCloudReset(paramVo);
//		if(vo.getDeviceCode() ==null) {
//			throw new DeviceServiceException("500");
//		}
		return new ApiResponse().ok().setData("摄像头取消控制成功");
    }*/
	
	
	@ApiOperation(value="摄像头初始化锁时状态")
	@ApiImplicitParams({
		@ApiImplicitParam(name="deviceCode",value="设备编码",required=true)
	})
	@GetMapping(value = "/cameraLockState")
    public ApiResponse<Object> cameraLockState(String deviceCode) {
		
		String lockKey = deviceCode + "_LOCK";
		Map<String, Object> response = new HashMap<String,Object>();
		Boolean state = false;
		
		if (redisTemplate.hasKey(lockKey)) {
			state = true;
			Map<String, Object> map = (Map<String, Object>) redisTemplate.opsForValue().get(lockKey);
			long mapLockTime = (long) map.get("mapLockTime");
			response.put("lockTime", mapLockTime);
		}
		response.put("state", state);
		return new ApiResponse().ok().setData(response);
    }
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value="摄像头锁时控制")
	@ApiImplicitParams({
		@ApiImplicitParam(name="deviceCode",value="设备编码",required=true),
		@ApiImplicitParam(name="time",value="锁时时间",required=true)
	})
	@GetMapping(value = "/cameraLock")
    public ApiResponse<Object> cameraLock(String deviceCode, long time) {
		
		getUserPermission(deviceCode, "16");//锁定云台控制
		String lockKey = deviceCode + "_LOCK";
		
		try {
			UserVo userVo = getLoginUser();
			
			//获取当前用户的权限级别
			Integer currLevel = userVo.getLevel();
			
			Map<String, Object> map = null;
			String dept = null;
			String account = null;
			long rest = 0;
			long lockTime = 0;
			long start = Calendar.getInstance().getTime().getTime();
			
			UserOperLogVo log = new UserOperLogVo();
			log.setDeviceCode(deviceCode);
			log.setStart(start);
			log.setMenuType(MenuTypeEnums.视频监控.getValue());
			log.setOperType(DevcOperTypeEnums.云台控制.getValue());
			log.setContent("摄像头锁时控制");
			log.setToken(this.token);
			
			//没有任何用户操作过当前设备或锁时控制时间已过期
			//注意：如果锁时控制时间已过期，那么对应用户的权限级别也会不复存在
			if (!redisTemplate.hasKey(lockKey)) {
				
				//开始争抢redis锁
				//先获取到redis锁的用户锁住当前操作
				if (lock.lock(redisTemplate)) {//锁住资源，并往redis缓存写入权限级别
					
					//请求到redis锁之后，需要将之前的用户权限级别覆盖掉
					this.setLock(userVo, lockKey, time);
					cameraLogService.saveCameraLog(log, userVo);
					
					return new ApiResponse().ok("摄像头锁时设置成功!");
				} else {//未抢到锁
					
					/************************************************ Added by huanhongliang 2018/9/25 ******************************************/
					//判断抢到锁的用户是否已存储到Redis缓存中
					map = (Map<String, Object>) redisTemplate.opsForValue().get(lockKey);
					if (null == map) {
						return new ApiResponse().error("锁定云台控制超时，请稍后重试！");
					}
					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
					//提示用户：当前操作已被其他用户锁定
					//return new ApiResponse().error("当前操作已被其他用户锁定!");
				}
			}//当前设备已被用户操作过，此时锁时控制时间未过期
			
			map = (Map<String, Object>) redisTemplate.opsForValue().get(lockKey);
			String userId = (String) map.get("mapUserId");
			
			if (!userId.equals(userVo.getId())) {//不同的用户之间争抢redis锁
				
				Integer permissionLevel = map.get("mapUserLevel")==null?0:(Integer) map.get("mapUserLevel");
				currLevel = currLevel==null?0:currLevel;
				
				/****************************************** Updated by huanhongliang 2018/10/26	**************************************/
				//如果当前用户权限级别比已锁定设备用户的权限级别高，则需要用redis锁 锁定当前操作，并将自己的权限级别写入缓存
				if(currLevel.intValue() != 0) {
					
					if (permissionLevel.intValue() == 0 || currLevel.intValue() < permissionLevel.intValue()) {
						//权限级别高的用户之间也要争抢redis锁
						if (lock.lock(redisTemplate)) {//锁住资源，并往redis缓存中下写入权限级别
							
							//请求到redis锁之后，需要将之前的用户权限级别覆盖掉
							this.setLock(userVo, lockKey, time);
							cameraLogService.saveCameraLog(log, userVo);
							
							return new ApiResponse().ok("摄像头锁时设置成功!");
						}
					}//当前用户的权限级别低于已锁定设备用户的权限级别，等待解锁
				}//当前用户的权限级别未设置，等待解锁
				/*********************************************************************************************************************/
				
			} else {//同一个用户，说明当前redis锁一直被当前用户持有
				//可以继续抢占redis锁
				if (lock.lock(redisTemplate)) {//锁住资源，并往redis缓存中下写入权限级别
					
					//请求到redis锁之后，需要将之前的用户权限级别覆盖掉
					this.setLock(userVo, lockKey, time);
					cameraLogService.saveCameraLog(log, userVo);
					
					return new ApiResponse().ok("摄像头锁时设置成功!");
				}
			}
			
			/************************************************ Added by huanhongliang 2018/9/25 ******************************************/
			//获取获取锁的用户以及其所在部门
			map = (Map<String, Object>) redisTemplate.opsForValue().get(lockKey);
			dept = (String) map.get("mapUserDept");
			account = (String) map.get("mapUserName");
			lockTime = (Long) map.get("mapLockTime");
			
			Calendar cal = Calendar.getInstance();
			rest = (lockTime - cal.getTime().getTime())/(1000*60);
			
			//该摄像机已被XX部门的XX用户云台锁定，还有XX时间可以解锁
			if ("".equals(dept.trim())) {
				return new ApiResponse().error("该摄像机已被账号为："+account+"的用户锁定云台控制，还有"+rest+"分钟才能解锁！");
			}
			
			return new ApiResponse().error("该摄像机已被"+dept+"部门的"+account+"用户锁定云台控制，还有"+rest+"分钟才能解锁！");
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
		} catch (InterruptedException e) {
			throw new ServiceException("锁定云台控制操作失败!");
			//e.printStackTrace();
			//释放redis锁
			//lock.unlock();
		} finally {
			/**
			 * 为了让分布式锁的算法更稳键些，持有锁的客户端在解锁之前应该再检查一次自己的锁是否已经超时，再去做DEL操作，
			 * 因为可能客户端因为某个耗时的操作而挂起，操作完的时候锁因为超时已经被别人获得，这时就不必解锁了。 ————这里没有做
			 */
			//释放redis锁
			lock.unlock(redisTemplate);
		}
		
    }
	
	@SuppressWarnings({ "unchecked" })
	@ApiOperation(value="摄像头取消锁时控制")
	@ApiImplicitParams({
		@ApiImplicitParam(name="deviceCode",value="设备编码",required=true)
	})
	@GetMapping(value = "/cameraUnLock")
    public ApiResponse<Object> cameraUnLock(String deviceCode, String confirm) {
		
		long start = Calendar.getInstance().getTime().getTime();
		getUserPermission(deviceCode, "16");//锁定云台控制
		
		String lockKey = deviceCode + "_LOCK";
		UserVo userVo = getLoginUser();
		Integer currLevel = userVo.getLevel();
		
		UserOperLogVo log = new UserOperLogVo();
		log.setDeviceCode(deviceCode);
		log.setStart(start);
		log.setMenuType(MenuTypeEnums.视频监控.getValue());
		log.setOperType(DevcOperTypeEnums.云台控制.getValue());
		log.setContent("取消摄像机锁时控制");
		log.setToken(this.token);
		
		//用户操作过当前设备或锁时控制时间未过期
		if (redisTemplate.hasKey(lockKey)) {
			
			Map<String, Object> map = (Map<String, Object>) redisTemplate.opsForValue().get(lockKey);
			String userId = (String) map.get("mapUserId");
			if (userId.equals(userVo.getId())) {//redis锁一直被当前用户持有
				
				//此时要删除设备编码的key
				redisTemplate.delete(lockKey);
				
				if (redisTemplate.hasKey(WebConstant.ALERT_MSG_KEY)) {
					redisTemplate.delete(WebConstant.ALERT_MSG_KEY);
					cameraLogService.saveCameraLog(log, userVo);
				}
				
				return new ApiResponse().ok("摄像头取消锁时控制成功");
				
			}
				
			/************************************************ Added by huanhongliang 2018/9/25 ******************************************/
			//获取获取锁的用户以及其所在部门
			String desc = "";
			String dept = (String) map.get("mapUserDept");
			String account = (String) map.get("mapUserName");
			long lockTime = (Long) map.get("mapLockTime");
			
			Calendar cal = Calendar.getInstance();
			long rest = (lockTime - cal.getTime().getTime())/(1000*60);
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			Integer permissionLevel = map.get("mapUserLevel")==null?0:(Integer) map.get("mapUserLevel");
			currLevel = currLevel==null?0:currLevel;
			
			//如果当前用户权限级别比已锁定设备用的权限级高,通知前端弹出对话框是否要确定要解锁操作
			if(currLevel.intValue() != 0) {
				
				if (permissionLevel.intValue() == 0 || currLevel.intValue() < permissionLevel.intValue()) {
					
					if ("OK".equals(confirm) && !redisTemplate.hasKey(WebConstant.ALERT_MSG_KEY)) {
						
						/************************************************ Added by huanhongliang 2018/9/25 ******************************************/
						if ("".equals(dept.trim())) {
							desc = "该摄像机已被账号为："+account+"的用户解锁云台控制，不能重复解锁！";
						} else {
							desc = "该摄像机已被"+dept+"部门的"+account+"用户解锁云台控制，不能重复解锁！";
						}
						/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						
						return new ApiResponse().error(desc);
					}
					
					//当前用户权限级别比已锁定设备用的权限级高，点击对话框的确认按钮才能够解锁
					if ("OK".equals(confirm) && redisTemplate.hasKey(WebConstant.ALERT_MSG_KEY)) {
						redisTemplate.delete(lockKey);
						redisTemplate.delete(WebConstant.ALERT_MSG_KEY);
						cameraLogService.saveCameraLog(log, userVo);
						return new ApiResponse().ok("摄像头取消锁时控制成功");
					}
					
					/************************************************ Added by huanhongliang 2018/9/25 ******************************************/
					if ("".equals(dept.trim())) {
						desc = "该摄像机当前正在被账号为："+account+"的用户锁定云台控制，还有"+rest+"分钟解锁，您确认要立即解锁吗？";
					} else {
						desc = "该摄像机当前正在被"+dept+"部门的"+account+"用户锁定云台控制，还有"+rest+"分钟解锁，您确认要立即解锁吗？";
					}
					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
					redisTemplate.opsForValue().set(WebConstant.ALERT_MSG_KEY, "ALERT_MSG");
					
					ApiResponse response = new ApiResponse();
					response.setData("ALERT_MSG");
					response.setMessage(desc);
					response.setCode("200");
					return response;
					
				}
			}
			
			/************************************************ Added by huanhongliang 2018/9/25 ******************************************/
			if ("".equals(dept.trim())) {
				desc = "解锁失败：权限级别过低，无法解除账号为："+account+"的用户锁定的云台控制！";
			} else {
				desc = "解锁失败：权限级别过低，无法解除"+dept+"部门的"+account+"用户锁定的云台控制！";
			}
			
			return new ApiResponse().error(desc);
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		}
		
		return new ApiResponse().ok("摄像头取消锁时控制成功");
    }
	
	
	@ApiOperation(value="根据轮巡预案id查询出摄像机列表")
	@ApiImplicitParam(name="pollingplanId",value="轮巡预案id",required=true)
	@GetMapping(value = "/getCamerasByPollingplanId")
    public ApiResponse<List<CameraLabelVo>> getCamerasByPollingplanId(String pollingplanId) {
		 List<CameraVo> list = pollingPlanService.getCamerasByPollingplanId(pollingplanId); 
		return new ApiResponse().ok().setData(list);
    }

}
