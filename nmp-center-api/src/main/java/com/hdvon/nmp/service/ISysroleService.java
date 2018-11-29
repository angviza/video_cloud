package com.hdvon.nmp.service;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.DepartmentUserTreeVo;
import com.hdvon.nmp.vo.SysroleVo;
import com.hdvon.nmp.vo.UserVo;

import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>系统角色表 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface ISysroleService{

    /**
     * 按分页查询角色列表
     * @param sysroleVo
     * @param pageParam
     * @return
     */
    public PageInfo<SysroleVo> getSysRoleListByPage(SysroleVo sysroleVo , PageParam pageParam);


    /**
     * 根据id查询角色信息
     * @param id
     * @return
     */
    public SysroleVo getSysRoleById(String id);

    /**
     * 保存角色
     * @param userVo 用户vo
     * @param sysroleVo 角色vo
     * @return
     */
    void saveSysrole(UserVo userVo, SysroleVo sysroleVo);

    /**
     * 保存角色菜单关联
     * @param roleId 角色id
     * @param menuIdList 菜单列表
     * @return
     */
    void saveSysroleMenu(String roleId, List<String> menuIdList);

    /**
     * 批量删除角色
     * @param ids
     * @return
     */
    void deleteSysrole(UserVo userVo,List<String> ids);
    
    /**
     * 查询可管理部门用户树，并标记关联单个系统角色的用户
     * @param map
     * @return
     */
    List<DepartmentUserTreeVo> getRelatedDeptUserTree(Map<String,Object> map);
    
    /**
     * 给系统角色分配可管理的用户
     * @param sysroleId
     * @param userIds
     */
    void relateUsersToSysroleId(UserVo userVo, String sysroleId, List<String> userIds);
    
    /**
     * 根据当前登录用户获取的系统角色用户树
     * @param userId
     */
    List<SysroleVo> getUserAuthorizeSysRoleTree(String userId);

    /**
     * 角色用户数量
     * @param param
     * @param pageParam
     * @return
     */
	public PageInfo<SysroleVo> getRoleUserCount(Map<String, Object> param, PageParam pageParam);
}
