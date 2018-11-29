package com.hdvon.nmp.mapper;

import com.hdvon.nmp.vo.CameragrouopVo;

import tk.mybatis.mapper.common.Mapper;

import com.hdvon.nmp.entity.Cameragrouop;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

import java.util.List;
import java.util.Map;

public interface CameragrouopMapper extends Mapper<Cameragrouop> , MySqlMapper<Cameragrouop>{

    List<CameragrouopVo> selectByParam(Map<String, Object> param);

	String selectMaxCode(Map<String, String> param);

	List<String> selectCameraIdByGroupId(String cameargroupId);
}