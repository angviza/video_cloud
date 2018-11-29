package com.hdvon.quartz.service;

import java.util.List;
import java.util.Map;

import com.hdvon.quartz.entity.RepTiggertask;

/**
 * <br>
 * <b>功能：</b>定时任务配置表 服务类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface ITiggerTaskService{

	List<RepTiggertask> gettiggerTask(Map<String, Object> task);

	void saveTiggerTask(RepTiggertask task);

}
