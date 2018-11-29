package com.hdvon.nmp.controller.system;

import java.util.ArrayList;
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
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.common.CameraPermissionVo;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.service.ICameraService;
import com.hdvon.nmp.service.ICameragrouopService;
import com.hdvon.nmp.service.IPlanPresentService;
import com.hdvon.nmp.service.IPlanShareService;
import com.hdvon.nmp.service.IPollingPlanService;
import com.hdvon.nmp.service.IPresentPositionService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.PollingPlanLinksVo;
import com.hdvon.nmp.vo.OrganizationTreeVo;
import com.hdvon.nmp.vo.PlanPresentVo;
import com.hdvon.nmp.vo.PollingPlanParamVo;
import com.hdvon.nmp.vo.PollingPlanVo;
import com.hdvon.nmp.vo.PollingplanCameraVo;
import com.hdvon.nmp.vo.PresentPositionVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/pollingPlan",tags="轮询预案模块",description="针对轮询预案的插入,删除,修改,查看等操作")
@RestController
@RequestMapping("/pollingPlan")
@Slf4j
public class PollingPlanController extends BaseController{
	
	@Reference
	private IPollingPlanService pollingPlanService;
	
	@Reference
	private ICameraService cameraService;
	
	@Reference
	private IPresentPositionService presentPositionService;
	
	@Reference
	private IPlanPresentService planPresentService;
	
	@Reference
	private IPlanShareService planShareService;
	
	@Reference
	private ICameragrouopService cameragrouopService;
	
	@ApiOperation(value="分页查询轮询预案列表")
	@ApiImplicitParams({})
	@GetMapping(value = "/page")
	public ApiResponse<PageInfo<PollingPlanVo>> page(PageParam pp,PollingPlanParamVo pollingPlanParamVo) {
		 ApiResponse<PageInfo<PollingPlanVo>> resp = new ApiResponse<PageInfo<PollingPlanVo>>();
		 UserVo userVo = getLoginUser();
         PageInfo<PollingPlanVo> pageInfo = pollingPlanService.getPollingPlanPages(pp, pollingPlanParamVo, userVo);

         resp.ok("查询成功！").setData(pageInfo);
		 return resp;
	}
	
	@ApiOperation(value="查询轮询预案列表")
	@ApiImplicitParams({})
	@GetMapping(value = "/list")
	public ApiResponse<List<PollingPlanVo>> list(PollingPlanParamVo pollingPlanParamVo) {
		 ApiResponse<List<PollingPlanVo>> resp = new ApiResponse<>();
		 UserVo userVo = getLoginUser();
         List<PollingPlanVo> pollingPlanVos = pollingPlanService.getPollingPlanList(pollingPlanParamVo , userVo);
         resp.ok("查询成功！").setData(pollingPlanVos);
		 return resp;
	}
	@ApiOperation(value = "查询轮询预案信息")
	@GetMapping(value="/detail")
	public ApiResponse<PollingPlanVo> detail(HttpServletRequest request, String id){
		 ApiResponse<PollingPlanVo> resp = new ApiResponse<PollingPlanVo>();
		 PollingPlanVo pollingPlanVo = null;
         if(StrUtil.isNotBlank(id)) {
        	 pollingPlanVo = pollingPlanService.getPollingPlanById(id);
         }
         resp.ok("查询成功！").setData(pollingPlanVo);
		 return resp;
	}
	@ControllerLog
	@ApiOperation(value = "保存轮询预案信息")
	@PostMapping(value = "/save")
	public ApiResponse<Object> save(HttpServletRequest request, PollingPlanParamVo pollingPlanParamVo) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		pollingPlanParamVo.convertTime();
		UserVo userVo = getLoginUser();
		String validMsg = "";
        if(StrUtil.isBlank(pollingPlanParamVo.getName())) {
            validMsg = "轮询预案名称不能为空！";
        }
        if(pollingPlanParamVo.getBgnTime() == null) {
        	validMsg = "预案开始时间不能为空！";
        }
        if(pollingPlanParamVo.getEndTime() == null) {
        	validMsg = "预案结束时间不能为空！";
        }
        if(StrUtil.isBlank(validMsg)) {
        	if(pollingPlanParamVo.getEndTime().compareTo(pollingPlanParamVo.getBgnTime())<=0) {
            	validMsg = "预案结束时间必须大于开始时间！";
            }
        }
        if(StrUtil.isNotBlank(validMsg)) {
            resp.error(validMsg);
            return resp;
        }
        
