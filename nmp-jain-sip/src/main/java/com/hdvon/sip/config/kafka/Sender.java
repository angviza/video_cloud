package com.hdvon.sip.config.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.hdvon.nmp.common.SipConstants;
import com.hdvon.nmp.common.SipLogBean;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Sender {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    
	public void sendLog(SipLogBean msgBean) {
		try {
			kafkaTemplate.send(SipConstants.KAFKA_SIP_TOPIC, JSON.toJSONString(msgBean));
		} catch (Exception e) {
			// TODO: handle exception
			log.error("发送数据出错！！！{}{}", SipConstants.KAFKA_SIP_TOPIC, JSON.toJSONString(msgBean));
		}
		
    }
	/**
	 * 发送消息
	 * @param topic
	 * @param msgContent
	 */
	public void sendMsg(String topic,String msgContent) {
		try {
			kafkaTemplate.send(topic, msgContent);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("发送数据出错！！！{}{}", topic, JSON.toJSONString(msgContent));
		}
		
    }
}
