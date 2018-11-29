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
import com.alibaba.fastjson.JSONObject;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.service.IBallPlanService;
import com.hdvon.nmp.service.IPresentPositionService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.vo.PresentPositionVo;
import com.hdvon.nmp.vo.PresentSettingVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/presentPosition",tags="预置位管理模块",description="针对预置位的插入,删除,修改,查看等操作")
@RestController
@RequestMapping("/presentPosition")
@Slf4j
public class PresentPositionController extends BaseController{

	@Reference
	private IPresentPositionService presentPositionService;
	
	@Reference
	private IBallPlanService ballPlanService;

	//查询预置位列表
	@ApiOperation(value="查询预置位列表")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "cameraId", value = "摄像机id", required = true),
		@ApiImplicitParam(name = "name", value = "预置位名称", required = false)
	})
	@GetMapping(value = "/list")
	public ApiResponse<List<PresentPositionVo>> list(PresentPositionVo presentPositionVo) {
		 ApiResponse<List<PresentPositionVo>> resp = new ApiResponse<List<PresentPositionVo>>();
         Map<String,Object> paramMap = new HashMap<String,Object>();
         if(StrUtil.isBlank(presentPositionVo.getCameraId())) {
        	 throw new ServiceException("摄像机id不能为空！");
         }
         paramMap.put("cameraId", presentPositionVo.getCameraId());
         paramMap.put("name", presentPositionVo.getName());
         List<PresentPositionVo> list = presentPositionService.getPresentPositionList(paramMap);

         resp.ok("查询成功！").setData(list);
		 return resp;
	}
	
	//添加矩阵或者编辑查看预置位初始化
	@ApiOperation(value = "查询预置位详细信息")
	@GetMapping(value="/detail")
	public ApiResponse<PresentPositionVo> detail(HttpServletRequest request, String id){
		 ApiResponse<PresentPositionVo> resp = new ApiResponse<PresentPositionVo>();
		 PresentPositionVo presentPositionVo = null;
         if(StrUtil.isNotBlank(id)) {
        	 presentPositionVo = presentPositionService.getPresentPositionById(id);
         }
         resp.ok("查询成功！").setData(presentPositionVo);
		 return resp;
	}
	
