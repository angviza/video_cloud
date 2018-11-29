package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hdvon.nmp.entity.CameraCameragroup;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.CameraCameragroupVo;


public interface CameraCameragroupMapper extends Mapper<CameraCameragroup> , MySqlMapper<CameraCameragroup>{

	List<CameraCameragroupVo> selectCamerasByGroupIds(@Param("groupIds")List<String> groupIds);

}