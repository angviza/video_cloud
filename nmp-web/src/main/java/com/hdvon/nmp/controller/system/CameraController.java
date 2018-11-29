package com.hdvon.nmp.controller.system;


import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.hdvon.client.vo.CameraMsg;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.config.kafka.KafkaMsgProducer;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.dto.UserDto;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.generate.FunTypeEnum;
import com.hdvon.nmp.generate.GenerateCameraExcel;
import com.hdvon.nmp.generate.GenerateCameraTemplate;
import com.hdvon.nmp.generate.util.PropertyConfig;
import com.hdvon.nmp.generate.util.PropertyUtil;
import com.hdvon.nmp.service.*;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.OrderedProperties;
import com.hdvon.nmp.util.OrderedPropertiesUtils;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.vo.*;
import com.hdvon.nmp.vo.sip.UserDeviceParamVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Api(value="/camera",tags="摄像机管理模块",description="针对摄像机删除，查询，分组，查看详情等操作")
@RestController
@RequestMapping("/camera")
@Slf4j
public class CameraController extends BaseController {
	
	@Reference
	private ICameraService cameraService;
	@Reference
	private IDeviceService deviceService;
	@Reference
	private ICameraCameragroupService cameraCameragroupService;
	@Reference
    private ICameragrouopService cameragrouopService;
	@Reference
	private IBussinessGroupService bussinessGroupService;
	/*
	@Reference
	private ISyncIndexDataService syncIndexDataService;
	*/
	@Reference
	private ITreeNodeService treeNodeService;
	@Reference
	private IDictionaryService dictionaryService;
	
	@Autowired
	private KafkaMsgProducer kafkaMsgProducer;
	
	@Reference
	private IGenerateValidService generateValidService;
	
	//分页查询
	@ApiOperation(value="分页查询摄像机")
	@GetMapping(value = "/getCameraByMapPage")
    public ApiResponse<PageInfo<CameraVo>> getCameraByMapPage(CameraParamVo vo,PageParam pageParam) {
		ApiResponse<PageInfo<CameraVo>> resp = new ApiResponse<PageInfo<CameraVo>>();
		TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
		if(StrUtil.isNotBlank(vo.getAddressId())) {
			if(StrUtil.isBlank(vo.getAddressCode())) {
				return resp.error("地址编号不能为空！");
			}
			List<TreeNode> addressNodes = treeNodeService.getChildNodesByCode(vo.getAddressCode(), TreeType.ADDRESS.getVal());
			treeNodeChildren.setAddressNodes(addressNodes);
		}
		
		if(StrUtil.isNotBlank(vo.getOrgId())) {
			if(StrUtil.isBlank(vo.getOrgCode())) {
				return resp.error("组织机构编号不能为空！");
			}
			List<TreeNode> orgNodes = treeNodeService.getChildNodesByCode(vo.getOrgCode(), TreeType.ORG.getVal());
			treeNodeChildren.setOrgNodes(orgNodes);
		}
		
        /*if(StrUtil.isNotBlank(vo.getDeptId())) {
        	if(StrUtil.isBlank(vo.getDeptCode())) {
        		return resp.error("部门编号不能为空！");
        	}
        	List<TreeNodeDepartment> deptNodes =  treeNodeService.getDeptChildNodesByCode(vo.getDeptCode(), TreeType.DEPARTMENT.getVal());
        	treeNodeChildren.setDeptNodes(deptNodes);
        }*/
		if(StrUtil.isNotBlank(vo.getProjectId())) {
			if(StrUtil.isBlank(vo.getProjectCode())) {
				return resp.error("项目编号不能为空！");
			}
			List<TreeNode> projectNodes = treeNodeService.getChildNodesByCode(vo.getProjectCode(), TreeType.PROJECT.getVal());
			treeNodeChildren.setProjectNodes(projectNodes);
		}
		if(StrUtil.isNotBlank(vo.getCameraGroupId())) {
			if(StrUtil.isBlank(vo.getCameraGroupCode())) {
				return resp.error("自定义分组编号不能为空！");
			}
			List<TreeNode> groupNodes = treeNodeService.getChildNodesByCode(vo.getCameraGroupCode(), TreeType.GROUP.getVal());
			treeNodeChildren.setGroupNodes(groupNodes);
		}
		
        PageInfo<CameraVo> page = cameraService.getCameraByPage(vo , treeNodeChildren, pageParam);
        return resp.ok().setData(page);
    }
	
