package com.hdvon.sip.task;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.service.SipService;
/**
 * 描述：定时信令服务器发送用户心跳
 * @author wanshaojian
 *
 */
public class HeartbeatQuartz extends QuartzJobBean{

	private static final Logger log = LoggerFactory.getLogger(HeartbeatQuartz.class);

	@Resource
	SipService sipService;
	
	@Autowired
	SipConfig sipConfig;
	
	/**
	 * 执行定时任务
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>发送用户心跳到信令服务器开始！");
		//sipService.checkHeartbeat();
	}

}
