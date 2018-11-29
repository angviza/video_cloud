package com.hdvon.quartz.container;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.hdvon.quartz.service.IServicesInfoService;
import com.hdvon.quartz.util.ReportDataBuilder;
import com.hdvon.quartz.vo.ServicesInfoVo;

import lombok.extern.slf4j.Slf4j;

/**
 * <br>
 * <b>功能：</b>容器启动时生成初始统计报表相关数据<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018/11/12<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Slf4j
@Component
public class CustomApplicationContextInitializer implements ApplicationRunner {
	
	@Autowired
	private IServicesInfoService servicesInfoService;
	
	@Override
	public void run(ApplicationArguments arguments) throws Exception {
		
		Long count = servicesInfoService.getSystemResourceCount();
		if (count == 0) {
			
			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>> 初始化统计报表[后台服务系统资源]相关数据[start] <<<<<<<<<<<<<<<<<<<<<<<<<<");
			
			List<ServicesInfoVo> list = ReportDataBuilder.createServicesInfo(1);
			
			//批量插入系统资源数据
			servicesInfoService.insertSystemResource(list);
			//servicesInfoService.updateSystemResource(list);
			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>> 初始化统计报表[后台服务系统资源]相关数据[end] <<<<<<<<<<<<<<<<<<<<<<<<<<");
			
		}
		
	}
	
}
