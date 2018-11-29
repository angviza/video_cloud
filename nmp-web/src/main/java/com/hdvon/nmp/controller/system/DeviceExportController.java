package com.hdvon.nmp.controller.system;

import java.util.Calendar;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.enums.DevcOperTypeEnums;
import com.hdvon.nmp.enums.MenuTypeEnums;
import com.hdvon.nmp.service.ICameraLogService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.ClientUtil;
import com.hdvon.nmp.vo.UserVo;
import com.hdvon.nmp.vo.video.UserOperLogVo;
import com.hdvon.sip.video.service.IVideoSipService;
import com.hdvon.sip.video.vo.CallbackResponseVo;
import com.hdvon.sip.video.vo.InviteOptionInputVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(value="/deviceExport",tags="案事件管理模块",description="针对摄像机播放、云台控制等操作")
@RestController
@RequestMapping("/deviceExport")
public class DeviceExportController extends BaseController {
	
	@Reference
	private IVideoSipService videoSipService;
	
	@Reference
	private ICameraLogService cameraLogService;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value="视频实时播放")
	@GetMapping(value = "/videoPlay") 
    public ApiResponse<CallbackResponseVo> videoPlay(String deviceCode) {
		
		//vo.setDeviceCode("38020000001320000010");
		//String deviceCode = vo.getDeviceCode();
		//getUserPermission(deviceCode,"2");//查看录像视频
		long start = Calendar.getInstance().getTime().getTime();
		if(StrUtil.isBlank(deviceCode)) {
			return new ApiResponse().error("设备编码不能为空！");
		}
		
		InviteOptionInputVo vo = new InviteOptionInputVo();
		vo.setPlayType("1");//实时播放
		
		String clientIp = ClientUtil.getClientIp(this.request);
		List<Integer> list = ClientUtil.creatProts(clientIp, 1);
		//int port = ClientUtil.getClientProt(clientIp);
		vo.setPort(list.get(0));
		vo.setClientIp(clientIp);
		
		//调用视频播放接口
		CallbackResponseVo responseVo = videoSipService.invite(vo);
		
		UserVo userVo = new UserVo();
		UserOperLogVo log = new UserOperLogVo();
		log.setDeviceCode(deviceCode);
		log.setStart(start);
		log.setMenuType(MenuTypeEnums.案事件管理.getValue());
		log.setOperType(DevcOperTypeEnums.实时播放.getValue());
		log.setContent("视频实时播放");
		cameraLogService.saveCameraLog(log, userVo);
		
		return new ApiResponse().ok().setData(responseVo);
    }
	
	
	@ApiOperation(value="视频停止播放")
    @ApiImplicitParam(name = "callIds", value = "点播返回的callIds", required = true)
	@GetMapping(value = "/videoStop")
    public ApiResponse<Object> videoStop(String callIds) {
		
		if(StrUtil.isBlank(callIds)) {
			return new ApiResponse().error("参数不能为空！");
		}
		
		//调用视频停止播放接口
		videoSipService.videoStop(callIds);
		
		return new ApiResponse().ok().setData("视频停止成功");
    }
}
