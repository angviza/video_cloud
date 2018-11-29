package com.hdvon.nmp.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.mapper.PlatformInfoMapper;
import com.hdvon.nmp.service.IPlatformInfoService;

/**
 * <br>
 * <b>功能：</b>Service<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class PlatformInfoServiceImpl implements IPlatformInfoService {

	@Autowired
	private PlatformInfoMapper platformInfoMapper;

	@Override
	public List<Map<String, Object>> getPlatInfoUseList(Map<String, Object> map) {
		return platformInfoMapper.selectPlatInfoUseList(map);
	}

}
