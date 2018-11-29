package com.hdvon.nmp.controller.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdvon.nmp.client.WallCameraRpcClient;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.IBallPlanService;
import com.hdvon.nmp.service.ICameraService;
import com.hdvon.nmp.service.IDictionaryService;
import com.hdvon.nmp.service.IDictionaryTypeService;
import com.hdvon.nmp.service.IMatrixChannelService;
import com.hdvon.nmp.service.IMatrixService;
import com.hdvon.nmp.service.ISysmenuService;
import com.hdvon.nmp.service.IUserDepartmentService;
import com.hdvon.nmp.service.IUserLogService;
import com.hdvon.nmp.service.IWallCameraService;
import com.hdvon.nmp.service.IWallChannelService;
import com.hdvon.nmp.service.IWallPlanService;
import com.hdvon.nmp.service.IWallTaskService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.vo.BallPlanParamVo;
import com.hdvon.nmp.vo.BallPlanVo;
import com.hdvon.nmp.vo.DeviceVo;
import com.hdvon.nmp.vo.MatrixChannelVo;
import com.hdvon.nmp.vo.MatrixScreenVo;
import com.hdvon.nmp.vo.MatrixVo;
import com.hdvon.nmp.vo.UserVo;
import com.hdvon.nmp.vo.WallPlanVo;
import com.hdvon.nmp.vo.WallTaskParamVo;
import com.hdvon.nmp.vo.WallTaskVo;
import com.hdvon.nmp.vo.sip.InviteOptionVo;
import com.hdvon.nmp.vo.sip.VideoWallVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="/cameraWall",tags="视频上墙模块")
@RestController
@RequestMapping("/cameraWall")
public class CameraWallController extends BaseController {
	
	@Reference
	private ICameraService cameraService;
	
	@Reference
	private IUserLogService userLogService;
	
	@Reference
	private ISysmenuService sysmenuService;
	
	@Reference
	private IDictionaryService dictionaryService;
	
	@Reference
	private IDictionaryTypeService dictionaryTypeService;   
	
	@Reference
	private IWallTaskService wallTaskService;
	
	@Reference
	private IWallChannelService wallChannelService;
	
	@Reference
	private IWallCameraService wallCameraService;
	
	@Reference
	private IMatrixService matrixService;
	
	@Reference
	private IMatrixChannelService matrixChannelService;
	
	@Reference
	private IUserDepartmentService userDepartmentService;
	
	@Reference
	private IWallPlanService wallPlanService;
	
	@Reference
	private IBallPlanService ballPlanService;
	
	private static MatrixVo matrixVo  = null;

