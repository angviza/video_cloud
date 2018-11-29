package com.hdvon.quartz.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.common.MessageScheduleVo;
import com.hdvon.nmp.enums.MessageStatusEnums;
import com.hdvon.quartz.entity.MessageSchedule;
import com.hdvon.quartz.mapper.MessageScheduleMapper;
import com.hdvon.quartz.service.IMessageScheduleService;
import cn.hutool.core.convert.Convert;
import tk.mybatis.mapper.entity.Example;

@Service
public class MessageScheduleServiceImpl implements IMessageScheduleService {
	
	@Autowired
	private MessageScheduleMapper messageScheduleMapper;

	@Override
	public List<MessageScheduleVo> getUnreadMessageList(int type) {
		
		Example example = new Example(MessageSchedule.class);
		//example.createCriteria().andEqualTo("type", type);
		example.createCriteria().andNotEqualTo("status", MessageStatusEnums.已处理.getValue());
        
		List<MessageSchedule> list = messageScheduleMapper.selectByExample(example);
		
		List<MessageScheduleVo> tempList = new ArrayList<MessageScheduleVo>();
        for(MessageSchedule item : list) {
        	
        	MessageScheduleVo vo = Convert.convert(MessageScheduleVo.class, item);
            tempList.add(vo);
        }
		
		return tempList;
	}
	
}
