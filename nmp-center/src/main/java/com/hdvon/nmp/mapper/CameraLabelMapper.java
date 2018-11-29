package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.CameraLabel;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.CameraLabelVo;

public interface CameraLabelMapper extends Mapper<CameraLabel> , MySqlMapper<CameraLabel>{

	List<CameraLabelVo> selectCameraLabel(Map<String, Object> map);

}