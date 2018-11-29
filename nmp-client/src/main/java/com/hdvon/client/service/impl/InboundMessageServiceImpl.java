package com.hdvon.client.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.client.entity.InboundMessage;
import com.hdvon.client.mapper.InboundMessageMapper;
import com.hdvon.client.service.IInboundMessageService;
import com.hdvon.client.vo.InboundMessageVo;
import com.hdvon.nmp.enums.MessageStatusEnums;
import cn.hutool.core.convert.Convert;

/**
 * <br>
 * <b>功能：</b>Service<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class InboundMessageServiceImpl implements IInboundMessageService {

	@Autowired
	private InboundMessageMapper inboundMessageMapper;

	@Override
	public void saveMessageLog(InboundMessageVo vo, String msgId) {
		
		InboundMessage log = Convert.convert(InboundMessage.class, vo);
		if (null == log.getId()) {
			
			log.setId(msgId);
			log.setStatus(MessageStatusEnums.未处理.getValue());
			log.setSendTime(new Date());
			inboundMessageMapper.insertSelective(log);
			
		} else {
			
			if (null == msgId) {
				
				log.setStatus(MessageStatusEnums.已处理.getValue());
				log.setRecieveTime(new Date());
			} else {
				
				log.setStatus(MessageStatusEnums.处理中.getValue());
				log.setSendTime(new Date());
			}
			
			inboundMessageMapper.updateByPrimaryKeySelective(log);
			
		}
	}

	@Override
	public InboundMessageVo getMessage(String id) {
		
		InboundMessage message = inboundMessageMapper.selectByPrimaryKey(id);
		InboundMessageVo vo = null;
		
		/*
		if(null == message) {
        	throw new ServiceException("消息未发送！");
        }
		*/
		if(null != message) {
			vo = Convert.convert(InboundMessageVo.class, message);
        }
		
		return vo;
		
	}

}
