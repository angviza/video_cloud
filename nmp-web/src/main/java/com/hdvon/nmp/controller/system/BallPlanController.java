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
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.service.IBallPlanService;
import com.hdvon.nmp.service.ICameraService;
import com.hdvon.nmp.service.IPlanShareService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.BallPlanParamVo;
import com.hdvon.nmp.vo.BallPlanVo;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.PlanShareVo;
import com.hdvon.nmp.vo.PresentPositionVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value="/ballPlan",tags="球机巡航预案模块",description="针对球机巡航预案的插入,删除,修改,查看等操作")
@RestController
@RequestMapping("/ballPlan")
@Slf4j
public class BallPlanController extends BaseController{
	@Reference
	private IBallPlanService ballPlanService;
	
	@Reference
	private IPlanShareService planShareService;
	
	@Reference
	private ICameraService cameraService;
	
	//分页查询球机预案列表
	@ApiOperation(value="分页查询球机预案列表")
	@ApiImplicitParams({})
	@GetMapping(value = "/page")
	public ApiResponse<PageInfo<BallPlanVo>> page(PageParam pp, BallPlanParamVo ballPlanParamVo) {
		 ApiResponse<PageInfo<BallPlanVo>> resp = new ApiResponse<PageInfo<BallPlanVo>>();
		 UserVo userVo = getLoginUser();
		 Map<String,Object> paramMap = new HashMap<String,Object>();
         paramMap.put("name", ballPlanParamVo.getName());
         paramMap.put("status", ballPlanParamVo.getStatus());
         paramMap.put("account", userVo.getAccount());
         paramMap.put("isAdmin", userVo.isAdmin());
         PageInfo<BallPlanVo> pageInfo = ballPlanService.getBallPlanPages(pp, paramMap);

         resp.ok("查询成功！").setData(pageInfo);
		 return resp;
	}
	//查询球机预案列表
	@ApiOperation(value="查询球机预案列表")
	@ApiImplicitParams({})
	@GetMapping(value = "/list")
	public ApiResponse<List<BallPlanVo>> list(BallPlanParamVo ballPlanParamVo) {
		 ApiResponse<List<BallPlanVo>> resp = new ApiResponse<List<BallPlanVo>>();
		 UserVo userVo = getLoginUser();
         List<BallPlanVo> ballPlanVos = ballPlanService.getBallPlanList(ballPlanParamVo, userVo);

         resp.ok("查询成功！").setData(ballPlanVos);
		 return resp;
	}
	//添加球机巡航预案或者编辑球机巡航预案初始化
	@ApiOperation(value = "查询球机巡航预案信息")
	@GetMapping(value="/detail")
	public ApiResponse<BallPlanVo> detail(HttpServletRequest request, String id){
		 ApiResponse<BallPlanVo> resp = new ApiResponse<BallPlanVo>();
		 BallPlanVo ballPlanVo = null;
         if(StrUtil.isNotBlank(id)) {
        	 ballPlanVo = ballPlanService.getBallPlanById(id);
         }
         resp.ok("查询成功！").setData(ballPlanVo);
		 return resp;
	}
	
