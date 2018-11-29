package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hdvon.nmp.entity.PollingplanCamera;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.PollingplanCameraVo;

public interface PollingplanCameraMapper extends Mapper<PollingplanCamera> , MySqlMapper<PollingplanCamera>{

	List<PollingplanCameraVo> selectCamerasByPollingplanIds(@Param("pollingplanIds") List<String> pollingplanIds);
	
	List<PollingplanCameraVo> selectPlanRelatedCameras(Map<String,Object> map);
}