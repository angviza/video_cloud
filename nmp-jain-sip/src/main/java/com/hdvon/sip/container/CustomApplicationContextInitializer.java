package com.hdvon.sip.container;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.service.SipService;

import lombok.extern.slf4j.Slf4j;

/**
 * 初始化jain sip客户端配置
 * @author wanshaojian
 *
 */
@Slf4j
@Component
public class CustomApplicationContextInitializer implements ApplicationRunner {

	@Autowired
	SipConfig sipConfig;
	
	@Autowired 
	SipService sipService;
	
	@Override
	public void run(ApplicationArguments arguments) throws Exception {
		try {

			//用户注册
			sipService.register();
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error("服务启动注册用户失败:{}",e);
		}
		
	}
	
}
