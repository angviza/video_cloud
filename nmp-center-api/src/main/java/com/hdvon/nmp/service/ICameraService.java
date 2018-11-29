package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.CheckAttributeVo;
import com.hdvon.nmp.vo.DeviceVo;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.CameraCameragroupVo;
import com.hdvon.nmp.vo.CameraParamVo;
import com.hdvon.nmp.vo.UserVo;
import com.hdvon.nmp.vo.sip.UserDeviceParamVo;

/**
 * <br>
 * <b>功能：</b>摄像机表 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface ICameraService{

	/**
	 * 根据id查询摄像机
	 * @param id
	 * @return
	 */
	CameraVo getCameraInfo(String id);

	/**
	 * 根据ids删除摄像机(包括摄像机中间关联表)
	 * @param idList
	 */
	String delCameras(List<String> idList);

	PageInfo<CameraVo> getCameraByPage(CameraParamVo addressVo, TreeNodeChildren treeNodeChildren, PageParam pageParam);
	
	/**
	 * 根据设备编码查询摄像机
	 * @param sbbm
	 * @return
	 */
	List<CameraVo> getCameraBySbbm(String sbbm);

	/**
	 * 设置分组
	 * @param cameraIds
	 * @param groupId
	 */
	void setGroup(List<String> cameraIds, String groupId);

	/**
	 * 摄像机移除分组
	 * @param idList
	 */
	void delCameracroup(List<String> idList);
	
	/**
	 * 查询多个业务分组中的摄像机列表
	 * @param groupIds
	 * @return
	 */
	List<CameraCameragroupVo> getCamerasByGroupIds(List<String> groupIds);

	/**
	 * 查看当前用户是否拥有某个操作权限
	 */
	List<UserDeviceParamVo> getUserCameraPermission(Map<String, Object> param);


	/**
	 * 根据设备id查询摄像机
	 * @param map
	 * @return
	 */
	List<CameraVo> getCameraByDeviceId(String deviceId);
	
	/**
	 * 批量导入摄像机
	 * @param list
	 * @param titles
	 */
	String batchInsertCameras(List<Map<String, String>> list, List<CheckAttributeVo> titles, UserVo userVo, Map<String,List<String>> relateIdMap);
	
	List<Map<String,Object>> getCamerasByGroupId(String groupId);
	
	/**
	 * 根据摄像机编号集合查询摄像机
	 * @param cameraCodes
	 * @return
	 */
	List<DeviceVo> getcameraByCodes(List<String> cameraCodes);
	
	/**
	 * 根据摄像机id集合查询摄像机列表
	 * @param ids
	 * @return
	 */
	List<CameraVo> getCameraByIds(List<String> ids);
	

	/**
	 * 
	 * 根据摄像机id 获取device 集合
	 * @param param
	 * @return
	 */
	List<String> getDeviceIdByCamearId(Map<String, Object> param);
}
