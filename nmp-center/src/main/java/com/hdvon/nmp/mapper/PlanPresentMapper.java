package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.PlanPresent;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.PlanPresentVo;

public interface PlanPresentMapper extends Mapper<PlanPresent> , MySqlMapper<PlanPresent>{
	
	List<PlanPresentVo> selectWallPlanCameraPresent(Map<String,Object> map);
	
	List<PlanPresentVo> selectPollingPlanCameraPresent(Map<String,Object> map);
}