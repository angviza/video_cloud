package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.GatewayServer;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.GatewayServerParamVo;
import com.hdvon.nmp.vo.GatewayServerVo;

public interface GatewayServerMapper extends Mapper<GatewayServer> , MySqlMapper<GatewayServer>{
	
	GatewayServerVo selectGatewayServerByParam(Map<String,Object> map);
	
	List<GatewayServerVo> selectGatewayServersList(Map<String,Object> map);

	String selectMaxCodeBycode(Map<String, Object> map);
}