package com.hdvon.client.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.client.config.rabbitmq.CameraSender;
import com.hdvon.client.exception.CameraException;
import com.hdvon.client.service.CommonService;
import com.hdvon.client.service.ISyncIndexDataService;
import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.client.vo.CameraMsg;
/**
 * <br>
 * <b>功能：</b>ES索引数据同步接口实现<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018-6-15<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class SyncIndexDataServiceImpl implements ISyncIndexDataService{
	
    @Autowired
    private CameraSender cameraSender;
    
    @Autowired
    private CommonService commonService;
    
	/**
	 * 更新设备发送同步消息接口
	 * @param msg
	 * 
	 */
    @Override
	public void sendSyncDevicePermission(CameraMsg msg) throws CameraException{
		cameraSender.sendCamera(msg);
	}
    
	/**
	 * 分配涉嫌相机资源发送同步消息接口
	 * @param msg
	 * 
	 */
    @Override
	public void sendSyncDeviceMappingPermission(CameraMappingMsg msg) throws CameraException{
		cameraSender.sendCameraMapping(msg);
	}

	/**
	 * 生成地址编码
	 * @param pid
	 * @return
	 */
    @Override
	public String genAddressCode(String pid) {
		return commonService.genAddressCode(pid);
	}

	/**
	 * 生成自定义分组编码
	 * @param pid
	 * @return
	 */
    @Override
	public String genCameraGroupCode(String pid) {
		return commonService.genCameraGroupCode(pid);
	}


}
