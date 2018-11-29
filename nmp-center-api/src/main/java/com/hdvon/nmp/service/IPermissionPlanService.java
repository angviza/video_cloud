package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.DepartmentUserTreeVo;
import com.hdvon.nmp.vo.PermissionPlanParamVo;
import com.hdvon.nmp.vo.PermissionPlanVo;
import com.hdvon.nmp.vo.ResourceroleVo;
import com.hdvon.nmp.vo.SysroleVo;
import com.hdvon.nmp.vo.UserVo;
import com.hdvon.nmp.vo.WallPlanParamVo;
import com.hdvon.nmp.vo.WallPlanVo;

/**
 * <br>
 * <b>功能：</b>权限预案 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IPermissionPlanService{
	
	 /**
	    * 保存权限预案信息
	    * @param userVo
	    * @param permissionPlanParamVo
	    */
	   void savePermissionPlan(UserVo userVo, PermissionPlanParamVo permissionPlanParamVo);
	   /**
	    * 分页查询权限预案列表
	    * @param pp
	    * @param search
	    * @return
	    */
	   PageInfo<PermissionPlanVo> getPermissionPlanPages(PageParam pp, Map<String,Object> map);
	   /**
	    * 查询权限预案列表
	    * @param search
	    * @return
	    */
	   List<PermissionPlanVo> getPermissionPlanList(Map<String,Object> map);
	   /**
	    * 批量删除权限预案
	    * @param ids
	    */
	   void delPermissionPlansByIds(List<String> ids);
	   /**
	    * 根据id查询单个权限预案信息
	    * @param id
	    * @return
	    */
	   PermissionPlanVo getPermissionPlanById(String id);
	  /**
	 * 保存权限预案与用户的关联
	 * @param permissionPlanId
	 * @param UserIdList
	 */
	void saveRelateUsers(String permissionPlanId, List<String> userIdList);
	  /**
     * 保存权限预案与系统角色的关联
	 * @param permissionPlanId
	 * @param sysroleIdList
	 */
	void saveRelateSysroles(String permissionPlanId, List<String> sysroleIdList);
	/**
     * 保存权限预案与资源角色的关联
	 * @param permissionPlanId
	 * @param resourceroleIdList
	 */
	void saveRelateResourceroles(String permissionPlanId, List<String> resourceroleIdList);
	
	/**
	 * 查询权限预案关联的用户
	 * @param map
	 * @return
	 */
	List<UserVo> getRelateUsers(Map<String,Object> map);
	
	/**
	 * 查询权限预案关联的系统角色
	 * @param map
	 * @return
	 */
	List<SysroleVo> getRelateSysroles(Map<String,Object> map);
	
//	/**
//	 * 查询权限预案关联的资源角色
//	 * @param map
//	 * @return
//	 */
//	List<ResourceroleVo> getRelateResourceroels(Map<String,Object> map);
//
	/**
	 * 根据权限预案id查询所有资源角色中并标记权限预案关联的资源角色
	 * @param permissionPlanId
	 * @return
	 */
	List<ResourceroleVo> selectResourcerolesByPermissionPlanId(UserVo userVo,String permissionPlanId);
	
	/**
	 * 根据权限预案id查询部门树下的所有用户并标记权限预案关联的用户
	 * @param map
	 * @return
	 */
	List<UserVo> getRelateUsersByPermissionPlanId(UserVo userVo,String permissionPlanId);

}
