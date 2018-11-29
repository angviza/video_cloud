package com.hdvon.nmp.controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hdvon.client.form.CameraForm;
import com.hdvon.client.service.ICameraService;
import com.hdvon.client.service.ITreeService;
import com.hdvon.client.vo.*;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.IPlanCommonService;
import com.hdvon.nmp.service.IPollingPlanService;
import com.hdvon.nmp.service.IPresentPositionService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.CameraFormParamVo;
import com.hdvon.nmp.vo.CameraNode;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.DeviceVo;
import com.hdvon.nmp.vo.MapCameraVo;
import com.hdvon.nmp.vo.PlanCommonVo;
import com.hdvon.nmp.vo.PollingPlanParamVo;
import com.hdvon.nmp.vo.PollingPlanVo;
import com.hdvon.nmp.vo.PresentPositionVo;
import com.hdvon.nmp.vo.UserVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "/map", tags = "地图相关查询接口", description = "地图相关查询接口")
@RestController
@RequestMapping("/map")
@Slf4j
public class MapController extends BaseController {

    @Reference
    private ICameraService cameraService;

    @Reference
    private ITreeService treeService;

    @Reference
    private IPlanCommonService planCommonService;
    
    @Reference
    private com.hdvon.nmp.service.ICameraService centerCameraService;
    
	@Reference
	private IPollingPlanService pollingPlanService;
    /**
     * 默认分页数
     */
    private final Integer PAGE_SIZE = 1500;

    /**
     * 限制最大查询记录数
     */
    private final Integer MAX_LIMIT = 10000;
    
    @ApiOperation(value = "多条件查询地图上的摄像机")
    @GetMapping(value = "/searchCameraList")
    public ApiResponse<EsPage<EsCameraVo>> searchCameraList(MapQueryParam param) {
        //分页信息
        int currentPage = 1;
        if (param.getCurrentPage() != null && param.getCurrentPage().intValue() > 1) {
            currentPage = param.getCurrentPage();
        }
        int pageSize = PAGE_SIZE;
        if (param.getPageSize() != null) {
            if(param.getPageSize() < pageSize){
                pageSize = param.getPageSize();
            }
        }

        //查询最大限制
        int limitPage = MAX_LIMIT/pageSize;
        if(currentPage > limitPage){
            currentPage = limitPage;
        }

        //查询条件信息
        CameraForm form = new CameraForm();
        form.setUserId(getLoginUser().getId());
        form.setQueryStr(param.getKeyword());
        //设置树型过滤数据
        form.setOrgCode(param.getOrgCode());
        form.setProjectId(param.getProjectId());
        form.setAddressCode(param.getAddressCode());
        form.setGroupCode(param.getGroupCode());
        form.setDeviceId(param.getDeviceId() == null ? null : Long.parseLong(param.getDeviceId() ));
        
        form.setHighFilter(param.getHighFilter() == null ? false : (param.getHighFilter() == 1 ? true : false));//是否高级搜索
        if(param.getHighFilter() != null && param.getHighFilter() == 1) {
        	form.setDeviceName(param.getDeviceName());
            form.setDeviceCode(param.getDeviceCode());
            form.setDeviceType(param.getDeviceType());
            form.setEncoderServerId(param.getEncoderServerId());
            form.setIp(param.getIp());
            form.setStatus(param.getStatus());
            form.setConstructionUnit(param.getConstructionUnit());
            form.setUrbanConstructionUnit(param.getUrbanConstructionUnit());
            form.setDeviceCompany(param.getSbcs());
            form.setSxjssbm(param.getSxjssbm());
            form.setPositionType(param.getSxjwzlx());
            form.setCollectionCategory(param.getCjdlb());
            form.setFunctionalType(param.getSxjgnlx());
        }

        if (param.getChoseType() == null) {
            return new ApiResponse().error("类型参数错误");
        }
        int choseType = param.getChoseType().intValue();

        EsPage<EsCameraVo> page = new EsPage<>();
        if (choseType == 0) {//普通搜索
            page = cameraService.searchCameraList(form, currentPage, pageSize);
        } else if (choseType == 1) {//圈选
            if (param.getCycleCenter() == null || param.getCycleRadius() <= 0) {
                return new ApiResponse().error("圈选参数错误");
            }
            page = cameraService.getDistanceRangeCameraList(form, param.getCycleCenter().getLatitude(), param.getCycleCenter().getLongitude(), param.getCycleRadius(), currentPage, pageSize);
        } else if (choseType == 2) {//框选
            if (param.getRectangleLeft() == null || param.getRectangleRight() == null) {
                return new ApiResponse().error("框选参数错误");
            }
            page = cameraService.getBoundingBoxCameraList(form, param.getRectangleLeft().getLatitude(), param.getRectangleLeft().getLongitude(),
                    param.getRectangleRight().getLatitude(), param.getRectangleRight().getLongitude(), currentPage, pageSize);
        } else if (choseType == 3) {//多边形
            if (param.getPolygon() == null || param.getPolygon().size() == 0) {
                return new ApiResponse().error("多边形参数错误");
            }
            //检查多边形点数
            Map<String,Object> map = new HashMap<>();
            for(int i = 0 ; i < param.getPolygon().size() ; i++){
                PointVo pointVo = param.getPolygon().get(i);
                map.put(pointVo.getLatitude()+"_"+pointVo.getLongitude(),"1");
            }
            if(map.keySet().size() < 3){
                return new ApiResponse().error("点数太少，无法构成多边形，请重新选择");
            }
            page = cameraService.getPolygonCameraList(form, param.getPolygon(), currentPage, pageSize);
        } else{//线选

        }
        //返回最大限制，最大只能查询10000条记录
        if(page.getRecordCount() > MAX_LIMIT){
            page.setRecordCount(MAX_LIMIT);
        }
        return new ApiResponse().ok().setData(page);
    }

