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
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.IGatewayServerService;
import com.hdvon.nmp.service.ITreeNodeService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.vo.GatewayServerParamVo;
import com.hdvon.nmp.vo.GatewayServerVo;
import com.hdvon.nmp.vo.TreeNode;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/gatewayServer",tags="网关服务器管理模块",description="针对网关服务器的插入,删除,修改,查看等操作")
@RestController
@RequestMapping("/gatewayServer")
@Slf4j
public class GatewayServerController extends BaseController{
	@Reference
	private IGatewayServerService gatewayServerService;
	
	@Reference
	private ITreeNodeService treeNodeService;
	
	@ApiOperation(value="分页查询网关服务器列表")
	@ApiImplicitParam(name = "search", value = "网关服务器名称查询条件", required = false)
	@GetMapping(value = "/gatewayServerPage")
	public ApiResponse<PageInfo<GatewayServerVo>> gatewayServerPage(PageParam pp, GatewayServerParamVo gatewayServerParamVo) {
		 ApiResponse<PageInfo<GatewayServerVo>> resp = new ApiResponse<PageInfo<GatewayServerVo>>();
        
        TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
		if(StrUtil.isNotBlank(gatewayServerParamVo.getAddressId())) {
			List<TreeNode> addressNodes = treeNodeService.getChildNodesByCode(gatewayServerParamVo.getAddressCode(), TreeType.ADDRESS.getVal());
			treeNodeChildren.setAddressNodes(addressNodes);
		}
		
        PageInfo<GatewayServerVo> pageInfo= gatewayServerService.getGatewayServerPages(pp, treeNodeChildren, gatewayServerParamVo);
        resp.ok("查询成功！").setData(pageInfo);
		 return resp;
	}

	@ApiOperation(value="查询网关服务器列表")
	@ApiImplicitParam(name = "search", value = "网关服务器名称查询条件", required = false)
	@GetMapping(value = "/gatewayServerList")
	public ApiResponse<List<GatewayServerVo>> gatewayServerList(PageParam pp, GatewayServerParamVo gatewayServerParamVo) {
		 ApiResponse<List<GatewayServerVo>> resp = new ApiResponse<List<GatewayServerVo>>();
        
        TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
		if(StrUtil.isNotBlank(gatewayServerParamVo.getAddressId())) {
			List<TreeNode> addressNodes = treeNodeService.getChildNodesByCode(gatewayServerParamVo.getAddressCode(), TreeType.ADDRESS.getVal());
			treeNodeChildren.setAddressNodes(addressNodes);
		}
		
        List<GatewayServerVo> list= gatewayServerService.getGatewayServerList(treeNodeChildren, gatewayServerParamVo);
        resp.ok("查询成功！").setData(list);
		 return resp;
	}
	@ApiOperation(value = "添加或者编辑网关服务器初始化")
	@GetMapping(value="/edit")
	public ApiResponse<GatewayServerVo> edit(HttpServletRequest request, String id){
		 GatewayServerVo gatewayServerVo = gatewayServerService.getGatewayServerById(id);
		 if(gatewayServerVo == null){
             return new ApiResponse().error("网关服务器不存在");
         }else{
             return new ApiResponse().ok().setData(gatewayServerVo);
         }
	}

	@ApiOperation(value = "保存网关服务器信息")
	@PostMapping(value = "/save")
	public ApiResponse<Object> save(HttpServletRequest request, GatewayServerParamVo gatewayServerParamVo, String projectIds) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		String validMsg = "";
        if(StrUtil.isBlank(gatewayServerParamVo.getName())) {
            validMsg = "网关服务器名称不能为空！";
        }
        if(StrUtil.isBlank(gatewayServerParamVo.getCode())) {
            validMsg = "网关服务器编号不能为空！";
        }
        if(StrUtil.isBlank(gatewayServerParamVo.getIp())) {
            validMsg = "网关服务器ip不能为空！";
        }
        if(gatewayServerParamVo.getPort() == null) {
            validMsg = "网关服务器端口不能为空！";
        }
        if(StrUtil.isBlank(gatewayServerParamVo.getDomainName())) {
            validMsg = "网关服务器域名不能为空！";
        }
        if(StrUtil.isBlank(gatewayServerParamVo.getUserName())) {
            validMsg = "网关服务器操作系统登陆用户名不能为空！";
        }
        if(StrUtil.isBlank(gatewayServerParamVo.getPassword())) {
            validMsg = "网关服务器操作系统登陆密码不能为空！";
        }
        if(StrUtil.isBlank(gatewayServerParamVo.getRegisterUser())) {
        	validMsg = "注册用户名不能为空！";
        }
        if(StrUtil.isBlank(gatewayServerParamVo.getRegisterPass())) {
        	validMsg = "注册用户密码不能为空！";
        }
        if(StrUtil.isBlank(gatewayServerParamVo.getRegisterIp())) {
        	validMsg = "注册服务器IP不能为空！";
        }
        if(gatewayServerParamVo.getRegisterPort() == null) {
        	validMsg = "注册服务器端口不能为空！";
        }
        if(StrUtil.isBlank(gatewayServerParamVo.getRegisterDomain())) {
        	validMsg = "注册服务器域名不能为空！";
        }
        if(StrUtil.isBlank(gatewayServerParamVo.getAddressId())) {
        	validMsg = "网关服务器所属地址不能为空！";
        }
        String[] projectIdArr = projectIds.split(",");
        List<String> projectIdList = Arrays.asList(projectIdArr);
        if(projectIdList == null || projectIdList.size()==0) {
        	validMsg = "网关服务器所属项目不能为空！";
        }
        if(StrUtil.isNotBlank(validMsg)) {
            resp.error(validMsg);
            return resp;
        }
        gatewayServerService.saveGatewayServer(userVo, gatewayServerParamVo, projectIdList);
        resp.ok("保存成功！");
        return resp;
	}

	@ApiOperation(value = "删除网关服务器")
	@DeleteMapping(value = "/del")
	public ApiResponse<Object> del(HttpServletRequest request,@RequestParam(value="ids[]") String[] ids) {
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
        List<String> idList = Arrays.asList(ids);
        gatewayServerService.delGatewayServersByIds(idList);
        apiRes.ok("删除成功！");
		return apiRes;
	}
	
}
