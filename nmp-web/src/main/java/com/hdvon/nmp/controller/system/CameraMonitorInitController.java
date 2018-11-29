package com.hdvon.nmp.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdvon.client.enums.AggFieldEnum;
import com.hdvon.client.form.CameraForm;
import com.hdvon.client.service.ICameraService;
import com.hdvon.client.vo.CameraPermissionVo;
import com.hdvon.nmp.common.WebConstant;
import com.hdvon.nmp.config.WebAppConfig;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.IBallPlanService;
import com.hdvon.nmp.service.ICameraLabelService;
import com.hdvon.nmp.service.IPollingPlanService;
import com.hdvon.nmp.service.IPresentPositionService;
import com.hdvon.nmp.service.IScreenTemplateService;
import com.hdvon.nmp.service.ISysconfigParamService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.ClientUtil;
import com.hdvon.nmp.vo.BallPlanParamVo;
import com.hdvon.nmp.vo.BallPlanVo;
import com.hdvon.nmp.vo.CameraFormParamVo;
import com.hdvon.nmp.vo.PollingPlanParamVo;
import com.hdvon.nmp.vo.PollingPlanVo;
import com.hdvon.nmp.vo.PresentPositionVo;
import com.hdvon.nmp.vo.ScreenTemplateVo;
import com.hdvon.nmp.vo.SysconfigParamVo;
import com.hdvon.nmp.vo.UserVo;
import com.hdvon.nmp.vo.sip.PlugntNeedInfoVo;
import com.hdvon.nmp.vo.sip.WatermarkVo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="/videoMonitoringInit",tags="视频监控初始化接口模块",description="视频监控模块初始化")
@RestController
@RequestMapping("/videoMonitoringInit")
public class CameraMonitorInitController extends BaseController {
	
	@Reference
	private ICameraService cameraService;

	@Reference
	private ICameraLabelService cameraLabelService;
	
	@Reference
	private IPresentPositionService presentPositionService;
	
	@Reference
    private ISysconfigParamService sysconfigParamService;
	
	@Autowired
	private WebAppConfig webAppConfig;
	
	@Reference
	private IPollingPlanService pollingPlanService;
	
	@Reference
	private IBallPlanService ballPlanService;
	
	@Reference
	private IScreenTemplateService screenTemplateService;
	
	@ApiOperation(value="获取当前用户地址节点的拥有的摄像机资源")
	@ApiImplicitParam(name="pid",value="地址树pid",required=false)
	@GetMapping(value = "/getAddressCameraTree")
	 public ApiResponse<List<CameraPermissionVo>> getAddressCameraTree(String pid) {
		 UserVo userVo = getLoginUser();
		 if(StrUtil.isBlank(pid)) {
			 pid="0";
		 }
		 Integer isAdmin=0;
		 if(userVo.getAccount().toLowerCase().equals("admin")){//管理员返回所有
			 isAdmin=1;
	      }
		 List<CameraPermissionVo> list= cameraService.getAddressCameraPermissionTree(userVo.getId(), pid,isAdmin);
		 return new ApiResponse().ok().setData(list);
	 }

	
	@ApiOperation(value="获取当前用户行政区划节点的拥有的摄像机资源")
	@ApiImplicitParam(name="pid",value="行政区划pid",required=false)
	@GetMapping(value = "/getOrganizationCameraTree")
	 public ApiResponse<CameraPermissionVo> getOrganizationCameraTree(String pid) {
		 UserVo userVo = getLoginUser();
		 if(StrUtil.isBlank(pid)) {
			 pid="0";
		 }
		 Integer isAdmin=0;
		 if(userVo.getAccount().toLowerCase().equals("admin")){//管理员返回所有
			 isAdmin=1;
	      }
		 List<CameraPermissionVo> list= cameraService.getOrganizationCameraPermission(userVo.getId(), pid,isAdmin);
		 return new ApiResponse().ok().setData(list);
	 }
	
	
	@ApiOperation(value="获取当前用户项目分组节点的拥有的摄像机资源")
	@ApiImplicitParam(name="pid",value="项目分组pid",required=false)
	@GetMapping(value = "/getProjectCameraTree")
	 public ApiResponse<CameraPermissionVo> getProjectCameraTree(String pid) {
		 UserVo userVo = getLoginUser();
		 if(StrUtil.isBlank(pid)) {
			 pid="0";
		 }
		 Integer isAdmin=0;
		 if(userVo.getAccount().toLowerCase().equals("admin")){//管理员返回所有
			 isAdmin=1;
	      }
		 Integer isProject=1;
		 List<CameraPermissionVo> list= cameraService.getProjectCameraPermission(userVo.getId(), pid,isAdmin,isProject);
		 return new ApiResponse().ok().setData(list);
	 }
	
	
	@ApiOperation(value="获取当前用户自定义分组节点的拥有的摄像机资源")
	@ApiImplicitParam(name="pid",value="自定义分组pid",required=false)
	@GetMapping(value = "/getCameraGroupTree")
	 public ApiResponse<CameraPermissionVo> getCameraGroupTree(String pid) {
		 UserVo userVo = getLoginUser();
		 if(StrUtil.isBlank(pid)) {
			 pid="0";
		 }
		 Integer isAdmin=0;
		 if(userVo.getAccount().toLowerCase().equals("admin")){//管理员返回所有
			 isAdmin=1;
	      }
		 List<CameraPermissionVo> list= cameraService.getCameraGroupPermission(userVo.getId(),userVo.getAccount(), pid,isAdmin);
		 return new ApiResponse().ok().setData(list);
	 }
	
