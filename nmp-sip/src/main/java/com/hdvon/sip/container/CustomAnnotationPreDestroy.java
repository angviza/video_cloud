package com.hdvon.sip.container;

import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.hdvon.sip.video.utils.PropertiesUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAnnotationPreDestroy {
	
	/*
	//接受者 web服务器IP
	@Value("${jni.receiverIp}")  
	private String receiverIp;
	
	@Value("${jni.username}")
	private String userName;
	*/
	
	@SuppressWarnings("rawtypes")
	@Autowired
    private RedisTemplate redisTemplate;
	
	@SuppressWarnings("unchecked")
	@PreDestroy  
    public void destory() {  
		log.info("=============== web spring boot has been destoryed ================= ");
		String userName = PropertiesUtils.getProperty("jni.username");
		String userName1 = PropertiesUtils.getProperty("jni.username2");
		String receiverIp = PropertiesUtils.getProperty("jni.receiverIp");
		
		String key = userName+"_"+receiverIp+"_REG";
		key = key.replace(".", "");
		if (redisTemplate.hasKey(key)) {
			redisTemplate.delete(key);
		}
		
		key = userName1+"_"+receiverIp+"_REG";
		key = key.replace(".", "");
		if (redisTemplate.hasKey(key)) {//已经注册
			//从redis缓存中删除信令账号注册信息
			redisTemplate.delete(key);
		}
    }
}
