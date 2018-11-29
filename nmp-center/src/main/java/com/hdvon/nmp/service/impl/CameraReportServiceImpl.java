package com.hdvon.nmp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.entity.CameraReport;
import com.hdvon.nmp.mapper.CameraReportMapper;
import com.hdvon.nmp.service.ICameraReportService;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.ReportResponseVo;

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
		for(ReportResponseVo vo : list) {
			CameraReport report = new CameraReport();
			report.setId(IdGenerator.nextId());
			report.setSbmc(vo.getName());
			report.setSbbm(vo.getDeviceCode());
			report.setTotal(vo.getHotPoints());
			report.setCreatTime(new Date());
			entity.add(report);
		}
		if(entity.size() >0) {
			cameraReportMapper.insertList(entity);
		}
	}

}
