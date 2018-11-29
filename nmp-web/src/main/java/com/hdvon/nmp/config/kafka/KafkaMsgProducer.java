package com.hdvon.nmp.config.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.hdvon.client.exception.CameraException;
import com.hdvon.client.service.IInboundMessageService;
import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.client.vo.CameraMsg;
import com.hdvon.client.vo.InboundMessageVo;
import com.hdvon.nmp.common.SystemConstant;
import com.hdvon.nmp.enums.MsgTypeEnum;
import com.hdvon.nmp.util.snowflake.IdGenerator;

import lombok.extern.slf4j.Slf4j;

/**
 * <br>
 * <b>功能：</b>kafka消息生产者组件，默认为同步发送的方式<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018-10-31<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Component
@Slf4j
public class KafkaMsgProducer {
	
	@Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
	
	@Reference
	private IInboundMessageService inboundMessageService;
	
	private Object generateMsgId(CameraMsg msg, CameraMappingMsg msg1) {
		if (null == msg1) {
			
			//设置消息id
			msg.setMsgId(IdGenerator.nextId());
			return msg;
		}
		
		if (null == msg) {
			
			//设置消息id
			msg1.setMsgId(IdGenerator.nextId());
			return msg1;
		}
		
		return null;
	}
	
    public void sendCamera(CameraMsg msg) throws CameraException {
    	
    	log.info(">>>>>>>>>>>>>>>>>>>> sendCamera <<<<<<<<<<<<<<<<<<<");
    	log.info(">>>>>>>>>>>>>>>发送消息："+JSON.toJSONString(msg));
    	
    	/**
    	 * @author huanhongliang
    	 * @date 2018/11/6
    	 * @description 为了将消息ID通过消息队列发送到消息代理Broker中，需要在真正发送到消息队列之前利用雪花算法生成消息id
    	 */
    	msg = (CameraMsg) this.generateMsgId(msg, null);
    	
    	/**
    	 * @author huanhongliang
    	 * @date 2018/11/6
    	 * @description 发送消息到消息队列之前要将消息id入库，消息入库并且对应的状态应该要改为“未处理”
    	 * 注意： 发送消息到队列和消息内容入库并不属于同一事务；另外之所以要将消息在发送到消息队列之前入库，是因为如果在发送到消息队列之后再入库，
    	 * 		消息消费者有可能在消息发送之后并在真正入库之前就消费的话，有可能取出来的消息是空的从而导致更新消息状态失败。
    	 */
    	InboundMessageVo vo = new InboundMessageVo();
    	vo.setUserId(msg.getId());
    	
    	MsgTypeEnum msgEm = MsgTypeEnum.getMsgTypeEnumByValue(SystemConstant.CAMERA_KEY);
    	vo.setType(msgEm.getKey());
    	vo.setOperateType(String.valueOf(msg.getType()));
    	vo.setContent(msg.getContent());
    	vo.setDeviceIds(msg.getDeviceIds());
    	inboundMessageService.saveMessageLog(vo, msg.getMsgId());
    	
    	/*
    	 * @param topic		发送消息内容到指定的topic，用户只需指定消息的 Topic即可生产或消费数据而不必关心数据存于何处
    	 * @param key		用于存储在指定的TOPIC类别的 partition分区中的发送消息key
    	 * @param data		需要被序列化的json格式的消息内容
    	 */
    	kafkaTemplate.send(SystemConstant.CONTRACT_TOPIC1, msgEm.getValue(), JSON.toJSONString(msg));
    	
    	/**
    	 * @author huanhongliang
    	 * @date 2018/11/6
    	 * @description 发送消息到消息队列之后要将消对应的消息状态改为“处理中”
    	 * 注意：发送消息到队列和消息内容入库并不属于同一事务
    	 */
    	if (null == vo.getId()) {
    		vo.setId(msg.getMsgId());
    	}
    	inboundMessageService.saveMessageLog(vo, msg.getMsgId());
    	
    }
    
    public void sendCameraMapping(CameraMappingMsg msg) throws CameraException {
    	
    	if (null == msg.getUpdateIds() && null == msg.getDeleteIds() &&
    		null == msg.getRelationId()) {
    		
    		return;
    	}
    	
    	log.info(">>>>>>>>>>>>>>>>>>>> sendCameraMapping <<<<<<<<<<<<<<<<<<<");
    	log.info(">>>>>>>>>>>>>>>发送消息："+JSON.toJSONString(msg));
    	
    	InboundMessageVo vo = new InboundMessageVo();
    	
    	MsgTypeEnum msgEm = MsgTypeEnum.getMsgTypeEnumByKey(msg.getType());
    	String key = msgEm.getValue();
    	vo.setType(msgEm.getKey());

    	
    	/**
    	 * @author huanhongliang
    	 * @date 2018/11/6
    	 * @description 为了将消息ID通过消息队列发送到消息代理Broker中，需要在真正发送到消息队列之前利用雪花算法生成消息id
    	 */
    	msg = (CameraMappingMsg) this.generateMsgId(null, msg);
    	
    	/**
    	 * @author huanhongliang
    	 * @date 2018/11/6
    	 * @description 发送消息到消息队列之前要将消息id入库，消息入库并且对应的状态应该要改为“未处理”
    	 * 注意： 发送消息到队列和消息内容入库并不属于同一事务；另外之所以要将消息在发送到消息队列之前入库，是因为如果在发送到消息队列之后再入库，
    	 * 		消息消费者有可能在消息发送之后并在真正入库之前就消费的话，有可能取出来的消息是空的从而导致更新消息状态失败。
    	 */
    	vo.setContent(msg.getContent());
    	vo.setUserId(msg.getId());
    	vo.setUpdateIds(msg.getUpdateIds());
    	vo.setDeleteIds(msg.getDeleteIds());
    	vo.setRelationIds(msg.getRelationId());
    	inboundMessageService.saveMessageLog(vo, msg.getMsgId());
    	
    	/*
    	 * @param topic		发送消息内容到指定的topic，用户只需指定消息的 Topic即可生产或消费数据而不必关心数据存于何处
    	 * @param key		用于存储在指定的TOPIC类别的 partition分区中的发送消息key
    	 * @param data		需要被序列化的json格式的消息内容
    	 */
    	kafkaTemplate.send(SystemConstant.CONTRACT_TOPIC2, key, JSON.toJSONString(msg));
    	
    	/**
    	 * @author huanhongliang
    	 * @date 2018/11/6
    	 * @description 发送消息到消息队列之后要将对应的消息状态改为“处理中”
    	 * 注意：发送消息到队列和消息内容入库并不属于同一事务
    	 */
    	if (null == vo.getId()) {
    		vo.setId(msg.getMsgId());
    	}
    	inboundMessageService.saveMessageLog(vo, msg.getMsgId());
    	
    }
    
}