	@ApiOperation(value="视屏上墙/上墙轮巡")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "name", value = "任务名称", required = true)
	})
	@PostMapping(value = "/saveTask") 
    public ApiResponse<String> saveTask(WallTaskParamVo wallTaskParamVo) {
		ApiResponse<String> res = new ApiResponse<>();
		
		UserVo userVo = getLoginUser();

		wallTaskService.save(userVo, matrixVo, wallTaskParamVo);//保存任务
		res.ok().setData("保存成功！");

		return res;
    }
	
	@ApiOperation(value="暂停轮巡")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "taskId", value = "任务id", required = true)
	})
	@PostMapping(value = "/pausePolling") 
    public ApiResponse<String> pausePolling(String taskId) {
		ApiResponse<String> res = new ApiResponse<>();

		return res;
	}
	
	@ApiOperation(value="继续轮巡")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "taskId", value = "任务id", required = true),
		@ApiImplicitParam(name = "timeInterval", value = "轮巡时间间隔", required = true)
	})
	@PostMapping(value = "/continuePolling") 
    public ApiResponse<String> continuePolling(String taskId, Integer timeInterval) {
		ApiResponse<String> res = new ApiResponse<>();
		
		return res;
	}
	
	@ApiOperation(value="停止轮巡/视屏下墙、录像下墙")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "taskId", value = "任务id", required = true)
	})
	@PostMapping(value = "/deleteTask") 
    public ApiResponse<String> deleteTask(String taskId) {
		ApiResponse<String> res = new ApiResponse<>();

		WallTaskParamVo wallTaskParamVo = new WallTaskParamVo();
		wallTaskParamVo.setTaskIds(taskId);
		wallTaskParamVo.convertToList();
		wallTaskService.delete(wallTaskParamVo.getTaskIdList());//删除轮巡任务

		return res;
	}
	
	@ApiOperation(value="连接矩阵")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "matrixId", value = "矩阵id", required = true)
	})
	@GetMapping(value = "/matrixConnect")
    public ApiResponse<List<WallTaskVo>> matrixConnect(String matrixId) {
		ApiResponse<List<WallTaskVo>> res = new ApiResponse<>();
		UserVo userVo = getLoginUser();
		
		if(StrUtil.isBlank(matrixId)) {
			res.error("矩阵id不能为空！");
			return res;
		}
		matrixVo = this.matrixService.getMatrixById(matrixId);
		
		if(matrixVo == null) {
			res.error("矩阵不存在！");
			return res;
		}
		if(StrUtil.isBlank(matrixVo.getIp())) {
			res.error("矩阵ip不能为空！");
			return res;
		}
		if(matrixVo.getPort() == null) {
			res.error("矩阵端口号不能为空！");
			return res;
		}
		
	    WallTaskVo wallTaskVo = new WallTaskVo();
	    wallTaskVo.setMatrixId(matrixId);
	    wallTaskVo.setUserId(userVo.getId());//TODO 只能查出用户权限的task
	    
	    List<WallTaskVo> taskVos = wallTaskService.getWallTaskList(wallTaskVo);
	    res.ok().setData(taskVos);
	    return res;
    }

	@ApiOperation(value="断开矩阵")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "matrixId", value = "矩阵id", required = true)
	})
	@PostMapping(value = "/matrixDisconnect")
    public ApiResponse<String> matrixDisconnect(String matrixId) {
		ApiResponse<String> res = new ApiResponse<>();
		UserVo userVo = getLoginUser();
		
		if(StrUtil.isBlank(matrixId)) {
			res.error("矩阵id不能为空！");
			return res;
		}
		
		MatrixVo matrixVo = this.matrixService.getMatrixById(matrixId);
		if(matrixVo == null) {
			res.error("矩阵不存在！");
			return res;
		}
		if(StrUtil.isBlank(matrixVo.getIp())) {
			res.error("矩阵ip不能为空！");
			return res;
		}
		if(matrixVo.getPort() == null) {
			res.error("矩阵端口号不能为空！");
			return res;
		}
		WallTaskVo wallTaskVo = new WallTaskVo();
		wallTaskVo.setMatrixId(matrixId);
		wallTaskVo.setUserId(userVo.getId());//TODO 只能删除用户权限的task
		
		List<WallTaskVo> wallTaskVos = wallTaskService.getWallTaskList(wallTaskVo);
		List<String> taskIds = new ArrayList<String>();
		for(WallTaskVo taskVo : wallTaskVos) {
			taskIds.add(taskVo.getId());
		}
		
		wallTaskService.delete(taskIds);//删除矩阵上所有任务
			
				
		return res;
    }

    @ApiOperation(value="查询矩阵列表", notes = "查询矩阵列表")
	@GetMapping(value = "/listMatrix")
	public ApiResponse<List<MatrixVo>> listMatrix() {
    	ApiResponse<List<MatrixVo>> resp = new ApiResponse<List<MatrixVo>>();
    	UserVo userVo = getLoginUser();
		 if (userVo != null) {
			 String departmentId = this.userDepartmentService.findDepartmentidByUserid(userVo.getId());
			 if (departmentId != null) {
				 Map<String,Object> paramMap = new HashMap<String,Object>();
				 if (! userVo.isAdmin()) {
					 paramMap.put("deptId", departmentId);
				 } // if
				 List<MatrixVo> list = matrixService.getMatrixList(paramMap);
				 resp.ok("查询成功！").setData(list);
				 return resp;
			 } // if
		 } // if
		 return resp.error("查询不到矩阵数据！");
	}

	@ApiOperation(value="启动上墙预案")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "wallplanId", value = "上墙预案id", required = true)
	})
	@PostMapping(value = "/startWallplan")
    public ApiResponse<String> startWallplan(String wallplanId) {
		 ApiResponse<String> resp = new ApiResponse<String>();
		 
    	 WallPlanVo wallplanVo = wallPlanService.getWallChannelCameras(wallplanId);
    	 if(wallplanVo == null){
    		 return resp.error("不存在该上墙预案！");
    	 }
    	 return resp;
	}
	
	@ApiOperation(value="查询球机巡航预案")
	@PostMapping(value = "/getBallplanList") 
    public ApiResponse<List<BallPlanVo>> getBallplanList(String cameraNo) {
		ApiResponse<List<BallPlanVo>> resp = new ApiResponse<List<BallPlanVo>>();
		UserVo userVo = getLoginUser();
		if(StrUtil.isBlank(cameraNo)) {
			return resp.error("摄像机编码不能为空！");
		}
		BallPlanParamVo ballPlanParamVo = new BallPlanParamVo();
		ballPlanParamVo.setCameraNo(cameraNo);
		ballPlanParamVo.setIsValid(1);//有效
		ballPlanParamVo.setStatus(1);//启用
		
		List<BallPlanVo> ballPlanVos = ballPlanService.getBallPlanList(ballPlanParamVo, userVo);
		return resp.ok().setData(ballPlanVos);
	}
}
