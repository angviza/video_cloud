package com.hdvon.nmp.service;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.common.CameraPermissionVo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.*;

import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>存储服务器 服务类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IStoreServerService{
	/**
	 * 添加存储服务器
	 * @param userVo
	 * @param storeServerParamVo
	 * @param projectIds
	 */
	public void saveStoreServer(UserVo userVo, StoreServerParamVo storeServerParamVo, List<String> projectIds);
	
	/**
	 * 删除存储服务器
	 * @param ids
	 */
	public void delStoreServersByIds(UserVo userVo, List<String> ids);
	
	/**
	 * 查询存储服务器信息
	 * @param id
	 * @return storeServerParamVo
	 */
	public StoreServerVo getStoreServerById(String storeserverId) ;
	
	
	/**
	 * 分页查询存储服务器列表
	 * @param pp
	 * @param map
	 * @return
	 */
	public PageInfo<StoreServerVo> getStoreServerPages(PageParam pp, TreeNodeChildren treeNodeChildren, StoreServerParamVo storeServerParamVo);
	
	/**
	 * 查询存储服务器列表
	 * @param map
	 * @return
	 */
	public List<StoreServerVo> getStoreServerList(TreeNodeChildren treeNodeChildren, StoreServerParamVo storeServerParamVo);
	
	/**
	 * 查询存储服务器关联摄像机
	 * @param userVo
     * @param storeServerId
	 * @return
	 */
	List<CameraNode> getStoreserverCamera(UserVo userVo, String storeServerId);
	
	/**
	 * 保存存储服务器与摄像机的关联
	 * @param userVo
	 * @param storeserverId
	 * @param cameraIdList
	 */
	void relateCamerasToStoreserver(UserVo userVo, String storeserverId, List<String> cameraIdList);
	
	/**
	 * 查询当前用户在存储服务器中可见摄像机
	 * @return
	 */
	PageInfo<StoreserverCameraVo> getUserCamerasByStoreserverId(UserVo userVo,PageParam pp, StoreserverCameraVo storeserverCameraVo);
	
	void delRelatedCamerasByIds(UserVo userVo , String storeserverId, List<String> cameraIds);
	
	List<CameraPermissionVo> getStoreserverAddressCameraPermission(Map<String, Object> map);
	

	/**
	 * 保存录像定时设置
	 * @param userVo
	 * @param timingVedioParamVo
	 * @param keepDays
	 */
	void saveTimingVedioSet(UserVo userVo, TimingVedioParamVo timingVedioParamVo, Integer keepDays);
	
	/**
	 * 查询存储服务器关联的单个摄像机的定时设置
	 * @param storeCameraId
	 * @return
	 */
	List<TimingVedioResultVo> getStoreCameraTimingSet(String storeCameraId);

	/**
	 * 查询摄像机,并显示摄像机设置天数
	 * @param cameraId
	 * @return
	 */
	StoreserverCameraVo getStoreServerCameraByCameraId(String cameraId);
	
	/**
	 * 保存存储服务器关联的摄像机
	 * @param storeCameraIds
	 * @param keepDays
	 */
	void saveStoreServerCamera(String[] storeCameraIds, String keepDays);

	String getMaxCodeBycode(Map<String, Object> map);

	/**
	 * 导出存储服务器关联的摄像机
	 * @param storeserverId
	 * @return
	 */
	List<Map<String,Object>> getCameraByStoreId(UserVo userVo,String storeserverId);
}
