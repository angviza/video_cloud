package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hdvon.nmp.entity.StatusserverMapping;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.StatusserverMappingVo;

public interface StatusserverMappingMapper extends Mapper<StatusserverMapping> , MySqlMapper<StatusserverMapping>{

	List<StatusserverMappingVo> selectByParam(Map<String, Object> map);

	List<StatusserverMappingVo> selectProjectByStatusserverIds(@Param("statusserverIds")List<String> statusserverIds);

	int selectStatusServerProject(Map<String, Object> map);
}