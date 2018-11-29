package com.hdvon.sip.config.kafka;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.hdvon.nmp.common.SipUserVo;
import com.hdvon.nmp.common.SystemConstant;
import com.hdvon.sip.video.service.IVideoSipService;
import com.hdvon.sip.video.utils.PropertiesUtils;
import com.hdvon.sip.video.vo.RegisterCallback;

import lombok.extern.slf4j.Slf4j;


/**
 * <br>
 * <b>功能：</b>kafka消息消费者处理信令账号消息并重新注册信令账号组件<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018-11-12<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Component
@Slf4j
public class KafkaSipUserReciever {
	
	@Autowired
	private IVideoSipService videoSipService;
	
	/**
     * 监听topic,默认是每隔2个小时重新注册信令账号到信令服务器
     */
	@KafkaListener(topics = SystemConstant.SIPUSER_CONTRACT_TOPIC)
    public void sipUserRegisterProcess(String records) {
		
		List<SipUserVo> list = JSON.parseArray(records, SipUserVo.class);
		
		/**
		boolean isOnline = false;
		String key = null;
		
		for (SipUserVo vo : list) {
			
			key = vo.getUserName()+"_"+vo.getReceiverIp()+"_REG";
			key = key.replace(".", "");
			if (redisTemplate.hasKey(key)) {
				isOnline = true;
				break;
			}
		}
		**/
		
		//账号注册已过期
		//if (!isOnline) {
			
			int expire = PropertiesUtils.getIntegerProperty("jni.expire")/60;
			
			/**
			 * @author			huanhongliang
			 * @date			2018/11/12
			 * @description		添加账号切换注册服务接口以及容器启动时自动注册信令账号的业务逻辑
			 */
			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 每隔"+expire+"分钟重新注册信令账号  <<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			
			//重新注册信令账号到信令服务器
			RegisterCallback callback = videoSipService.register(null);
			
			String userName = "";
			if (null != callback && callback.getStatusCode() == 200) {
				
				for (SipUserVo vo : list) {
					
					userName = vo.getUserName();
					if (userName.equals(callback.getUsername())) {
						
						userName = userName + ",";
					}
					
				}
				
				if ("".equals(userName)) {
					
					log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 信令账号不匹配  <<<<<<<<<<<<<<<<<<<<<<<<<<<<");
					return;
				}
				
				if (userName.indexOf(',') != -1) {
					userName = userName.substring(0, userName.lastIndexOf(','));
					log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 信令账号["+userName+"]已成功注册到信令服务器  <<<<<<<<<<<<<<<<<<<<<<<<<<<<");
				}
				
			} else {
				
				log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 重新注册信令账号失败  <<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			}
			
		//}
    	
    }
	
}
