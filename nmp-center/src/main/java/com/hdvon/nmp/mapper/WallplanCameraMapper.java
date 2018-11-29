package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.WallplanCamera;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.WallplanCameraVo;

public interface WallplanCameraMapper extends Mapper<WallplanCamera> , MySqlMapper<WallplanCamera>{

	List<WallplanCameraVo> selectByParam(Map<String, Object> map);

}