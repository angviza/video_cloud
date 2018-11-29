package com.hdvon.nmp.controller.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hdvon.nmp.vo.*;
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
import com.hdvon.nmp.service.IAddressService;
import com.hdvon.nmp.service.ICameraService;
import com.hdvon.nmp.service.ICameragrouopService;
import com.hdvon.nmp.service.IPlanPresentService;
import com.hdvon.nmp.service.IPlanShareService;
import com.hdvon.nmp.service.IPresentPositionService;
import com.hdvon.nmp.service.IWallPlanService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/wallPlan",tags="上墙预案模块",description="针对上墙预案的插入,删除,修改,查看等操作")
@RestController
@RequestMapping("/wallPlan")
@Slf4j
public class WallPlanController extends BaseController{
	@Reference
	private IWallPlanService wallPlanService;
	
	@Reference
	private ICameraService cameraService;
	
	@Reference
	private IPresentPositionService presentPositionService;
	
	@Reference
	private IPlanPresentService planPresentService;
	
	@Reference
	private IPlanShareService planShareService;
	
	@Reference
	private IAddressService addressService;
	
	@Reference
	private ICameragrouopService cameragrouopService;
	
	//分页查询上墙预案列表
	@ApiOperation(value="分页查询上墙预案列表")
	@ApiImplicitParams({})
	@GetMapping(value = "/page")
	public ApiResponse<PageInfo<WallPlanVo>> page(PageParam pp, WallPlanParamVo wallPlanParamVo) {
		 ApiResponse<PageInfo<WallPlanVo>> resp = new ApiResponse<PageInfo<WallPlanVo>>();
		 UserVo userVo = getLoginUser();
         Map<String,Object> paramMap = new HashMap<String,Object>();
         paramMap.put("id", wallPlanParamVo.getId());
         paramMap.put("name", wallPlanParamVo.getName());
         paramMap.put("status", wallPlanParamVo.getStatus());
         paramMap.put("account", userVo.getAccount());
         paramMap.put("isAdmin", userVo.isAdmin());
         PageInfo<WallPlanVo> pageInfo = wallPlanService.getWallPlanPages(pp, paramMap);

         resp.ok("查询成功！").setData(pageInfo);
		 return resp;
	}
	
	//查询上墙预案列表
	@ApiOperation(value="查询上墙预案列表")
	@ApiImplicitParams({})
	@GetMapping(value = "/list")
	public ApiResponse<List<WallPlanVo>> list(WallPlanParamVo wallPlanParamVo) {
		 ApiResponse<List<WallPlanVo>> resp = new ApiResponse<List<WallPlanVo>>();
		 UserVo userVo = getLoginUser(); 
         List<WallPlanVo> wallplanVos = wallPlanService.getWallPlanList(wallPlanParamVo, userVo);

         resp.ok("查询成功！").setData(wallplanVos);
		 return resp;
	}
	
