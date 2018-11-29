package com.hdvon.quartz.task;

import java.util.ArrayList;
import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hdvon.quartz.service.IPlatformInfoService;
import com.hdvon.quartz.service.IServicesInfoService;
import com.hdvon.quartz.service.IUserLogService;
import com.hdvon.quartz.util.ReportDataBuilder;
import com.hdvon.quartz.vo.PlatformInfoVo;
import com.hdvon.quartz.vo.ServicesInfoVo;
import com.hdvon.quartz.vo.UserLogVo;

import lombok.extern.slf4j.Slf4j;


/**
 * <br>
 * <b>功能：</b>实时更新统计报表定时任务通用组件<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018-11-12<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@EnableScheduling
@Component
@Slf4j
public class StatisticReportScheduleTask {
	
	@Autowired
	private IServicesInfoService servicesInfoService;
	
	@Autowired
	private IUserLogService userLogService;
	
	@Autowired
	private IPlatformInfoService platformInfoService;
	
	/**
	 * @author huanhongliang
	 * @description 定时更新后台系统资源数据
	 * @throws SchedulerException
	 */
	@Scheduled(fixedRate = 1000*60*10) // 每隔  1000*60 表示1分钟
	public void endpointServiceTask() throws SchedulerException {
		
		Long count = servicesInfoService.getSystemResourceCount();
		if (count != 0) {
			
			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>> 实时更新统计报表[后台服务系统资源]相关数据[start] <<<<<<<<<<<<<<<<<<<<<<<<<<");
			
			List<ServicesInfoVo> list = ReportDataBuilder.createServicesInfo(2);
			
			//批量更新系统资源数据
			servicesInfoService.updateSystemResource(list);
			
			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>> 实时更新统计报表[后台服务系统资源]相关数据[end] <<<<<<<<<<<<<<<<<<<<<<<<<<");
		}
		
	}
	
	
	/**
	 * @author huanhongliang
	 * @description 定时摄像机操作日志数据
	 * @throws SchedulerException
	 */
	/*
	@Scheduled(fixedRate = 1000*60*10) // 每隔  1000*60 表示1分钟
	public void pullCameraLogTask() throws SchedulerException {
		
		List<UserLogVo> logs = userLogService.getCameraLogList();
		
		if (logs.size() > 0) {
			
			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>> 实时同步统计报表[摄像机操作日志]相关数据[start] <<<<<<<<<<<<<<<<<<<<<<<<<<");
			
			List<PlatformInfoVo> list = new ArrayList<PlatformInfoVo>();
			PlatformInfoVo vo = null;
			
			for (UserLogVo log : logs) {
				
				vo = new PlatformInfoVo();
				vo.setUserName(log.getAccount());
				vo.setDeviceCode(log.getOperationObject());
				vo.setOperateTime(log.getOperationTime());
				vo.setOperateType(log.getType());
				list.add(vo);
				
			}
			
			//将t_user_log中的摄像机操作日志同步到t_platform_info中
			platformInfoService.batchInsertList(list);
			
			//将t_user_log中的is_sync字段值更改为"1"
			userLogService.updateUserLogList(logs);
			
			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>> 实时同步统计报表[摄像机操作日志]相关数据[end] <<<<<<<<<<<<<<<<<<<<<<<<<<");
			
		}
		
	}
	*/
	
}
