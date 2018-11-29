package com.hdvon.client.mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.client.config.mybatis.MyMapper;
import com.hdvon.client.vo.CameraMapVo;
import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.client.vo.CameraPermissionVo;
import com.hdvon.client.vo.EsCameraPermissionVo;
/**
 * 用户摄像机Mapper
 * @author wanshaojian
 *
 */
public interface CameraPermissionMapper extends MyMapper<CameraPermissionVo> {

	
	
	/**
	 * 获取根据设备id获取摄像机资源权限
	 * @param map
	 * @return
	 */
	List<EsCameraPermissionVo> findCameraPermissionByMap(CameraMapVo entity);
	

	
	/**
	 * 根据预案id获取摄像机资源权限
	 * @param planId
	 * @return
	 */
	List<EsCameraPermissionVo> findCameraPermissionByPlan(CameraMapVo entity);
	/**
	 * 获取当前角色的拥有的摄像机资源
	 * @param modelMap
	 * @return
	 */
	List<CameraPermissionVo> selectRoleCameraPermission(Map<String,Object> modelMap);
	
	/**
	 * 获取热点摄像机列表
	 * @param pageSize
	 * @return
	 */
	List<CameraPermissionVo> getHotsCamera(int pageSize);
	
	/**
	 * 获取用户收藏的摄像机列表
	 * @param userName
	 * @return
	 */
	List<CameraPermissionVo> getCollectCameraList(String userName);
	
	
	/**
	 * 根据预案id获取相关的用户id集合
	 * @param planId
	 * @return
	 */
	List<String> findPlanUserListByPlan(String planId);
	
	/**
	 * 获取所有授权用户列表
	 * @return
	 */
	List<CameraMappingMsg> findAllPermisllionUserList();

}



