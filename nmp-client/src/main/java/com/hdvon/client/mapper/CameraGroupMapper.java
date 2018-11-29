package com.hdvon.client.mapper;

import java.util.List;

import com.hdvon.client.config.mybatis.MyMapper;
import com.hdvon.client.entity.CameraGroup;
import com.hdvon.client.vo.CameraMapVo;
import com.hdvon.client.vo.EsCameraGroupVo;
import com.hdvon.client.vo.EsUserCameraGroupVo;
/**
 * 摄像机分组mapper接口
 * @author wanshaojian
 *
 */
public interface CameraGroupMapper extends MyMapper<CameraGroup>{
	/**
	 * 根据条件查询设备信息
	 * @param entity 包括 groupId 或者 userIds
	 * @return
	 */
	List<EsCameraGroupVo> findCameraGroupListByMap(CameraMapVo entity);
	
		
	/**
	 * 根据条件d获取摄像机分组中的设备信息
	 * @param entity
	 * @return
	 */
	List<EsUserCameraGroupVo> findUserCameraGroupListByMap(CameraMapVo entity);
	
	/**
	 * 根据预案id获取摄像机资源权限
	 * @param planId
	 * @return
	 */
	List<EsUserCameraGroupVo> findUserCameraGroupListByPlan(CameraMapVo entity);
	

	

}
