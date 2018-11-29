package com.hdvon.quartz.mapper;

import com.hdvon.quartz.entity.MessageSchedule;
import com.hdvon.quartz.util.MySqlMapper;
import tk.mybatis.mapper.common.Mapper;

public interface MessageScheduleMapper extends Mapper<MessageSchedule> , MySqlMapper<MessageSchedule> {
	
}
