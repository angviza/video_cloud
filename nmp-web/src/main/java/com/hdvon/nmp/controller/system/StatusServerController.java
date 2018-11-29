package com.hdvon.nmp.controller.system;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hdvon.nmp.vo.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.common.CameraPermissionVo;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.IAddressService;
import com.hdvon.nmp.service.IStatusServerService;
import com.hdvon.nmp.service.ITreeNodeService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.TreeType;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/statusServer",tags="状态服务器管理模块",description="针对状态服务器的插入,删除,修改,查看等操作")
@RestController
@RequestMapping("/statusServer")
@Slf4j
public class StatusServerController extends BaseController{
	@Reference
	private IStatusServerService statusServerService;
	
	@Reference
	private IAddressService addressService;
	
	@Reference
	private ITreeNodeService treeNodeService;
	
	//分页查询转发服务器列表
	@ApiOperation(value="分页查询状态服务器列表")
	@ApiImplicitParam(name = "search", value = "状态服务器名称查询条件", required = false)
	@GetMapping(value = "/statusServerPage")
	public ApiResponse<PageInfo<StatusServerVo>> storeServerPage(PageParam pp, StatusServerParamVo statusServerParamVo) {
		 ApiResponse<PageInfo<StatusServerVo>> resp = new ApiResponse<PageInfo<StatusServerVo>>();
        
        TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
		if(StrUtil.isNotBlank(statusServerParamVo.getAddressId())) {
			List<TreeNode> addressNodes = treeNodeService.getChildNodesByCode(statusServerParamVo.getAddressCode(), TreeType.ADDRESS.getVal());
			treeNodeChildren.setAddressNodes(addressNodes);
		}
		
        PageInfo<StatusServerVo> pageInfo= statusServerService.getStatusServerPages(pp, treeNodeChildren, statusServerParamVo);
        resp.ok("查询成功！").setData(pageInfo);
		 return resp;
	}

