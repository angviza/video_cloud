package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.PlatformInfo;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

public interface PlatformInfoMapper extends Mapper<PlatformInfo> , MySqlMapper<PlatformInfo> {
	
	public List<Map<String, Object>> selectPlatInfoUseList(Map<String, Object> map);
}