package com.hdvon.nmp.service;

import java.util.List;

import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.vo.AddressAndCameraVo;
import com.hdvon.nmp.vo.ResourceroleAddressPermissionVo;
import com.hdvon.nmp.vo.ResourceroleCameraPermissionVo;

/**
 * <br>
 * <b>功能：</b>资源角色与摄像头权限关联表 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IResourceroleCameraPermissionService {

    /**
     * 查询资源角色的摄像机权限列表
     * @param resourceroleId
     * @return
     */
    List<ResourceroleCameraPermissionVo> getCameraPermission(String resourceroleId);

    /**
     * 保存资源角色与摄像机权限关联
     * @param resourceVo
     */
	void saveCameraPermission(ResourceroleAddressPermissionVo resourceVo);

}
