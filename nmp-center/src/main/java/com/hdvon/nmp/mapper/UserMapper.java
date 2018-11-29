package com.hdvon.nmp.mapper;

import com.hdvon.nmp.vo.UserAddrCameraPermissionVo;
import com.hdvon.nmp.vo.UserVo;
import tk.mybatis.mapper.common.Mapper;

import com.hdvon.nmp.common.CameraPermissionVo;
import com.hdvon.nmp.entity.User;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface UserMapper extends Mapper<User> , MySqlMapper<User>{

	/**
	* 登录
	 */
	List<UserVo> login(Map<String, Object> param);
    /**
     * 查询所有用户，支持按部门、系统角色、资源角色、可管理部门、用户id等等的查询
     * @param param
     * @return
     */
    List<UserVo> selectUsersByParam(Map<String, Object> param);

    List<UserAddrCameraPermissionVo> selectAddrCameraPermission(String userId);

    List<UserVo> selectUsersInPermissionPlan(Map<String, Object> map);
    
    /**
	 * 当前登录用户的地址树摄像机权限
	 * @param modelMap
	 * @return
	 */
	List<CameraPermissionVo> selectAddressCameraPermission(Map<String,Object> modelMap);

    /**
     * 通知公告
     * @param id
     * @return
     */
	List<UserVo> queryUserByNoticeId(@Param("id") String id);

    /**
     * 查询当前用户可管理的用户id列表(列出可管理部门及当前部门的所有用户id)
     * @param userId 当前用户id
     * @return
     */
	List<String> selectAuthUserIds(@Param("userId") String userId);
   
	/**
	 * 长时间不登录用户
	 * @param param
	 * @return
	 */
	List<UserVo> selectStaticUserByParam(Map<String, Object> param);

	
	List<UserVo> selectDepartmentUserCount(Map<String, Object> param);

	List<Map<String, Object>> selectRoleUserCount(Map<String, Object> param);
}