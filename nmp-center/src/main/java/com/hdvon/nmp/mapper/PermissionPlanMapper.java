package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.PermissionPlan;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.PermissionPlanVo;

public interface PermissionPlanMapper extends Mapper<PermissionPlan> , MySqlMapper<PermissionPlan>{

	List<PermissionPlanVo> selectByParam(Map<String, Object> map);

}