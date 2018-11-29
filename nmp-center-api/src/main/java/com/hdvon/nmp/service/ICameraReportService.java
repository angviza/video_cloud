package com.hdvon.nmp.service;

import java.util.List;

import com.hdvon.nmp.vo.ReportResponseVo;

/**
 * <br>
 * <b>功能：</b>摄像机访问历史报表 服务类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface ICameraReportService{

	void saveLog(List<ReportResponseVo> list);

}
