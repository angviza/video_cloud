package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;
import com.hdvon.nmp.entity.ServicesInfo;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.ServicesInfoVo;

public interface ServicesInfoMapper extends Mapper<ServicesInfo> , MySqlMapper<ServicesInfo> {
	
	public List<ServicesInfoVo> selectRealtimeServicesInfoList(Map<String, Object> param);
	
	public List<Map<String, Object>> selectSystemResourceRates(Map<String, Object> map);
}