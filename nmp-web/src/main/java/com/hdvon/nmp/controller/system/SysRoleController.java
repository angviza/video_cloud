package com.hdvon.nmp.controller.system;


import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.aop.ControllerLog;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.dto.UserDto;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.service.ISysmenuService;
import com.hdvon.nmp.service.ISysroleService;
import com.hdvon.nmp.service.IUserService;
import com.hdvon.nmp.util.ApiResponse;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.ResponseCode;
import com.hdvon.nmp.vo.SysmenuVo;
import com.hdvon.nmp.vo.SysroleVo;
import com.hdvon.nmp.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value="/sysroles",tags="系统角色管理模块",description="管理系统角色")
@RestController
@RequestMapping("/sysroles")
@Slf4j
public class SysRoleController extends BaseController{
    @Reference
    private ISysroleService sysroleService;
    @Reference
    private ISysmenuService sysmenuService;
    @Reference
    private IUserService userService;
 
    @ApiOperation(value="获取单个系统角色信息")
    @ApiImplicitParam(name = "id", value = "系统角色id", required = true)
    @GetMapping(value = "/{id}")
    public ApiResponse<SysroleVo> getRoleById(@PathVariable String id) {
        if(StrUtil.isBlank(id)){
            return new ApiResponse().error("id参数不允许为空");
        }
        SysroleVo sysroleVo = sysroleService.getSysRoleById(id);
        if(sysroleVo == null){
            return new ApiResponse().error("该角色信息不存在");
        }
        return new ApiResponse().ok().setData(sysroleVo);
    }

    @ApiOperation(value="分页获取系统角色子节点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "模糊条件", required = false),
            @ApiImplicitParam(name = "id", value = "系统角色id", required = false),
    })
    @GetMapping(value = "/page")
    public ApiResponse<PageInfo<SysroleVo>> getRoleChildrens(SysroleVo sysroleVo, PageParam pageParam) {
        PageInfo<SysroleVo> page = sysroleService.getSysRoleListByPage(sysroleVo , pageParam);
        return new ApiResponse<PageInfo<SysroleVo>>().ok().setData(page);
    }

    @ControllerLog
    @ApiOperation(value="保存系统角色")
    @PostMapping(value = "/")
    public ApiResponse saveRole(SysroleVo sysrole) {
        UserVo userVo = getLoginUser();
        if(StrUtil.isBlank(sysrole.getName())){
            return new ApiResponse().error("系统角色名称不允许为空");
        }
        if(StrUtil.isNotBlank(sysrole.getId()) && sysrole.getPid().equals(sysrole.getId())) {
        	return new ApiResponse().error("系统角色的上级角色不能是自身！");
        }
        sysroleService.saveSysrole(userVo,sysrole);
        return new ApiResponse().ok("保存系统角色成功");
    }

    @ControllerLog
    @ApiOperation(value="保存系统角色菜单授权")
    @PostMapping(value = "/menu")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "系统角色id", required = true),
            @ApiImplicitParam(name = "menuIds", value = "菜单id列表", required = true)
    })
    public ApiResponse saveMenu(String roleId , @RequestParam(value="menuIds")String menuIds) {
        if(StrUtil.isEmpty(roleId)){
            throw new ServiceException("缺少系统角色id");
        }
        List<String> menuIdList = null;
        if(StrUtil.isNotEmpty(menuIds)){
            menuIdList = Arrays.asList(menuIds.split(","));
        }

        sysroleService.saveSysroleMenu(roleId,menuIdList);
        return new ApiResponse().ok("保存成功");
    }


    @ApiOperation(value="批量删除系统角色")
    @DeleteMapping(value = "/")
    @ApiImplicitParam(name = "roleIds[]", value = "系统角色id数组", required = true)
    public ApiResponse deleteRole(@RequestParam(value="roleIds[]") String[] roleIds) {
        List<String> roleIdList = Arrays.asList(roleIds);
        sysroleService.deleteSysrole(getLoginUser() , roleIdList);
        return new ApiResponse().ok("删除角色成功");
    }


    @ApiOperation(value="查询系统角色授权菜单")
    @ApiImplicitParam(name = "roleId", value = "系统角色id", required = true)
    @GetMapping(value = "/menu")
    public ApiResponse<List<SysmenuVo>> getMenuByRoleId(String roleId ){
    	Map<String ,Object> param = new HashMap<String ,Object>();
    	param.put("roleId", roleId);
    	param.put("rePid", "2022");// 不显示公共权限菜单
        List<SysmenuVo> list = sysmenuService.getMenuVoByRoleId(param);
        return new ApiResponse().ok().setData(list);
    }


    @ApiOperation(value="查询系统角色关联用户列表")
    @ApiImplicitParam(name = "sysroleId", value = "系统角色id", required = true)
    @GetMapping(value = "/user")
    public ApiResponse<PageInfo<UserVo>> getUserByRoleId(PageParam pp, String sysroleId ){
        if(StrUtil.isBlank(sysroleId)){
            return new ApiResponse().error("系统角色id不能为空");
        }
        UserDto userDto = new UserDto();
        userDto.setSysroleId(sysroleId);
        PageInfo<UserVo> page = userService.getUsersByPage(pp, null, userDto);
        return new ApiResponse().ok().setData(page);
    }
    @ApiOperation(value="系统角色授权界面初始化查询")
    @GetMapping(value = "/getRelatedDeptUser")
    public ApiResponse<List<UserVo>> getRelatedDeptUserTree(String sysroleId) {
		ApiResponse<List<UserVo>> resp = new ApiResponse<List<UserVo>>();
        UserDto userDto = new UserDto();
        userDto.setSysroleId(sysroleId);
        List<UserVo> userVos = userService.getUsersByParam(userDto);
        return resp.ok().setData(userVos);
    }
    
    @ControllerLog
    @ApiOperation(value="保存系统角色可管理的用户关联")
    @PostMapping(value = "/saveSysroleRelateUsers")
    public ApiResponse<Object> saveSysroleRelateUsers(String sysroleId, String userIds) {
		UserVo userVo = getLoginUser();
        List<String> userIdList = null;
		if(StrUtil.isNotEmpty(userIds)){
            String[] userIdArr = userIds.split(",");
            userIdList = Arrays.asList(userIdArr);
        }
        sysroleService.relateUsersToSysroleId(userVo,sysroleId,userIdList);
        return new ApiResponse<Object>().ok("保存成功");
    }

    @ControllerLog
    @ApiOperation(value="根据当前登录用户获取的系统角色用户树")
    @GetMapping(value = "/getUserAuthorizeSysRoleTree")
    public ApiResponse<List<SysroleVo>> getUserAuthorizeSysRoleTree() {
		UserVo userVo = getLoginUser();
		ApiResponse<List<SysroleVo>> resp = new ApiResponse<List<SysroleVo>>();
		if(userVo != null) {
			try {
				List<SysroleVo> list = sysroleService.getUserAuthorizeSysRoleTree(userVo.getId());
		        resp.ok(ResponseCode.SUCCESS.getMessage()).setData(list);
			} catch (Exception e) {
				// TODO: handle exception
				log.debug("根据当前登录用户获取的系统角色用户树失败:"+e.getMessage());
				resp.error(ResponseCode.FAILURE.getMessage());
			}
		}
		return resp;
    }
}
