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
import com.hdvon.nmp.service.ITranspondServerService;
import com.hdvon.nmp.service.ITreeNodeService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.vo.TranspondServerParamVo;
import com.hdvon.nmp.vo.TranspondServerVo;
import com.hdvon.nmp.vo.TreeNode;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/transpondServer",tags="转发服务器管理模块",description="针对转发服务器的插入,删除,修改,查看等操作")
@RestController
@RequestMapping("/transpondServer")
@Slf4j
public class TranspondServerController extends BaseController{
	@Reference
	private ITranspondServerService transpondServerService;
	
	@Reference
	private ITreeNodeService treeNodeService;
	
	//分页查询转发服务器列表
	@ApiOperation(value="分页查询信令中心服务器列表")
	@ApiImplicitParam(name = "search", value = "信令中心服务器名称查询条件", required = false)
	@GetMapping(value = "/transpondServerPage")
	public ApiResponse<PageInfo<TranspondServerVo>> transpondServerPage(PageParam pp, TranspondServerParamVo transpondServerParamVo) {
		 ApiResponse<PageInfo<TranspondServerVo>> resp = new ApiResponse<PageInfo<TranspondServerVo>>();
        
        TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
		if(StrUtil.isNotBlank(transpondServerParamVo.getAddressId())) {
			List<TreeNode> addressNodes = treeNodeService.getChildNodesByCode(transpondServerParamVo.getAddressCode(), TreeType.ADDRESS.getVal());
			treeNodeChildren.setAddressNodes(addressNodes);
		}
		
        PageInfo<TranspondServerVo> pageInfo= transpondServerService.getTranspondServerPages(pp, treeNodeChildren, transpondServerParamVo);
        resp.ok("查询成功！").setData(pageInfo);
		 return resp;
	}

	//查询信令中心服务器列表
	@ApiOperation(value="查询转发服务器列表")
	@ApiImplicitParam(name = "search", value = "转发服务器名称查询条件", required = false)
	@GetMapping(value = "/transpondServerList")
	public ApiResponse<List<TranspondServerVo>> transpondServerList(PageParam pp, TranspondServerParamVo transpondServerParamVo) {
		 ApiResponse<List<TranspondServerVo>> resp = new ApiResponse<List<TranspondServerVo>>();

        TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
		if(StrUtil.isNotBlank(transpondServerParamVo.getAddressId())) {
			List<TreeNode> addressNodes = treeNodeService.getChildNodesByCode(transpondServerParamVo.getAddressCode(), TreeType.ADDRESS.getVal());
			treeNodeChildren.setAddressNodes(addressNodes);
		}
		
        List<TranspondServerVo> list= transpondServerService.getTranspondServerList(treeNodeChildren, transpondServerParamVo);
        resp.ok("查询成功！").setData(list);
		 return resp;
	}
	//添加项目或者编辑转发服务器初始化
	@ApiOperation(value = "添加或者编辑转发服务器初始化")
	@GetMapping(value="/edit")
	public ApiResponse<TranspondServerVo> edit(HttpServletRequest request, TranspondServerParamVo transpondServerParamVo){
		 ApiResponse<TranspondServerVo> resp = new ApiResponse<TranspondServerVo>();
		 TranspondServerVo transpondServerVo = null;
         if(transpondServerParamVo!=null) {
        	 transpondServerVo = transpondServerService.getTranspondServerById(transpondServerParamVo);
         }
         resp.ok("查询成功！").setData(transpondServerVo);

		 return resp;
	}
	//添加或者编辑提交
	@ControllerLog
	@ApiOperation(value = "保存转发服务器信息")
	@PostMapping(value = "/save")
	public ApiResponse<Object> save(HttpServletRequest request, TranspondServerParamVo transpondServerParamVo, String projectIds) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		String validMsg = "";
        if(StrUtil.isBlank(transpondServerParamVo.getName())) {
            validMsg = "转发服务器名称不能为空！";
        }
        if(StrUtil.isBlank(transpondServerParamVo.getCode())) {
            validMsg = "转发服务器编号不能为空！";
        }
        if(StrUtil.isBlank(transpondServerParamVo.getIp())) {
            validMsg = "转发服务器ip不能为空！";
        }
        if(transpondServerParamVo.getPort() == null) {
            validMsg = "转发服务器端口不能为空！";
        }
        if(StrUtil.isBlank(transpondServerParamVo.getUserName())) {
            validMsg = "转发服务器操作系统登陆用户名不能为空！";
        }
        if(StrUtil.isBlank(transpondServerParamVo.getPassword())) {
            validMsg = "转发服务器操作系统登陆密码不能为空！";
        }
        if(StrUtil.isBlank(transpondServerParamVo.getAddressId())) {
        	validMsg = "转发服务器所属地址不能为空！";
        }
        String[] projectIdArr = projectIds.split(",");
        List<String> projectIdList = Arrays.asList(projectIdArr);
        if(projectIdList == null || projectIdList.size()==0) {
        	validMsg = "转发服务器所属项目不能为空！";
        }
        if(StrUtil.isNotBlank(validMsg)) {
            resp.error(validMsg);
            return resp;
        }
        transpondServerService.saveTranspondServer(userVo, transpondServerParamVo, projectIdList);
        resp.ok("保存成功！");
        return resp;
	}
	//删除转发服务器
	@ApiOperation(value = "删除转发服务器")
	@DeleteMapping(value = "/del")
	public ApiResponse<Object> del(HttpServletRequest request,@RequestParam(value="ids[]") String[] ids) {
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
        List<String> idList = Arrays.asList(ids);
        transpondServerService.delTranspondServersByIds(idList);
        apiRes.ok("删除成功！");
		return apiRes;
	}
}
