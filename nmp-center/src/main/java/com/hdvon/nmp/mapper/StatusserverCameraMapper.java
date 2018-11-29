package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hdvon.nmp.entity.StatusserverCamera;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.StatusserverCameraVo;

public interface StatusserverCameraMapper extends Mapper<StatusserverCamera> , MySqlMapper<StatusserverCamera>{

	List<StatusserverCameraVo> selectUserStatusserverCamera(Map<String,Object> map);
}