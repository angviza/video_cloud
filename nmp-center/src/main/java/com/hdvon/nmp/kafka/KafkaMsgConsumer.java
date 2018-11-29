package com.hdvon.nmp.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hdvon.nmp.common.CruiseBean;
import com.hdvon.nmp.common.PresentBean;
import com.hdvon.nmp.common.PresentListBean;
import com.hdvon.nmp.common.SystemConstant;
import com.hdvon.nmp.enums.CruiseTypeEnum;
import com.hdvon.nmp.enums.PresetTypeEnum;
import com.hdvon.nmp.service.IPresetService;

import lombok.extern.slf4j.Slf4j;

/**
 * kafka消息消费者组件
 * 处理预置位和球机巡航消息
 * @author wanshaojian
 *
 */
@Component
@Slf4j
public class KafkaMsgConsumer {
	@Autowired
	IPresetService presetService;
	
	
	/**
	 * 处理球机巡航消息
	 * @param record
	 */
	@KafkaListener(topics = {SystemConstant.CRUISE_TOPIC})
    public void cruiseProcess(ConsumerRecord<?, ?> record) {
		
		if(record.value()!=null) {

			String value = record.value().toString();
			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>同步球机巡航信息："+value);
			CruiseBean bean = JSON.parseObject(value, CruiseBean.class);
			//获取操作类型
			CruiseTypeEnum typeEm = CruiseTypeEnum.getObjectByKey(bean.getType());
			if(CruiseTypeEnum.ADD.equals(typeEm)) {
				//新增巡航点
				presetService.savePlanPresent(bean);
			}else if(CruiseTypeEnum.DEL.equals(typeEm)) {
				//删除巡航
				presetService.deletePlanPresent(bean);
			}else if(CruiseTypeEnum.SET_SPEED.equals(typeEm)){
				//设置巡航速度
				presetService.updatePlanPresent(bean);
			}else if(CruiseTypeEnum.SET_STOPOVER.equals(typeEm)){
				//设置巡航停留时间
				presetService.updatePlanPresent(bean);
			}
		}
		
    }
	
	/**
	 * 处理预置位消息
	 * @param record
	 */
	@KafkaListener(topics = {SystemConstant.PRESET_TOPIC})
    public void presetProcess(ConsumerRecord<?, ?> record) {
		if(record.value()!=null) {
			String value = record.value().toString();
			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>同步预知位信息："+value);
			PresentBean bean = JSON.parseObject(value, PresentBean.class);
			//获取操作类型
			PresetTypeEnum typeEm = PresetTypeEnum.getObjectByKey(bean.getType());
			if(PresetTypeEnum.SET.equals(typeEm)) {
				//新增巡航点
				presetService.savePreset(bean);
			}else if(PresetTypeEnum.DELETE.equals(typeEm)) {
				//删除巡航
				presetService.deletePreset(bean);
			}
		}
		
    }
    
	/**
	 * 预置位查询消息，增量同步预置位信息
	 * @param record
	 */
	@KafkaListener(topics = {SystemConstant.PRESET_QUERY_TOPIC})
    public void presetQueryProcess(ConsumerRecord<?, ?> record) {
		
		if(record.value()!=null) {
			String value = record.value().toString();
			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>校验并修正预置位数据："+value);
			PresentListBean vo= JSONObject.parseObject(value, PresentListBean.class);
			presetService.batchUpdatePresent(vo);
		}
		
    }
}
