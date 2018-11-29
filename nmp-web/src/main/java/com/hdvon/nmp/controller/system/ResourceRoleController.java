package com.hdvon.nmp.controller.system;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.config.kafka.KafkaMsgProducer;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.dto.UserDto;
import com.hdvon.nmp.service.*;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value="/resourceRoles",tags="资源角色管理模块",description="管理资源角色")
@RestController
@RequestMapping("/resourceRoles")
public class ResourceRoleController extends BaseController{
    @Reference
    private IResourceroleService resourceroleService;
    @Reference
    private ISysmenuService sysmenuService;
    @Reference
    private IUserService userService;
    @Reference
    private IAddressService addressService;
    @Reference
    private IPermissionService permissionService;
    @Reference
    private IResourceroleCameraPermissionService resourceroleAddressPermissionService;
    @Autowired
	private KafkaMsgProducer kafkaMsgProducer;

    @ApiOperation(value="获取单个资源角色信息")
    @ApiImplicitParam(name = "id", value = "资源角色id", required = true)
    @GetMapping(value = "/{id}")
    public ApiResponse<ResourceroleVo> getRoleById(@PathVariable String id) {
        if(StrUtil.isBlank(id)){
            return new ApiResponse().error("id参数不允许为空");
        }
        ResourceroleVo resourceroleVo = resourceroleService.getResRoleById(id);
        if(resourceroleVo == null){
            return new ApiResponse().error("该角色信息不存在");
        }
        return new ApiResponse().ok().setData(resourceroleVo);
    }

