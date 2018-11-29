package com.hdvon.nmp.mapper;

import com.hdvon.nmp.vo.PlanCommonVo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.PollingPlan;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.PollingPlanVo;

public interface PollingPlanMapper extends Mapper<PollingPlan> , MySqlMapper<PollingPlan>{

	List<PollingPlanVo> selectPollingPlanList(Map<String,Object> map);


    /**
     * 查询所有预案，包括：轮巡预案、球机轮巡、上墙预案
     * @param map
     * @return
     */
    List<PlanCommonVo> selectAllPlanList(Map<String,Object> map);
}