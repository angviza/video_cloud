package com.hdvon.client.service;

import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.client.vo.CameraMsg;

public interface IUserResources {
	
	/**
	 * 跟新用户摄像机权限同步到es
	 * @param msg
	 */
	void sendSyncDeviceMappingPermission(CameraMappingMsg msg) ;
	
	/**
	 * 更新设备发送同步消息接口
	 * @param msg
	 * 
	 */
	void sendSyncDevicePermission(CameraMsg msg);
	

}
