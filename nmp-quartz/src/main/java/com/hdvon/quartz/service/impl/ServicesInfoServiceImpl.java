package com.hdvon.quartz.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.quartz.entity.ServicesInfo;
import com.hdvon.quartz.mapper.ServicesInfoMapper;
import com.hdvon.quartz.service.IServicesInfoService;
import com.hdvon.quartz.vo.ServicesInfoVo;

import cn.hutool.core.convert.Convert;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>后台系统服务实现类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class ServicesInfoServiceImpl implements IServicesInfoService {

	@Autowired
	private ServicesInfoMapper servicesInfoMapper;

	@Override
	public List<Map<String, Object>> getSystemResourceRates(Map<String, Object> map) {
		
		return servicesInfoMapper.selectSystemResourceRates(map);
	}

	@Override
	public Long getSystemResourceCount() {
		
		return servicesInfoMapper.selectSystemResourceCount();
	}

	@Override
	public void insertSystemResource(List<ServicesInfoVo> list) {
		
		List<ServicesInfo> datas = new ArrayList<ServicesInfo>();
		
		for (ServicesInfoVo vo : list) {
			
			ServicesInfo info = Convert.convert(ServicesInfo.class, vo);
			datas.add(info);
		}
		
		servicesInfoMapper.insertList(datas);
		
	}

	@Override
	public void updateSystemResource(List<ServicesInfoVo> list) {
		
		for (ServicesInfoVo vo : list) {
			
			ServicesInfo info = Convert.convert(ServicesInfo.class, vo);
			//info.setUpdateTime(new Date());
			info.setUpdateUser("admin");
			
			Example example = new Example(ServicesInfo.class);
			example.createCriteria().andEqualTo("ipAddress", info.getIpAddress());
			
			servicesInfoMapper.updateByExampleSelective(info, example);
			
		}
		
	}

}