	//添加或者编辑提交
	@ControllerLog
	@ApiOperation(value = "保存球机巡航预案信息")
	@PostMapping(value = "/save")
	public ApiResponse<Object> save(HttpServletRequest request, BallPlanParamVo ballPlanParamVo) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		ballPlanParamVo.convertTime();
		String validMsg = "";
        if(StrUtil.isBlank(ballPlanParamVo.getName())) {
            validMsg = "球机预案名称不能为空！";
        }
        if(StrUtil.isBlank(ballPlanParamVo.getCameraId())) {
        	validMsg = "摄像机id不能为空！";
        }
        if(ballPlanParamVo.getBgnTime() == null) {
        	validMsg = "预案开始时间不能为空！";
        }
        if(ballPlanParamVo.getEndTime() == null) {
        	validMsg = "预案结束时间不能为空！";
        }
        if(StrUtil.isBlank(validMsg)) {
        	if(ballPlanParamVo.getEndTime().compareTo(ballPlanParamVo.getBgnTime())<=0) {
            	validMsg = "预案结束时间必须大于开始时间！";
            }
        }
        if(StrUtil.isNotBlank(validMsg)) {
            resp.error(validMsg);
            return resp;
        }
        if("d_".equals(ballPlanParamVo.getCameraId().substring(0, 2))) {
        	ballPlanParamVo.setCameraId(ballPlanParamVo.getCameraId().substring(2));
        }
//       String deviceId = "d_".equals(ballPlanParamVo.getCameraId().substring(0, 2))?ballPlanParamVo.getCameraId().substring(2):ballPlanParamVo.getCameraId();
//        
//        List<CameraVo> cameraVos = cameraService.getCameraByDeviceId(deviceId);
//        if(cameraVos != null && cameraVos.size()>0) {
//        	ballPlanParamVo.setCameraId(cameraVos.get(0).getId());
//        }
        ballPlanService.saveBallPlan(userVo, ballPlanParamVo);
        resp.ok("保存成功！");

