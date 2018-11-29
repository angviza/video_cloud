package com.hdvon.nmp.controller.system;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.IDictionaryTypeService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.DictionaryTypeVo;
import com.hdvon.nmp.vo.DictionaryVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/dictionaryType",tags="数据字典类型管理模块",description="针对数据字典类型的增加，删除，修改，查询，查看详情等操作")
@RestController
@RequestMapping("/dictionaryType")
@Slf4j
public class DictionaryTypeController extends BaseController{
	
	@Reference
	private IDictionaryTypeService dictionaryTypeService;
	
	//分页查询数据字典类型信息
	@ApiOperation(value="分页查询数据字典类型数据")
	@ApiImplicitParam(name = "search", value = "数据字典类型名称查询条件", required = false)
	@GetMapping(value = "/queryDictionaryTypePage")
	public ApiResponse<PageInfo<DictionaryTypeVo>> queryDictionaryTypePage(HttpServletRequest request,PageParam pp,String search){
		ApiResponse<PageInfo<DictionaryTypeVo>> resp = new ApiResponse<PageInfo<DictionaryTypeVo>>();
		PageInfo<DictionaryTypeVo> pageInfo = dictionaryTypeService.getDictionaryTypePage(pp, search);
        resp.ok().setData(pageInfo);
		return resp;
	}
	
	//初始化加载添加、编辑或者查看详情页面
	@ApiOperation(value="查询数据字典信息")
	@GetMapping(value = "/{id}/loadDictionaryTypeInfo")
	public ApiResponse<DictionaryTypeVo> loadDictionaryTypeInfo(@PathVariable String id){
		ApiResponse<DictionaryTypeVo> resp = new ApiResponse<DictionaryTypeVo>();
		DictionaryTypeVo dicVo = null;
        if(StringUtils.isNotEmpty(id)) {//编辑或者查看详情页面初始化
            dicVo = dictionaryTypeService.getDictionaryTypeById(id);
        }
        resp.ok().setData(dicVo);
		return resp;
	}
	//添加、编辑页面提交
	@ControllerLog
	@ApiOperation(value="保存字典类型")
	@PostMapping(value = "/addDictionaryType")
	public ApiResponse<Object> addDictionaryType(HttpServletRequest request, DictionaryTypeVo dicTypeVo){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();

        if(StrUtil.isBlank(dicTypeVo.getChName())) {
            resp.error("字典类型中文名不能为空！");
            return resp;
        }
        if(StrUtil.isBlank(dicTypeVo.getEnName())) {
            resp.error("字典类型英文名不能为空！");
            return resp;
        }
        dictionaryTypeService.editDictionaryType(userVo,dicTypeVo);
        resp.ok("保存成功！");

		return resp;
	}
	
	//删除单条数据字典类型
	/*@ApiOperation(value="删除单条数据字典类型", notes = "删除单条数据字典类型")
	@RequestMapping(value = "/{id}/delDictionaryType", method = RequestMethod.DELETE)
	@ResponseBody
	public ApiResponse<Object> delDictionaryType(HttpServletRequest request,@PathVariable String id){
		dictionaryTypeService.delDictionaryType(id);
		ApiResponse<Object> resp = new ApiResponse<Object>();
		resp.ok();
		return resp;
	}*/
	//删除多条数据字典类型
	@ApiOperation(value="删除数据字典类型")
	@DeleteMapping(value = "/delDictionaryTypes")
	public ApiResponse<Object> delDictionaryTypes(@RequestParam(value="ids[]") String[] ids){
		ApiResponse<Object> resp = new ApiResponse<Object>();
        List<String> idList = Arrays.asList(ids);
        dictionaryTypeService.delDictionaryTypes(idList);
        resp.ok();
		return resp;
	}

}
