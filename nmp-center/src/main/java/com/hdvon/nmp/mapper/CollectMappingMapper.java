package com.hdvon.nmp.mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.CollectMapping;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.CollectMappingVo;

import tk.mybatis.mapper.common.Mapper;

public interface CollectMappingMapper extends Mapper<CollectMapping> , MySqlMapper<CollectMapping>{

	List<CollectMappingVo> getListBySub(Map<String, String> params);
}