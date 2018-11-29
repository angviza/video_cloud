package com.hdvon.nmp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.mapper.CameraMappingMapper;
import com.hdvon.nmp.service.ICameraMappingService;

/**
 * <br>
 * <b>功能：</b>摄像机关联中间表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class CameraMappingServiceImpl implements ICameraMappingService {

	@Autowired
	private CameraMappingMapper cameraMappingMapper;

}
