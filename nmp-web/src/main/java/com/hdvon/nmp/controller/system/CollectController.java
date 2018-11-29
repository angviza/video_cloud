package com.hdvon.nmp.controller.system;

import java.util.Collections;
import java.util.List;

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
import com.hdvon.nmp.service.ICollectMappingService;
import com.hdvon.nmp.service.ICollectService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.CollectMappingVo;
import com.hdvon.nmp.vo.CollectVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/collect",tags="用户收藏模块",description="用户收藏模块")
@RestController
@RequestMapping("/collect")
@Slf4j
public class CollectController extends BaseController{

    @Reference
    private ICollectService collectService;
    
    @Reference
	private ICollectMappingService collectMappingService;
    
    @SuppressWarnings("unchecked")
	@ApiOperation(value="获取所有收藏夹列表")
    @GetMapping(value = "/")
    public ApiResponse<List<CollectVo>> getCollectList() {
    	UserVo user = getLoginUser();
    	
    	List<CollectVo> list = collectService.getList(user);
    	if(list==null || list.isEmpty()) {
    		return new ApiResponse().ok().setData(Collections.emptyList());
    	}
        return new ApiResponse().ok().setData(list);
    }
    

    @ControllerLog
    @ApiOperation(value="保存收藏夹")
    @PostMapping(value = "/saveCollect")
    @ApiImplicitParam(name = "name", value = "收藏夹名称", required = true)
    public ApiResponse<CollectVo> saveCollect(String name) {
        UserVo userVo = getLoginUser();
        if(StrUtil.isBlank(name)){
            return new ApiResponse().error("收藏夹名称不允许为空");
        }
        //判断该记录是否存在
        if(collectService.existsCollect(name, userVo)) {
        	return new ApiResponse().error("保存失败,收藏夹名称已存在");
        }
        CollectVo collectVo = new CollectVo();
        collectVo.setName(name);
        		
        collectService.saveCollect(collectVo,userVo);
        
        return new ApiResponse().ok("保存收藏夹成功");
    }

    @ApiOperation(value="批量删除收藏夹")
    @DeleteMapping(value = "/deleteCollect")
    @ApiImplicitParam(name = "collectIds[]", value = "收藏夹id数组", required = true)
    public ApiResponse deleteCollect(@RequestParam(value="collectIds[]") String[] collectIds) {
    	collectService.batchDeleteCollect(collectIds);
        
        return new ApiResponse().ok("批量删除收藏夹成功");
    }
    
    
    @ApiOperation(value="获取分页收藏摄像机列表信息")
    @ApiImplicitParam(name = "collectId", value = "收藏夹id", required = false)
    @GetMapping(value = "/getCollectMapping")
    public ApiResponse<PageInfo<CollectMappingVo>> getCollectMappingPage(String collectId,PageParam pageParam) {
        UserVo user = getLoginUser();
        PageInfo<CollectMappingVo> page = collectMappingService.getListByPage(user.getAccount(), collectId, pageParam);

        return new ApiResponse().ok().setData(page);
    }

    @ControllerLog
    @ApiOperation(value="收藏摄像机")
    @PostMapping(value = "/saveCollectMapping")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "collectId", value = "收藏夹id", required = true),
    	@ApiImplicitParam(name = "deviceIds", value = "摄像机id数组", required = true)
    })   
    public ApiResponse<CollectVo> saveCollectMapping(@RequestParam(value = "collectId",required=true)String collectId,@RequestParam(value="deviceIds") String deviceIds) {
    	String[] deviceArr = deviceIds.split(",");
    	boolean flag = collectMappingService.saveMapping(collectId, deviceArr);
    	if(!flag) {
    		return new ApiResponse().error("保存失败,收藏夹或者摄像机不存在！");
    	}
        return new ApiResponse().ok("收藏摄像机成功");
    }

    @ApiOperation(value="批量删除收藏摄像机")
    @DeleteMapping(value = "/deleteCollectMapping")
    @ApiImplicitParam(name = "deviceIds[]", value = "摄像机id数组", required = true)
    public ApiResponse deleteCollectMapping(@RequestParam(value="deviceIds[]") String[] deviceIds) {
    	collectMappingService.deleteMapping(deviceIds);
        
        return new ApiResponse().ok("批量删除收藏摄像机成功");
    }
}