	//添加上墙预案或者编辑上墙预案初始化
	@ApiOperation(value = "查询上墙预案信息")
	@GetMapping(value="/detail")
	public ApiResponse<WallPlanVo> detail(HttpServletRequest request, String id){
		 ApiResponse<WallPlanVo> resp = new ApiResponse<WallPlanVo>();
		 WallPlanVo wallPlanVo = null;
         if(StrUtil.isNotBlank(id)) {
        	 wallPlanVo = wallPlanService.getWallPlanById(id);
         }
         resp.ok("查询成功！").setData(wallPlanVo);
		 return resp;
	}
	//添加或者编辑提交
	@ControllerLog
	@ApiOperation(value = "保存上墙预案信息")
	@PostMapping(value = "/save")
	public ApiResponse<Object> save(HttpServletRequest request, WallPlanParamVo wallPlanParamVo) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		wallPlanParamVo.convertTime();
		String validMsg = "";
        if(StrUtil.isBlank(wallPlanParamVo.getName())) {
            validMsg = "上墙预案名称不能为空！";
        }
        if(StrUtil.isBlank(wallPlanParamVo.getMatrixId())) {
        	validMsg = "矩阵中心不能为空！";
        }
        if(wallPlanParamVo.getBgnTime() == null) {
        	validMsg = "预案开始时间不能为空！";
        }
        if(wallPlanParamVo.getEndTime() == null) {
        	validMsg = "预案结束时间不能为空！";
        }
        if(StrUtil.isBlank(validMsg)) {
        	if(wallPlanParamVo.getEndTime().compareTo(wallPlanParamVo.getBgnTime())<=0) {
            	validMsg = "预案结束时间必须大于开始时间！";
            }
        }
       /* if(wallPlanVo.getMatrixChannels() ==null || wallPlanVo.getMatrixChannels().size()==0) {
            validMsg = "矩阵通道不能为空！";
        }*/
        /*if(wallPlanParamVo.getShareStatus() == 3) {
        	if(StrUtil.isBlank(wallPlanParamVo.getShareDeptIds())) {
        		validMsg = "共享状态为指定部门共享时，共享部门id不能为空！";
        	}
        }else {
        	if(StrUtil.isNotBlank(wallPlanParamVo.getShareDeptIds())) {
        		validMsg = "共享状态为私有、全局共享、部门共享时，共享部门id必须为空！";
        	}
        }*/
        if(StrUtil.isNotBlank(validMsg)) {
            resp.error(validMsg);
            return resp;
        }
       /* String[] matrixChannelIdArr = matrixChannelIds.split(",");
        
        String[] matrixCameraIds = cameraIds.split("~");//矩阵通道对应的多个摄像机id，用,隔开
        List<Map<String,List<String>>> list = new ArrayList<Map<String,List<String>>>();
        for(int i=0;i<matrixChannelIdArr.length;i++) {
        	String matrixChannelId = matrixChannelIdArr[i];
        	String[] cameraArr = matrixCameraIds[i].split(",");
        	List<String> cameraIdList = Arrays.asList(cameraArr);
        	Map<String,List<String>> map = new HashMap<String,List<String>>();
        	map.put(matrixChannelId, cameraIdList);
        	list.add(map);
        }
        wallPlanVo.setIdList(list);*/
        wallPlanService.saveWallPlan(userVo, wallPlanParamVo);
        resp.ok("保存成功！");

