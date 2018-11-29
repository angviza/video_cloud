package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.vo.CameraCameragroupVo;
import com.hdvon.nmp.vo.CameraNode;
import com.hdvon.nmp.vo.UserVo;

/**
 * <br>
 * <b>功能：</b>摄像机表与摄像机分组的关联表 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface ICameraCameragroupService{

	List<CameraNode> selectByGroup(UserVo userVo, String cameragroupId);

	/**
	 * 获取分组下的所有摄像机
	 * @param cameargroupId
	 * @return
	 */
	List<CameraNode> selectCamearByGroup(String cameargroupId);

}
