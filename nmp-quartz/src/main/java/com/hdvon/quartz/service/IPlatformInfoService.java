package com.hdvon.quartz.service;

import java.util.List;
import java.util.Map;

import com.hdvon.quartz.vo.PlatformInfoVo;

/**
 * <br>
 * <b>功能：</b>平台使用情况服务接口类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IPlatformInfoService {
	
	public List<Map<String, Object>> getPlatInfoUseList(Map<String, Object> map);
	
	public void batchInsertList(List<PlatformInfoVo> list);
	
}
