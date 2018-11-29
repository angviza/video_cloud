package com.hdvon.client.mapper;

import java.util.List;

import com.hdvon.client.config.mybatis.MyMapper;
import com.hdvon.client.vo.CameraMapVo;
import com.hdvon.client.vo.CameraVo;
import com.hdvon.client.vo.CheckCameraVo;
import com.hdvon.client.vo.EsCameraVo;
/**
 * 摄像机Mapper类
 * @author wanshaojian
 *
 */
public interface CameraMapper extends MyMapper<EsCameraVo>{
	/**
	 * 获取所有摄像机信息
	 * @return
	 */
	List<EsCameraVo>  findAllCamera();	
	
	
	/**
	 * 根据设备id获取设备的详细信息
	 * @param deviceId
	 * @return
	 */
	List<CameraVo> findCameraRecord(String deviceId);
	/**
	 * 根据设备id查询设备信息
	 * @param deviceId
	 * @return
	 */
	List<EsCameraVo> findCameraByDevId(CameraMapVo form);

	/**
	 * 获取用户的摄像机权限列表
	 * @param userId
	 * @return
	 */
	List<CheckCameraVo> checkUserCameraRights(String userId);
	

}
