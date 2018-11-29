package com.hdvon.nmp.controller.system;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.IDictionaryService;
import com.hdvon.nmp.service.IDictionaryTypeService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.FileUtil;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.DictionaryParamVo;
import com.hdvon.nmp.vo.DictionaryTypeImportVo;
import com.hdvon.nmp.vo.DictionaryTypeVo;
import com.hdvon.nmp.vo.DictionaryVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/dictionary",tags="数据字典管理模块",description="针对数据字典的增加，删除，修改，查询，查看详情等操作")
@RestController
@RequestMapping("/dictionary")
@Slf4j
public class DictionaryController extends BaseController{
	
	@Reference
	private IDictionaryService dictionaryService;
	
	@Reference
	private IDictionaryTypeService dictionaryTypeService;

	//分页查询数据字典信息
	@ApiOperation(value="分页查询数据字典数据")
	@ApiImplicitParam(name = "search", value = "数据字典名称查询条件", required = false)
	@GetMapping(value = "/queryDictionaryPage")
	public ApiResponse<PageInfo<DictionaryVo>> queryDictionaryPage(HttpServletRequest request,PageParam pp,DictionaryParamVo paramVo){
		ApiResponse<PageInfo<DictionaryVo>> resp = new ApiResponse<PageInfo<DictionaryVo>>();

		PageInfo<DictionaryVo> pageInfo = dictionaryService.getDictionaryPage(pp, paramVo);
        resp.ok().setData(pageInfo);
		return resp;
	}
	
	//初始化加载添加、编辑或者查看详情页面
	@ApiOperation(value="查询数据字典信息")
	@GetMapping(value = "/{id}/loadDictionaryInfo")
	public ApiResponse<DictionaryVo> loadDictionaryInfo(@PathVariable String id){
		ApiResponse<DictionaryVo> resp = new ApiResponse<DictionaryVo>();
		DictionaryVo dicVo = null;
        if(StringUtils.isNotEmpty(id)) {//编辑或者查看详情页面初始化
            dicVo = dictionaryService.getDictionaryById(id);
        }
        resp.ok().setData(dicVo);
		return resp;
	}
	//添加、编辑或者查看详情页面提交
	@ControllerLog
	@ApiOperation(value="保存数据字典")
	@PostMapping(value = "/addDictionary")
	public ApiResponse<Object> addDictionary(HttpServletRequest request, DictionaryVo dicVo){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
        if(StrUtil.isBlank(dicVo.getChName())) {
            return resp.error("字典中文名不能为空！");
        }
        if(StrUtil.isBlank(dicVo.getValue())) {
            return resp.error("字典值不能为空！");
        }
        dictionaryService.editDictionary(userVo, dicVo);
        resp.ok("保存成功！");
		return resp;
	}
	
	//删除单条数据字典
	/*@ApiOperation(value="删除单条数据字典")
	@RequestMapping(value = "/{id}/delDictionary", method = RequestMethod.DELETE)
	@ResponseBody
	public ApiResponse<Object> delDictionary(HttpServletRequest request,@PathVariable String id){
		dictionaryService.delDictionary(id);
		ApiResponse<Object> resp = new ApiResponse<Object>();
		resp.ok();
		return resp;
	}*/
	//删除多条数据字典
	@ApiOperation(value="删除数据字典")
	@DeleteMapping(value = "/delDictionarys")
	public ApiResponse<Object> delDictionarys(@RequestParam(value="ids[]") String[] ids){
		ApiResponse<Object> resp = new ApiResponse<Object>();
        List<String> idList = Arrays.asList(ids);
        dictionaryService.delDictionaries(idList);
        resp.ok();
		return resp;
	}
	/*@ApiOperation(value="批量删除角色")
    @DeleteMapping(value = "/delDictionarys")
    @ResponseBody
    public ApiResponse delDictionarys(@RequestParam(value="ids[]") String[] ids) {
        try {
            List<String> roleIdList = Arrays.asList(ids);
            dictionaryService.delDictionaries(roleIdList);
        } catch (Exception e) {
            log.error("删除角色失败:{}",e.getMessage());
            return new ApiResponse().error("删除角色失败:"+e.getMessage());
        }
        return new ApiResponse().ok("删除角色成功");
    }*/
	//查询数据字典类型列表
	@ApiOperation(value="查询数据字典类型列表")
	@ApiImplicitParam(name = "search", value = "数据字典名称查询条件", required = false)
	@GetMapping(value = "/queryDictionaryTypeList")
	public ApiResponse<List<DictionaryTypeVo>> queryDictionaryTypeList(HttpServletRequest request,String search){
		ApiResponse<List<DictionaryTypeVo>> resp = new ApiResponse<List<DictionaryTypeVo>>();
        List<DictionaryTypeVo> list = dictionaryTypeService.getDictionaryTypeList(search);
        resp.ok().setData(list);
		return resp;
	}
	
	//根据字段类型查询字典列表
	@ApiOperation(value="根据数字字典类型英文名查询字典列表")
	@ApiImplicitParam(name = "searchType[]", value = "数字字典类型英文名", required = false)
	@GetMapping(value = "/getDictionaryList")
	public ApiResponse<List<DictionaryVo>> getDictionaryList(@RequestParam(value="searchType[]")String[] searchType){
		ApiResponse<List<DictionaryVo>> resp = new ApiResponse<List<DictionaryVo>>();
        List<DictionaryVo> list = dictionaryService.getDictionaryList(searchType);
        resp.ok().setData(list);
		return resp;
	}
	
	//导入数据字典
	@ControllerLog
	@ApiOperation(value="导入数据字典数据")
	@PostMapping(value = "/importDictionarys")
	public ApiResponse<Object> importDictionarys(HttpServletRequest request, HttpServletResponse response, MultipartFile file){
		ApiResponse<Object> resp = new ApiResponse<Object>();
        List<DictionaryTypeImportVo> list = null;
		try {
			list = FileUtil.importDictionaryData(file);
        }catch(Exception e) {
            log.error(e.getMessage());
            resp.error("导入失败："+e.getMessage());
            return resp;
        }
        dictionaryService.batchInsertDictionarys(list);
        resp.ok("导入成功！");
		return resp;
	}
	
}
