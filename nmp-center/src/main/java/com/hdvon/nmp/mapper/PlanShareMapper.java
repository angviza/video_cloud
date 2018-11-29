package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.PlanShare;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.PlanShareVo;

public interface PlanShareMapper extends Mapper<PlanShare> , MySqlMapper<PlanShare>{

	List<PlanShareVo> selectByParam(Map<String, Object> param);

}