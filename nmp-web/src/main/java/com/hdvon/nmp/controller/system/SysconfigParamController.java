package com.hdvon.nmp.controller.system;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.common.WebConstant;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.IDictionaryService;
import com.hdvon.nmp.service.ISysconfigParamService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.SysconfigParamVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/sysconfigParam",tags="系统配置管理模块",description="系统配置管理增、删、改、查")
@RestController
@RequestMapping("/sysconfigParam")
@Slf4j
public class SysconfigParamController extends BaseController {
	
	@Reference
    private ISysconfigParamService sysconfigParamService;
	@Reference
	private IDictionaryService dictionaryService;
	
	@ApiOperation(value="分页查询限制列表")
	@ApiImplicitParam(name="name",value="系统配置项名称",required=false)
    @GetMapping(value = "/page")
	public ApiResponse<List<SysconfigParamVo>> getPage(PageParam pageParam,String name) {
    	Map<String,Object> param =new HashMap<String,Object>();
    	param.put("name", name);
    	PageInfo<SysconfigParamVo> page =sysconfigParamService.getSysConfigByPage(pageParam,param);
    	return new ApiResponse().ok().setData(page);
    }
	
	
	@ApiOperation(value="保存系统配置")
    @PostMapping(value = "/save")
	public ApiResponse<SysconfigParamVo> updateState(String id,String value) {
		UserVo userVo = getLoginUser();
		SysconfigParamVo vo=new SysconfigParamVo();
		vo.setId(id);
		vo.setValue(value);
		
		if(StrUtil.isBlank(vo.getValue())) {
			return new ApiResponse().error("系统参数值不能为空！");
		}
		if(StrUtil.isBlank(vo.getId())) {
			return new ApiResponse().error("修改记录不能为空！");
		}
		
		sysconfigParamService.save(vo,userVo);
    	return new ApiResponse().ok("保存成功");
    }
	

	@ApiOperation(value="查看限制详细信息")
	@ApiImplicitParam(name="id",value="限制类型",required=true)
    @GetMapping(value = "/getInfo")
	public ApiResponse<SysconfigParamVo> getInfo(String id) {
		SysconfigParamVo vo =sysconfigParamService.getInfo(id);
    	return new ApiResponse().ok().setData(vo);
    }
	
//	@ApiOperation(value="批量删除系统配置")
//    @DeleteMapping(value = "/delete")
//    @ApiImplicitParam(name = "ids[]", value = "限制id数组", required = true)
//    public ApiResponse delete(@RequestParam(value="ids[]") String[] ids) {
//        List<String> idList = Arrays.asList(ids);
//        sysconfigParamService.deleteByIds(idList);
//        return new ApiResponse().ok("删除成功");
//    }
	
	

}