        return resp;
	}
	//删除球机巡航预案
	@ApiOperation(value = "删除球机巡航预案")
	@DeleteMapping(value = "/del")
	public ApiResponse<Object> del(HttpServletRequest request,@RequestParam(value="ids[]") String[] ids) {
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
        List<String> idList = Arrays.asList(ids);
        ballPlanService.delBallPlansByIds(idList);
        apiRes.ok("删除成功！");

		return apiRes;
	}
	//保存球机轮询预案的共享设置
	@ApiOperation(value = "保存球机轮询预案的共享设置")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ballplanId", value = "球机轮询预案id", required = true),
		@ApiImplicitParam(name = "departmentIds", value = "英文逗号隔开的多个部门id字符串", required = false)
	})
	@PostMapping(value = "/saveBallplanShares")
	@ControllerLog
	public ApiResponse<Object> saveBallplanShares(HttpServletRequest request, String ballplanId, Integer shareStatus, String departmentIds) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		
		if(StrUtil.isBlank(ballplanId)) {
			throw new ServiceException("球机轮询预案id不能为空！");
		}
		List<String> departmentIdList = null;
		if(StrUtil.isBlank(departmentIds)) {
			departmentIdList = new ArrayList<>();
		}else {
			String[] departmentIdArr = departmentIds.split(",");
			departmentIdList = Arrays.asList(departmentIdArr);
		}

		planShareService.savePlanShares(ballplanId, shareStatus, departmentIdList, "ballplan");
        resp.ok("保存成功！");

        return resp;
	}	
	
	//查询球机巡航预案中的共享设置
	@ApiOperation(value = "查询球机巡航预案中的共享设置")
	@GetMapping(value = "/getBallplanShares")
	public ApiResponse<BallPlanVo> getBallplanShares(HttpServletRequest request, String ballplanId) {
		 ApiResponse<BallPlanVo> resp = new ApiResponse<BallPlanVo>();
		 BallPlanVo ballPlanVo = ballPlanService.getBallplanShares(ballplanId);
         resp.ok("查询成功！").setData(ballPlanVo);
		 return resp;
	}
	
	//查询球机巡航预案中的预置位列表
	@ApiOperation(value = "查询球机巡航预案中的预置位列表")
	@GetMapping(value = "/getBallplanPresents")
	public ApiResponse<List<PresentPositionVo>> getBallplanPresents(HttpServletRequest request, String ballplanId, String cameraId) {
		 ApiResponse<List<PresentPositionVo>> resp = new ApiResponse<List<PresentPositionVo>>();
		 Map<String,Object> map = new HashMap<String,Object>();
		 map.put("ballplanId", ballplanId);
		 map.put("cameraId", cameraId);
    	 List<PresentPositionVo> list = ballPlanService.getPresentPositionsInBallPlan(map);
         resp.ok("查询成功！").setData(list);
		 return resp;
	}
	
	//查询巡航预案中的预置位立标并标记球机巡航预案中的预置位列表
	@ApiOperation(value = "查询巡航预案中的预置位立标并标记球机巡航预案中的预置位列表")
	@GetMapping(value = "/getBallPresentsInCamera")
	public ApiResponse<List<PresentPositionVo>> getBallPresentsInCamera(HttpServletRequest request, String ballplanId, String cameraId) {
		 ApiResponse<List<PresentPositionVo>> resp = new ApiResponse<List<PresentPositionVo>>();
		 Map<String,Object> map = new HashMap<String,Object>();
		 map.put("ballplanId", ballplanId);
		 map.put("cameraId", cameraId);
    	 List<PresentPositionVo> list = ballPlanService.getBallPresentsInCamera(map);
         resp.ok("查询成功！").setData(list);
		 return resp;
	}
	
	//保存球机巡航预案预置位
	@ApiOperation(value = "保存球机巡航预案预置位")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ballplanId", value = "球机巡航预案id", required = true),
		@ApiImplicitParam(name = "ballpresentIds", value = "英文逗号隔开的关联预案的多个预置位id字符串", required = true),
		@ApiImplicitParam(name = "presentIds", value = "英文逗号隔开的多个预置位id字符串", required = true),
		@ApiImplicitParam(name = "timeIntervals", value = "英文逗号隔开的多个时间间隔字符串", required = true)
		
	})
	@PostMapping(value = "/saveBallplanPresents")
	@ControllerLog
	public ApiResponse<Object> saveBallplanPresents(HttpServletRequest request, String ballplanId, String ballpresentIds, String timeIntervals, String presentIds) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		
		if(StrUtil.isBlank(ballplanId)) {
			throw new ServiceException("球机巡航预案id不能为空！");
		}
		List<String> presentIdList = null;
		if(StrUtil.isBlank(presentIds)) {
			//throw new ServiceException("请选择要设置的预置位！");
			presentIdList = new ArrayList<>();
		}else {
			String[] presentIdArr = presentIds.split(",");
			presentIdList = Arrays.asList(presentIdArr);
		}
		
		List<String> timeIntervalList = null;
		if(StrUtil.isBlank(timeIntervals)) {
			timeIntervalList = new ArrayList<>();
		}else {
			String[] timeIntervalArr = timeIntervals.split(",");
			timeIntervalList = Arrays.asList(timeIntervalArr);
		}
		
		List<String> ballPresentIdList = null;
		if(StrUtil.isBlank(ballpresentIds)) {
			ballPresentIdList = new ArrayList<>();
		}else {
			String[] ballPresentIdArr = ballpresentIds.split(",");
			ballPresentIdList = Arrays.asList(ballPresentIdArr);
		}
		ballPlanService.savePresentPositionToBallPlan(ballplanId, ballPresentIdList, timeIntervalList, presentIdList);
        resp.ok("保存成功！");

        return resp;
	}	
	 
	//预置位顺序移位
	@ApiOperation(value = "预置位顺序移位")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ballplanId", value = "球机轮询预案id", required = true),
		@ApiImplicitParam(name = "presentIds", value = "英文逗号隔开的多个预置位id字符串", required = false)
	})
	@PostMapping(value = "/shifting")
	@ControllerLog
	public ApiResponse<Object> shifting(HttpServletRequest request, String ballplanId, String presentIds) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		
		if(StrUtil.isBlank(ballplanId)) {
			throw new ServiceException("球机轮询预案id不能为空！");
		}
		List<String> presentIdList = null;
		if(StrUtil.isBlank(presentIds)) {
			presentIdList = new ArrayList<>();
		}else {
			String[] presentIdArr = presentIds.split(",");
			presentIdList = Arrays.asList(presentIdArr);
		}

		ballPlanService.shiftingUpDown(ballplanId, presentIdList);
        resp.ok("移位成功！");

        return resp;
	}	
}
