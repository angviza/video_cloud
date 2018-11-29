package com.hdvon.sip.container;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.hdvon.nmp.util.LibLoader;
import com.hdvon.sip.app.SipStackManage;
import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.config.redis.BaseRedisDao;
import com.hdvon.sip.video.service.IVideoSipService;
import com.hdvon.sip.video.utils.PropertiesUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <br>
 * <b>功能：</b>容器启动时自动注册信令账号组件<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018/11/2<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Slf4j
@Component
public class CustomApplicationContextInitializer implements ApplicationRunner {

	@Autowired
	private SipConfig sipConfig;
	
	/*
	@Autowired 
	private SipService sipService;
	*/
	
	@Resource
	private BaseRedisDao<String, Object> redisDao;
	
	@SuppressWarnings("rawtypes")
	@Autowired
    private RedisTemplate redisTemplate;
	
	@Autowired
	private IVideoSipService videoSipService;
	
	//接受者 web服务器IP
	/*
	@Value("${jni.receiverIp}")  
	private String receiverIp;
	
	@Value("${jni.username}")
	private String userName;
	
	@Value("${lib.libName}")
	private String libName;
	*/
	
	@SuppressWarnings("unchecked")
	@Override
	public void run(ApplicationArguments arguments) throws Exception {
		String userName = PropertiesUtils.getProperty("jni.username");
		String userName1 = PropertiesUtils.getProperty("jni.username2");
		String receiverIp = PropertiesUtils.getProperty("jni.receiverIp");
		String libName = PropertiesUtils.getProperty("lib.libName");
		
		String key = userName+"_"+receiverIp+"_REG";
		key = key.replace(".", "");
		//容器重启时判断信令账号有无注册过
		if (redisTemplate.hasKey(key)) {//已经注册
			//从redis缓存中删除信令账号注册信息
			redisTemplate.delete(key);
		}
		
		key = userName1+"_"+receiverIp+"_REG";
		key = key.replace(".", "");
		//容器重启时判断信令账号有无注册过
		if (redisTemplate.hasKey(key)) {//已经注册
			//从redis缓存中删除信令账号注册信息
			redisTemplate.delete(key);
		}
		
		/*************************** Added by huanhongliang 2018/10/8 容器启动时加载dll文件用于JNI接口的调用   *************************/
		log.info("=========================== 准备加载本地dll类库文件  =======================");
		//spring容器加载时读取JNI接口调用需要的类库文件
		LibLoader.loadLib(libName);
		log.info("=========================== 加载本地dll类库文件完成  =======================");
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		/**
		 * @author			huanhongliang
		 * @date			2018/11/2
		 * @description		添加账号切换注册服务接口以及容器启动时自动注册信令账号的业务逻辑	
		 */
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 信令账号开始注册  <<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		videoSipService.register(null);
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 信令账号注册成功  <<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		
		
		log.info("=========================== SIP服务启动执行,执行信令账号注册等操作  =======================");
		try {
			//初始SipStack
			SipStackManage.getInstance(sipConfig);
			
			/*//清空redis中注册的用户
			redisDao.remove(SipConstants.REDIS_USER_KYE);
			
			log.info("初始化jain sip客户端配置完成");
			//用户注册
			sipService.register();
			
			log.info("向信令服务器注册账号【"+sipConfig.getUserlist()+"】成功！");
			Thread.sleep(500);
			//发送心跳
			sipService.checkHeartbeat();*/
		} catch (Exception e) {
			// TODO: handle exception
			
			log.error("服务启动注册用户失败:{}",e);
		}
		
	}
	
}