	@ApiOperation(value="保存摄像机(包括一机一档信息)")
	@PostMapping(value = "/addDevice")
	@ControllerLog(description="")
	public ApiResponse<Object> addCamera(HttpServletRequest request,DeviceParamVo vo) throws ServiceException{
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
//		if(StrUtil.isNotBlank(vo.getSbbm())) {
//			userPermission(null,"8",vo.getSbbm());//修改摄像机信息权限
//		}
		String error=deviceInfoIsBlank(vo);
		if(StrUtil.isNotBlank(error)) {
			return resp.error(error);
		}
		
		vo.setEncoderServerId("e_".equals(vo.getEncoderServerId().substring(0, 2))?vo.getEncoderServerId().substring(2):vo.getEncoderServerId());
	    String deviceIds=deviceService.editDevice(userVo,vo);
	    //同步设备信息到ES
		CameraMsg msg= new CameraMsg();
		msg.setId(userVo.getId());
		msg.setDeviceIds(deviceIds);
		if(StrUtil.isBlank(vo.getId())) {
			msg.setType(1);
		}else {
			msg.setType(2);
		}
		
		/*
		if(syncIndexDataService == null) {
			throw new ServiceException("找不到同步的服务方法");
		}
		*/
		//syncIndexDataService.sendSyncDevicePermission(msg);
		
		/************************************************************* Added by huanhongliang ***********************************************/
		/**
		 * @author 		huanhongliang
		 * @description		kafka替代MQ调用更新设备发送同步消息接口
		 * @date		2018/10/31	
		 */
		kafkaMsgProducer.sendCamera(msg);
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		return resp.ok("保存成功！");
	}
	

	@ApiOperation(value="查询摄像机信息(包括一机一档信息)")
	@GetMapping(value = "/getCameraAndDeviceInfo")
	public ApiResponse<DeviceParamVo> getCameraAndDeviceInfo(@RequestParam(value = "cameraId",required=true)String cameraId){
		ApiResponse<DeviceParamVo> resp = new ApiResponse<DeviceParamVo>();
		DeviceParamVo vo = null;
		UserVo userVo = getLoginUser();
//		userPermission(cameraId,"1",null);//查看摄像机信息权限
		
		if(StringUtils.isNotEmpty(cameraId)) {//编辑或者查看详情页面初始化
			vo = deviceService.getCameraAndDeviceInfo(cameraId);
			if(vo == null){
			    return resp.error("找不到该摄像机信息!");
            }
		}
		resp.ok("查询成功！").setData(vo);
		return resp;
	}
	
	/*//初始化加载添加、编辑或者查看详情页面
	@ApiOperation(value="查询摄像机信息(不包括一机一档)")
	@GetMapping(value = "/{id}/getCameraInfo")
	public ApiResponse<CameraVo> getCameraInfo(String id){
		ApiResponse<CameraVo> resp = new ApiResponse<CameraVo>();
		CameraVo cameraVo = null;
		try {
			if(StringUtils.isNotEmpty(id)) {//编辑或者查看详情页面初始化
				cameraVo = cameraService.getCameraInfo(id);
			}
			resp.ok().setData(cameraVo);
		}catch(Exception e) {
			log.error(e.getMessage());
			resp.error("查询失败！");
		}
		return resp;
	}*/
	
