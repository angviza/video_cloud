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
import com.hdvon.nmp.service.IScreenTemplateService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.ScreenTemplateVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/screenTemplate",tags="自定义分屏模板管理",description="针对自定义分屏的插入,删除,修改,查看等操作")
@RestController
@RequestMapping("/screenTemplate")
@Slf4j
public class ScreenTemplateController extends BaseController{
	@Reference
	private IScreenTemplateService screenTemplateService;
	
	//分页查询自定义分屏模板列表
	@ApiOperation(value="分页查询分屏模板列表")
	@ApiImplicitParam(name = "search", value = "自定义分屏模板名称查询条件", required = false)
	@GetMapping(value = "/page")
	public ApiResponse<PageInfo<ScreenTemplateVo>> page(PageParam pp, String search) {
		 ApiResponse<PageInfo<ScreenTemplateVo>> resp = new ApiResponse<PageInfo<ScreenTemplateVo>>();
         Map<String,String> paramMap = new HashMap<String,String>();
         paramMap.put("templateName", search);
         PageInfo<ScreenTemplateVo> pageInfo = screenTemplateService.getScreenTemplatePages(pp, paramMap);
         resp.ok("查询成功！").setData(pageInfo);
		 return resp;
	}
	//查询自定义分屏模板列表
	@ApiOperation(value="查询分屏模板列表")
	@ApiImplicitParam(name = "search", value = "自定义分屏模板名称查询条件", required = false)
	@GetMapping(value = "/list")
	public ApiResponse<List<ScreenTemplateVo>> list(String search) {
		 ApiResponse<List<ScreenTemplateVo>> resp = new ApiResponse<List<ScreenTemplateVo>>();
         Map<String,String> paramMap = new HashMap<String,String>();
         paramMap.put("templateName", search);
         List<ScreenTemplateVo> list = screenTemplateService.getScreenTemplateList(paramMap);
         resp.ok("查询成功！").setData(list);
		 return resp;
	}
	//添加或者编辑分屏模板初始化
	@ApiOperation(value = "添加或者编辑分屏模板初始化")
	@GetMapping(value="/detail")
	public ApiResponse<ScreenTemplateVo> detail(HttpServletRequest request, String id) {
		 ApiResponse<ScreenTemplateVo> resp = new ApiResponse<ScreenTemplateVo>();
		 ScreenTemplateVo screenTemplateVo = null;
         if(StrUtil.isNotBlank(id)) {
             screenTemplateVo = screenTemplateService.getScreenTemplateById(id);
         }
         resp.ok("查询成功！").setData(screenTemplateVo);
		 return resp;
	}
	//添加或者编辑提交
	@ControllerLog
	@ApiOperation(value = "保存分屏模板信息")
	@PostMapping(value = "/save")
	public ApiResponse<Object> save(HttpServletRequest request, ScreenTemplateVo screenTemplateVo) {
		
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		String validMsg = "";
		screenTemplateVo.convertCellinfos();
        if(StrUtil.isBlank(screenTemplateVo.getName())) {
            validMsg = "模板名称不能为空！";
        }
        if(screenTemplateVo.getRows()==null) {
            validMsg = "模板行数不能为空！";
        }
        if(screenTemplateVo.getCols()==null) {
            validMsg = "模板列数不能为空！";
        }
        if(StrUtil.isNotBlank(validMsg)) {
            resp.error(validMsg);
            return resp;
        }
        screenTemplateService.saveScreenTemplate(userVo, screenTemplateVo);
        resp.ok("保存成功！");

		 return resp;
	}
	//删除分屏模板
	@ApiOperation(value = "删除分屏模板")
	@DeleteMapping(value = "/del")
	public ApiResponse<Object> del(HttpServletRequest request,@RequestParam(value="ids[]") String[] ids) {
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
        List<String> idList = Arrays.asList(ids);
        screenTemplateService.delScreenTemplatesByIds(idList);
        apiRes.ok("删除成功！");

		return apiRes;
	}
	
}
