package com.hdvon.client.mapper;

import java.util.List;

import com.hdvon.client.entity.PermissionPlan;
import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.client.vo.EsCameraPermissionVo;
/**
 * 摄像机预授权mapper接口
 * @author wanshaojian
 *
 */
public interface PermissionPlanMapper {
	/**
	 * 根据权限预案id获取预案信息
	 * @param planId
	 * @return
	 */
	List<PermissionPlan> findRecord(String planId);
	/**
	 * 获取当前预授权的用户集合【普通用户】
	 * @return
	 */
	List<String> getPermissionPlanUserList();
	
	
	/**
	 * 获取当前预授权的摄像机集合【普通用户】
	 * @return
	 */
	List<String> getPermissionPlanCameraList();
	
	/**
	 * 获取当前预授权的白名单摄像机集合
	 * @return
	 */
	List<EsCameraPermissionVo> getPermissionPlanWhiteCameraList();
	
	
	/**
	 * 获取当前预授权的黑名单摄像机集合
	 * @param userId 用户标识
	 * @return
	 */
	List<Long> getPermissionPlanBlackCameraList(Long userId);
	
	
	/**
	 * 获取权限预案id集合
	 * @return
	 */
	List<CameraMappingMsg> getAllPermissionPlanList();
	
	/**
	 * 根据资源角色获取关联的新增授权预案
	 * @param relationId
	 * @return
	 */
	List<String> selectPlanIdByResId(String resId);
	
	/**
	 * 根据新增授权预案id找出关联的用户
	 * @param planId
	 * @return
	 */
	List<String> selectUserIdByPlan(String planId);
}
