package com.hdvon.quartz.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.quartz.entity.RepTiggertask;
import com.hdvon.quartz.mapper.RepTiggertaskMapper;
import com.hdvon.quartz.service.IRepTiggertaskService;
import com.hdvon.quartz.util.IdGenerator;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>定时任务配置表Service<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class RepTiggertaskServiceImpl implements IRepTiggertaskService {

	@Autowired
	private RepTiggertaskMapper repTiggertaskMapper;

	@Override
	public List<RepTiggertask> gettiggerTask(Map<String, Object> task) {
		Example example = new Example(RepTiggertask.class);
        example.createCriteria().andEqualTo("type",task.get("type"));
        List<RepTiggertask> list = repTiggertaskMapper.selectByExample(example);
		return list;
	}

	
	@Override
	public void saveRepTiggertask(RepTiggertask task) {
		if(StrUtil.isBlank(task.getId())) {
			task.setId(IdGenerator.nextId());
			repTiggertaskMapper.insert(task);
		}else {
			repTiggertaskMapper.updateByPrimaryKey(task);
		}
		
	}
}
