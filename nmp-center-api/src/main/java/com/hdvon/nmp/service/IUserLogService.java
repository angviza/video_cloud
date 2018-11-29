package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.ReportResponseVo;
import com.hdvon.nmp.vo.CameraGroupReportVo;
import com.hdvon.nmp.vo.UserLogVo;

/**
 * <br>
 * <b>功能：</b>用户行为日志表 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IUserLogService{

	public PageInfo<UserLogVo> getUserLogPage(Map<String, String> map, PageParam pageParam);

	public List<Map<String,Object>> getUserLogList(Map<String, String> map);
	
	public void saveUserLog(UserLogVo logVO);

	public List<ReportResponseVo> historyCamara(Map<String, Object> param);
	
	public List<ReportResponseVo> getDefaultHistoryCamara(Map<String, Object> param);
	
	public PageInfo<ReportResponseVo> getHistoryCamaraPage(Map<String, Object> map, PageParam pageParam);

	public List<ReportResponseVo> getCameraLog(Map<String, Object> param);

	public List<Map<String,Object>> getHistoryHotPoints(Map<String, Object> map);
	
	public List<Map<String,Object>> getGroupHistoryHotPoints(Map<String, Object> map);
	
	public PageInfo<CameraGroupReportVo> getGroupHistoryPage(Map<String, Object> map, PageParam pageParam);
	
	public List<CameraGroupReportVo> getDefaultGroupHistoryList(Map<String, Object> param);
	
	public List<Map<String, Object>> getCameraPeriodHotPoints(Map<String, Object> map);
	
	public List<Map<String, Object>> getCameraGroupPeriodHotPoints(Map<String, Object> map);
	
	public PageInfo<ReportResponseVo> getRealTimeCameraPage(Map<String, Object> map, PageParam pageParam);
	
	public PageInfo<CameraGroupReportVo> getRealTimeCameraGroupPage(Map<String, Object> map, PageParam pageParam);
	
	public Long getRealTimeCameraCount(Map<String, Object> map);
	
	public List<CameraGroupReportVo> getGroupHistorySubList(Map<String, Object> map, PageParam pageParam);
	
	public List<ReportResponseVo> getDefaultRealTimeCamera(Map<String, Object> param);
	
	public List<CameraGroupReportVo> getDefaultRealTimeCameraGroup(Map<String, Object> param);
}
