package com.hdvon.sip.config;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hdvon.sip.task.HeartbeatQuartz;

@Configuration
public class QuartzConfig {
	@Bean
    public JobDetail heartbeatQuartzDetail(){
        return JobBuilder.newJob(HeartbeatQuartz.class).withIdentity("heartbeatQuartz").storeDurably().build();
    }

    @Bean
    public Trigger testQuartzTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(30)  //设置时间周期单位秒
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(heartbeatQuartzDetail())
                .withIdentity("heartbeatQuartz")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
