package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hdvon.nmp.entity.WallPlan;
import com.hdvon.nmp.vo.WallPlanVo;

import com.hdvon.nmp.mybatisExt.MySqlMapper;

public interface WallPlanMapper extends Mapper<WallPlan> , MySqlMapper<WallPlan>{

	List<WallPlanVo> selectWallPlanList(Map<String,Object> map);
	
	List<WallPlanVo> selectWallPlanByPresents(@Param("presentIds")List<String> presentIds);
}