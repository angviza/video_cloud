package com.hdvon.quartz.service;

import java.util.List;
import java.util.Map;

import com.hdvon.quartz.vo.ServicesInfoVo;

/**
 * <br>
 * <b>功能：</b>后台系统服务接口类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IServicesInfoService {
	
	public List<Map<String,Object>> getSystemResourceRates(Map<String, Object> map);
	
	public Long getSystemResourceCount();
	
	public void insertSystemResource(List<ServicesInfoVo> list);
	
	public void updateSystemResource(List<ServicesInfoVo> list);
}
