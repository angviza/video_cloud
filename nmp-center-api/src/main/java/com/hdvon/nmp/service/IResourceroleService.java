package com.hdvon.nmp.service;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.DepartmentUserTreeVo;
import com.hdvon.nmp.vo.ResourceroleVo;
import com.hdvon.nmp.vo.SysroleVo;
import com.hdvon.nmp.vo.UserVo;

import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>资源角色表 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IResourceroleService{

    /**
     * 按分页查询资源角色列表
     * @param resourceroleVo
     * @param pageParam
     * @return
     */
    public PageInfo<ResourceroleVo> getResRoleListByPage(ResourceroleVo resourceroleVo , PageParam pageParam);


    /**
     * 根据id查询资源角色
     * @param id
     * @return
     */
    public ResourceroleVo getResRoleById(String id);

    /**
     * 保存资源角色
     * @param userVo 用户vo
     * @param resourceroleVo 角色vo
     * @return
     */
    void saveResRole(UserVo userVo, ResourceroleVo resourceroleVo) ;

    /**
     * 保存资源角色与摄像头权限关联
     * @param resourceRoleId 资源角色id
     * @param permissionContent 权限json树结构
     * @return
     */
    void saveResPermission(String resourceRoleId, String permissionContent);

    /**
     * 批量删除角色
     * @param ids
     * @return
     */
    void deleteResRole(UserVo userVo, List<String> ids);

    /**
     * 给资源角色分配可管理的用户
     * @param userIds
     */
    void saveUsersToResourceroleId(UserVo userVo, String resourceroleId, List<String> userIds);


    /**
     * 根据资源角色ids 查询出对应的用户ids
     * @param param
     * @return
     */
	public List<String> getUserIdsByParam(Map<String, Object> param);

	/**
	 * 
	 * 根据条件查找资源角色用户
	 * @param param
	 * @return
	 */

	public List<ResourceroleVo> getResourceByParam(Map<String, Object> param);
}
