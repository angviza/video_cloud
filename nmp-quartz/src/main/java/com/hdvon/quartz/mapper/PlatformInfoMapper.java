package com.hdvon.quartz.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.quartz.entity.PlatformInfo;
import com.hdvon.quartz.util.MySqlMapper;

public interface PlatformInfoMapper extends Mapper<PlatformInfo> , MySqlMapper<PlatformInfo> {
	
	public List<Map<String, Object>> selectPlatInfoUseList(Map<String, Object> map);
	
}