        pollingPlanService.savePollingPlan(userVo, pollingPlanParamVo, null, false);
        resp.ok("保存成功！");

        return resp;
	}
	@ApiOperation(value = "删除轮询预案")
	@DeleteMapping(value = "/del")
	public ApiResponse<Object> del(HttpServletRequest request,@RequestParam(value="ids[]") String[] ids) {
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
        List<String> idList = Arrays.asList(ids);
        pollingPlanService.delPollingPlansByIds(idList);
        apiRes.ok("删除成功！");

		return apiRes;
	}
	@ApiOperation(value = "保存轮询预案关联的摄像机")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "pollingplanId", value = "轮询预案id", required = true),
		@ApiImplicitParam(name = "cameraIds", value = "英文逗号隔开的多个摄像机id字符串", required = false),
		@ApiImplicitParam(name = "cameraGroupIds", value = "英文逗号隔开的多个摄像机分组id字符串", required = false),
        @ApiImplicitParam(name = "mapCameraIds", value = "英文逗号隔开的多个地图选择的摄像机id字符串", required = false)
	})
	@PostMapping(value = "/savePollingCameras")
	public ApiResponse savePollingCameras(String pollingplanId, String cameraIds, String cameraGroupIds , String mapCameraIds) {
		ApiResponse resp = new ApiResponse();
		UserVo userVo = getLoginUser();
		
		if(StrUtil.isBlank(pollingplanId)) {
			throw new ServiceException("轮询预案id不能为空！");
		}
        //摄像机id
        List<String> cameraIdList = StrUtil.isBlank(cameraIds) ? new ArrayList<>() : Arrays.asList(cameraIds.split(","));

        //分组id
        List<String> cameraGroupIdList = StrUtil.isBlank(cameraGroupIds) ? new ArrayList<>() : Arrays.asList(cameraGroupIds.split(","));

        //地图摄像机id
        List<String> mapCameraIdList = StrUtil.isBlank(mapCameraIds) ? new ArrayList<>() : Arrays.asList(mapCameraIds.split(","));

        pollingPlanService.savePollingRelateCameras(pollingplanId, cameraIdList, cameraGroupIdList , mapCameraIdList);
        resp.ok("保存成功！");
        return resp;
	}

