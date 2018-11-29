package com.hdvon.client.service;

import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.client.vo.CameraMsg;
/**
 * <br>
 * <b>功能：</b>ES索引数据同步接口<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018-6-15<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface ISyncIndexDataService {
	/**
	 * 更新设备发送同步消息接口
	 * @param msg
	 * 
	 */
	void sendSyncDevicePermission(CameraMsg msg);
	
	/**
	 * 对摄像机进行资源权限分配是发送消息
	 * 保护【用户授权   权限预案 自定义分组等】
	 * @param msg
	 * 
	 */
	void sendSyncDeviceMappingPermission(CameraMappingMsg msg) ;
	
	/**
	 * 生成地址编码
	 * @param pid
	 * @return
	 */
	public String genAddressCode(String pid);

	/**
	 * 生成自定义分组编码
	 * @param pid
	 * @return
	 */
	public String genCameraGroupCode(String pid);
}
