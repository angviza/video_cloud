package com.hdvon.nmp.controller.system;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.IEncoderServerService;
import com.hdvon.nmp.service.IProjectService;
import com.hdvon.nmp.service.ITreeNodeService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.DepartmentProject;
import com.hdvon.nmp.vo.EncoderServerVo;
import com.hdvon.nmp.vo.TreeNode;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/encoderServer",tags="编码器管理模块",description="针对编码器的插入,删除,修改,查看等操作")
@RestController
@RequestMapping("/encoderServer")
@Slf4j
public class EncoderServerController extends BaseController{
	@Reference
	private IEncoderServerService encoderServerService;
	
	@Reference
	private IProjectService projectService;
	
	@Reference
	private ITreeNodeService treeNodeService;
	
	//分页查询编码器列表
	@ApiOperation(value="分页查询编码器列表")
	@ApiImplicitParam(name = "search", value = "编码器名称查询条件", required = false)
	@GetMapping(value = "/encoderServerPage")
	public ApiResponse<PageInfo<EncoderServerVo>> encoderServerPage(PageParam pp,EncoderServerVo encoderServerVo) {
		ApiResponse<PageInfo<EncoderServerVo>> resp = new ApiResponse<PageInfo<EncoderServerVo>>();
		try {
			TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
			if(StrUtil.isNotBlank(encoderServerVo.getAddressId())) {
				List<TreeNode> addressNodes = treeNodeService.getChildNodesByCode(encoderServerVo.getAddressCode(), TreeType.ADDRESS.getVal());
				treeNodeChildren.setAddressNodes(addressNodes);
			}
			if(StrUtil.isNotBlank(encoderServerVo.getProjectId())) {
				List<TreeNode> projectNodes = treeNodeService.getChildNodesByCode(encoderServerVo.getProjectCode(), TreeType.PROJECT.getVal());
				treeNodeChildren.setProjectNodes(projectNodes);
			}
			PageInfo<EncoderServerVo> pageInfo = encoderServerService.getEncoderServerPage(pp, encoderServerVo, treeNodeChildren);
			resp.ok().setData(pageInfo);
		}catch(Exception e){
			log.error(e.getMessage());
			resp.error("查询失败："+e.getMessage());
		}
		return resp;  
	}
	//查询编码器列表
	@ApiOperation(value="查询编码器列表")
	@ApiImplicitParam(name = "search", value = "编码器名称查询条件", required = false)
	@GetMapping(value = "/encoderServerList")
	public ApiResponse<List<EncoderServerVo>> encoderServerList(EncoderServerVo encoderServerVo) {
		ApiResponse<List<EncoderServerVo>> resp = new ApiResponse<List<EncoderServerVo>>();
		try {
			TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
			if(StrUtil.isNotBlank(encoderServerVo.getAddressId())) {
				List<TreeNode> addressNodes = treeNodeService.getChildNodesByCode(encoderServerVo.getAddressCode(), TreeType.ADDRESS.getVal());
				treeNodeChildren.setAddressNodes(addressNodes);
			}
			if(StrUtil.isNotBlank(encoderServerVo.getProjectId())) {
				List<TreeNode> projectNodes = treeNodeService.getChildNodesByCode(encoderServerVo.getProjectCode(), TreeType.PROJECT.getVal());
				treeNodeChildren.setProjectNodes(projectNodes);
			}
			List<EncoderServerVo> list = encoderServerService.getEncoderServerList(encoderServerVo, treeNodeChildren);
			resp.ok().setData(list);
		}catch(Exception e){
			log.error(e.getMessage());
			resp.error("查询失败："+e.getMessage());
		}
		return resp;  
	}

	//添加或者编辑初始化
	@ApiOperation(value = "查询编码器信息")
	@GetMapping(value="/{id}/edit")
	public ApiResponse<EncoderServerVo> edit(HttpServletRequest request,  Model model, @PathVariable String id){
		EncoderServerVo encoderVo = null;
		ApiResponse<EncoderServerVo> resp = new ApiResponse<EncoderServerVo>();
        if(StringUtils.isNotEmpty(id)) {//编辑或者查看页面详情
            encoderVo = encoderServerService.getEncoderServerById(id);
        }
        resp.ok().setData(encoderVo);

		return resp;
	}
	
