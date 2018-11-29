package com.hdvon.nmp.controller.system;

import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.config.kafka.KafkaMsgProducer;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.IAddressService;
import com.hdvon.nmp.service.ICameraCameragroupService;
import com.hdvon.nmp.service.ICameraService;
import com.hdvon.nmp.service.ICameragrouopService;
import com.hdvon.nmp.service.ITreeNodeService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.FileUtil;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.CameraNode;
import com.hdvon.nmp.vo.CameragrouopVo;
import com.hdvon.nmp.vo.TreeNode;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Api(value="/groups",tags="自定义分组树管理模块",description="管理分组树模块")
@RestController
@RequestMapping("/groups")
@Slf4j
public class GroupController extends BaseController{

    @Reference
    private ICameragrouopService cameragrouopService;
    
    @Reference
    private IAddressService addressService;
    
    @Reference
    private ICameraService cameraService;
   
	@Reference
	private ITreeNodeService treeNodeService;
	
	@Reference
	private ICameraCameragroupService cameraCameragroupService;
	
	@Autowired
	private KafkaMsgProducer kafkaMsgProducer;


    @ApiOperation(value="获取单个分组信息")
    @ApiImplicitParam(name = "id", value = "分组id", required = true)
    @GetMapping(value = "/{id}")
    public ApiResponse<CameragrouopVo> getGroupById(@PathVariable String id) {
        if(StrUtil.isBlank(id)){
            return new ApiResponse().error("id参数不允许为空");
        }
        CameragrouopVo CameragrouopVo = cameragrouopService.getGroupById(id);
        if(CameragrouopVo == null){
            return new ApiResponse().error("该分组不存在");
        }
        return new ApiResponse().ok().setData(CameragrouopVo);
    }