	@ApiOperation(value="设备高级搜索")
	@GetMapping(value = "/searchHighCameraList")
	 public ApiResponse<CameraPermissionVo> searchHighCameraList(CameraFormParamVo param) {

		String fields="addressId,addressName,cameraId,deviceId,deviceName,deviceCode,deviceType,permissionValue";
		UserVo userVo = getLoginUser();
		param.setUserId(userVo.getId());
		CameraForm form=new CameraForm();
		BeanUtils.copyProperties(param, form);
		
		List<CameraPermissionVo> list=cameraService.searchHighCameraList(form,fields, AggFieldEnum.ADDRESS_CODE);
		return new ApiResponse().ok().setData(list);
	 }
	
	
	
	@ApiOperation(value="设备普通搜索")
	@GetMapping(value = "/searchNomalCameraList")
	@ApiImplicitParams({
		@ApiImplicitParam(name="queryStr",value="查询条件",required=false),
		@ApiImplicitParam(name="treeType",value="1 地址树；2 行政区划；3 项目分组；4 自定义分组",required=true)
	})
	public ApiResponse<CameraPermissionVo> searchNomalCameraList(String queryStr,String deviceCode,String treeType) {
		String fields="orgId,projectId,groupId,addressId,addressName,cameraId,deviceId,deviceName,deviceCode,deviceType,permissionValue";
		
		if(StrUtil.isBlank(queryStr)) {
			return new ApiResponse().ok().setData(null);
		}
		UserVo userVo = getLoginUser();
		AggFieldEnum type=null;
		if(StrUtil.isNotBlank(treeType)) {
			if(treeType.equals("1")) {
				type=AggFieldEnum.ADDRESS_CODE;
			} else if(treeType.equals("2")) {
				type=AggFieldEnum.ORG_CODE;
			}else if(treeType.equals("3")) {
				type=AggFieldEnum.PROJECT_NAME;
			}else if(treeType.equals("4")) {
				type=AggFieldEnum.GROUP_NAME;
			}
		}
		List<CameraPermissionVo> list=cameraService.searchNomalCameraList(queryStr,userVo.getId() ,fields, type);
		return new ApiResponse().ok().setData(list);
	 }
	
	
	@ApiOperation(value="视频监控初始化返回插件需要的信息")
	@ApiImplicitParam(name="number",value="需要生成多少个本地空闲端口",required=true)
	@GetMapping(value = "/getPlugntNeedInfo")
    public ApiResponse<PlugntNeedInfoVo> getPlugntNeedInfo(int number) {
		String clientIp = ClientUtil.getClientIp(request);
		List<PlugntNeedInfoVo> listVo = this.creatPlugntNeedInfo(clientIp, number);
		return new ApiResponse().ok().setData(listVo);
    }
	
	
	@ApiOperation(value="视频监控水印信息")
	@GetMapping(value = "/watermark")
    public ApiResponse<WatermarkVo> watermark() {
		 UserVo user = getLoginUser();
		 WatermarkVo vo=new WatermarkVo();
		 vo.setCardId(user.getCardId());
		 vo.setName(user.getName());
		 vo.setSystemName("视频联网智能合成管理平台");
		return new ApiResponse().ok().setData(vo);
    }
	
