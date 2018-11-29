package com.hdvon.nmp.service;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.dto.UserDto;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.*;

import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>用户表 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IUserService{


	/**
	 * 保存添加或者编辑的用户信息
	 * @param loginUser	 当前登录用户的信息
	 * @param userParamVo	当前保存的用户信息
	 */
	void saveUser(UserVo loginUser, UserParamVo userParamVo);

    /**
     * 更新用户状态
     * @param userVo
     */
    void updateUserStatus(UserVo loginUser , UserVo userVo);

    /**
     * 删除用户信息
     * @param loginUser 登录用户
     * @param ids 需要删除的用户id集合
     */
    void delUsers(UserVo loginUser , List<String> ids);

	/**
	 * 根据用户id查询用户信息
	 * @param id	用户id
	 * @return
	 */
	UserVo getUserInfoById(String id);

    List<UserVo> getUsersByParam(UserDto userDto);

    /**
     * 分页查询子部门用户列表
     * @param pp 分页
     * @param userDto
     * @return
     */
    PageInfo<UserVo> getUsersByPage(PageParam pp, TreeNodeChildren treeNodeChildren, UserDto userDto);

	/**
	 * 修改用户密码
	 * @param id
     * @param password
	 */
	void updateUserPassword(String id , String password ) ;

	/**
	 * 授予资源角色
	 */
	void grantResourceRole(UserVo uservo,List<String> userIds, List<String> resourceRoles);

	/**
	 * 授予系统角色
	 */
	void grantSysRole(UserVo uservo,List<String> userIds, List<String> sysroleVos);

	/*
	 * 查询授予用户的所有资源角色
	 * @param userIds
	 * @return
	 */
	List<ResourceroleVo> getResourceRolesByUserId(List<String> userIds);

	/**
	 * 查询授予用户的所有系统菜单角色
	 * @return
	 */
	List<SysroleVo> getSysrolesByUserId(List<String> userIds);

	/**
	 * 根据用户账号查询用户信息
	 * @param account
	 * @return
	 */
	List<UserVo> getUserByAccount(String account);


    /**
     * 根据账号密码查询账号信息
     * @param username
     * @param password
     * @return
     */
    UserVo login(String username , String password);

    /**
     * 查找出单前用户的资源权限
     * @param userId
     * @return
     */
	List<UserAddrCameraPermissionVo> getAddrCameraPermission(String userId);
	
	/**
	 * 查询自定义菜单列表
	 * @param uid
	 * @return
	 */
	List<SysmenuVo> optionMenu(String uid);
	
	void departmentToUser(UserVo uservo,List<String> userIdList, String[] departmentIds);

	/**
	 * 用户可管理部门树
	 * @param userId
	 * @return
	 */
	List<DepartmentVo> getDepartmentUser(String userId);
	/**
	 * 用户锁屏
	 * @param userId 用户id
	 * @param token token
	 * @return
	 */
	boolean lockScreen(String userId,String token);
	/**
	 * 用户解锁
	 * @param userId 用户id
	 * @return
	 */
	boolean unLockScreen(String userId);
	

	/**
	 * 设置用户验证方式
	 * @param type
	 * @param user
	 */
	void saveUserValidType(Integer type,UserVo user);
     /**
      * 查看僵尸用户	
      * @param param
      * @return
      */

	PageInfo<UserVo> getStaticUser(Map<String, Object> param,PageParam pageParam);
	
	/**
	 * 导出僵死用户
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> exportStaticUser(Map<String, Object> param);
	

	/**
	 * 导出部门用户
	 * @param param
	 * @return
	 */
	List<Map<String,Object>> exportDepartmentUser(Map<String, Object> param);

	/**
	 * 导出角色用户
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> exportRoleUser(Map<String, Object> param);

	/**
	 * 根据资源角色ID查找用户
	 * @param resouceroleId
	 * @return
	 */
	List<UserVo> getUsersByResourceroleId(String resouceroleId);

	/**
	 * 根据条件查询部门下用户
	 * @param pageParam
	 * @param param
	 * @return
	 */
	PageInfo<UserVo> getUsersByDepart(PageParam pageParam, Map<String, Object> param);

	public UserVo getUserById(String id);

	void updateByParam(UserVo userVo);
	
}