	//查询状态服务器列表
	@ApiOperation(value="查询状态服务器列表")
	@ApiImplicitParam(name = "search", value = "状态服务器名称查询条件", required = false)
	@GetMapping(value = "/statusServerList")
	public ApiResponse<List<StatusServerVo>> statusServerList(PageParam pp, StatusServerParamVo statusServerParamVo) {
		 ApiResponse<List<StatusServerVo>> resp = new ApiResponse<List<StatusServerVo>>();

	    TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
		if(StrUtil.isNotBlank(statusServerParamVo.getAddressId())) {
			List<TreeNode> addressNodes = treeNodeService.getChildNodesByCode(statusServerParamVo.getAddressCode(), TreeType.ADDRESS.getVal());
			treeNodeChildren.setAddressNodes(addressNodes);
		}
			
        List<StatusServerVo> list= statusServerService.getStatusServerList(treeNodeChildren, statusServerParamVo);
        resp.ok("查询成功！").setData(list);
		 return resp;
	}
	//添加项目或者编辑状态服务器初始化
	@ApiOperation(value = "添加或者编辑状态服务器初始化")
	@GetMapping(value="/edit")
	public ApiResponse<StatusServerVo> edit(HttpServletRequest request, String id){
		 ApiResponse<StatusServerVo> resp = new ApiResponse<StatusServerVo>();
		 StatusServerVo statusServerVo = statusServerService.getStatusServerById(id);;
         if(statusServerVo == null) {
             return resp.error("状态服务器不存在");
         }else{
             return resp.ok("查询成功！").setData(statusServerVo);
         }
	}
	//添加或者编辑提交
	@ControllerLog
	@ApiOperation(value = "保存状态服务器信息")
	@PostMapping(value = "/save")
	public ApiResponse<Object> save(HttpServletRequest request, StatusServerParamVo statusServerParamVo, String projectIds) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		String validMsg = "";
        if(StrUtil.isBlank(statusServerParamVo.getName())) {
            validMsg = "状态服务器名称不能为空！";
        }
        if(StrUtil.isBlank(statusServerParamVo.getCode())) {
            validMsg = "状态服务器编号不能为空！";
        }
        if(StrUtil.isBlank(statusServerParamVo.getIp())) {
            validMsg = "状态服务器ip不能为空！";
        }
        if(statusServerParamVo.getPort() == null) {
            validMsg = "状态服务器端口不能为空！";
        }
        if(StrUtil.isBlank(statusServerParamVo.getUserName())) {
            validMsg = "状态服务器操作系统登陆用户名不能为空！";
        }
        if(StrUtil.isBlank(statusServerParamVo.getPassword())) {
            validMsg = "状态服务器操作系统登陆密码不能为空！";
        }
        if(StrUtil.isBlank(statusServerParamVo.getAddressId())) {
        	validMsg = "状态服务器所属地址不能为空！";
        }
        String[] projectIdArr = projectIds.split(",");
        List<String> projectIdList = Arrays.asList(projectIdArr);
        if(projectIdList == null || projectIdList.size()==0) {
        	validMsg = "状态服务器所属项目不能为空！";
        }
        if(StrUtil.isNotBlank(validMsg)) {
            resp.error(validMsg);
            return resp;
        }
        statusServerService.saveStatusServer(userVo, statusServerParamVo, projectIdList);
        resp.ok("保存成功！");
        return resp;
	}
	//删除状态服务器
	@ApiOperation(value = "删除状态服务器")
	@DeleteMapping(value = "/del")
	public ApiResponse<Object> del(HttpServletRequest request,@RequestParam(value="ids[]") String[] ids) {
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
        List<String> idList = Arrays.asList(ids);
        statusServerService.delStatusServersByIds(idList);
        apiRes.ok("删除成功！");
		return apiRes;
	}
	
	@ApiOperation(value="关联摄像机界面初始化查询")
    @GetMapping(value = "/getRelatedCamera")
    public ApiResponse<List<CameraNode>> getRelatedCamera(String statusServerId) {
		UserVo userVo = getLoginUser();
		List<CameraNode> cameraList = statusServerService.getStatusServerCamera(userVo,statusServerId);
        return new ApiResponse().ok().setData(cameraList);
    }
	
	@ControllerLog
	@ApiOperation(value="保存状态服务器下关联的摄像机")
    @PostMapping(value = "/saveStatusserverCameras")
    public ApiResponse<Object> saveStatusserverCameras(String statusserverId, String cameraIds) {
		UserVo userVo = getLoginUser();
		if(cameraIds == null){
            cameraIds = "";
        }
		String[] cameraIdArr = cameraIds.split(",");
		List<String> cameraIdList = Arrays.asList(cameraIdArr);
		statusServerService.relateCamerasToStatusserver(userVo, statusserverId, cameraIdList);
        return new ApiResponse<Object>().ok("保存成功");
    }
	
	@ApiOperation(value="查询状态服务器下关联的摄像机")
    @GetMapping(value = "/getRelatedCamerasByStatusserverId")
    public ApiResponse<PageInfo<StatusserverCameraVo>> getRelatedCamerasByStatusserverId(PageParam pp, StatusserverCameraVo statusserverCameraVo) {
    	ApiResponse<PageInfo<StatusserverCameraVo>> resp = new ApiResponse<>();
		UserVo userVo = getLoginUser();
		PageInfo<StatusserverCameraVo> cameraVos = statusServerService.getUserCamerasByStatusserverId(userVo , pp ,statusserverCameraVo);
        return resp.ok().setData(cameraVos);
    }
	
	//删除状态服务器下的摄像机关联
	@ApiOperation(value = "删除状态服务器下的摄像机关联")
	@DeleteMapping(value = "/delRelatedCameras")
	public ApiResponse<Object> delRelatedCameras(HttpServletRequest request,String statusserverId, @RequestParam(value="ids[]") String[] ids) {
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
        List<String> idList = Arrays.asList(ids);
        UserVo userVo = getLoginUser();
        statusServerService.delRelatedCamerasByIds(userVo,statusserverId, idList);
        apiRes.ok("删除成功！");
		return apiRes;
	}
}