	@ApiOperation(value="系统是否启动水印")
	@GetMapping(value = "/isUserWate")
    public ApiResponse<SysconfigParamVo> isUserWate() {
		Map<String,Object>  param =new HashMap<String,Object>();
		param.put("enName", WebConstant.WEB_SYS_WATE);//水印参数标识
		List<SysconfigParamVo> vo= sysconfigParamService.getSysConfigByParam(param);
		if(vo.size() > 0) {
			if(! vo.get(0).getValue().equals("1")) {
				return new ApiResponse().ok().setData(false);//关闭
			}
			return new ApiResponse().ok().setData(true);
		}else {
			return new ApiResponse().ok().setData(true);//默认开启
		}
    }
	
	
//	@ApiOperation(value="获取下载文件默认名称")
//	@ApiImplicitParams({
//		@ApiImplicitParam(name="name",value="摄像机名称",required=true),
//		@ApiImplicitParam(name="startTime",value="录像开始时间",required=true),
//		@ApiImplicitParam(name="endTime",value="录像结束时间",required=false)
//	})
//	@GetMapping(value = "/downloadName")
//    public ApiResponse<Object> downloadName(String name ,String startTime,String endTime ) {
//		String downloadName=null;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
//		String startName=startTime.replace("-", "").replace(":", "").replace(" ", "");
//		String endName=null;
//		if(StrUtil.isBlank(endTime)) {
//			endName=sdf.format(new Date());
//		}else {
//			endName=endTime.replace("-", "").replace(":", "").replace(" ", "");
//		}
//		downloadName=name+"_"+startName+"_"+endName;
//		return new ApiResponse().ok().setData(downloadName);
//    }
//	
//	
//	@ApiOperation(value="获取截图默认名称")
//	@ApiImplicitParams({
//		@ApiImplicitParam(name="name",value="摄像机名称",required=true)
//	})
//	@GetMapping(value = "/imgName")
//    public ApiResponse<Object> imgName(String name ) {
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//		String date=formatter.format(new Date());
//		String imgName=name+"_"+date;
//		return new ApiResponse().ok().setData(imgName);
//    }
	
	
	@ApiOperation(value="平台预置位查询")
	@ApiImplicitParams({
		@ApiImplicitParam(name="deviceCode",value="设备编码",required=true),
		@ApiImplicitParam(name="name",value="预置位名称",required=false),
		@ApiImplicitParam(name="isKeepwatch",value="是否守望为,不为空则查询守望位 ",required=false)
	})
	@GetMapping(value = "/selectPreset")
    public ApiResponse<Object> selectPreset(String deviceCode,String name,String isKeepwatch) {
		Map<String,Object> param = new HashMap<String,Object>();
		if("true".equals(webAppConfig.getIsDev())) {	//开发环境或测试环境
			deviceCode=webAppConfig.getDeviceCode();
		}
		param.put("deviceCode", deviceCode);
		param.put("name", name);
		param.put("isKeepwatch", isKeepwatch);
		List<PresentPositionVo> list =presentPositionService.selectPresetList(param);
		return new ApiResponse().ok().setData(list);
    }
	
	
	@ApiOperation(value="查询轮询预案列表")
	@GetMapping(value = "/pollingPlanList")
	public ApiResponse<List<PollingPlanVo>> list(PollingPlanParamVo pollingPlanParamVo) {
		 ApiResponse<List<PollingPlanVo>> resp = new ApiResponse<>();
		 UserVo userVo = getLoginUser();
		 pollingPlanParamVo.setIsValid(1);
         List<PollingPlanVo> pollingPlanVos = pollingPlanService.getPollingPlanList(pollingPlanParamVo , userVo);
         resp.ok().setData(pollingPlanVos);
		 return resp;
	}

	
	//查询球机预案列表
	@ApiOperation(value="查询球机预案列表")
	@ApiImplicitParams({})
	@GetMapping(value = "/ballList")
	public ApiResponse<List<BallPlanVo>> list(BallPlanParamVo ballPlanParamVo) {
		 ApiResponse<List<BallPlanVo>> resp = new ApiResponse<List<BallPlanVo>>();
		 UserVo userVo = getLoginUser();
		 ballPlanParamVo.setIsValid(1);
         List<BallPlanVo> ballPlanVos = ballPlanService.getBallPlanList(ballPlanParamVo, userVo);

         resp.ok("查询成功！").setData(ballPlanVos);
		 return resp;
	}
	
	
	//查询自定义分屏模板列表
	@ApiOperation(value="分屏模板列表")
	@ApiImplicitParam(name = "search", value = "自定义分屏模板名称查询条件", required = false)
	@GetMapping(value = "/screenList")
	public ApiResponse<List<ScreenTemplateVo>> list(String search) {
		 ApiResponse<List<ScreenTemplateVo>> resp = new ApiResponse<List<ScreenTemplateVo>>();
         Map<String,String> paramMap = new HashMap<String,String>();
         paramMap.put("templateName", search);
         List<ScreenTemplateVo> list = screenTemplateService.getScreenTemplateList(paramMap);
         resp.ok("查询成功！").setData(list);
		 return resp;
	}
	

}
