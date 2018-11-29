package com.hdvon.nmp.mapper;

import com.hdvon.nmp.entity.TranspondserverMapping;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.TranspondserverMappingVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface TranspondserverMappingMapper extends Mapper<TranspondserverMapping> , MySqlMapper<TranspondserverMapping>{

	List<TranspondserverMappingVo> selectByParam(Map<String, Object> map);
	
	List<TranspondserverMappingVo> selectProjectByTransserverIds(@Param("transserverIds")List<String> transserverIds);

	int selectTranServerProject(Map<String, Object> map);

}