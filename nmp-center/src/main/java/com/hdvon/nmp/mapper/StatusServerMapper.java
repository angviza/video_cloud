package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.common.CameraPermissionVo;
import com.hdvon.nmp.entity.StatusServer;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.StatusServerParamVo;
import com.hdvon.nmp.vo.StatusServerVo;

public interface StatusServerMapper extends Mapper<StatusServer> , MySqlMapper<StatusServer>{

	StatusServerVo selectStatusServerByParam(Map<String,Object> map);
	
	List<StatusServerVo> selectStatusServersList(Map<String,Object> map);

	String selectMaxCodeBycode(Map<String, Object> map);
	
}