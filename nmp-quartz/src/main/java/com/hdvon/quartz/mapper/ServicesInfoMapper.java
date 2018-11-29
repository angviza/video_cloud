package com.hdvon.quartz.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.quartz.entity.ServicesInfo;
import com.hdvon.quartz.util.MySqlMapper;
import com.hdvon.quartz.vo.ServicesInfoVo;

public interface ServicesInfoMapper extends Mapper<ServicesInfo> , MySqlMapper<ServicesInfo> {
	
	public List<ServicesInfoVo> selectRealtimeServicesInfoList(Map<String, Object> param);
	
	public List<Map<String, Object>> selectSystemResourceRates(Map<String, Object> map);
	
	public Long selectSystemResourceCount();
	
}