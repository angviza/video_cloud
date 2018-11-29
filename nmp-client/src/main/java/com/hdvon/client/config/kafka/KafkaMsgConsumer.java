package com.hdvon.client.config.kafka;

import javax.annotation.Resource;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.hdvon.client.service.EsUpdateService;
import com.hdvon.client.service.IInboundMessageService;
import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.client.vo.CameraMsg;
import com.hdvon.client.vo.InboundMessageVo;
import com.hdvon.nmp.common.SystemConstant;
import com.hdvon.nmp.enums.MessageStatusEnums;
import com.hdvon.nmp.enums.MsgTypeEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * <br>
 * <b>功能：</b>kafka消息消费者组件<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018-10-31<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Component
@Slf4j
public class KafkaMsgConsumer {
	
	@Resource
	private EsUpdateService esUpdateIndexService;
	
	@Autowired
	private IInboundMessageService inboundMessageService;
	
	
	private Object convert(ConsumerRecord<?, ?> record, String topic) {
		
		if (null != record.value()) {
			
			String value = record.value().toString();
			log.info(">>>>>>>>>>>>>>>>>>>>>kafka的value: " + value);
			
			if (SystemConstant.CONTRACT_TOPIC1.equals(topic)) {
				
				CameraMsg msg = JSON.parseObject(value, CameraMsg.class);
				return msg;
			}
			
			if (SystemConstant.CONTRACT_TOPIC2.equals(topic)) {
				
				CameraMappingMsg msg = JSON.parseObject(value, CameraMappingMsg.class);
				return msg;
			}
			
		}
		
		return null;
	}
	
	
	private void syncEsIndex(CameraMappingMsg mapping, CameraMsg msg) {
		
		String msgId = null;
		MsgTypeEnum msgEm = null;
		
		if (null != mapping) {
			
			msgId = mapping.getMsgId();
			msgEm = MsgTypeEnum.getMsgTypeEnumByKey(mapping.getType());
		}
		
		if (null != msg) {
			
			msgId = msg.getMsgId();
			msgEm = MsgTypeEnum.getMsgTypeEnumByValue(SystemConstant.CAMERA_KEY);
		}
		
		if (null != msgEm) {

			/**
			 * @author		huanhongliang
			 * @date 		2018/11/6
			 * @description 消息消费之后更改消息状态为“已处理”
			 */
			InboundMessageVo vo = inboundMessageService.getMessage(msgId);
			if (null != vo) {
				
				if (vo.getStatus() != MessageStatusEnums.已处理.getValue()) {
					
					//根据用户id集合更新摄像索引库【保护hdvon_camera_permission,hdvon_camera_group】
					if (MsgTypeEnum.ES_USER_MSG.equals(msgEm)) {
						
						//根据资源角色关联用户更新索引数据
						esUpdateIndexService.batchUpdateIndexByUser(mapping);
						log.info("资源授权更新的消息  : msg-> {} ", JSON.toJSONString(mapping));
						
					} else if (MsgTypeEnum.ES_PLLAN_MSG.equals(msgEm)) {
						
						//根据权限预案对用户授权更新索引库
						esUpdateIndexService.batchUpdateIndexByPlan(mapping);
						log.info("权限预案更新的消息  : msg-> {} ", JSON.toJSONString(mapping));
				        
					} else if (MsgTypeEnum.ES_GROUP_MSG.equals(msgEm)) {
						
						//根据分组更新索引库
						esUpdateIndexService.batchUpdateIndexByGroup(mapping);
						log.info("自定义分组更新的消息  : msg-> {} ", JSON.toJSONString(mapping));
						
					} else if (MsgTypeEnum.ES_USERROLE_MSG.equals(msgEm)) {
						
						//根据用户分配资源角色更新ES用户摄像机资源数据
						esUpdateIndexService.updateByUserRole(mapping);
						log.info("根据用户分配资源角色更新的消息  : msg-> {} ", JSON.toJSONString(mapping));
					} else if (MsgTypeEnum.ES_CAMERA_MSG.equals(msgEm)) {
						
						//更新设备的索引库
						//根据设备id集合更新摄像索引库【保护hdvon_camera_permission,hdvon_camera_group】
						esUpdateIndexService.updateIndexBydevId(msg);
						log.info("设备更新的消息  : msg-> {} ", JSON.toJSONString(msg));
					}
					
					inboundMessageService.saveMessageLog(vo, null);
					
				}
			}
			
		}
		
	}
	
	
	/**
	 * 处理设备更新
	 * @param record
	 */
	@KafkaListener(topics = {SystemConstant.CONTRACT_TOPIC1})
    public void deviceProcess(ConsumerRecord<?, ?> record) {
		
		CameraMsg msg = (CameraMsg) this.convert(record, SystemConstant.CONTRACT_TOPIC1);
		
		//处理同步更新ES索引库的通用业务逻辑
		this.syncEsIndex(null, msg);
		
    }
	
	
	/**
	 * 处理资源授权、权限预案、自定义分组、用户分配角色更新
	 * @param record
	 */
	@KafkaListener(topics = {SystemConstant.CONTRACT_TOPIC2})
    public void cameraProcess(ConsumerRecord<?, ?> record) {
		
		CameraMappingMsg msg = (CameraMappingMsg) this.convert(record, SystemConstant.CONTRACT_TOPIC2);
		
		//处理同步更新ES索引库的通用业务逻辑
		this.syncEsIndex(msg, null);
		
    }
    
}
