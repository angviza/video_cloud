package com.hdvon.client.es.search;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;

import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.client.vo.CameraMsg;
import com.hdvon.client.vo.EsCameraPermissionVo;

/**
 * UserCamera索引接口类
 * @author wanshaojian
 *
 */
public interface UserCameraSearchService {

	
	/**
	 * 根据用户变更摄像机信息更新索引库
	 * @param deviceIds
	 */
	void updateByDevId(CameraMsg msg);
	
	
	/**
	 * 根据资源角色关联用户更新索引库
	 * @param model 消息对象
	 */
	List<EsCameraPermissionVo> updateByUserId(CameraMappingMsg model);
	
	
	/**
	 * 根据权限预案对用户授权更新索引库
	 * @param model 消息对象
	 */
	List<EsCameraPermissionVo> updateByPlanId(CameraMappingMsg model);
	
	void delete(String index, String mapping,BoolQueryBuilder delQuery);


	/**
	 * 根据角色ID查找出角色关联的预案
	 * @param model
	 * @return
	 */
	List<EsCameraPermissionVo> updatePlanByResId(CameraMappingMsg model);
}
