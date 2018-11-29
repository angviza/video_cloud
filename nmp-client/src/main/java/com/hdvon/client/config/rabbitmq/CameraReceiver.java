/*package com.hdvon.client.config.rabbitmq;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.hdvon.client.config.SystemConstant;
import com.hdvon.client.service.EsUpdateService;
import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.client.vo.CameraMsg;
*//**
 * 消息监听处理实现
 * @author wanshaojian
 *
 *//*
@Component
public class CameraReceiver {

	private static final Logger logger = LoggerFactory.getLogger(CameraReceiver.class);
	
	@Resource
	EsUpdateService service;
	
	@RabbitListener(queues = SystemConstant.QUEUE_ES_CAMERA)
    @RabbitHandler
    public void deviceProcess(@Payload CameraMsg msg) throws InterruptedException {
    	//根据设备id集合更新摄像索引库【保护hdvon_camera_permission,hdvon_camera_group】
    	service.updateIndexBydevId(msg);
    	
        logger.info("device receive  : id-> {} ,deviceIds-> {} , content-> {} ",msg.getId(),msg.getDeviceIds(),msg.getContent());
    }
	@RabbitListener(queues = SystemConstant.QUEUE_ES_USER_CAMERA)
    @RabbitHandler
    public void userCameraProcess(@Payload CameraMappingMsg msg) throws InterruptedException {
    	//根据用户id集合更新摄像索引库【保护hdvon_camera_permission,hdvon_camera_group】
		String userIds = msg.getIds();
    	service.batchUpdateIndexByUser(userIds);
    	
        logger.info("device receive  : id-> {} ,userids-> {} , content-> {} ",msg.getId(),userIds,msg.getContent());
    }
	
	@RabbitListener(queues = SystemConstant.QUEUE_ES_PLAN_CAMERA)
    @RabbitHandler
    public void planCameraProcess(@Payload CameraMappingMsg msg) throws InterruptedException {
		//根据用户id和角色id更新ES用户摄像机资源数据
		String planIds = msg.getIds();
		service.batchUpdateIndexByPlan(planIds);
        
        logger.info("device receive  : id-> {} ,planids-> {} , content-> {} ",msg.getId(),planIds,msg.getContent());
    }
	
	@RabbitListener(queues = SystemConstant.QUEUE_ES_CAMERA_GROUP)
    @RabbitHandler
    public void cameraGroupProcess(@Payload CameraMappingMsg msg) throws InterruptedException {
		//根据用户id和角色id更新ES用户摄像机资源数据
		String groupIds = msg.getIds();
		service.batchUpdateIndexByGroup(groupIds);
		
		logger.info("device receive  : id-> {} ,groupids-> {} , content-> {} ",msg.getId(),groupIds,msg.getContent());
    }
}
*/