//	@ControllerLog
	@ApiOperation(value = "查询轮询预案中单个分组关联的摄像机信息")
	@GetMapping(value = "/getPollingGroupCameras")
	public ApiResponse<List<CameraVo>> getPollingGroupCameras(HttpServletRequest request, String pollingplanId) {
		 ApiResponse<List<CameraVo>> resp = new ApiResponse<>();
    	 List<CameraVo> list = pollingPlanService.getPollingGroupCameras(pollingplanId);

    	 List<PresentPositionVo> presentPositionVos = presentPositionService.getPresentPositionsInPlan(pollingplanId);
    	 Multimap<String,PresentPositionVo> presentPositionMutimap=ArrayListMultimap.create();
    	 for(PresentPositionVo presentVo : presentPositionVos) {
    		 presentPositionMutimap.put(presentVo.getCameraId(), presentVo);
    	 }
    	 for(CameraVo cameraVo : list) {
    		 cameraVo.setPresentPositions((List<PresentPositionVo>) presentPositionMutimap.get(cameraVo.getId()));
    	 }
         resp.ok("查询成功！").setData(list);
		 return resp;
	}
	
	@ControllerLog
	@ApiOperation(value = "查询轮询预案中的共享设置")
	@GetMapping(value = "/getPollingplanShares")
	public ApiResponse<PollingPlanVo> getPollingplanShares(HttpServletRequest request, String pollingplanId) {
		 ApiResponse<PollingPlanVo> resp = new ApiResponse<PollingPlanVo>();
		 PollingPlanVo pollingPlanVo = pollingPlanService.getPollingplanShares(pollingplanId);
         resp.ok("查询成功！").setData(pollingPlanVo);
		 return resp;
	}
	
	@ControllerLog
	@ApiOperation(value = "保存轮询预案的共享设置")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "pollingplanId", value = "轮询预案id", required = true),
		@ApiImplicitParam(name = "departmentIds", value = "英文逗号隔开的多个部门id字符串", required = false)
	})
	@PostMapping(value = "/savePollingplanShares")
	public ApiResponse<Object> savePollingplanShares(HttpServletRequest request, String pollingplanId, Integer shareStatus, String departmentIds) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		
		if(StrUtil.isBlank(pollingplanId)) {
			throw new ServiceException("轮询预案id不能为空！");
		}
		List<String> departmentIdList = null;
		if(StrUtil.isBlank(departmentIds)) {
			departmentIdList = new ArrayList<>();
		}else {
			String[] departmentIdArr = departmentIds.split(",");
			departmentIdList = Arrays.asList(departmentIdArr);
		}

		planShareService.savePlanShares(pollingplanId, shareStatus, departmentIdList, "pollingplan");
        resp.ok("保存成功！");

        return resp;
	}	
	@ControllerLog
	@ApiOperation(value = "保存摄像头预置位信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "planId", value = "轮询预案id", required = true),
		@ApiImplicitParam(name = "cameraId", value = "摄像机id", required = true),
		@ApiImplicitParam(name = "presentId", value = "预置位id", required = true)
	})
	@PostMapping(value = "/savePlanPresent")
	public ApiResponse<Object> savePlanPresent(HttpServletRequest request, PlanPresentVo palnPresentVo) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		String validMsg = "";
        if(StrUtil.isBlank(palnPresentVo.getPlanId())) {
            validMsg = "轮询预案id不能为空！";
        }
        if(StrUtil.isBlank(palnPresentVo.getPresentId())) {
        	validMsg = "预置位id不能为空！";
        }
        if(StrUtil.isNotBlank(validMsg)) {
            resp.error(validMsg);
            return resp;
        }
        planPresentService.savePlanPresent("polling", palnPresentVo);
        resp.ok().setMessage("调整预置位成功！");
        return resp;
	}

    @ApiOperation(value = "查询轮巡预案关联数据，包括地址树己选的摄像机、分组列表、地图己选的摄像机")
    @GetMapping(value = "/getRelatedCameras")
    public ApiResponse<PollingPlanLinksVo<PollingplanCameraVo>> getRelatedCameras(String pollingplanId) {
        UserVo userVo = getLoginUser();
        PollingPlanLinksVo pollingPlanLinksVo = pollingPlanService.getPollingPlanLinks(pollingplanId, userVo);
        return new ApiResponse().ok().setData(pollingPlanLinksVo);
    }
    
	@ApiOperation(value = "保存摄像头预置位信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "pollingplanId", value = "轮巡预案id", required = true),
		@ApiImplicitParam(name = "curCameraId", value = "选中移动的摄像机id", required = true),
		@ApiImplicitParam(name = "cameraId", value = "移动之后位置变动的摄像机id", required = true)
	})
	@PostMapping(value = "/shifting")
	public ApiResponse<Object> shifting(HttpServletRequest request, String pollingplanId, String curCameraId, String cameraId) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		String validMsg = "";
        if(StrUtil.isBlank(pollingplanId)) {
            validMsg = "轮询预案id不能为空！";
        }
        if(StrUtil.isBlank(curCameraId)) {
        	validMsg = "选中移动的摄像机id不能为空！";
        }
        if(StrUtil.isBlank(cameraId)) {
        	validMsg = "移动之后位置变动的摄像机id不能为空！";
        }
        if(StrUtil.isNotBlank(validMsg)) {
            resp.error(validMsg);
            return resp;
        }
        pollingPlanService.changeCameraSort(pollingplanId, curCameraId, cameraId);
        resp.ok().setMessage("摄像机移位成功！");
        return resp;
	}
}
