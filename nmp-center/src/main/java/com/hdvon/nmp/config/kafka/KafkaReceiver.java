package com.hdvon.nmp.config.kafka;

import java.util.Date;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hdvon.nmp.common.SipConstants;
import com.hdvon.nmp.common.SipLogBean;
import com.hdvon.nmp.service.ICameraLogService;
import com.hdvon.nmp.service.ISipLogService;
import com.hdvon.nmp.vo.CameraLogVo;
import com.hdvon.nmp.vo.sip.SipLogVo;
import lombok.extern.slf4j.Slf4j;

/**
 * <br>
 * <b>功能：</b>kafka消息消费者处理sip日志组件<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018-11-7<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Component
@Slf4j
public class KafkaReceiver {
	
	@Autowired
	private ISipLogService sipLogService;
	
	@Autowired
	private ICameraLogService cameraLogService;
	
	private SipLogBean convertToSipLog(ConsumerRecord<?, ?> record) {
		
		SipLogBean sip = null;
		
		if (null != record.value()) {
			
			String value = record.value().toString();
			log.info(">>>>>>>>>>>>>>>>>>>>>kafka的value: " + value);
			
			JSONObject sbj = JSON.parseObject(value);
			String transactionId = (String) sbj.get("transactionID");
			String callId = (String) sbj.get("callID");
			String sipId = (String) sbj.get("sipId");
			String userId = (String) sbj.get("userId");
			String token = (String) sbj.get("token");
			String method = (String) sbj.get("method");
			String reqIp = (String) sbj.get("reqIp");
			String deviceId = (String) sbj.get("deviceID");
			Long date = (Long) sbj.get("reqDate");
			//String strDate = date != null ? DateFormatUtil.formatDate(date, "yyyy-MM-dd HH:mm:ss") : null;
			//Date reqDate = strDate != null ? DateUtil.parse(strDate, "yyyy-MM-dd HH:mm:ss") : null;
			String param = (String) sbj.get("param");
			
			sip = SipLogBean.builder().transactionID(transactionId).callID(callId).sipId(sipId).token(token).
				userId(userId).method(method).reqIp(reqIp).deviceID(deviceId).reqDate(new Date(date)).param(param).build();
				
		}
		
		return sip;
	}
	
	/**
	 * 手动消费
	 * @param record
	 * @param ack
	 */
	@KafkaListener(topics = {SipConstants.KAFKA_SIP_TOPIC})
    public void handleSipLog(ConsumerRecord<?, ?> record) {
		
		try {
			log.info(">>>>>>>>>>>>>>>>>>>>>kafka的key: " + record.key());
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			//ack.acknowledge();;//手动提交偏移量	Acknowledgment ack
		}
		
		SipLogBean sip = this.convertToSipLog(record);
		if (null != sip) {
			
			if (null != sip.getReqIp() && null != sip.getParam()) {
				
				SipLogVo vo = new SipLogVo();
				vo.setCallId(sip.getCallID());
				vo.setDeviceId(sip.getDeviceID());
				vo.setMethod(sip.getMethod());
				vo.setParam(sip.getParam());
				vo.setReqIp(sip.getReqIp());
				vo.setTransactionId(sip.getTransactionID());
				vo.setUserId(sip.getUserId());
				vo.setReqTime(sip.getReqDate());
				
				sipLogService.saveSipLog(vo);
			}
			
			
			if (null == sip.getCallID()) {
				return;
			}
			
			CameraLogVo log = new CameraLogVo();
			log.setUserId(sip.getUserId());
			log.setCallId(sip.getCallID());
			log.setUserIp(sip.getReqIp());
			log.setSbbm(sip.getDeviceID());
			log.setPlayType(sip.getMethod());
			
			//同步到在线摄像机日志表
			cameraLogService.syncCameraSipLog(log);
			
		}

    }
}
