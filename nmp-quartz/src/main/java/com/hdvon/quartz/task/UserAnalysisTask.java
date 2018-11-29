package com.hdvon.quartz.task;
 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hdvon.quartz.entity.RepTiggertask;
import com.hdvon.quartz.entity.RepUseranalysis;
import com.hdvon.quartz.service.IRepTiggertaskService;
import com.hdvon.quartz.service.IUserAnalysisService;
import com.hdvon.quartz.service.IUserLogService;
import com.hdvon.quartz.util.Constant;
import lombok.extern.slf4j.Slf4j;

 
@EnableScheduling
@Component
@Slf4j
public class UserAnalysisTask {
	
	@Autowired
	private IUserLogService userLogService;
	@Autowired
	private IUserAnalysisService userAnalysisService;
	@Autowired
	private IRepTiggertaskService tiggerTaskService;
	
 
	@Scheduled(cron="0 10 */1 * * ?") //
	//@Scheduled(fixedRate = 1000*60*1) // 每隔  1000*60 表示1分钟
	public void creatUserAnalysis() throws SchedulerException {
		long start =System.currentTimeMillis();
		Date startDate=null;
		Date endDate=new Date();
		RepTiggertask task=null;
		Map<String ,Object> taskParam =new HashMap<String ,Object>();
		taskParam.put("type",Constant.USET_ANALUYSIS_TYPE);//统计用户报表
		List<RepTiggertask> tiggerTask = tiggerTaskService.gettiggerTask(taskParam);
		if(tiggerTask.size() >0 ) {
			task=tiggerTask.get(0);
			startDate=tiggerTask.get(0).getUpdateTime();//添加时间
		}
		if(startDate==null) {
			startDate=new Date();
		}
		
		//统计结果入库处理
		Map<String ,Object> param =new HashMap<String ,Object>();
		param.put("startDate",startDate);//上一次统计时间
		param.put("endDate",endDate);//统计截止时间点
		log.info("统计开始时间点：" + startDate);
		log.info("统计截止时间点：" + endDate);
	    List<RepUseranalysis> camearTotal =userLogService.getCamearTotal(param);//操作设备数量
	    List<RepUseranalysis> onlineTotal =userLogService.getOnlineTotal(param);//在线时长
	    List<RepUseranalysis> loiginTotal =userLogService.getLoiginTotal(param);//登录次数
	    
	    //记录这次统计时间
	    if(task ==null) {
	    	task=new RepTiggertask();
	    	task.setName("统计用户报表");
	    	task.setType(Constant.USET_ANALUYSIS_TYPE);
	    	task.setDescription("该定时任务统计到上一次统计到系统当前时间，定时时间相隔两个时间查不应少于一个小时，但也不能大于一天");
	    	task.setCreatTime(new Date());
	    	task.setUpdateTime(endDate);
	    }
	    task.setUpdateTime(endDate);//这次统计时间
	    tiggerTaskService.saveRepTiggertask(task);
	    userAnalysisService.saveUserLog(camearTotal,onlineTotal,loiginTotal,param);
	    
	    log.info("creatUserAnalysis neet time  >>>>>>>>>"+(System.currentTimeMillis()-start));
	}
	

	/**
	 * 系统时间的上一个小时的59分59秒
	 * @return
	 */
	private Date beforeHourToNowDate(int number) {
		Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - number);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String source=df.format(calendar.getTime())+":59:59";
//		log.info("当前的时间：" + df2.format(new Date()));
		try {
			 return  df2.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}


}