	//添加或者编辑提交
	@ControllerLog
	@ApiOperation(value = "保存编码器信息")
	@PostMapping(value = "/addOrEdit")
	public ApiResponse<Object> addOrEdit(HttpServletRequest request,EncoderServerVo encoderVo) {
		UserVo userVo = getLoginUser();
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
        String validMsg = "";
        if(StrUtil.isBlank(encoderVo.getName())) {
            validMsg = "编码器名称不能为空！";
        }
        if(StrUtil.isBlank(encoderVo.getIp())) {
            validMsg = "编码器ip不能为空！";
        }
        if(encoderVo.getPort() == null) {
            validMsg = "编码器端口不能为空！";
        }
        if(StrUtil.isBlank(encoderVo.getUsername())) {
            validMsg = "编码器用户名不能为空！";
        }
        if(StrUtil.isBlank(encoderVo.getPassword())) {
            validMsg = "编码器密码不能为空！";
        }
        if(StrUtil.isBlank(encoderVo.getDevicesNo())) {
            validMsg = "编码器编号不能为空！";
        }
        if(StrUtil.isBlank(encoderVo.getAddressId())) {
            validMsg = "编码器所属地址不能为空！";
        }
        if(StrUtil.isBlank(encoderVo.getProjectId())) {
            validMsg = "编码器所属项目不能为空！";
        }
        /*if(StrUtil.isBlank(encoderVo.getRegisterUser())) {
            validMsg = "编码器注册账户不能为空！";
        }
        if(StrUtil.isBlank(encoderVo.getRegisterPassword())) {
            validMsg = "编码器注册用户密码不能为空！";
        }
        if(StrUtil.isBlank(encoderVo.getRegisterIp())) {
            validMsg = "编码器注册ip不能为空！";
        }
        if(encoderVo.getRegisterPort() == null) {
            validMsg = "编码器注册端口不能为空！";
        }*/
        if(StrUtil.isNotBlank(validMsg)) {
            apiRes.error(validMsg);
            return apiRes;
        }
        encoderServerService.editEncoderServer(userVo, encoderVo);
        apiRes.ok("提交成功！");

		return apiRes;
	}
	//删除编码器
	@ApiOperation(value = "删除编码器")
	@DeleteMapping(value = "/delEncoders")
	public ApiResponse<Object> del(HttpServletRequest request, @RequestParam(value="ids[]") String[] ids) {
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
        List<String> idList = Arrays.asList(ids);
        encoderServerService.delEncoderServers(idList);
        apiRes.ok("删除成功！");
		return apiRes;
	}
	//分页查询编码器关联的摄像机列表
	@ApiOperation(value = "查询编码器关联的摄像机列表")
	@ApiImplicitParam(name = "encodeId", value = "编码器id", required = true, dataType = "String")
	@GetMapping(value = "queryCamerasReferEncoder")
	public ApiResponse<PageInfo<CameraVo>> queryCamerasReferEncoder(HttpServletRequest request,PageParam pp, String encodeId){
		ApiResponse<PageInfo<CameraVo>> apiRes = new ApiResponse<PageInfo<CameraVo>>();
        PageInfo<CameraVo> pageUsers = encoderServerService.getCamerasByEncodeId(pp, encodeId);
        apiRes.ok().setData(pageUsers);

		return apiRes; 
	}
	
	//批量删除关联摄像机
	@ApiOperation(value = "删除编码器关联的摄像机")
	@DeleteMapping(value="delCamerasReferEncoder")
	public ApiResponse<Object> delCamerasReferEncoder(String encoderId, @RequestParam(value="ids[]") String[] ids){
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
        List<String> idList = Arrays.asList(ids);
        encoderServerService.delCamerasReferEncoder(encoderId, idList);
        apiRes.ok("删除成功！");

		return apiRes;
	}
}
