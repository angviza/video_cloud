package com.hdvon.nmp.mapper;

import com.hdvon.nmp.entity.AlarmserverMapping;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.AlarmserverMappingVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface AlarmserverMappingMapper extends Mapper<AlarmserverMapping> , MySqlMapper<AlarmserverMapping>{
	
	List<AlarmserverMappingVo> selectByParam(Map<String, Object> map);
	
	List<AlarmserverMappingVo> selectProjectByAlarmserverIds(@Param("alarmserverIds")List<String> alarmserverIds);

	int selectAlarmProject(Map<String, Object> param);
}