package com.hdvon.nmp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.entity.StoreserverCamera;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.StoreserverCameraMapper;
import com.hdvon.nmp.service.IStoreserverCameraService;
import com.hdvon.nmp.vo.StoreserverCameraVo;

import cn.hutool.core.convert.Convert;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>存储服务器关联摄像机Service<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class StoreserverCameraServiceImpl implements IStoreserverCameraService {

	@Autowired
	private StoreserverCameraMapper storeserverCameraMapper;

	@Override
	public StoreserverCameraVo getStoreServerCameraById(String storeserverCameraId) {
		StoreserverCamera storeserverCamera = storeserverCameraMapper.selectByPrimaryKey(storeserverCameraId);
		if(storeserverCamera == null) {
			throw new ServiceException("存储服务器不存在该关联的摄像机！");
		}
		StoreserverCameraVo storeserverCameraVo = Convert.convert(StoreserverCameraVo.class, storeserverCamera);
		return storeserverCameraVo;
	}

}
