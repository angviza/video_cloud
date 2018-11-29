package com.hdvon.nmp.mapper;

import com.hdvon.nmp.vo.ReportResponseVo;
import com.hdvon.nmp.vo.CameraGroupReportVo;
import com.hdvon.nmp.vo.UserLogVo;
import tk.mybatis.mapper.common.Mapper;
import com.hdvon.nmp.entity.UserLog;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

import java.util.List;
import java.util.Map;

public interface UserLogMapper extends Mapper<UserLog> , MySqlMapper<UserLog>{

	public List<UserLogVo> selectUserLogPage(Map<String, String> map);

	public List<Map<String, Object>> selectUserLogList(Map<String, String> map);

    /**
     * 摄像机的历史访问热点
     * @param param
     * @return
     */
	public List<ReportResponseVo> selectHistoryCamara(Map<String, Object> param);
	
	public List<ReportResponseVo> selectDefaultHistoryCamara(Map<String, Object> param);
	
	public List<Map<String, Object>> selectHistoryHotPoints(Map<String, Object> map);
	
	public List<Map<String, Object>> selectGroupHistoryHotPoints(Map<String, Object> map);
	
	public List<CameraGroupReportVo> selectGroupHistoryList(Map<String, Object> param);
	
	public List<CameraGroupReportVo> selectDefaultGroupHistoryList(Map<String, Object> param);
	
	public List<Map<String, Object>> selectCameraPeriodHotPoints(Map<String, Object> map);
	
	public List<Map<String, Object>> selectCameraGroupPeriodHotPoints(Map<String, Object> map);
	
	public List<ReportResponseVo> selectRealTimeCameraList(Map<String, Object> param);
	
	public List<CameraGroupReportVo> selectRealTimeCameraGroupList(Map<String, Object> param);
	
	public Long selectRealTimeCameraCount(Map<String, Object> map);
	
	public List<ReportResponseVo> selectDefaultRealTimeCamera(Map<String, Object> param);
	
	public List<CameraGroupReportVo> selectDefaultRealTimeCameraGroup(Map<String, Object> param);
	
}