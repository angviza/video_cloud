package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.TranspondServer;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.TranspondServerParamVo;
import com.hdvon.nmp.vo.TranspondServerVo;

public interface TranspondServerMapper extends Mapper<TranspondServer> , MySqlMapper<TranspondServer>{
	
	TranspondServerVo selectTranspondServerByParam(TranspondServerParamVo transpondServerParamVo);
	
	List<TranspondServerVo> selectTranspondServersList(Map<String,Object> map);

	String selectMaxCodeBycode(Map<String, Object> map);
}