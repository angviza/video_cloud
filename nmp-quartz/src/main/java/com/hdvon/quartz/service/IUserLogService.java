package com.hdvon.quartz.service;

import java.util.List;
import java.util.Map;

import com.hdvon.quartz.entity.ReportResponseVo;
import com.hdvon.quartz.vo.UserLogVo;
import com.hdvon.quartz.vo.UserVo;
import com.hdvon.quartz.entity.RepUseranalysis;

/**
 * <br>
 * <b>功能：</b>用户行为日志表 服务类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IUserLogService{

	public List<ReportResponseVo> getCameraLog(Map<String, Object> param);

	public List<RepUseranalysis> getCamearTotal(Map<String, Object> param);

	public List<RepUseranalysis> getOnlineTotal(Map<String, Object> param);

	public List<RepUseranalysis> getLoiginTotal(Map<String, Object> param);

	public List<UserVo> getCloseInviteUser(Map<String, Object> param);
	
	public List<UserLogVo> getCameraLogList();
	
	public void updateUserLogList(List<UserLogVo> list);

}
