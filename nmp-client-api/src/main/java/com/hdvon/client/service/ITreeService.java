package com.hdvon.client.service;

import java.util.List;

import com.hdvon.client.exception.CameraException;
import com.hdvon.client.exception.UserException;
import com.hdvon.client.vo.CameraPermissionVo;
/**
 * <br>
 * <b>功能：</b>摄像机行政区域树服务接口<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018-6-20<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface ITreeService {

	
	
	/**
	 * 获取地址树
	 * @return
	 * @throws UserException
	 */
	List<CameraPermissionVo> getAddressTree() throws CameraException;
	
	

	
	/**
	 * 获取当前行政树
	 * @return
	 * @throws UserException
	 */
	List<CameraPermissionVo> getOrganizationTree() throws CameraException;
	

	
	/**
	 * 获取项目分组树
	 * @return
	 * @throws UserException
	 */
	List<CameraPermissionVo> getProjectTree() throws CameraException;
	

	
	/**
	 * 获取当前自定义分组树
	 * @param userId 用户id
	 * @return
	 * @throws UserException
	 */
	List<CameraPermissionVo> getCameraGroupTree(String userId) throws CameraException;
	
		
	
}
