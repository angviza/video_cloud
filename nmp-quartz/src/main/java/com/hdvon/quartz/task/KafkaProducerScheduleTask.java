package com.hdvon.quartz.task;

import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdvon.client.service.IInboundMessageService;
import com.hdvon.client.service.IUserResources;
import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.client.vo.CameraMsg;
import com.hdvon.client.vo.InboundMessageVo;
import com.hdvon.nmp.common.MessageScheduleVo;
import com.hdvon.nmp.enums.MessageStatusEnums;
import com.hdvon.nmp.enums.MsgTypeEnum;
import com.hdvon.quartz.service.IMessageScheduleService;


/**
 * <br>
 * <b>功能：</b>kafka消息生产者定时发送未处理的消息组件<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018-11-11<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Configuration
@EnableScheduling
@Component
public class KafkaProducerScheduleTask {
	
	@Autowired
	private IMessageScheduleService messageScheduleService;
	
	@Reference
	private IUserResources userResources;
	
	@Reference
	private IInboundMessageService inboundMessageService;
	
	
	//@Scheduled(cron="0 10 */1 * * ?") // 每隔  1000*60 表示1分钟
	@Scheduled(fixedRate = 1000*60*10) // 每隔  1000*60 表示1分钟
	public void producerScheduleTask() throws SchedulerException {
		
		//获取状态为“未消费”的消息
		List<MessageScheduleVo> list = messageScheduleService.getUnreadMessageList(0);
		
		if (null != list && list.size() > 0) {
			
			for (MessageScheduleVo vo : list) {
				
				this.syncEsIndex(vo);
			}
			
		}
		
	}
	
	private void syncEsIndex(MessageScheduleVo msgSchd) {
		
		String msgId = null;
		MsgTypeEnum msgEm = null;
		
		CameraMappingMsg mapping = null;
		CameraMsg msg = null;
			
		msgId = msgSchd.getId();
		
		/**
		 * @author		huanhongliang
		 * @date 		2018/11/6
		 * @description 消息消费之后更改消息状态为“已处理”
		 */
		InboundMessageVo vo = inboundMessageService.getMessage(msgId);
		if (null != vo) {
			
			if (vo.getStatus() != MessageStatusEnums.已处理.getValue()) {
				
				String userId = msgSchd.getUserId();
				int type = msgSchd.getType();
				String content = msgSchd.getContent();
				
				msgEm = MsgTypeEnum.getMsgTypeEnumByKey(type);
				
				if (MsgTypeEnum.ES_USER_MSG.equals(msgEm) || MsgTypeEnum.ES_PLLAN_MSG.equals(msgEm) ||
					MsgTypeEnum.ES_GROUP_MSG.equals(msgEm) || MsgTypeEnum.ES_USERROLE_MSG.equals(msgEm)) {
					
					mapping = new CameraMappingMsg();
					mapping.setMsgId(msgId);
					mapping.setId(userId);
					mapping.setUpdateIds(msgSchd.getUpdateIds());
					mapping.setDeleteIds(msgSchd.getDeleteIds());
					mapping.setRelationId(msgSchd.getRelationIds());
					mapping.setType(type);
					mapping.setContent(content);
					
				} else if (MsgTypeEnum.ES_CAMERA_MSG.equals(msgEm)) {
					
					msg = new CameraMsg();
					msg.setMsgId(msgId);
					msg.setId(userId);
					msg.setType(type);
					msg.setDeviceIds(msgSchd.getDeviceIds());
					msg.setContent(content);
					
				}
				
				if (null != mapping) {
					
					//更新用户摄像机权限同步到es
					userResources.sendSyncDeviceMappingPermission(mapping);
				}
				
				if (null != msg) {
					
					//更新设备发送同步接口同步到es
					userResources.sendSyncDevicePermission(msg);
				}
				
				inboundMessageService.saveMessageLog(vo, null);
				
			}
		}
		
	}
	
	
}
