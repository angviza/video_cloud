package com.hdvon.quartz.service;

import java.util.List;

import com.hdvon.nmp.common.MessageScheduleVo;

public interface IMessageScheduleService {
	
	public List<MessageScheduleVo> getUnreadMessageList(int type);
	
}
