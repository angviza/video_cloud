package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.AlarmServer;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.AlarmServerVo;

public interface AlarmServerMapper extends Mapper<AlarmServer> , MySqlMapper<AlarmServer>{
	
	AlarmServerVo selectAlarmServerByParam(Map<String,Object> map);
	
	List<AlarmServerVo> selectAlarmServersList(Map<String, Object> map);

	String selectMaxCodeBycode(Map<String, Object> map);
}