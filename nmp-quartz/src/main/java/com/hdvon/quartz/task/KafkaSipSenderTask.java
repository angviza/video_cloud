//package com.hdvon.quartz.task;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.quartz.CronTrigger;
//import org.quartz.JobDetail;
//import org.quartz.Scheduler;
//import org.quartz.SchedulerException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import com.alibaba.fastjson.JSON;
//import com.hdvon.nmp.common.SipUserVo;
//import com.hdvon.nmp.common.SystemConstant;
//import com.hdvon.quartz.jni.JNIConfig;
//
//
///**
// * <br>
// * <b>功能：</b>kafka消息生产者定时发送信令账号消息组件<br>
// * <b>作者：</b>huanhongliang<br>
// * <b>日期：</b>2018-11-12<br>
// * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
// */
//@Configuration
//@EnableScheduling
//@Component
//public class KafkaSipSenderTask {
//	
//	@Autowired
//	private JNIConfig jNIConfig;
//	
//	@Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//	
//	@Resource(name = "jobDetail")
//	private JobDetail jobDetail;
// 
//	@Resource(name = "jobTrigger")
//	private CronTrigger cronTrigger;
// 
//	@Resource(name = "scheduler")
//	private Scheduler scheduler;
//	
//	
//	//@Scheduled(cron="0 10 */1 * * ?")
//	@Scheduled(fixedRate = 1000*60*105) // 每隔  1000*60 表示1分钟
//	public void sipSenderTask() throws SchedulerException {
//		
//		String userName = jNIConfig.getUsername();
//		String userName2 = jNIConfig.getUsername2();
//		String ip = jNIConfig.getReceiverIp();
//		
//		List<SipUserVo> list = new ArrayList<SipUserVo>();
//		SipUserVo vo = new SipUserVo();
//		vo.setUserName(userName);
//		vo.setReceiverIp(ip);
//		list.add(vo);
//		
//		vo = new SipUserVo();
//		vo.setUserName(userName2);
//		vo.setReceiverIp(ip);
//		list.add(vo);
//			
//		kafkaTemplate.send(SystemConstant.SIPUSER_CONTRACT_TOPIC, JSON.toJSONString(list));
//		
//	}
//	
//}
