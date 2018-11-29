package com.hdvon.nmp.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.mapper.ServicesInfoMapper;
import com.hdvon.nmp.service.IServicesInfoService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.ServicesInfoVo;

/**
 * <br>
 * <b>功能：</b>Service<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class ServicesInfoServiceImpl implements IServicesInfoService {

	@Autowired
	private ServicesInfoMapper servicesInfoMapper;

	@Override
	public PageInfo<ServicesInfoVo> getRealtimeServicesInfoPage(Map<String, Object> map, PageParam pageParam) {
		
		PageHelper.startPage(pageParam.getPageNo(), pageParam.getPageSize());
		List<ServicesInfoVo> list = servicesInfoMapper.selectRealtimeServicesInfoList(map);
		
		return new PageInfo<>(list);
	}

	@Override
	public List<Map<String, Object>> getSystemResourceRates(Map<String, Object> map) {
		
		return servicesInfoMapper.selectSystemResourceRates(map);
	}

}
