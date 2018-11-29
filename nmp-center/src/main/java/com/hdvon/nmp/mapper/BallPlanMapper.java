package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.BallPlan;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.BallPlanVo;

public interface BallPlanMapper extends Mapper<BallPlan> , MySqlMapper<BallPlan>{
	
	List<BallPlanVo> selectBallPlanList(Map<String,Object> map);
	
	BallPlanVo selectBallPlanInfoById(String id);

}