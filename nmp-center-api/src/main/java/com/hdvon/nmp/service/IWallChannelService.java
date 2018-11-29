package com.hdvon.nmp.service;

import java.util.List;

import com.hdvon.nmp.vo.WallChannelVo;

/**
 * <br>
 * <b>功能：</b>上墙轮巡的矩阵通道表 服务类<br>
 * <b>作者：</b>zhanqf<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IWallChannelService{
	
	/**
	 * 根据上墙轮巡主表id查询。
	 * @param id
	 * @return
	 */
	public List<WallChannelVo> findByRotateId(String id);
	
	/**
	 * 更新当前播放的摄像机id
	 * @param id
	 * @param cameraId
	 */
	public void updateCameraId(
			String id,
			String cameraId);
	
	/**
	 * 根据上墙轮巡主表id删除。
	 * @param id
	 */
	public void deleteByRotateId(String id);
	
	public void saveList(
			String rotateId,
			List<String> idLst);

}
