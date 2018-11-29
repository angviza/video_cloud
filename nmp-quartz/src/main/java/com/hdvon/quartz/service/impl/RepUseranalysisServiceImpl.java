package com.hdvon.quartz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.quartz.mapper.RepUseranalysisMapper;
import com.hdvon.quartz.service.IRepUseranalysisService;

/**
 * <br>
 * <b>功能：</b>用户操作行为统计表Service<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class RepUseranalysisServiceImpl implements IRepUseranalysisService {

	@Autowired
	private RepUseranalysisMapper repUseranalysisMapper;

}