    @ApiOperation(value="分页获取分组子节点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchText", value = "模糊条件", required = false),
            @ApiImplicitParam(name = "id", value = "节点id", required = false),
    })
    @GetMapping(value = "/children")
    public ApiResponse<PageInfo<CameragrouopVo>> getRoleChildrens(PageParam pageParam , CameragrouopVo cameragroupVo) {
    	
    	TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
 		if(StrUtil.isNotBlank(cameragroupVo.getId())) {
 			List<TreeNode> groupNodes = treeNodeService.getChildNodesByCode(cameragroupVo.getCode(), TreeType.GROUP.getVal());
 			treeNodeChildren.setGroupNodes(groupNodes);
 		}
 		
        PageInfo<CameragrouopVo> page = cameragrouopService.getGroupListByPage(cameragroupVo ,treeNodeChildren, pageParam);
        return new ApiResponse().ok().setData(page);
    }

    @ControllerLog
    @ApiOperation(value="保存分组")
    @PostMapping(value = "/")
    public ApiResponse<CameragrouopVo> saveGroup(CameragrouopVo cameragrouopVo) {
        UserVo userVo = getLoginUser();
        if(StrUtil.isBlank(cameragrouopVo.getName())){
            return new ApiResponse().error("分组名称不允许为空");
        }
        if(StrUtil.isNotBlank(cameragrouopVo.getId()) && cameragrouopVo.getPid().equals(cameragrouopVo.getId())) {
        	return new ApiResponse().error("自定义分组的上级分组不能是自身！");
        }

        cameragrouopService.saveGroup(userVo,cameragrouopVo);
        return new ApiResponse().ok("保存分组成功");
    }

    @ApiOperation(value="批量删除分组")
    @DeleteMapping(value = "/")
    @ApiImplicitParam(name = "groupIds[]", value = "分组id数组", required = true)
    public ApiResponse deleteGroup(@RequestParam(value="groupIds[]") String[] groupIds) {
        List<String> groupIdList = Arrays.asList(groupIds);
        cameragrouopService.deleteGroup(new UserVo() , groupIdList);
        return new ApiResponse().ok("删除分组成功");
    }
    
    @ApiOperation(value = "下载自定义分组文件模板")
	@GetMapping(value="/downloadCameraGroupTemplate")
	public ApiResponse<Object> downloadCameraGroupTemplate(HttpServletRequest request, HttpServletResponse response){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		try {
			String templateName = "cameragroup_template.xls";
			FileUtil.downloadTemplate(request, response, templateName, null);
			resp.ok("下载成功！");
		}catch(Exception e) {
			log.error(e.getMessage());
			resp.error("下载失败："+e.getMessage());
		}
		return resp;
	}
    
    @ControllerLog
    @ApiOperation(value = "导入摄像机分组数据")
	@PostMapping(value="/importCameraGroup")
	public ApiResponse<Object> importCameraGroup(HttpServletRequest request, HttpServletResponse response, MultipartFile file){
		ApiResponse<Object> resp = new ApiResponse<Object>();
		try {
		    // 模板
			String[] titles = new String[] {"name","code","description"};
			List<Map<String,Object>> list = FileUtil.importData(file, titles);
			// 字段
            String[] colums = new String[] {"id","name","pid","code","description"};
            List<Map<String,Object>> glist =cameragrouopService.transpCodeToId(list,colums);

			cameragrouopService.batchInsertCameragroups(glist,colums);
			resp.ok("导入成功！");
		}catch(IOException e) {
			log.error(e.getMessage());
			resp.error("导入失败："+e.getMessage());
		}
		return resp;
	}

    @ApiOperation(value = "导出分组数据")
    @GetMapping(value="/exportGrpup")
    public ApiResponse<Object> exportGrpup(HttpServletResponse response,CameragrouopVo cameragroupVo){
        ApiResponse<Object> resp = new ApiResponse<Object>();

        TreeNodeChildren treeNodeChildren = new TreeNodeChildren();
        if(StrUtil.isNotBlank(cameragroupVo.getId())) {
            List<TreeNode> groupNodes = treeNodeService.getChildNodesByCode(cameragroupVo.getCode(), TreeType.GROUP.getVal());
            treeNodeChildren.setGroupNodes(groupNodes);
        }

        String[] titles = new String[] {"id","name","code","pid","pcode","description"};
        List<Map<String,Object>> objList = new ArrayList<>();

        List<CameragrouopVo> list = cameragrouopService.exportGrpup(cameragroupVo.getName(),treeNodeChildren);
        for(CameragrouopVo item : list){
            Map<String,Object> tempMap = new HashMap<>();
            tempMap.put("id", item.getId());
            tempMap.put("name", item.getName());
            tempMap.put("code", item.getCode());
            tempMap.put("pid", item.getPid());
            tempMap.put("pcode", item.getPcode());
            tempMap.put("description", item.getDescription());
            objList.add(tempMap);
        }
        try {
            FileUtil.exportExcel(response,"自定义分组树",titles,objList);
            resp.ok("导出成功！");
        }catch (Exception e){
            e.printStackTrace();
            resp.error("导出失败！");
        }
        return resp;
    }

    @ControllerLog
	@ApiOperation(value="保存自定义分组关联的摄像机")
    @PostMapping(value = "/saveCameragroupCameras")
    public ApiResponse<Object> saveCameragroupCameras(String cameargroupId, String cameraIds,String deviceIds) {
		UserVo userVo = getLoginUser();
		String deleteIds="";
        List<String> cameraIdList = null;
		if(!StrUtil.isBlank(cameraIds)){
            String[] cameraIdArr = cameraIds.split(",");
            cameraIdList = Arrays.asList(cameraIdArr);
        }
		//以前节点下的所有摄像机
		List<CameraNode> cameraList = cameraCameragroupService.selectCamearByGroup(cameargroupId);//
		for(CameraNode node :cameraList) {
			if(! deviceIds.contains(node.getDeviceId())) {
				deleteIds+=node.getDeviceId()+",";
			}
		}
		cameragrouopService.relateCamerasToCameargroup(userVo, cameargroupId, cameraIdList);
		
		//自定义分组关联设备同步到ES
        CameraMappingMsg msg=new CameraMappingMsg();
	    msg.setId(IdGenerator.nextId());
	    msg.setUpdateIds(deviceIds);// 更新的id
	    msg.setRelationId(cameargroupId);// 分组id
	    // 删除的deviceids
	    msg.setDeleteIds(StrUtil.isNotBlank(deleteIds)? deleteIds.substring(0, deleteIds.length()-1 ): null);
	    msg.setType(3);
	   
		kafkaMsgProducer.sendCameraMapping(msg);
		
        return new ApiResponse<Object>().ok("保存成功");
    }
}
