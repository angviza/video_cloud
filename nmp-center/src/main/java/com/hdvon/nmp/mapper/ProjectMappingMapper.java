package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hdvon.nmp.entity.ProjectMapping;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.ProjectMappingVo;

public interface ProjectMappingMapper extends Mapper<ProjectMapping> , MySqlMapper<ProjectMapping>{

	List<ProjectMappingVo> selectByParam(Map<String, Object> map);
	
	List<ProjectMappingVo> selectProjectBySigserverIds(@Param("sigserverIds")List<String> sigserverIds);

	int selectSigServerProject(Map<String, Object> map);
	
}