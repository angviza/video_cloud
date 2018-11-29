package com.hdvon.nmp.service;

import com.hdvon.nmp.vo.StoreserverCameraVo;

/**
 * <br>
 * <b>功能：</b>存储服务器关联摄像机 服务类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IStoreserverCameraService{
	
	StoreserverCameraVo getStoreServerCameraById(String storeserverCameraId);
}