    @ApiOperation(value="分页获取资源角色子节点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchText", value = "模糊条件", required = false),
            @ApiImplicitParam(name = "id", value = "节点id", required = false),
    })
    @GetMapping(value = "/children")
    public ApiResponse<PageInfo<ResourceroleVo>> getRoleChildrens(ResourceroleVo resourceroleVo, PageParam pageParam) {
        PageInfo<ResourceroleVo> page = resourceroleService.getResRoleListByPage(resourceroleVo , pageParam);
        return new ApiResponse<PageInfo<ResourceroleVo>>().ok().setData(page);
    }

    @ControllerLog
    @ApiOperation(value="保存资源角色")
    @PostMapping(value = "/")
    public ApiResponse saveRole(ResourceroleVo resourceroleVo) {
        UserVo userVo = getLoginUser();
        if(StrUtil.isBlank(resourceroleVo.getName())){
            return new ApiResponse().error("资源角色名称不允许为空！");
        }
        if(StrUtil.isNotBlank(resourceroleVo.getId()) && resourceroleVo.getPid().equals(resourceroleVo.getId())) {
        	return new ApiResponse().error("资源角色的上级角色不能是自身！");
        }
        resourceroleService.saveResRole(userVo,resourceroleVo);
        return new ApiResponse().ok("保存资源角色成功！");
    }

    @ControllerLog
    @ApiOperation(value="保存资源角色地址树关联")
    @PostMapping(value = "/addressTree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "资源角色id", required = true),
            @ApiImplicitParam(name = "treeIds", value = "地址树ids", required = true)
    })
    public ApiResponse saveAddressTree(String resourceRoleId , @RequestParam(value="treeIds")String treeIds) {
        if(StrUtil.isBlank(treeIds)){
            treeIds = "";
        }
        List<String> menuIdList = Arrays.asList(treeIds.split(","));
        String permissionData = "";
        resourceroleService.saveResPermission(resourceRoleId,permissionData);
        return new ApiResponse().ok("保存成功");
    }


    @ApiOperation(value="批量删除资源角色")
    @DeleteMapping(value = "/")
    @ApiImplicitParam(name = "roleIds[]", value = "资源角色id数组", required = true)
    public ApiResponse deleteRole(@RequestParam(value="roleIds[]") String[] roleIds) {
        List<String> roleIdList = Arrays.asList(roleIds);
        resourceroleService.deleteResRole(new UserVo() , roleIdList);
        return new ApiResponse().ok("删除资源角色成功");
    }

    @ApiOperation(value="查询资源角色关联用户列表")
    @ApiImplicitParam(name = "resourceId", value = "资源角色id", required = true)
    @GetMapping(value = "/user")
    public ApiResponse<PageInfo<UserVo>> getUserByRoleId(PageParam pp, String resourceId ){
        if(StrUtil.isBlank(resourceId)){
            return new ApiResponse().error("资源角色id不能为空");
        }
        UserDto userDto = new UserDto();
        userDto.setResourceRoleId(resourceId);
        PageInfo<UserVo> page = userService.getUsersByPage(pp ,null, userDto);
        return new ApiResponse().ok().setData(page);
    }
    
    
    @ApiOperation(value="获取资源角色下的摄像机权限")
    @ApiImplicitParam(name="resourceroleId",value="资源角色id",required=false)
    @GetMapping(value = "/resourceroleCamera")
    public ApiResponse<List<ResourceroleCameraPermissionVo>> resourceroleCamera(String resourceroleId) {
        List<ResourceroleCameraPermissionVo> resourceroleTree = resourceroleAddressPermissionService.getCameraPermission(resourceroleId);
        return new ApiResponse().ok().setData(resourceroleTree);
    }
    
    @ApiOperation(value="资源角色授权")
    @PostMapping(value = "/saveResourceroleCamera")
    public ApiResponse saveResourceroleCamera(ResourceroleAddressPermissionVo vo) {
        resourceroleAddressPermissionService.saveCameraPermission(vo);
        // 
        Map<String,Object> param = new HashMap<String,Object>();
        List<String> resIds=new ArrayList<String>();
        resIds.add(vo.getResouceroleId());
        param.put("resourceroleIds", resIds);
        
        List<String> oldUserIds=resourceroleService.getUserIdsByParam(param);
        /*
         * 用户授权 同步到ES
         * 资源角色摄像机权限改变，用户数量不变
         * 所以 deleteIds 为null  和updateIds 为以前资源角色管理的用户
         */
        if(StrUtil.isNotBlank(vo.getResouceroleId())) {
        	 CameraMappingMsg msg=new CameraMappingMsg();
    		 msg.setId(IdGenerator.nextId());
    		 msg.setDeleteIds(null);
    		 msg.setUpdateIds(oldUserIds.size()>0 ?ArrayUtil.join(oldUserIds.toArray(), ","):null);
    		 msg.setRelationId(vo.getResouceroleId());
    		 msg.setType(1);
    		 
    		 kafkaMsgProducer.sendCameraMapping(msg);
        }
       
        return new ApiResponse().ok("保存成功");
    }
    
    
	@ApiOperation(value="获取资源操作权限表")
    @GetMapping(value = "/permission")
    public ApiResponse<List<PermissionVo>> getPermissionList() {
        List<PermissionVo> list = permissionService.getPermissionList();
        return new ApiResponse().ok().setData(list);
    }
    
	@ApiOperation(value="资源角色授权界面初始化查询")
    @GetMapping(value = "/getRelatedUser")
    public ApiResponse<List<UserVo>> getRelatedUser(String resourceroleId) {
		UserVo userVo = getLoginUser();
        UserDto userDto = new UserDto();
        userDto.setResourceRoleId(resourceroleId);
        List<UserVo> userVos = userService.getUsersByParam(userDto);
        return new ApiResponse().ok().setData(userVos);
    }
    
	@ControllerLog
	@ApiOperation(value="保存资源角色可管理的用户关联")
    @PostMapping(value = "/saveResourceroleRelateUsers")
    public ApiResponse<Object> saveResourceroleRelateUsers(String resourceroleId, String userIds) {
		UserVo userVo = getLoginUser();
        List<String> userIdList = null;
        if(StrUtil.isNotEmpty(userIds)){
            String[] userIdArr = userIds.split(",");
            userIdList = Arrays.asList(userIdArr);
        }
        Map<String,Object> param = new HashMap<String,Object>();
        List<String> resIds=new ArrayList<String>();
        resIds.add(resourceroleId);
        param.put("resourceroleIds", resIds);
        
//        List<String> oldUserIds=resourceroleService.getUserIdsByParam(param);
//        String deleteIds="";
//        for(String id :oldUserIds) {
//        	if(! userIds.contains(id)) {
//        		deleteIds+=id+",";
//        	}
//        }
        
		resourceroleService.saveUsersToResourceroleId(userVo, resourceroleId, userIdList);
		 /*
		  * 用户授权 同步到ES
		  * 以前资源角色的用户数量和更新后的用户数量有变化
		  * 所以 deleteIds 和updateIds 都不同
		  */
        if(StrUtil.isNotBlank(resourceroleId)) {
    		 CameraMappingMsg msg=new CameraMappingMsg();
    		 msg.setId(IdGenerator.nextId());
    		 msg.setUpdateIds(userIds);
//    		 msg.setDeleteIds(StrUtil.isNotBlank(deleteIds) ? deleteIds.substring(0, deleteIds.length()-1):null);
    		 msg.setRelationId(resourceroleId);
    		 msg.setType(1);
    		 
    		 kafkaMsgProducer.sendCameraMapping(msg);
        }
        return new ApiResponse<Object>().ok("保存成功");
    }
	
}
