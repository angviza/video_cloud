package com.hdvon.nmp.mapper;

import com.hdvon.nmp.entity.GatewayserverMapping;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.GatewayserverMappingVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface GatewayserverMappingMapper extends Mapper<GatewayserverMapping> , MySqlMapper<GatewayserverMapping>{

	List<GatewayserverMappingVo> selectByParam(Map<String,Object> map);
	
	List<GatewayserverMappingVo> selectProjectByGatewayserverIds(@Param("gatewayserverIds")List<String> gatewayserverIds);

	int selectGatewayServerProject(Map<String, Object> map);
}