    @ApiOperation(value = "获取权限树")
    @GetMapping(value = "/getTree")
    @ApiImplicitParam(name = "type", value = "树型类型(org:行政区划、project:项目、address:地址、group:分组)", required = true)
    public ApiResponse<List<CameraPermissionVo>> getTree(String type) {
        List<CameraPermissionVo> list = null;
        if("org".equals(type)){//行政区划
            list = treeService.getOrganizationTree();
        }else if("project".equals(type)){//项目
            list = treeService.getProjectTree();
        }else if("address".equals(type)){//地址
            list = treeService.getAddressTree();
        }else if("group".equals(type)){//分组
            list = treeService.getCameraGroupTree(getLoginUser().getId());
        }else{
            list = new ArrayList<>();
        }
        return new ApiResponse().ok().setData(list);
    }

    @ApiOperation(value = "根据设备id获取摄像机详情")
    @ApiImplicitParam(name = "id", value = "设备id", required = true)
    @GetMapping(value = "/getCameraInfo")
    public ApiResponse<CameraVo> getCameraInfo(String deviceId) {
        if(deviceId == null || "".equals(deviceId)){
            return new ApiResponse().error("参数错误");
        }
        com.hdvon.client.vo.CameraVo cameraVo = cameraService.findCameraRecord(deviceId);
        if(cameraVo == null){
            return new ApiResponse().error("找不到该摄像机详情");
        }
        return new ApiResponse().ok().setData(cameraVo);
    }

    @ApiOperation(value = "查询预案")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "name", value = "预案名称"),
        @ApiImplicitParam(name = "type", value = "预案类型")
    })
    @GetMapping(value = "/getPlanList")
    public ApiResponse<PageInfo<PlanCommonVo>> getPlanList(String name, String type , PageParam pp) {
        PageInfo<PlanCommonVo> page = planCommonService.getPlanCommonList(pp,name,type);
        return new ApiResponse().ok().setData(page);
    }

    @ApiOperation(value = "更新预案状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "预案名称"),
            @ApiImplicitParam(name = "type", value = "预案类型"),
            @ApiImplicitParam(name = "status", value = "预案状态")
    })
    @PostMapping(value = "/updatePlanStatus")
    public ApiResponse updatePlanStatus(String id, String type , String status) {
        planCommonService.updatePlanCommon(id,type,status);
        return new ApiResponse().ok();
    }

    @ApiOperation(value = "查询预案相关摄像机列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "预案id"),
            @ApiImplicitParam(name = "type", value = "预案类型")
    })
    @GetMapping(value = "/getPlanCameraList")
    public ApiResponse<List<CameraNode>> getPlanCameraList(String id, String type) {
        List<CameraNode> list  = planCommonService.getPlanCameraList(id,type);
        return new ApiResponse().ok().setData(list);
    }
    
    @ApiOperation(value = "查询预案相关摄像机列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "单个或者逗号隔开的多个预案id"),
            @ApiImplicitParam(name = "isEdit", value = "是否是编辑")
    })
    @GetMapping(value = "/getPollingPlanDetail")
    public ApiResponse<MapCameraVo> getPollingPlanDetail(String planId,String ids) {
    	MapCameraVo mapCamera = new MapCameraVo();

		PollingPlanVo pollingPlanVo = pollingPlanService.getPollingPlanById(planId);
		mapCamera.setPollingPlanVo(pollingPlanVo);
		String[] arr = ids.split(",");
    	List<String> idList = Arrays.asList(arr);
        List<com.hdvon.nmp.vo.CameraVo> list  = centerCameraService.getCameraByIds(idList);
        mapCamera.setCameraVos(list);
        return new ApiResponse<MapCameraVo>().ok().setData(mapCamera);
    }
    @ApiOperation(value = "新增轮巡预案")
    @PostMapping(value = "/savePollingplan")
    public ApiResponse savePollingplan(PollingPlanParamVo pollingPlanParamVo, String cameraIds) {
    	 UserVo userVo = getLoginUser();
    	 pollingPlanParamVo.convertTime();
    	 pollingPlanService.savePollingPlan(userVo, pollingPlanParamVo, cameraIds, true);
    	 
        return new ApiResponse().ok();
    }
}
