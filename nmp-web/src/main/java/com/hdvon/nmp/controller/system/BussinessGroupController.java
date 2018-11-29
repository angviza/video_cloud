package com.hdvon.nmp.controller.system;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hdvon.nmp.vo.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.IAddressService;
import com.hdvon.nmp.service.IBussinessGroupService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/bussinessGroup",tags="业务分组管理模块",description="针对业务分组新增，查询，查看详情删除等操作")
@RestController
@RequestMapping("/bussinessGroup")
@Slf4j
public class BussinessGroupController extends BaseController {
	
	@Reference
	private IBussinessGroupService bussinessGroupService;
	@Reference
    private IAddressService addressService;
	
	@ApiOperation(value="分页查询业务分组")
	@ApiImplicitParams({
		@ApiImplicitParam(value="分组名称",name="name"),
		@ApiImplicitParam(value="分组编码",name="code"),
	})
	@GetMapping(value = "/getBussinessGroup")
    public ApiResponse<PageInfo<BussinessGroupVo>> getBussinessGroup(String name,String code, PageParam pageParam) {
		BussinessGroupVo vo=new BussinessGroupVo();
		if(StrUtil.isNotBlank(name)) {
			vo.setName(name);
		}
		if(StrUtil.isNotBlank(code)) {
			vo.setCode(code);
		}
		PageInfo<BussinessGroupVo> page = bussinessGroupService.getBussinessGroupByPage(vo , pageParam);
		return new ApiResponse().ok().setData(page);
    }
	
	@ApiOperation(value="查询业务分组列表")
	@GetMapping(value = "/getBussinessGroupList")
    public ApiResponse<List<BussinessGroupVo>> getBussinessGroupList() {
		List<BussinessGroupVo> list = bussinessGroupService.getBussinessGroupList();
		return new ApiResponse().ok().setData(list);
    }
	
	@ApiOperation(value="获取单个业务分组")
	@ApiImplicitParam(value="业务分组id",name="id",required=true)
	@GetMapping(value = "/getBussinessGroupById")
    public ApiResponse<CameraBussinessGroupVo> getBussinessGroupById(String id) {
		BussinessGroupVo vo=bussinessGroupService.getBuessByCameraPage(id);
		return new ApiResponse().ok().setData(vo);
    }
	
	@ApiOperation(value="业务分组关联的摄像机列表分页")
	@ApiImplicitParams({
		@ApiImplicitParam(value="业务分组id",name="buessId",required=true),
		@ApiImplicitParam(value="设备名称",name="sbmc",required=false),
		@ApiImplicitParam(value="设备编码",name="sbbm",required=false)
	})
	@GetMapping(value = "/getBuessByCameraPage")
    public ApiResponse<PageInfo<CameraBussinessGroupVo>> getBuessByCameraPage(String buessId,String sbmc,String sbbm, PageParam pageParam) {
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("buessId", buessId);
		param.put("sbmc", sbmc);
		param.put("sbbm", sbbm);
		PageInfo<CameraBussinessGroupVo> page = bussinessGroupService.getBuessByCameraPage(param, pageParam);
		return new ApiResponse().ok().setData(page);
    }
	
	@ApiOperation(value="业务分组关联的摄像机列表,支持分页")
	@ApiImplicitParams({
		@ApiImplicitParam(value="分组id",name="buessId",required=true),
		@ApiImplicitParam(value="设备名称",name="sbmc",required=false),
		@ApiImplicitParam(value="设备编码",name="sbbm",required=false)
	})
	@GetMapping(value = "/getBuessByCameraList")
    public ApiResponse<PageInfo<CameraBussinessGroupVo>> getBuessByCameraList(String buessId,String sbmc,String sbbm,PageParam pageParam) {
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("buessId", buessId);
		param.put("sbmc", sbmc);
		param.put("sbbm", sbbm);
		PageInfo<CameraBussinessGroupVo> list = bussinessGroupService.getBuessByCameraList(pageParam,param);
		return new ApiResponse().ok().setData(list);
    }
	
	
	@ApiOperation(value="保存业务分组")
    @PostMapping(value = "/saveBussinessGroup")
	@ControllerLog
    public ApiResponse<AddressVo> saveBussinessGroup(BussinessGroupVo vo) {
        UserVo userVo = getLoginUser();
        if(StrUtil.isBlank(vo.getCode())){  
            return new ApiResponse().error("分组编号不允许为空！");
        }
        if(StrUtil.isBlank(vo.getName())){
            return new ApiResponse().error("分组名称不允许为空！");
        }
    	bussinessGroupService.saveBussinessGroup(userVo,vo);
        return new ApiResponse().ok("保存业务分组成功");
    }
	
	@ApiOperation(value="保存业务分组关联摄像机")
    @PostMapping(value = "/saveRelateCamera")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "buessId", value = "业务分组id", required = true),
    	@ApiImplicitParam(name = "deviceIds", value = "需要分组摄像机ids", required =false)
    })
	@ControllerLog
    public ApiResponse saveRelateCamera(String buessId, String deviceIds) {
        UserVo userVo = getLoginUser();
        List<String> deviceIdList = null;
        if(StrUtil.isNotEmpty(deviceIds)){
            String[] deviceId = deviceIds.split(",");
            deviceIdList = Arrays.asList(deviceId);
        }
        bussinessGroupService.saveRelateCamera(userVo, buessId, deviceIdList);
        return new ApiResponse().ok();
    }

    @ApiOperation(value="获取业务分组关联摄像机")
    @GetMapping(value = "/getRelateCameras")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "buessId", value = "业务分组id", required = true)
    })
    @ControllerLog
    public ApiResponse<List<CameraNode>> getRelateCameras(String buessId) {
	    UserVo userVo = getLoginUser();
        List<CameraNode> list = bussinessGroupService.getRelateCamera(userVo, buessId);
        return new ApiResponse().ok().setData(list);
    }
	
	
	@ApiOperation(value="批量删除业务分组")
    @DeleteMapping(value = "/deleteBussinessGroup")
    @ApiImplicitParam(name = "buessIds[]", value = "业务分组id数组", required = true)
    public ApiResponse deleteBussinessGroup(@RequestParam(value="buessIds[]") String[] buessIds) {
        List<String> buessIdList = Arrays.asList(buessIds);
        bussinessGroupService.deleteBussinessGroup(buessIdList);
        return new ApiResponse().ok("删除业务分组成功");
    }
	
}
