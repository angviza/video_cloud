package com.hdvon.nmp.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.IUserFilepathService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.ClientUtil;
import com.hdvon.nmp.vo.UserFilepathVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
@Api(value="/userSetting",tags="用户个人信息设置模块",description="用户个人信息设置模块")
@RestController
@RequestMapping("/userSetting")
@Slf4j
public class UserSettingController extends BaseController{
	@Reference
	private  IUserFilepathService userFilepathService;
	
	@ApiOperation(value="获取用户个人设置信息")
	@GetMapping(value = "/userFilepath")
    public ApiResponse<UserFilepathVo> getUserFilepath() {
		 UserVo user = getLoginUser();
		 Map<String ,String> map =new HashMap<String ,String>();
		 String macId=ClientUtil.getClientMac();
		 map.put("userId", user.getId());
		 map.put("macId", macId);
		 List<UserFilepathVo> list=userFilepathService.getUserFilepath(map);
		 UserFilepathVo record = null;
		 if(list.size()>0) {
			 record = list.get(0);
		 }
		 return new ApiResponse().ok().setData(record);
    }
	
	@ControllerLog
	@ApiOperation(value="保存用户个人设置信息")
	@PostMapping(value = "/saveUserFilepath")
    public ApiResponse<UserFilepathVo> saveUserFilepath(UserFilepathVo vo) {
		 UserVo user = getLoginUser();
		 String macId=ClientUtil.getClientMac();
		 if(StrUtil.isBlank(macId)) {
			 return new ApiResponse().error("无法获取本地mac地址！");
		 }
		 vo.setMacId(macId);
		 userFilepathService.saveUserFilepath(vo,user);
		return new ApiResponse().ok().setData("保存成功！");
    }
	
	

}
