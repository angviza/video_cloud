package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.CameraLogVo;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.UserVo;
import com.hdvon.nmp.vo.video.UserOperLogVo;

/**
 * <br>
 * <b>功能：</b>设备播放记录（临时）表 服务类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface ICameraLogService{

	void saveLog(CameraLogVo log);

	void deleteLog(List<String> callIdList);

	List<CameraLogVo> deleteByParam(Map<String, String> param);

	PageInfo<CameraVo> getCameraLogByPage(Map<String, Object> param, PageParam pageParam);

	/**
	 * 正在操作设备用户
	 * @param pp
	 * @param param
	 * @return
	 */
	PageInfo<UserVo> getOnlineUsersByPage(PageParam pp, Map<String, Object> param);

	public void saveCameraLog(UserOperLogVo vo, UserVo userVo);
	
	public void syncCameraSipLog(CameraLogVo vo);
	
}
