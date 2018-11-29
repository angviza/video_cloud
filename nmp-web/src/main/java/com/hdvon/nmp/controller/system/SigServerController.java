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
import com.hdvon.nmp.service.ISigServerService;
import com.hdvon.nmp.service.ITreeNodeService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.TreeType;
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

@Api(value="/sigServer",tags="信令中心服务器管理模块",description="针对信令中心服务器的插入,删除,修改,查看等操作")
@RestController
@RequestMapping("/sigServer")
@Slf4j
public class SigServerController extends BaseController{
	@Reference
	private ISigServerService sigServerService;
	
	@Reference
	private ITreeNodeService treeNodeService;
	
	//分页查询信令中心服务器列表
	@ApiOperation(value="分页查询信令中心服务器列表")
	@ApiImplicitParam(name = "search", value = "信令中心服务器名称查询条件", required = false)
	@GetMapping(value = "/sigServerPage")
	public ApiResponse<PageInfo<SigServerVo>> sigServerPage(PageParam pp, SigServerParamVo sigServerParamVo) {
		 ApiResponse<PageInfo<SigServerVo>> resp = new ApiResponse<PageInfo<SigServerVo>>();

        TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
		if(StrUtil.isNotBlank(sigServerParamVo.getAddressId())) {
			List<TreeNode> addressNodes = treeNodeService.getChildNodesByCode(sigServerParamVo.getAddressCode(), TreeType.ADDRESS.getVal());
			treeNodeChildren.setAddressNodes(addressNodes);
		}
		
        PageInfo<SigServerVo> pageInfo= sigServerService.getSigServerPages(pp, treeNodeChildren, sigServerParamVo);
        resp.ok("查询成功！").setData(pageInfo);
		 return resp;
	}

	//查询信令中心服务器列表
	@ApiOperation(value="查询信令中心服务器列表")
	@ApiImplicitParam(name = "search", value = "信令中心服务器名称查询条件", required = false)
	@GetMapping(value = "/sigServerList")
	public ApiResponse<List<SigServerVo>> sigServerList(PageParam pp, SigServerParamVo sigServerParamVo) {
		 ApiResponse<List<SigServerVo>> resp = new ApiResponse<List<SigServerVo>>();
         
        TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
		if(StrUtil.isNotBlank(sigServerParamVo.getAddressId())) {
			List<TreeNode> addressNodes = treeNodeService.getChildNodesByCode(sigServerParamVo.getAddressCode(), TreeType.ADDRESS.getVal());
			treeNodeChildren.setAddressNodes(addressNodes);
		}
		
        List<SigServerVo> list= sigServerService.getSigServerList(treeNodeChildren, sigServerParamVo);
        resp.ok("查询成功！").setData(list);
		 return resp;
	}
	//添加项目或者编辑信令中心服务器初始化
	@ApiOperation(value = "添加或者编辑信令中心服务器初始化")
	@GetMapping(value="/edit")
	public ApiResponse<SigServerVo> edit(HttpServletRequest request, SigServerParamVo sigServerParamVo){
		 ApiResponse<SigServerVo> resp = new ApiResponse<SigServerVo>();
		 SigServerVo sigServerVo = null;
         if(sigServerParamVo!=null) {
        	 sigServerVo = sigServerService.getSigServerById(sigServerParamVo);
         }
         resp.ok("查询成功！").setData(sigServerVo);

		 return resp;
	}
	//添加或者编辑提交
	@ControllerLog
	@ApiOperation(value = "保存中心信令服务器信息")
	@PostMapping(value = "/save")
	public ApiResponse<Object> save(HttpServletRequest request, SigServerParamVo sigServerParamVo, String projectIds) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		String validMsg = "";
        if(StrUtil.isBlank(sigServerParamVo.getName())) {
            validMsg = "中心信令服务器名称不能为空！";
        }
        if(StrUtil.isBlank(sigServerParamVo.getCode())) {
            validMsg = "中心信令服务器编号不能为空！";
        }
        if(StrUtil.isBlank(sigServerParamVo.getIp())) {
            validMsg = "中心信令服务器ip不能为空！";
        }
        if(sigServerParamVo.getPort() == null) {
            validMsg = "中心信令服务器端口不能为空！";
        }
        if(StrUtil.isBlank(sigServerParamVo.getDomainName())) {
            validMsg = "中心信令服务器域名不能为空！";
        }
        if(StrUtil.isBlank(sigServerParamVo.getUserName())) {
            validMsg = "中心信令服务器操作系统登陆用户名不能为空！";
        }
        if(StrUtil.isBlank(sigServerParamVo.getPassword())) {
            validMsg = "中心信令服务器操作系统登陆密码不能为空！";
        }
        if(StrUtil.isBlank(sigServerParamVo.getAddressId())) {
        	validMsg = "中心信令服务器所属地址不能为空！";
        }
        String[] projectIdArr = projectIds.split(",");
        List<String> projectIdList = Arrays.asList(projectIdArr);
        if(projectIdList == null || projectIdList.size()==0) {
        	validMsg = "中心信令服务器所属项目不能为空！";
        }
        if(StrUtil.isNotBlank(validMsg)) {
            resp.error(validMsg);
            return resp;
        }
        sigServerService.saveSigServer(userVo, sigServerParamVo, projectIdList);
        resp.ok("保存成功！");
        return resp;
	}
	//删除中心信令服务器
	@ApiOperation(value = "删除中心信令服务器")
	@DeleteMapping(value = "/del")
	public ApiResponse<Object> del(HttpServletRequest request,@RequestParam(value="ids[]") String[] ids) {
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
        List<String> idList = Arrays.asList(ids);
        sigServerService.delSigServersByIds(idList);
        apiRes.ok("删除成功！");
		return apiRes;
	}
}