	@ApiOperation(value="删除摄像机(包括一机一档)")
	@DeleteMapping(value = "/delCamera")
	public ApiResponse<Object> delCameras(@RequestParam(value="ids[]",required=true) String[] ids){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		log.debug("参数：------------------"+ids.toString());
		List<String> idList = Arrays.asList(ids);
		String deviceIds =cameraService.delCameras(idList);
		//同步设备信息到ES
		if(StrUtil.isNotBlank(deviceIds)) {
			CameraMsg msg= new CameraMsg();
			msg.setId(userVo.getId());
			msg.setDeviceIds(deviceIds);
		    msg.setType(3);
		    
			//syncIndexDataService.sendSyncDevicePermission(msg);
		    
		    /************************************************************* Added by huanhongliang ***********************************************/
			/**
			 * @author 		huanhongliang
			 * @description		kafka替代MQ调用更新设备发送同步消息接口
			 * @date		2018/10/31	
			 */
			kafkaMsgProducer.sendCamera(msg);
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		}
		resp.ok("删除成功！");
		return resp;
	}
	
	/*@ApiOperation(value="摄像机设置分组")
	@PostMapping(value = "/setGroup")
	@ApiImplicitParams({
		 @ApiImplicitParam(name = "cameraIds", value = "需要分组的摄像机id", required = true),
		 @ApiImplicitParam(name = "groupId", value = "分组到那个分组id", required = true)
	})
	public ApiResponse<Object> setGroup(String cameraIds,String groupId){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		log.debug("摄像机参数：-----------------"+cameraIds.toString());
		log.debug("分组参数：-----------------"+groupId);
		String [] arry =cameraIds.split(",");
		List<String> idList = Arrays.asList(arry);
		cameraService.setGroup(idList,groupId);
		resp.ok("分组成功！");
		return resp;
	}
	
	@ApiOperation(value="摄像机移除分组")
	@DeleteMapping(value = "/delCameracroup")
	public ApiResponse<Object> delCameracroup(@RequestParam(value="cameraIds[]",required=true) String[] cameraIds){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		log.debug("参数：----------------"+cameraIds.toString());
		List<String> idList = Arrays.asList(cameraIds);
		cameraService.delCameracroup(idList);
		resp.ok("移除分组成功！");
		return resp;
	}*/
	
	//必填项
	private String deviceInfoIsBlank(DeviceParamVo vo) {
		if(StrUtil.isBlank(vo.getSbmc())) {
			return "摄像机名称不能为空！";
		}
		if(StrUtil.isBlank(vo.getSbbm())) {
			return "设备编号不能为空！";
		}
		if(vo.getSxjlx() ==null) {
			return "摄像机类型不能为空！";
		}
		if(vo.getSxjgnlx() ==null) {
			return "摄像机功能类型不能为空！";
		}
		if(StrUtil.isBlank(vo.getAddressId())) {
			return "所属地址树不能为空！";
		}
		if(StrUtil.isBlank(vo.getEncoderServerId())) {
			return "所属编码器不能为空！";
		}
		if(StrUtil.isBlank(vo.getBusinessGroupId())) {
			return "所属业务分组不能为空！";
		}
		if(vo.getSbzt() ==null) {
			return "摄像机状态不能为空！";
		}
		/*if(StrUtil.isBlank(vo.getLxccsbip())) {
			return "录像存储设备IP不能为空！";
		}*/
		if(vo.getCcxttdh()==null) {
			return "存储系统通道号不能为空！";
		}
		if(StrUtil.isBlank(vo.getRegisteredName())) {
			return "注册用户名不能为空！";
		}
		if(StrUtil.isBlank(vo.getRegisteredPass())) {
			return "注册密码不能为空！";
		}
		if(vo.getRegisteredServerPort() == null) {
			return "注册服务器端口号不能为空！";
		}
		if(StrUtil.isBlank(vo.getGatewayid())) {
			return "注册服务器id不能为空！";
		}
		if(StrUtil.isBlank(vo.getGatewayip())) {
			return "注册服务器IP不能为空！";
		}
		if(StrUtil.isBlank(vo.getGatewaydomain())) {
			return "注册服务器域名不能为空！";
		}
		if(StrUtil.isBlank(vo.getProjectId())) {
			return "所属项目不能为空！";
		}
		if(vo.getJkdwlx()==null) {
			return "监控点位类型不能为空！";
		}
		if(StrUtil.isBlank(vo.getJsdw())) {
			return "建设单位不能为空！";
		}
		if(StrUtil.isBlank(vo.getCjdw())) {
			return "承建单位不能为空！";
		}
		
		if(StrUtil.isBlank(vo.getOrgId())) {
			return "所属行政区划不能为空！";
		}
		
		if(StrUtil.isBlank(vo.getAzdz())) {
			return "安装详细地址不能为空！";
		}
		if(StrUtil.isBlank(vo.getCameraIp())) {
			return "ip地址不能为空！";
		}
		/*if(StrUtil.isBlank(vo.getSbyhm())) {
			return "设备用户名不能为空！";
		}
		if(StrUtil.isBlank(vo.getPassword())) {
			return "设备口令不能为空！";
		}
		if(vo.getJd() ==null) {
			return "经度不能为空！";
		}
		if(vo.getWd() ==null) {
		   return "纬度不能为空！";
		}*/
		return null;
	}
		
