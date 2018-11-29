package com.hdvon.nmp.controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdvon.nmp.common.WebConstant;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.service.ITreeNodeService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(value="/treeNode",tags="公共树节点查询",description="公共树节点查询")
@RestController
@RequestMapping("/treeNode")
@Slf4j
public class TreeNodeController extends BaseController {

    @Reference
    private ITreeNodeService treeNodeService;

    @ApiOperation(value="获取地址树")
    @GetMapping(value = "/addressTree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "父节点id", required = false)
    })
    public ApiResponse<List<TreeNode>> addressTree(String pid) {
        UserVo userVo = this.getLoginUser();
        List<TreeNode> treeList = treeNodeService.getAddressTree(userVo, pid);
        return new ApiResponse().ok().setData(treeList);
    }

    @ApiOperation(value="获取地址摄像机树")
    @GetMapping(value = "/addressCameraTree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "父节点id", required = false),
            @ApiImplicitParam(name = "deviceType", value = "摄像机类型，默认值为空则查询所有，ball查询可控球机", required = false),
            @ApiImplicitParam(name = "name", value = "关键字搜索", required = false)
    })
    public ApiResponse<List<TreeNodeCamera>> addressCameraTree(String pid , String deviceType,String name) {
        UserVo userVo = this.getLoginUser();
        List<TreeNodeCamera> list = treeNodeService.getAddressCameraTree(userVo, pid , deviceType, name);
        return new ApiResponse().ok().setData(list);
    }

    @ApiOperation(value="获取分组摄像机树")
    @GetMapping(value = "/groupCameraTree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "父节点id", required = false),
            @ApiImplicitParam(name = "name", value = "关键字搜索", required = false)
    })
    public ApiResponse<List<TreeNodeCamera>> groupCameraTree(String pid,String name) {
        UserVo userVo = this.getLoginUser();
        List<TreeNodeCamera> list = treeNodeService.getGroupCameraTree(userVo, pid , name);
        return new ApiResponse().ok().setData(list);
    }

    @ApiOperation(value = "获取编码器树")
    @GetMapping(value = "/addressEncoderTree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "父节点id", required = false),
            @ApiImplicitParam(name = "name", value = "关键字搜索", required = false)
    })
    public ApiResponse<List<TreeNode>> addressEncoderTree(String pid, String name) {
        UserVo userVo = this.getLoginUser();
        List<TreeNode> list = treeNodeService.getAddressEncoderTree(userVo, pid, name);
        return new ApiResponse().ok().setData(list);
    }

    @ApiOperation(value="获取部门树")
    @GetMapping(value = "/departmentTree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "父节点id", required = false)
    })
    public ApiResponse<List<TreeNodeDepartment>> departmentTree(String pid) {
        UserVo userVo = this.getLoginUser();
        List<TreeNodeDepartment> treeList = treeNodeService.getDepartmentTree(userVo,pid);
        return new ApiResponse().ok().setData(treeList);
    }

    @ApiOperation(value="获取部门用户树")
    @GetMapping(value = "/departmentUserTree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "父节点id", required = false)
    })
    public ApiResponse<List<TreeNodeUser>> departmentUserTree(String pid) {
        UserVo userVo = this.getLoginUser();
        List<TreeNodeUser> treeList = treeNodeService.getDepartmentUserTree(userVo, pid);
        return new ApiResponse().ok().setData(treeList);
    }

    @ApiOperation(value="获取系统角色用户树")
    @GetMapping(value = "/sysRoleUserTree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "父节点id", required = false)
    })
    public ApiResponse<List<TreeNodeUser>> sysRoleUserTree(String pid) {
        UserVo userVo = this.getLoginUser();
        List<TreeNodeUser> treeList = treeNodeService.getSysRoleUserTree(userVo, pid);
        return new ApiResponse().ok().setData(treeList);
    }

    @ApiOperation(value="获取行政区划树")
    @GetMapping(value = "/organizationTree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hasVirtual", value = "是否包含虚拟组织", required = false),
    })
    public ApiResponse<List<TreeNodeOrg>> organizationTree(Boolean hasVirtual ) {
        UserVo userVo = this.getLoginUser();
        List<TreeNodeOrg> treeList = treeNodeService.getOrganizationTree(userVo, hasVirtual);
        return new ApiResponse().ok().setData(treeList);
    }

    @ApiOperation(value="获取项目树")
    @GetMapping(value = "/projectTree")
    public ApiResponse<List<TreeNode>> projectTree() {
        UserVo userVo = this.getLoginUser();
        List<TreeNode> treeList = treeNodeService.getProjectTree(userVo);
        return new ApiResponse().ok().setData(treeList);
    }

    @ApiOperation(value="获取分组树")
    @GetMapping(value = "/groupTree")
    public ApiResponse<List<TreeNode>> groupTree() {
        UserVo userVo = this.getLoginUser();
        List<TreeNode> treeList = treeNodeService.getGroupTree(userVo);
        return new ApiResponse().ok().setData(treeList);
    }

    @ApiOperation(value="获取资源角色树")
    @GetMapping(value = "/getResourceRoleTree")
    public ApiResponse<List<TreeNode>> getResourceRoleTree() {
        UserVo userVo = this.getLoginUser();
        List<TreeNode> treeList = treeNodeService.getResourceRoleTree(userVo);
        return new ApiResponse().ok().setData(treeList);
    }

    @ApiOperation(value="获取系统角色树")
    @GetMapping(value = "/getSysRoleTree")
    public ApiResponse<List<TreeNode>> getSysRoleTree() {
        UserVo userVo = this.getLoginUser();
        List<TreeNode> treeList = treeNodeService.getSysRoleTree(userVo);
        return new ApiResponse().ok().setData(treeList);
    }



//        //通过不同类型来读取缓存
//        String redisKey = (hasVirtual == null || hasVirtual) ? WebConstant.REDIS_ORG_KEY : WebConstant.REDIS_VIRTUAL_ORG_KEY;
//        List<TreeNodeOrg> orgList;
//        if(redisTemplate.hasKey(redisKey)){
//            orgList = (List<TreeNodeOrg>) redisTemplate.opsForValue().get(WebConstant.REDIS_ORG_KEY);
//        }else{
//            //查询不出来则更新缓存
//            orgList = treeNodeService.getOrganizationTree(userVo, hasVirtual);
//            redisTemplate.opsForValue().set(redisKey, orgList);
//        }

}
