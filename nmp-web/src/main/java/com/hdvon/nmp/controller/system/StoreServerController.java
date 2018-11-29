package com.hdvon.nmp.controller.system;

import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.generate.GenerateCameraExcel;
import com.hdvon.nmp.generate.GenerateCameraTemplate;
import com.hdvon.nmp.service.*;
import com.hdvon.nmp.util.*;
import com.hdvon.nmp.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;


@Api(value="/storeServer",tags="存储服务器管理模块",description="针对存储服务器的插入,删除,修改,查看等操作")
@RestController
@RequestMapping("/storeServer")
@Slf4j
public class StoreServerController extends BaseController{
	@Reference
	private IStoreServerService storeServerService;
	
	@Reference
	private IStoreserverCameraService storeserverCameraService;
	
	@Reference
	private IAddressService addressService;
	
	@Reference
	private ICameraService cameraService;
	
	@Reference
	private ITreeNodeService treeNodeService;

    @Reference
    private IDictionaryService dictionaryService;

    @Reference
    private IDeviceService deviceService;
	
	//分页查询转发服务器列表
	@ApiOperation(value="分页查询存储服务器列表")
	@ApiImplicitParam(name = "search", value = "存储服务器名称查询条件", required = false)
	@GetMapping(value = "/storeServerPage")
	public ApiResponse<PageInfo<StoreServerVo>> storeServerPage(PageParam pp, StoreServerParamVo storeServerParamVo) {
		 ApiResponse<PageInfo<StoreServerVo>> resp = new ApiResponse<PageInfo<StoreServerVo>>();
        
        TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
		if(StrUtil.isNotBlank(storeServerParamVo.getAddressId())) {
			List<TreeNode> addressNodes = treeNodeService.getChildNodesByCode(storeServerParamVo.getAddressCode(), TreeType.ADDRESS.getVal());
			treeNodeChildren.setAddressNodes(addressNodes);
		}
		
        PageInfo<StoreServerVo> pageInfo= storeServerService.getStoreServerPages(pp, treeNodeChildren, storeServerParamVo);
        resp.ok("查询成功！").setData(pageInfo);
		 return resp;
	}

