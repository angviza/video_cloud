package com.hdvon.quartz.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.quartz.entity.CameraReport;
import com.hdvon.quartz.entity.ReportResponseVo;
import com.hdvon.quartz.mapper.CameraReportMapper;
import com.hdvon.quartz.service.ICameraReportService;
import com.hdvon.quartz.util.IdGenerator;

/**
 * <br>
 * <b>功能：</b>摄像机访问历史报表Service<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class CameraReportServiceImpl implements ICameraReportService {

	@Autowired
	private CameraReportMapper cameraReportMapper;

	@Override
	public void saveLog(List<ReportResponseVo> list) {
		
		List<CameraReport> entity= new ArrayList<CameraReport>();
		Calendar ca=Calendar.getInstance();
		ca.add(Calendar.HOUR, -1);
		Date date=ca.getTime();
		
		for(ReportResponseVo vo : list) {
			CameraReport report = new CameraReport();
			report.setId(IdGenerator.nextId());
			report.setCameraId(vo.getCameraId());
			report.setSbmc(vo.getName());
			report.setSbbm(vo.getDeviceCode());
			report.setTotal(vo.getHotPoints());
			report.setCreatTime(date);//数据发生时间
			entity.add(report);
		}
		if(entity.size() >0) {
			cameraReportMapper.insertList(entity);
		}
		
	}

	
	
	public static void main(String[] args) {
		Calendar ca=Calendar.getInstance();
		ca.add(Calendar.HOUR, -1);
		Date a=ca.getTime();
		System.out.println(a);
	}
}
