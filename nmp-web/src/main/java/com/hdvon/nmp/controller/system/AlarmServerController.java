package com.hdvon.nmp.controller.system;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.IAlarmServerService;
import com.hdvon.nmp.service.ICameraService;
import com.hdvon.nmp.service.ISigServerService;
import com.hdvon.nmp.service.ITreeNodeService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.vo.AlarmServerParamVo;
import com.hdvon.nmp.vo.AlarmServerVo;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.SigServerParamVo;
import com.hdvon.nmp.vo.SigServerVo;
import com.hdvon.nmp.vo.TreeNode;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/alarmServer",tags="报警设备管理模块",description="针对报警设备管理的插入,删除,修改,查看等操作")
@RestController
@RequestMapping("/alarmServer")
@Slf4j
public class AlarmServerController extends BaseController{
	@Reference
	private IAlarmServerService alarmServerService;
	
	@Reference
	private ICameraService cameraService;
	
	@Reference
	private ITreeNodeService treeNodeService;
	
	//分页查询报警设备列表
	@ApiOperation(value="分页查询报警设备列表")
	@ApiImplicitParam(name = "search", value = "报警设备名称查询条件", required = false)
	@GetMapping(value = "/alarmServerPage")
	public ApiResponse<PageInfo<AlarmServerVo>> alarmServerPage(PageParam pp, AlarmServerParamVo alarmServerParamVo) {
		 ApiResponse<PageInfo<AlarmServerVo>> resp = new ApiResponse<PageInfo<AlarmServerVo>>();
        
        TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
		if(StrUtil.isNotBlank(alarmServerParamVo.getAddressId())) {
			List<TreeNode> addressNodes = treeNodeService.getChildNodesByCode(alarmServerParamVo.getAddressCode(), TreeType.ADDRESS.getVal());
			treeNodeChildren.setAddressNodes(addressNodes);
		}
		
        PageInfo<AlarmServerVo> pageInfo= alarmServerService.getAlarmServerPages(pp, treeNodeChildren, alarmServerParamVo);
        resp.ok("查询成功！").setData(pageInfo);
		 return resp;
	}

	//查询报警设备列表
	@ApiOperation(value="查询报警设备列表")
	@ApiImplicitParam(name = "search", value = "报警设备名称查询条件", required = false)
	@GetMapping(value = "/alarmServerList")
	public ApiResponse<List<AlarmServerVo>> alarmServerList(PageParam pp, AlarmServerParamVo alarmServerParamVo) {
		 ApiResponse<List<AlarmServerVo>> resp = new ApiResponse<List<AlarmServerVo>>();
        
        TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
		if(StrUtil.isNotBlank(alarmServerParamVo.getAddressId())) {
			List<TreeNode> addressNodes = treeNodeService.getChildNodesByCode(alarmServerParamVo.getAddressCode(), TreeType.ADDRESS.getVal());
			treeNodeChildren.setAddressNodes(addressNodes);
		}
		
        List<AlarmServerVo> list= alarmServerService.getAlarmServerList(treeNodeChildren, alarmServerParamVo);
        resp.ok("查询成功！").setData(list);
		 return resp;
	}
	//添加项目或者编辑报警设备初始化
	@ApiOperation(value = "添加或者编辑报警设备初始化")
	@GetMapping(value="/edit")
	public ApiResponse<AlarmServerVo> edit(HttpServletRequest request, AlarmServerParamVo alarmServerParamVo){
		 ApiResponse<AlarmServerVo> resp = new ApiResponse<AlarmServerVo>();
		 AlarmServerVo alarmServerVo = null;
		 
         if(alarmServerParamVo!=null) {
        	 alarmServerVo = alarmServerService.getAlarmServerById(alarmServerParamVo);
         }
         resp.ok("查询成功！").setData(alarmServerVo);

		 return resp;
	}
	//添加或者编辑提交
	@ControllerLog
	@ApiOperation(value = "保存报警设备信息")
	@PostMapping(value = "/save")
	public ApiResponse<Object> save(HttpServletRequest request, AlarmServerParamVo alarmServerParamVo, String projectIds) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		String validMsg = "";
        if(StrUtil.isBlank(alarmServerParamVo.getName())) {
            validMsg = "报警设备名称不能为空！";
        }
        if(StrUtil.isBlank(alarmServerParamVo.getCode())) {
            validMsg = "报警设备编号不能为空！";
        }
        if(StrUtil.isBlank(alarmServerParamVo.getIp())) {
            validMsg = "报警设备ip不能为空！";
        }
		if(StrUtil.isBlank(alarmServerParamVo.getCameraId())) {
			validMsg = "报警设备关联摄像机不能为空！";
		}
        if(StrUtil.isBlank(alarmServerParamVo.getAddressId())) {
        	validMsg = "报警设备所属地址不能为空！";
        }
		if(StrUtil.isBlank(projectIds)) {
			validMsg = "报警设备所属项目不能为空！";
		}
		if(StrUtil.isNotBlank(validMsg)) {
			resp.error(validMsg);
			return resp;
		}
        String[] projectIdArr = projectIds.split(",");
        List<String> projectIdList = Arrays.asList(projectIdArr);

        String deviceId = "d_".equals(alarmServerParamVo.getCameraId().substring(0, 2))?alarmServerParamVo.getCameraId().substring(2):alarmServerParamVo.getCameraId();
		alarmServerParamVo.setCameraId(deviceId);

//        List<CameraVo> cameraVos = cameraService.getCameraByDeviceId(deviceId);
//        if(cameraVos != null && cameraVos.size()>0) {
//        	alarmServerParamVo.setCameraId(cameraVos.get(0).getId());
//        }

        alarmServerService.saveAlarmServer(userVo, alarmServerParamVo, projectIdList);
        resp.ok("保存成功！");
        return resp;
	}
	//删除报警设备
	@ApiOperation(value = "删除报警设备")
	@DeleteMapping(value = "/del")
	public ApiResponse<Object> del(HttpServletRequest request,@RequestParam(value="ids[]") String[] ids) {
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
        List<String> idList = Arrays.asList(ids);
        alarmServerService.delAlarmServersByIds(idList);
        apiRes.ok("删除成功！");
		return apiRes;
	}
}
