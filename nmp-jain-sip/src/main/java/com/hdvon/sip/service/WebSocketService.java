package com.hdvon.sip.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hdvon.nmp.common.SipConstants;
import com.hdvon.sip.entity.ResponseBean;


@Service
public class WebSocketService {
	private static Logger logger = LoggerFactory.getLogger(WebSocketService.class);
	@Autowired
    private SimpMessagingTemplate template;

    /**
     * 广播
     * 发给所有在线用户
     *
     * @param msg
     */
    public void sendMsg(ResponseBean msg) {
        template.convertAndSend(SipConstants.WEBSOCKET_BROADCAST_PATH_MSG, msg);
    }
    /**
     * 发送给指定用户
     * @param userName
     * @param msg
     */
    public void sendUser(String userName, ResponseBean msg) {
    	if(!StringUtils.isEmpty(userName)) {
    		template.convertAndSendToUser(userName, SipConstants.WEBSOCKET_P2P_PATH_MSG, msg);
    		
        	logger.info("================userName:{},msg:{}",userName,JSON.toJSONString(msg));
    	}
    }
    /**
     * 发送给指定用户
     * @param users
     * @param msg
     */
    public void send2Users(List<String> users, ResponseBean msg) {
        users.forEach(userName -> {
            template.convertAndSendToUser(userName, SipConstants.WEBSOCKET_P2P_PATH_MSG, msg);
        });
    }
}
