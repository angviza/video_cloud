package com.hdvon.client.es.search;

import com.hdvon.client.vo.CameraMsg;

/**
 * Camera索引接口类
 * @author wanshaojian
 *
 */
public interface CameraSearchService {
	/**
	 * 根据用户变更摄像机信息更新索引库
	 * @param deviceIds
	 */
	void updateByDevId(CameraMsg msg);
	
	/**
	 * 同步所有摄像机信息
	 */
	void syncAllCamera();
}
