package com.hdvon.nmp.service;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.*;

import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>状态服务器 服务类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IStatusServerService{
	
	/**
	 * 添加状态服务器
	 * @param userVo
	 * @param statusServerParamVo
	 * @param projectIds
	 */
	public void saveStatusServer(UserVo userVo, StatusServerParamVo statusServerParamVo, List<String> projectIds);
	
	/**
	 * 删除状态服务器
	 * @param ids
	 */
	public void delStatusServersByIds(List<String> ids);
	
	/**
	 * 查询状态服务器信息
	 * @param id
	 * @return storeStatusParamVo
	 */
    public StatusServerVo getStatusServerById(String statusserverId) ;
	
	
	/**
	 * 分页查询状态服务器列表
	 * @param pp
	 * @param map
	 * @return
	 */
	public PageInfo<StatusServerVo> getStatusServerPages(PageParam pp, TreeNodeChildren treeNodeChildren, StatusServerParamVo statusServerParamVo);
	
	/**
	 * 查询状态服务器列表
	 * @param map
	 * @return
	 */
	public List<StatusServerVo> getStatusServerList(TreeNodeChildren treeNodeChildren, StatusServerParamVo statusServerParamVo);
	
	/**
	 * 查询状态服务器关联的摄像机
	 * @param userVo
     * @param statusServerId 状态服务器id
	 * @return
	 */
    List<CameraNode> getStatusServerCamera(UserVo userVo, String statusServerId);

    /**
	 * 保存状态服务器与摄像机的关联
	 * @param userVo
	 * @param statusserverId
	 * @param cameraIdList
	 */
	void relateCamerasToStatusserver(UserVo userVo, String statusserverId, List<String> cameraIdList);
	
	/**
	 * 查询当前用户在状态服务器中可见摄像机
	 * @return
	 */
	PageInfo<StatusserverCameraVo> getUserCamerasByStatusserverId(UserVo userVo,PageParam pp, StatusserverCameraVo statusserverCameraVo);
	
	void delRelatedCamerasByIds(UserVo userVo , String statusserverId, List<String> cameraIds);

	public String getMaxCodeBycode(Map<String, Object> map);
	
//	List<CameraPermissionVo> getStatusserverAddressCameraPermission(Map<String, Object> map);
}
