package com.hdvon.client.config.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.hdvon.client.exception.CameraException;
import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.client.vo.CameraMsg;
import com.hdvon.nmp.common.SystemConstant;


/**
 * 发送消息
 * @author Administrator
 *
 */
@Component
public class CameraSender {
	private static Logger LOG =  LoggerFactory.getLogger(CameraSender.class);
	
	/*
    @Autowired
    private AmqpTemplate rabbitTemplate;
    */
	
    /**
     * 消息类型  1:用户授权 2:权限预案 3:项目分组
     */
    private static final int USER_TYPE_MSG = 1;
    private static final int PLAN_TYPE_MSG = 2;
    private static final int GROUP_TYPE_MSG = 3;
    
    public void sendCamera(CameraMsg msg) throws CameraException {
    	LOG.info(">>>>>>>>>>>>>>>发送消息："+JSON.toJSONString(msg));
        //this.rabbitTemplate.convertAndSend(SystemConstant.CONTRACT_TOPIC,SystemConstant.QUEUE_ES_CAMERA, msg);
    }
    
    public void sendCameraMapping(CameraMappingMsg msg) throws CameraException{
    	
    	LOG.info(">>>>>>>>>>>>>>>发送消息："+JSON.toJSONString(msg));
    	
    	String queueName = null;
    	if(msg.getType() == USER_TYPE_MSG) {
    		queueName = SystemConstant.QUEUE_ES_USER_CAMERA;
    	}else if(msg.getType() == PLAN_TYPE_MSG) {
    		queueName = SystemConstant.QUEUE_ES_PLAN_CAMERA;
    	}else {
    		queueName = SystemConstant.QUEUE_ES_CAMERA_GROUP;
    	}
    	//this.rabbitTemplate.convertAndSend(SystemConstant.CONTRACT_TOPIC,queueName, msg);
    }
    
}
