package com.hdvon.nmp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.mapper.AlarmserverMappingMapper;
import com.hdvon.nmp.service.IAlarmserverMappingService;

/**
 * <br>
 * <b>功能：</b>报警设备关联表Service<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class AlarmserverMappingServiceImpl implements IAlarmserverMappingService {

	@Autowired
	private AlarmserverMappingMapper alarmserverMappingMapper;

}
