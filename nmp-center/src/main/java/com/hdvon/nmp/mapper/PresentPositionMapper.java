package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hdvon.nmp.entity.PresentPosition;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.PresentPositionVo;

public interface PresentPositionMapper extends Mapper<PresentPosition> , MySqlMapper<PresentPosition>{

	List<PresentPositionVo> selectPresentsInBallPlan(Map<String,Object> map);
	
	List<PresentPositionVo> selectPresentPositionsInPlan(Map<String,Object> map);
	
	List<PresentPositionVo> selectBallPresentsInCamera(Map<String,Object> map);

	List<PresentPositionVo> selectByParam(Map<String, Object> map);
	
	List<PresentPositionVo> selectAlarmPresentsInCamera(Map<String, Object> map);
	
	List<PresentPositionVo> selectPresentPositionsInCameras(Map<String, Object> map);
	
	List<PresentPositionVo> selectPresentPositionsByCamera(Map<String, Object> map);

	String selectMaxPresetNum(String deviceCode);

	List<PresentPositionVo> selectPresetList(Map<String, Object> param);
	
	String selectMaxPresetNo(String cameraId);
	
	Integer selectMaxSort(String cameraId);
	
	List<String> selectPresentNosByCamera(@Param("cameraId")String cameraId);
}