	/*private String deviceInfoIsBlank(DeviceParamVo vo) {
		if(StrUtil.isBlank(vo.getSbmc())) {
			return "设备/区域/系统名称不能为空！";
		}
		if(StrUtil.isBlank(vo.getXzqh())) {
			return "行政区划不能为空！";
		}
		if(vo.getParental() ==null) {
			return "子设备不能为空！";
		}
		if(vo.getSbcs() ==null) {
			return "设备厂商不能为空！";
		}
		if(StrUtil.isBlank(vo.getBlock())) {
			return "警区不能为空！";
		}
		if(StrUtil.isBlank(vo.getAzdz())) {
			return "安装地址不能为空！";
		}
		if(vo.getJd() ==null) {
			return "经度不能为空！";
		}
		if(vo.getWd() ==null) {
		   return "纬度不能为空！";
		}
		if(vo.getRegisterWay() ==null) {
			return "注册方式不能为空！";
		}
		if(vo.getLwsx() ==null) {
			return "联网属性不能为空！";
		}
		if(vo.getQysj() ==null) {
			return "启动时间不能为空！";
		}
		if(vo.getSbzt() ==null) {
			return "设备状态不能为空！";
		}
		if(vo.getSpds() ==null) {
			return "视频丢失不能为空！";
		}
		if(vo.getScsz() ==null) {
			return "色彩失真不能为空！";
		}
		if(vo.getSpmh() ==null) {
			return "视频模糊不能为空！";
		}
		if(vo.getLdyc() ==null) {
			return "亮度异常不能为空！";
		}
		if(vo.getSpgr() ==null) {
			return "视频干扰不能为空！";
		}
		if(vo.getSpkd() ==null) {
			return "视频卡顿不能为空！";
		}
		if(vo.getSpzd() ==null) {
			return "视频遮挡不能为空！";
		}
		if(vo.getCjbg() ==null) {
			return "场景变更不能为空！";
		}
		if(vo.getZxsc() ==null) {
			return "在线时长不能为空！";
		}
		if(vo.getLxsc() ==null) {
			return "离线时长不能为空！";
		}
		if(vo.getXlsy() ==null) {
			return "信令时延不能为空！";
		}
		if(vo.getSplsy() ==null) {
			return "视频流时延不能为空！";
		}
		if(vo.getGjzsy() ==null) {
			return "关键帧时延不能为空！";
		}
		
		if(vo.getLxbcts() ==null) {
			return "录像保存天数不能为空！";
		}
		
		if(vo.getJkdwlx() ==null) {
			return "监控点位类型不能为空！";
		}
		if(vo.getJslx() ==null) {
			return "建设类型不能为空！";
		}
		if(vo.getSxjssbm() ==null) {
			return "摄像机所属部门（警种）不能为空！";
		}
		if(StrUtil.isBlank(vo.getJsdw())) {
			return "建设单位/平台归属代码不能为空！";
		}
		if(StrUtil.isBlank(vo.getCjdw())) {
			return "承建单位不能为空！";
		}
		return null;
	}*/


