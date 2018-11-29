package com.hdvon.client.es.search;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;

import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.client.vo.CameraMsg;
import com.hdvon.client.vo.EsCameraPermissionVo;
import com.hdvon.client.vo.EsUserCameraGroupVo;

/**
 * UserCameraGroup索引接口类
 * @author wanshaojian
 *
 */
public interface UserCameraGroupSearchService {
	
	
	/**
	 * 根据用户变更摄像机信息更新索引库
	 * @param deviceIds
	 */
	void updateByDevId(CameraMsg msg);
	
	
	/**
	 * 根据资源角色关联用户更新索引库
	 * @param model 消息对象
	 */
	List<EsUserCameraGroupVo> updateByUserId(CameraMappingMsg model);
	
	/**
	 * 根据设备分组更新索引库
	 * @param model 消息对象
	 */
	List<EsUserCameraGroupVo> updateByGroupId(CameraMappingMsg model);
	/**
	 * 根据权限预案对用户授权更新索引库
	 * @param model 消息对象
	 */
	List<EsUserCameraGroupVo> updateByPlanId(CameraMappingMsg model);

	void delete(String index, String mapping,BoolQueryBuilder delQuery);


	void detePlanByGroup(CameraMappingMsg model);

	/**
	 * 添加已存在的有效预案关联的摄像机
	 * @param model
	 * @return
	 */
	List<EsUserCameraGroupVo> updatePlanByGroup(CameraMappingMsg model);
}