	//查询存储服务器列表
	@ApiOperation(value="查询存储服务器列表")
	@ApiImplicitParam(name = "search", value = "存储服务器名称查询条件", required = false)
	@GetMapping(value = "/storeServerList")
	public ApiResponse<List<StoreServerVo>> storeServerList(PageParam pp, StoreServerParamVo storeServerParamVo) {
		 ApiResponse<List<StoreServerVo>> resp = new ApiResponse<List<StoreServerVo>>();
       
		 TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
		if(StrUtil.isNotBlank(storeServerParamVo.getAddressId())) {
			List<TreeNode> addressNodes = treeNodeService.getChildNodesByCode(storeServerParamVo.getAddressCode(), TreeType.ADDRESS.getVal());
			treeNodeChildren.setAddressNodes(addressNodes);
		}
			
        List<StoreServerVo> list= storeServerService.getStoreServerList(treeNodeChildren, storeServerParamVo);
        resp.ok("查询成功！").setData(list);
		 return resp;
	}
	//添加项目或者编辑存储服务器初始化
	@ApiOperation(value = "添加或者编辑存储服务器初始化")
	@GetMapping(value="/edit")
	public ApiResponse<StoreServerVo> edit(HttpServletRequest request, String id){
		 ApiResponse<StoreServerVo> resp = new ApiResponse<StoreServerVo>();
		 StoreServerVo storeServerVo = storeServerService.getStoreServerById(id);
		 if(storeServerVo == null){
		     return resp.error("存储服务器不存在");
         }else{
             return resp.ok().setData(storeServerVo);
         }
	}
	//添加或者编辑提交
	@ControllerLog
	@ApiOperation(value = "保存存储服务器信息")
	@PostMapping(value = "/save")
	public ApiResponse<Object> save(HttpServletRequest request, StoreServerParamVo storeServerParamVo, String projectIds) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		String validMsg = "";
        if(StrUtil.isBlank(storeServerParamVo.getName())) {
            validMsg = "存储服务器名称不能为空！";
        }
        if(StrUtil.isBlank(storeServerParamVo.getCode())) {
            validMsg = "存储服务器编号不能为空！";
        }
        if(StrUtil.isBlank(storeServerParamVo.getIp())) {
            validMsg = "存储服务器ip不能为空！";
        }
        if(storeServerParamVo.getPort() == null) {
            validMsg = "存储服务器端口不能为空！";
        }
        if(StrUtil.isBlank(storeServerParamVo.getUserName())) {
            validMsg = "存储服务器操作系统登陆用户名不能为空！";
        }
        if(StrUtil.isBlank(storeServerParamVo.getPassword())) {
            validMsg = "存储服务器操作系统登陆密码不能为空！";
        }
        if(StrUtil.isBlank(storeServerParamVo.getAddressId())) {
        	validMsg = "存储服务器所属地址不能为空！";
        }
        String[] projectIdArr = projectIds.split(",");
        List<String> projectIdList = Arrays.asList(projectIdArr);
        if(projectIdList == null || projectIdList.size()==0) {
        	validMsg = "存储服务器所属项目不能为空！";
        }
        if(StrUtil.isNotBlank(validMsg)) {
            resp.error(validMsg);
            return resp;
        }
        storeServerService.saveStoreServer(userVo, storeServerParamVo, projectIdList);
        resp.ok("保存成功！");
        return resp;
	}
	//删除存储服务器
	@ApiOperation(value = "删除存储服务器")
	@DeleteMapping(value = "/del")
	public ApiResponse<Object> del(HttpServletRequest request,@RequestParam(value="ids[]") String[] ids) {
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		
		List<String> idList = Arrays.asList(ids);
        storeServerService.delStoreServersByIds(userVo, idList);
        apiRes.ok("删除成功！");
		return apiRes;
	}
	
	@ApiOperation(value="获取存储服务器关联摄像机")
    @GetMapping(value = "/getRelatedCamera")
    public ApiResponse<List<CameraNode>> getRelatedCamera(String pid, String storeserverId) {
		UserVo userVo = getLoginUser();
		List<CameraNode> list = storeServerService.getStoreserverCamera(userVo,storeserverId);
        return new ApiResponse<List<CameraNode>>().ok().setData(list);
    }
	
	@ControllerLog
	@ApiOperation(value="保存存储服务器下关联的摄像机")
    @PostMapping(value = "/saveStoreserverCameras")
    public ApiResponse<Object> saveStoreserverCameras(String storeserverId, String cameraIds) {
		UserVo userVo = getLoginUser();
		if(cameraIds == null){
		    cameraIds = "";
        }
		String[] cameraIdArr = cameraIds.split(",");
		List<String> cameraIdList = Arrays.asList(cameraIdArr);
		storeServerService.relateCamerasToStoreserver(userVo, storeserverId, cameraIdList);
        return new ApiResponse<Object>().ok("保存成功");
    }
	
	@ApiOperation(value="查询存储服务器下关联的摄像机,并分页")
    @GetMapping(value = "/getRelatedCamerasInStoreserver")
    public ApiResponse<PageInfo<StoreserverCameraVo>> getRelatedCamerasByStoreserverId(PageParam pp, StoreserverCameraVo storeserverCameraVo) {
    	ApiResponse<PageInfo<StoreserverCameraVo>> resp = new ApiResponse<PageInfo<StoreserverCameraVo>>();
		UserVo userVo = getLoginUser();
		PageInfo<StoreserverCameraVo> cameraVos = storeServerService.getUserCamerasByStoreserverId(userVo,pp ,storeserverCameraVo );
        return resp.ok().setData(cameraVos);
    }
	
	//删除存储服务器下的摄像机关联
	@ApiOperation(value = "删除存储服务器下的摄像机关联")
	@DeleteMapping(value = "/delRelatedCameras")
	public ApiResponse<Object> delRelatedCameras(String storeserverId, @RequestParam(value="ids[]") String[] ids) {
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
        UserVo userVo = getLoginUser();
        List<String> idList = Arrays.asList(ids);
        storeServerService.delRelatedCamerasByIds(userVo, storeserverId, idList);
        apiRes.ok("删除成功！");
		return apiRes;
	}
	
	//保存单个摄像机对应的每周定时录像设置
	@ControllerLog
	@ApiOperation(value = "保存单个摄像机对应的每周定时录像设置")
	@PostMapping(value = "/saveTimingVedioSet")
	public ApiResponse<Object> saveTimingVedioSet(HttpServletRequest request, TimingVedioParamVo timingVedioParamVo) {
		ApiResponse<Object> apiRes = new ApiResponse<Object>();
		UserVo userVo = getLoginUser();
		timingVedioParamVo.convertWeekSets();
		StoreserverCameraVo storeserverCameraVo = storeserverCameraService.getStoreServerCameraById(timingVedioParamVo.getStoreCameraId());
		
		Integer keepDays = storeserverCameraVo.getKeepDays();
		if(keepDays == null || keepDays == 0) {
			return apiRes.error("没有录像保存天数！");
		}
		storeServerService.saveTimingVedioSet(userVo, timingVedioParamVo, storeserverCameraVo.getKeepDays());
		apiRes.ok("保存成功！");
		return apiRes;
	}
	
	@ApiOperation(value="查询存储服务器下关联的单个摄像机的定时设置")
    @GetMapping(value = "/getRelatedCameraTimingSet")
    public ApiResponse<List<TimingVedioResultVo>> getRelatedCameraTimingSet(String storeCameraId) {
    	ApiResponse<List<TimingVedioResultVo>> resp = new ApiResponse<List<TimingVedioResultVo>>();
    	if(StrUtil.isBlank(storeCameraId)) {
    		return resp.error("存储服务器关联摄像机的id不能为空！");
    	}
		List<TimingVedioResultVo> timingVedioResultVos = storeServerService.getStoreCameraTimingSet(storeCameraId);
        return resp.ok().setData(timingVedioResultVos);
    }

	@ApiOperation(value="查询摄像机,并显示摄像机设置天数")
	@GetMapping(value = "/getStoreServerCameraByCameraId")
	public ApiResponse<StoreserverCameraVo> getStoreServerCameraByCameraId(String cameraId) {
		ApiResponse<StoreserverCameraVo> resp = new ApiResponse<>();
		StoreserverCameraVo cameraVo = storeServerService.getStoreServerCameraByCameraId(cameraId );
		return resp.ok().setData(cameraVo);
	}
	
	@ApiOperation(value = "保存存储服务器关联摄像机的存储天数")
	@PostMapping(value = "/saveRelateCameraSaveDay")
	public ApiResponse<Object> saveRelateCameraSaveDay(String storeCameraIds,String keepDays) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		if(StrUtil.isBlank(storeCameraIds)) {
			return resp.error("id不能为空！");
		}
		if(StrUtil.isBlank(keepDays)) {
			return resp.error("录像保存天数不能为空！");
		}
		String[] storeCameraIdsArr = storeCameraIds.split(",");
		storeServerService.saveStoreServerCamera(storeCameraIdsArr ,keepDays);
		return resp.ok();
	}

    @ApiOperation(value="导出存储服务器关联的摄像机")
    @GetMapping(value = "/exportStoreRelateCamera")
    public ApiResponse<Object> exportStoreRelateCamera(HttpServletResponse response,String storeserverId) throws IOException {
        UserVo userVo = getLoginUser();
        String templateName = "camera_template.xls";
        long startTime = System.currentTimeMillis();
        ApiResponse<Object> resp = new ApiResponse<>();
        if(StrUtil.isBlank(storeserverId)){
            return resp.error("请选择服务器");
        }

        List<String> titles = new ArrayList<String>();
        List<String> titleNames = new ArrayList<String>();

        OrderedProperties expProperties = OrderedPropertiesUtils.getProperties("exportCamera");
        Set<String> en = expProperties.stringPropertyNames();
        Iterator<String> it = en.iterator();
        while(it.hasNext()) {
            String key = (String) it.next();
            String value = expProperties.getProperty(key);
            titles.add(key);
            titleNames.add(value);
        }

        OrderedProperties cameraProperties = OrderedPropertiesUtils.getProperties("camera");
        String sheetSize = cameraProperties.getProperty("sheetSize");
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

        Map<String, List<DictionaryVo>> dicMap = getDeviceDics(dicKeys);
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

            long startTransTime = System.currentTimeMillis();
            long startQueryTime = System.currentTimeMillis();

            List<Map<String,Object>>  list = storeServerService.getCameraByStoreId(userVo,storeserverId);
            long endQueryTime = System.currentTimeMillis();
            System.out.println("单次查询耗时："+(endQueryTime-startQueryTime)/1000);
            long startExcelTime = System.currentTimeMillis();

            //根据字典数据和摄像机导入模板第一行的key值，生成有详细内容的说明行
            HSSFRow explainRow = GenerateCameraTemplate.getCameraTemplateExplain(templateName, templateCols, dicMap);
            GenerateCameraExcel.export2007Data(xwb, Integer.parseInt(sheetSize), Integer.parseInt(flushRows), list, titleNames, titles, templateName, explainRow, Integer.parseInt(colWidth));
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

        return resp.ok();
    }

    /**
     * 摄像机涉及的字典数据
     * @return
     */
    private Map<String,List<DictionaryVo>> getDeviceDics(List<String> dicKeys){

        List<DictionaryVo> dics = dictionaryService.getDictionaryList(dicKeys);

        Map<String,List<DictionaryVo>> dicMap = new HashMap<String,List<DictionaryVo>>();
        for(DictionaryVo dicVo : dics) {
            if(dicMap.get(dicVo.getTypeEhName()) != null) {
                dicMap.get(dicVo.getTypeEhName()).add(dicVo);
            }else {
                List<DictionaryVo> list = new ArrayList<DictionaryVo>();
                list.add(dicVo);
                dicMap.put(dicVo.getTypeEhName(), list);
            }
        }
        return dicMap;
    }
}