//	//添加或者编辑提交
//	@ControllerLog
//	@ApiOperation(value = "保存预置位信息")
//	@PostMapping(value = "/save")
//	public ApiResponse<Object> save(HttpServletRequest request, PresentPositionVo presentPositionVo) {
//		ApiResponse<Object> resp = new ApiResponse<Object>();
//		UserVo userVo = getLoginUser();
//		String validMsg = "";
//        if(StrUtil.isBlank(presentPositionVo.getName())) {
//            validMsg = "预置位名称不能为空！";
//        }
//        if(StrUtil.isBlank(presentPositionVo.getCameraId())) {
//            validMsg = "摄像机id不能为空！";
//        }
//       /* if(presentPositionVo.getIsKeepwatch()==null) {
//        	validMsg = "守望位不能为空";
//        }*/
//       /* if(StrUtil.isBlank(presentPositionVo.getDeviceCode())) {
//        	return resp.error("设备编码不能为空！");
//        }*/
//        //TODO 预置位添加参数待确定
//       /* String presentNo=compositeParamsUtils.getCameraPresentNo(presentPositionVo.getDeviceCode());
//        presentPositionVo.setPresentNo(presentNo);*/
//
//        if(StrUtil.isBlank(presentPositionVo.getPresentNo())) {
//        	validMsg = "预置位编号不能为空！";
//        }
//        if(Integer.parseInt(presentPositionVo.getPresentNo()) > 255) {
//        	validMsg = "预置位编号不能大于255";
//        }
//        if(StrUtil.isNotBlank(validMsg)) {
//            resp.error(validMsg);
//            return resp;
//        }
//		
//        presentPositionService.savePresentPosition(userVo, presentPositionVo);
//        resp.ok("保存成功！");
//
//        return resp;
//	}
	
	//添加或者编辑提交
	@ControllerLog
	@ApiOperation(value = "保存预置位")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "cameraId", value = "摄像机id", required = true),
		@ApiImplicitParam(name = "jsonMessage", value = "json格式设备预置位的信息，name=预置位名称，presentNo=预置位编码", required = true)
	})
	@PostMapping(value = "/save")
	public ApiResponse<Object> save(String jsonMessage, String cameraId) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		String validMsg = "";
		if(StrUtil.isBlank(cameraId)) {
            validMsg = "摄像机id不能为空！";
		}
		if(StrUtil.isNotBlank(validMsg)) {
			resp.error(validMsg);
			return resp;
        }
		List<PresentPositionVo>	list=JSONObject.parseArray(jsonMessage, PresentPositionVo.class);
		
		 presentPositionService.savePresentPosition(userVo, list,cameraId);
         resp.ok("保存成功！");
		return resp;
	}
	
	//添加或者编辑提交
	@ControllerLog
	@ApiOperation(value = "设置守望位")
	@PostMapping(value = "/resetKeepwatch")
	public ApiResponse<Object> resetKeepwatch(HttpServletRequest request, String cameraId, String presentId) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		if(StrUtil.isBlank(presentId)) {
			throw new ServiceException("预置位id不能为空！");
		}
		presentPositionService.resetKeepwatch(cameraId, presentId);
		resp.ok("设置守望位成功！");
		return resp;
	}
	//删除预置位
	@ApiOperation(value = "删除预置位")
	@DeleteMapping(value = "/del")
	public ApiResponse<Object> del(HttpServletRequest request,@RequestParam(value="ids[]") String[] ids) {
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
        List<String> idList = Arrays.asList(ids);
        presentPositionService.delPresentPositionsByIds(idList);
        apiRes.ok("删除成功！");

		return apiRes;
	}
	
	//查询预置位列表
	@ApiOperation(value="查询预置位列表")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "cameraId", value = "摄像机id", required = true)
	})
	@GetMapping(value = "/getAlarmPresentsInCamera")
	public ApiResponse<List<PresentPositionVo>> getAlarmPresentsInCamera(String cameraId) {
		 ApiResponse<List<PresentPositionVo>> resp = new ApiResponse<List<PresentPositionVo>>();
         Map<String,Object> paramMap = new HashMap<String,Object>();
         if(StrUtil.isBlank(cameraId)) {
        	 throw new ServiceException("摄像机id不能为空！");
         }
         /*if(StrUtil.isBlank(presentId)) {
        	 throw new ServiceException("预置位id不能为空！");
         }*/
         paramMap.put("cameraId", cameraId);
         //paramMap.put("presentId", presentId);
         List<PresentPositionVo> list = presentPositionService.getPresentPositionList(paramMap);

         resp.ok("查询成功！").setData(list);
		 return resp;
	}

	//查询预置位列表，并查询关联查询预置位在预案中的使用状态
	@ApiOperation(value="查询预置位列表，并查询关联查询预置位在预案中的使用状态")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ballPlanId", value = "球机巡航预案id", required = true),
		@ApiImplicitParam(name = "cameraId", value = "摄像机id", required = true)
	})
	@GetMapping(value = "/getPresentsInBallPlanCamera")
	public ApiResponse<PresentSettingVo> getPresentsInBallPlanCamera(String ballPlanId, String cameraId) {
		 ApiResponse<PresentSettingVo> resp = new ApiResponse<PresentSettingVo>();
         Map<String,Object> paramMap = new HashMap<String,Object>();
         if(StrUtil.isBlank(ballPlanId)) {
        	 throw new ServiceException("球机巡航预案id不能为空！");
         }
         if(StrUtil.isBlank(cameraId)) {
        	 throw new ServiceException("摄像机id不能为空！");
         }
         paramMap.put("ballplanId", ballPlanId);
         paramMap.put("cameraId", cameraId);

         List<PresentPositionVo> list = ballPlanService.getBallPresentsInCamera(paramMap);
         
         //String maxPresentNo = presentPositionService.getMaxPresetNo(cameraId);
         List<Integer> presentNos = presentPositionService.getEnablePresentPositions(cameraId);

         PresentSettingVo presentSetting = new PresentSettingVo();
         presentSetting.setPresentPositions(list);
         presentSetting.setMaxNo(presentNos.size() > 0 ? presentNos.get(0) : 0);//预置位编号为0时,说明没有可以用来设置的预置位编号
         resp.ok("查询成功！").setData(presentSetting);
		 return resp;
	}
}
