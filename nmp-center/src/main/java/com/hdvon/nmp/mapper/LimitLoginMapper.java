package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.LimitLogin;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.LimitLoginVo;

public interface LimitLoginMapper extends Mapper<LimitLogin> , MySqlMapper<LimitLogin>{
	
	List<LimitLoginVo> selectByParam(Map<String, Object> param);

}