        return resp;
	}
	//删除上墙预案
	@ApiOperation(value = "删除上墙预案")
	@DeleteMapping(value = "/del")
	public ApiResponse<Object> del(HttpServletRequest request,@RequestParam(value="ids[]") String[] ids) {
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
        List<String> idList = Arrays.asList(ids);
        wallPlanService.delWallPlansByIds(idList);
        apiRes.ok("删除成功！");

		return apiRes;
	}
	//查询上墙预案中单个通道关联的摄像机信息
	@ApiOperation(value = "查询上墙预案中单个通道关联的摄像机信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "wallplanId", value = "上墙预案id", required = true),
		@ApiImplicitParam(name = "channelId", value = "矩阵通道id", required = true)
	})
	@GetMapping(value = "/getChannelCameras")
	public ApiResponse<List<CameraVo>> getChannelCameras(HttpServletRequest request, String wallplanId, String channelId) {
		 ApiResponse<List<CameraVo>> resp = new ApiResponse<List<CameraVo>>();
    	 Map<String,Object> map = new HashMap<String,Object>();
    	 map.put("wallplanId", wallplanId);
    	 map.put("channelId", channelId);
    	 List<CameraVo> list = wallPlanService.getWallChannelCameraList(map);

    	 List<PresentPositionVo> presentPositionVos = presentPositionService.getPresentPositionsInPlan(wallplanId);
    	 Multimap<String,PresentPositionVo> presentPositionMutimap=ArrayListMultimap.create();
    	 for(PresentPositionVo presentVo : presentPositionVos) {
    		 presentPositionMutimap.put(presentVo.getCameraId(), presentVo);
    	 }
    	 for(CameraVo cameraVo : list) {
    		 List<PresentPositionVo> presents = (List<PresentPositionVo>) presentPositionMutimap.get(cameraVo.getId());
    		 cameraVo.setPresentPositions(presents);
    	 }
         resp.ok("查询成功！").setData(list);
		 return resp;
	}
	
	//保存矩阵通道关联的摄像机
	@ControllerLog
	@ApiOperation(value = "保存矩阵通道关联的摄像机")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "wallplanId", value = "上墙预案id", required = true),
		@ApiImplicitParam(name = "channelId", value = "矩阵通道id", required = true),
		@ApiImplicitParam(name = "cameraIds", value = "英文逗号隔开的多个摄像机id字符串", required = false),
		@ApiImplicitParam(name = "cameraGroupIds", value = "英文逗号隔开的多个摄像机分组id字符串", required = false)
	})
	@PostMapping(value = "/saveChannelCameras")
	public ApiResponse<Object> saveChannelCameras(HttpServletRequest request, String wallplanId, String channelId, String cameraIds, String cameraGroupIds, String mapCameraIds) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		if(StrUtil.isBlank(wallplanId)) {
			throw new ServiceException("上墙预案id不能为空！");
		}
		if(StrUtil.isBlank(channelId)) {
			throw new ServiceException("上墙预案关联的矩阵通道id不能为空！");
		}
		
		//摄像机id
		List<String> cameraIdList = null;
		if(StrUtil.isBlank(cameraIds)) {
			cameraIdList = new ArrayList<>();
		}else {
			String[] cameraIdArr = cameraIds.split(",");
			cameraIdList = Arrays.asList(cameraIdArr);
		}
		
		//分组id
		List<String> cameraGroupIdList = null;
		if(StrUtil.isBlank(cameraGroupIds)) {
			cameraGroupIdList = new ArrayList<>();
		}else {
			String[] cameraGroupIdArr = cameraGroupIds.split(",");
			cameraGroupIdList = Arrays.asList(cameraGroupIdArr);
		}

		//地图摄像机id
        List<String> mapCameraIdList = StrUtil.isBlank(mapCameraIds) ? new ArrayList<>() : Arrays.asList(mapCameraIds.split(","));
        
        wallPlanService.saveChannelRelateCameras(wallplanId, channelId, cameraIdList, cameraGroupIdList, mapCameraIdList);
        resp.ok("保存成功！");

        return resp;
	}
	
	//查询上墙预案中的共享设置
	@ApiOperation(value = "查询上墙预案中的共享设置")
	@GetMapping(value = "/getWallplanShares")
	public ApiResponse<WallPlanVo> getWallplanShares(HttpServletRequest request, String wallplanId) {
		 ApiResponse<WallPlanVo> resp = new ApiResponse<WallPlanVo>();
		 WallPlanVo wallPlanVo = wallPlanService.getWallplanShares(wallplanId);
         resp.ok("查询成功！").setData(wallPlanVo);
		 return resp;
	}
	
	//保存上墙预案的共享设置
	@ControllerLog
	@ApiOperation(value = "保存上墙预案的共享设置")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "wallplanId", value = "上墙预案id", required = true),
		@ApiImplicitParam(name = "shareStatus", value = "共享设置", required = true),
		@ApiImplicitParam(name = "departmentIds", value = "英文逗号隔开的多个部门id字符串", required = false)
	})
	@PostMapping(value = "/saveWallplanShares")
	public ApiResponse<Object> saveWallplanShares(HttpServletRequest request, String wallplanId, Integer shareStatus, String departmentIds) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		
		if(StrUtil.isBlank(wallplanId)) {
			throw new ServiceException("上墙预案id不能为空！");
		}
		if(shareStatus == null) {
			throw new ServiceException("共享设置不能为空！");
		}
		List<String> departmentIdList = null;
		if(StrUtil.isBlank(departmentIds)) {
			departmentIdList = new ArrayList<>();
		}else {
			String[] departmentIdArr = departmentIds.split(",");
			departmentIdList = Arrays.asList(departmentIdArr);
		}

		planShareService.savePlanShares(wallplanId, shareStatus, departmentIdList, "wallplan");
        resp.ok("保存成功！");

        return resp;
	}	
	
	//保存摄像头预置位信息
	@ControllerLog
	@ApiOperation(value = "保存摄像头预置位信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "planId", value = "上墙预案id", required = true),
		@ApiImplicitParam(name = "presentId", value = "预置位id", required = true)
	})
	@PostMapping(value = "/savePlanPresent")
	public ApiResponse<Object> savePlanPresent(HttpServletRequest request, PlanPresentVo palnPresentVo) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		String validMsg = "";
        if(StrUtil.isBlank(palnPresentVo.getPlanId())) {
            validMsg = "上墙预案id不能为空！";
        }
        if(StrUtil.isBlank(palnPresentVo.getPresentId())) {
        	validMsg = "预置位id不能为空！";
        }
        if(StrUtil.isNotBlank(validMsg)) {
            resp.error(validMsg);
            return resp;
        }
        planPresentService.savePlanPresent("wall",palnPresentVo);
        resp.ok().setMessage("调整预置位成功！");
        return resp;
	}

	//查询上墙预案单个通道关联的摄像机
	@ApiOperation(value = "查询上墙预案单个通道关联的摄像机")
	@GetMapping(value = "/getChannelRelatedCameras")
    public ApiResponse<PlanCameraTreeGroupVo> getChannelRelatedCameras(HttpServletRequest request, String wallplanId, String channelId) {
        ApiResponse<PlanCameraTreeGroupVo> resp = new ApiResponse<PlanCameraTreeGroupVo>();
        UserVo userVo = getLoginUser();

        //查询上墙预案单个通道关联的所有摄像机
        List<CameraNode> relatedCameras = wallPlanService.getWallChannelRelatedCameras(userVo,wallplanId,channelId);

        //查询摄像机自定义分组并标记关联预案的分组
        List<CameragrouopVo> groupVos = cameragrouopService.getRelateWallPlanCameraGroupTree(userVo,wallplanId,channelId);

        //将所有结果封装成对象返回到界面
        PlanCameraTreeGroupVo<CameraNode> cameraTreeGroupVo = new PlanCameraTreeGroupVo<>();
        cameraTreeGroupVo.setCameras(relatedCameras);
        cameraTreeGroupVo.setGroups(groupVos);
        return resp.ok().setData(cameraTreeGroupVo);
    }
	
	//查询上墙预案所有通道及分别关联的摄像机
	@ApiOperation(value = "查询上墙预案所有通道及分别关联的摄像机")
	@GetMapping(value = "/getWallplanRelatedCameras")
	public ApiResponse<WallPlanVo> getChannelRelatedCameras(HttpServletRequest request, String wallplanId) {
		 ApiResponse<WallPlanVo> resp = new ApiResponse<WallPlanVo>();

    	 WallPlanVo wallplanVo = wallPlanService.getWallChannelCameras(wallplanId);
    	 return resp.ok().setData(wallplanVo);
	}
	
	@PostMapping(value = "/shifting")
	public ApiResponse<Object> shifting(HttpServletRequest request, String wallplanId, String matrixchannelId, String curCameraId, String cameraId) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		String validMsg = "";
        if(StrUtil.isBlank(wallplanId)) {
            validMsg = "上墙预案id不能为空！";
        }
        if(StrUtil.isBlank(matrixchannelId)) {
            validMsg = "矩阵通道不能为空！";
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
        wallPlanService.changeCameraSort(wallplanId, matrixchannelId, curCameraId, cameraId);
        resp.ok().setMessage("摄像机移位成功！");
        return resp;
	}
}
