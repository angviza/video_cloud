package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.CameraParamVo;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.DeviceParamVo;
import com.hdvon.nmp.vo.DeviceVo;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;

/**
 * <br>
 * <b>功能：</b>一机一档 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IDeviceService {

	String editDevice(UserVo userVo, DeviceParamVo vo);

	DeviceParamVo getCameraAndDeviceInfo(String cameraId);

	/**
	 * 根据设备编码前几位获取最大的设备编码
	 * @param string
	 * @return
	 */
	String getMaxCodeBycode(String smbh);

	/**
	 * 根据设备编码查询设备
	 * @param sbbm
	 * @return
	 */
	List<DeviceVo> getDeviceBySbbm(String sbbm);

	List<DeviceVo> selectByParam(Map<String, Object> param);

	/**
	 * 根据id查询设备
	 * @param id
	 * @return
	 */
	public DeviceVo getDeviceInfo(String id);

	public DeviceVo getDeviceByCameraId(String id);
	
	public PageInfo<Map<String,Object>> getDeviceByPage(CameraParamVo vo, TreeNodeChildren treeNodeChildren, PageParam pageParam);
	
	int countCmaerasByParam(CameraParamVo vo, TreeNodeChildren treeNodeChildren);
}
