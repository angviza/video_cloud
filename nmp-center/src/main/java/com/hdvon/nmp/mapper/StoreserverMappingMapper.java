package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hdvon.nmp.entity.StoreserverMapping;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.StoreserverMappingVo;

public interface StoreserverMappingMapper extends Mapper<StoreserverMapping> , MySqlMapper<StoreserverMapping>{

	List<StoreserverMappingVo> selectByParam(Map<String, Object> map);

	List<StoreserverMappingVo> selectProjectByStoreserverIds(@Param("storeserverIds")List<String> storeserverIds);

	int selectStoreServerProject(Map<String, Object> map);
}