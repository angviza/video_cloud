package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.DevicecodeDictionary;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.DevicecodeDictionaryVo;

public interface DevicecodeDictionaryMapper extends Mapper<DevicecodeDictionary> , MySqlMapper<DevicecodeDictionary>{

	List<DevicecodeDictionaryVo> selectByParam(Map<String, Object> param);

	List<DevicecodeDictionaryVo> selectAddrList(List<String> params);
}