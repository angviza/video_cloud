package com.hdvon.nmp.controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.ISysmenuService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.vo.SysmenuVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
@Api(value="/sysmenu",tags="系统菜单管理模块",description="针对系统菜单的查询，查看详情等操作")
@RestController
@RequestMapping("/sysmenu")
@Slf4j
public class SysmenuController extends BaseController{
	@Reference
	private ISysmenuService sysmenuService;
	
	//查询系统菜单信息
	@ApiOperation(value="查询系统菜单信息")
	@ApiImplicitParam(name = "search", value = "系统菜单名称查询条件", required = false)
	@GetMapping(value = "/querySysmenus")
	public ApiResponse<List<SysmenuVo>> querySysmenus(@RequestParam(required = false)String search) {
		ApiResponse<List<SysmenuVo>> resp = new ApiResponse<List<SysmenuVo>>();
        List<SysmenuVo> pageInfo = sysmenuService.getSysMenus(search);
        resp.ok().setData(pageInfo);

		return resp;
	}
	@ApiOperation(value="查询系统菜单信息")
	@GetMapping(value = "/viewSysmenuDetail")
	public ApiResponse<SysmenuVo> viewSysmenuDetail(@RequestParam(required = true)String id){
		ApiResponse<SysmenuVo> resp = new ApiResponse<SysmenuVo>();
        SysmenuVo sysmenu = sysmenuService.getSysMenuDetail(id);
        resp.ok().setData(sysmenu);
		return resp;
	}
	
	
	//查询系统菜单信息
	@ApiOperation(value="系统菜单列表(用户行为日志查询关联)")
	@GetMapping(value = "/getSysmenusList")
	public ApiResponse<List<SysmenuVo>> getSysmenusList() {
		ApiResponse<List<SysmenuVo>> resp = new ApiResponse<List<SysmenuVo>>();
		List<SysmenuVo> list = sysmenuService.getSysmenusList();
		return resp.ok().setData(list);
	}
	
	//修改系统菜单
	@ControllerLog
	@ApiOperation(value = "修改系统菜单")
	@PostMapping(value = "/save")
	public ApiResponse<Object> save(HttpServletRequest request, SysmenuVo sysmenuVo) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		String validMsg = "";
		if(StrUtil.isBlank(sysmenuVo.getId())) {
			validMsg = "id不能为空！";
		}
		if(StrUtil.isBlank(sysmenuVo.getName())) {
			validMsg = "菜单名称不能为空！";
		}
//		if(StrUtil.isBlank(sysmenuVo.getCode())) {
//			validMsg = "菜单编码不能为空！";
//		}
		/*if(StrUtil.isBlank(sysmenuVo.getMethod())) {
			validMsg = "菜单请求方式不能为空！";
		}*/
		/*if(StrUtil.isBlank(sysmenuVo.getUrl())) {
			validMsg = "菜单url不能为空！";
		}*/
		if(StrUtil.isNotBlank(validMsg)) {
			resp.error(validMsg);
			return resp;
		}
		sysmenuService.saveSysmenu(userVo, sysmenuVo);
		return resp.ok("修改成功");
	}
}
