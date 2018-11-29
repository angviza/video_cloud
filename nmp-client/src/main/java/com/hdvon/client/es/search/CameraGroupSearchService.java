package com.hdvon.client.es.search;

import java.util.List;

import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.client.vo.CameraMsg;
import com.hdvon.client.vo.EsCameraGroupVo;

/**
 * CameraGroup索引接口类
 * @author wanshaojian
 *
 */
public interface CameraGroupSearchService {
	/**
	 * 根据用户变更摄像机信息更新索引库
	 * @param deviceIds
	 */
	void updateByDevId(CameraMsg msg);
	
	
	/**
	 * 根据设备分组更新索引库
	 * @param model 消息对象
	 */
	List<EsCameraGroupVo>  updateByGroupId(CameraMappingMsg model);
	
	
	/**
	 * 同步所有摄像机分组信息
	 */
	void syncAllCamera();
}
