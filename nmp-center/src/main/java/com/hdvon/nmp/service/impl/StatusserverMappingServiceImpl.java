package com.hdvon.nmp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.mapper.StatusserverMappingMapper;
import com.hdvon.nmp.service.IStatusserverMappingService;

/**
 * <br>
 * <b>功能：</b>状态服务器关联项目表Service<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class StatusserverMappingServiceImpl implements IStatusserverMappingService {

	@Autowired
	private StatusserverMappingMapper statusserverMappingMapper;

}
