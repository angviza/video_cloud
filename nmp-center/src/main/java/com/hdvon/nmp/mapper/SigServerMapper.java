package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.SigServer;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.SigServerParamVo;
import com.hdvon.nmp.vo.SigServerVo;

public interface SigServerMapper extends Mapper<SigServer> , MySqlMapper<SigServer>{

	SigServerVo selectSigServerByParam(SigServerParamVo sigServerParamVo);
	
	List<SigServerVo> selectSigServersList(Map<String,Object> map);

	String selectMaxCodeBycode(Map<String, Object> map);
}