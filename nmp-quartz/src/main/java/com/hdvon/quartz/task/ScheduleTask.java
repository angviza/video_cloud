package com.hdvon.quartz.task;
 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.hdvon.quartz.service.ICameraReportService;
import com.hdvon.quartz.service.IUserLogService;

@Component // 此注解必加
@EnableScheduling // 此注解必加
public class ScheduleTask {
	
	@Autowired
	private ICameraReportService cameraReportService;
	@Autowired
	private IUserLogService userLogService;
	
	/**
	 * 目前定时统计是每小时统计一次，时间改了 sql也有改
	 */
	public void cameraReport(){
//		Map<String,Object> param = new HashMap<String,Object>();
//		log.info("开始统计时间---"+System.currentTimeMillis());
//		
//		List<ReportResponseVo> list =userLogService.getCameraLog(param);
//		cameraReportService.saveLog(list);
//		
//		log.info("结束统计时间---"+System.currentTimeMillis());
	}
}