	@ApiOperation(value="查询自定义分组中摄像机列表")
	@ApiImplicitParam(name = "groupIds", value = "多个自定义分组id用都好隔开")
	@GetMapping(value = "/getCamerasByGroupids")
	public ApiResponse<List<CameraCameragroupVo>> getCamerasByGroupids(String groupIds){
		ApiResponse<List<CameraCameragroupVo>> resp = new ApiResponse<List<CameraCameragroupVo>>();
		
		String[] groupIdArr = groupIds.split(",");
		List<String> groupIdList = Arrays.asList(groupIdArr);
		List<CameraCameragroupVo> cameraVos = cameraService.getCamerasByGroupIds(groupIdList);
		resp.ok().setData(cameraVos);
		return resp;
	}
	
	@ApiOperation(value="当前登录用户的地址树摄像机自定义分组")
	@GetMapping(value = "/getAddressCameraGroupid")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "groupId", value = "自定义分组id")
	})
	public ApiResponse<List<CameraNode>> getAddressCameraGroupid(String groupId){
	    UserVo userVo = getLoginUser();
        List<CameraNode> list = cameraCameragroupService.selectByGroup(userVo, groupId);
		return new ApiResponse().ok().setData(list);
	}
	
	@ApiOperation(value = "下载摄像机文件模板")
	@GetMapping(value="/downloadCameraTemplate")
	public ApiResponse<Object> downloadCameraTemplate(HttpServletRequest request, HttpServletResponse response){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		try {
			List<String> dicKeys = new ArrayList<String>();
			OrderedProperties dicPreperties = PropertyUtil.getProperties(PropertyConfig.PROPERTY_CAMERA_DIC);
			Set<String> dicEn = dicPreperties.stringPropertyNames();
			Iterator<String> dicIt = dicEn.iterator();
			while(dicIt.hasNext()) {
				String dicKey = (String) dicIt.next();
				String dicVal = dicPreperties.getProperty(dicKey);
				dicKeys.add(dicVal);
			}
			
			Map<String,List<DictionaryVo>> dicMap = dictionaryService.getDictionaryMap(dicKeys);
			String templateName = "camera_template.xls";
			
			OrderedProperties cameraProperties = PropertyUtil.getProperties(PropertyConfig.PROPERTY_CAMERA);
			String cols = cameraProperties.getProperty("maxCols");
			String colWidth = cameraProperties.getProperty("colWidth");
			
			GenerateCameraTemplate.downloadCameraTemplate(request, response, templateName, null, dicMap, cols, Integer.parseInt(colWidth));
			resp.ok("下载成功！");
		}catch(Exception e) {
			log.error(e.getMessage());
			resp.error("下载失败："+e.getMessage());
		}
		return resp;
	}
	
	@ControllerLog
    @ApiOperation(value = "导入摄像机列表")
	@PostMapping(value="/importCameras")
	public ApiResponse<Object> importCameras(HttpServletRequest request, HttpServletResponse response, MultipartFile file){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		List<CheckAttributeVo> checkAttrs = PropertyUtil.getCheckAttributes(PropertyConfig.PROPERTY_CAMERA_ATTR);
		
		OrderedProperties cameraProperties = PropertyUtil.getProperties(PropertyConfig.PROPERTY_CAMERA);
		int headRow = Integer.parseInt(cameraProperties.getProperty("headCol"));
		
		List<String> dicKeys = new ArrayList<String>();
		Map<String,String> dicKeyMap = new HashMap<String,String>();
		OrderedProperties dicPreperties = PropertyUtil.getProperties(PropertyConfig.PROPERTY_CAMERA_DIC);
		Set<String> dicEn = dicPreperties.stringPropertyNames();
		Iterator<String> dicIt = dicEn.iterator();
		while(dicIt.hasNext()) {
			String dicKey = (String) dicIt.next();
			String dicVal = dicPreperties.getProperty(dicKey);
			dicKeys.add(dicVal);
			dicKeyMap.put(dicKey, dicVal);
		}
		Map<String,List<DictionaryVo>> dicMap = dictionaryService.getDictionaryMap(dicKeys);
		Map<String,Object> result = GenerateCameraExcel.generateCameraExcel(file, checkAttrs, headRow, dicMap, dicKeyMap);
		List<Map<String,String>> list = (List<Map<String, String>>) result.get("excelData");
		int[] validCols = (int[]) result.get("validCols");
		Integer cursRow = (Integer) result.get("cursRow");
		Map<String,List<String>> relateIdMap = generateValidService.checkCameraAttrExists(list, new ValidAttrVo(FunTypeEnum.CAMERA.getVal(),validCols), cursRow);
		String deviceIds = cameraService.batchInsertCameras(list, checkAttrs, userVo, relateIdMap);
		
		
		CameraMsg msg= new CameraMsg();
		msg.setId(userVo.getId());
		msg.setDeviceIds(deviceIds);
	    msg.setType(1);
	    //kafka推送消息，同步摄像机到es
		kafkaMsgProducer.sendCamera(msg);
		
		resp.ok("导入成功！");
		return resp;
	}
	
    @ApiOperation(value = "导出摄像机列表")
	@GetMapping(value="/exportCameras")
	public ApiResponse<Object> exportCameras(HttpServletRequest request, HttpServletResponse response, CameraParamVo vo,PageParam pageParam) throws IOException{
		String templateName = "camera_template.xls";
    	long startTime = System.currentTimeMillis();
    	ApiResponse<Object> resp = new ApiResponse<Object>();
		//List<Map<String,Object>> result = cameraService.getCamerasByGroupId(groupId);
    	
    	//CameraParamVo vo = new CameraParamVo();
    	//PageParam pageParam = new PageParam();
		TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
		if(StrUtil.isNotBlank(vo.getAddressId())) {
			List<TreeNode> addressNodes = treeNodeService.getChildNodesByCode(vo.getAddressCode(), TreeType.ADDRESS.getVal());
			treeNodeChildren.setAddressNodes(addressNodes);
		}
		if(StrUtil.isNotBlank(vo.getOrgId())) {
			List<TreeNode> orgNodes = treeNodeService.getChildNodesByCode(vo.getOrgCode(), TreeType.ORG.getVal());
			treeNodeChildren.setOrgNodes(orgNodes);
		}
		if(StrUtil.isNotBlank(vo.getProjectId())) {
			List<TreeNode> projectNodes = treeNodeService.getChildNodesByCode(vo.getProjectCode(), TreeType.PROJECT.getVal());
			treeNodeChildren.setProjectNodes(projectNodes);
		}
		if(StrUtil.isNotBlank(vo.getCameraGroupId())) {
			List<TreeNode> groupNodes = treeNodeService.getChildNodesByCode(vo.getCameraGroupCode(), TreeType.GROUP.getVal());
			treeNodeChildren.setGroupNodes(groupNodes);
		}
		
		List<String> titles = new ArrayList<String>();
		List<String> titleNames = new ArrayList<String>();

		/*OrderedProperties expProperties = OrderedPropertiesUtils.getProperties(PropertyConfig.PROPERTY_CAMERA_ATTR);
		Set<String> en = expProperties.stringPropertyNames();
		Iterator<String> it = en.iterator();
		while(it.hasNext()) {
			String key = (String) it.next();
			String value = expProperties.getProperty(key);
			titles.add(key);
			titleNames.add(value);
		}*/
		List<CheckAttributeVo> checkAttrs = PropertyUtil.getCheckAttributes(PropertyConfig.PROPERTY_CAMERA_ATTR);
		for(CheckAttributeVo checkVo : checkAttrs) {
			titles.add(checkVo.getCode());
			titleNames.add(checkVo.getName());
		}
		
		OrderedProperties cameraProperties = OrderedPropertiesUtils.getProperties("camera");
		String sheetSize = cameraProperties.getProperty("sheetSize");
		String expCount = cameraProperties.getProperty("expCount");
		String templateCols = cameraProperties.getProperty("maxCols");
		String flushRows = cameraProperties.getProperty("flushRows");
		String colWidth = cameraProperties.getProperty("colWidth");
		
		List<String> dicKeys = new ArrayList<String>();
		OrderedProperties dicPreperties = OrderedPropertiesUtils.getProperties("cameraDic");
		Set<String> dicEn = dicPreperties.stringPropertyNames();
		Iterator<String> dicIt = dicEn.iterator();
		while(dicIt.hasNext()) {
			String key = (String) dicIt.next();
			dicKeys.add(key);
		}

		Map<String,List<DictionaryVo>> dicMap = dictionaryService.getDictionaryMap(dicKeys);
		OutputStream output = null;
		SXSSFWorkbook xwb = new SXSSFWorkbook();
		try {
			System.out.println("开始导出：" + System.currentTimeMillis());
			String fileName = "摄像机列表.xls";
			response.reset();
			response.setCharacterEncoding("utf-8");
		    response.setHeader("Content-disposition", "attachment;charset=utf-8; filename="+fileName);
		    response.setContentType("application/msexcel"); 
			
		    output = response.getOutputStream();
			pageParam.setPageSize(expCount);
			
			long startTransTime = System.currentTimeMillis();
			
			long startQueryTime = System.currentTimeMillis();
			PageInfo<Map<String,Object>> page = deviceService.getDeviceByPage(vo , treeNodeChildren, pageParam);
			long endQueryTime = System.currentTimeMillis();
			System.out.println("单次查询耗时："+(endQueryTime-startQueryTime)/1000);
			long startExcelTime = System.currentTimeMillis();
			HSSFRow explainRow = GenerateCameraTemplate.getCameraTemplateExplain(templateName, templateCols, dicMap);//根据字典数据和摄像机导入模板第一行的key值，生成有详细内容的说明行
			GenerateCameraExcel.export2007Data(xwb, Integer.parseInt(sheetSize), Integer.parseInt(flushRows), page.getList(), titleNames, titles, templateName, explainRow, Integer.parseInt(colWidth));
			long endExcelTime = System.currentTimeMillis();
			System.out.println("单次写入excel耗时："+(endExcelTime-startExcelTime)/1000);
			long endTransTime = System.currentTimeMillis();
			System.out.println("单次查询及写入excel耗时：" + (endTransTime-startTransTime));
			output.flush();
			xwb.write(output);
			output.close();
			System.out.println("结束导出：" + System.currentTimeMillis());
			resp.ok("导出成功!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(output != null) {
				output.close();
			}
			resp.error("导出失败!");
		} finally {
			if(output != null) {
				output.close();
			}
		}
		
		long endTime = System.currentTimeMillis();
		long seconds = endTime-startTime;
		System.out.println("总耗时："+seconds);
		return resp;
	}
	public static void main(String[] args) {
		OrderedProperties preperties = OrderedPropertiesUtils.getProperties("importCamera");
		Set<String> en = preperties.stringPropertyNames();
		Iterator<String> it = en.iterator();
		while(it.hasNext()) {
			String key = (String) it.next();
			String value = preperties.getProperty(key);
			if("SBBM".equals(key)) {

				JSONObject json = (JSONObject) JSONObject.parse(value);
				String name = (String) json.get("name");
				String obj = (String) json.get("valid");
				JSONObject validObj = (JSONObject) JSONObject.parse(obj);
				String arrayStr = (String) validObj.get("DATE");
				JSONArray array = (JSONArray) JSONObject.parse(arrayStr);
				System.out.println(array);
			}
		}
	}
	/**
	 * 判断用户是有权限操作摄像机
	 * @param cameraId
	 * @param value  权限值
	 * @param deviceCode
	 */
	private void userPermission(String cameraId,String value,String deviceCode) {
		UserVo userVo = getLoginUser();
		Map<String ,Object> param =new HashMap<String ,Object>();
		if(! userVo.isAdmin()){//管理员返回所有
			param.put("userId", userVo.getId());
			param.put("cameraId", cameraId);
			param.put("deviceCode", deviceCode);
			List<UserDeviceParamVo> list= cameraService.getUserCameraPermission(param);
			if(list.size()>0) {
				if(StrUtil.isNotBlank(list.get(0).getPermissionVlaue())) {
					if( ! list.get(0).getPermissionVlaue().contains(value)) {
						throw new ServiceException("您没有操作该摄像机权限!");
					}
				}
			}else {
				throw new ServiceException("您没有操作该摄像机权限!");
			}
	     }
		
	}
	
}
