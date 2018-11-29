package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.CameraLog;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.CameraLogVo;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.UserVo;

public interface CameraLogMapper extends Mapper<CameraLog> , MySqlMapper<CameraLog>{

	List<CameraLogVo> seleteByParam(Map<String, String> param);

	List<CameraVo> selectgCameraLogByPage(Map<String, Object> param);

	List<UserVo> selectOnlineUsersByPage(Map<String, Object> param);

}