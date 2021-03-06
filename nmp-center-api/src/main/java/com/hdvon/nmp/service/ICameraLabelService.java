package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.vo.CameraLabelVo;
import com.hdvon.nmp.vo.UserVo;

/**
 * <br>
 * <b>功能：</b>摄像头录像标签表 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface ICameraLabelService{

	void saveCameraLabel(UserVo user, CameraLabelVo vo);

	List<CameraLabelVo> getCameraLabel(Map<String, Object> map);

	void deleteByIds(List<String> ids);

}
