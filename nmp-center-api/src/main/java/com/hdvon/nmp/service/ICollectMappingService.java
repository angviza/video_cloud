package com.hdvon.nmp.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.CollectMappingVo;

/**
 * <br>
 * <b>功能：</b>收藏夹关联表 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface ICollectMappingService{
	/**
	 * 获取用户收藏摄像机列表
	 * @param account 用户账号
	 * @param collectId 收藏夹id
	 * @param 分页对象
	 * @return
	 */
	PageInfo<CollectMappingVo> getListByPage(String account,String collectId, PageParam pageParam);
	/**
	 * 获取用户收藏摄像机列表
	 * @param collectId 收藏夹id
	 * @param 分页对象
	 * @return
	 */
	List<CollectMappingVo> getCollectCameraList(String collectId);
	/**
	 * 收藏摄像机
	 * @param collectId 收藏夹id
	 * @param deviceIds 摄像机id数组
	 * @return 添加成功 返回 true 失败返回false
	 */
	boolean saveMapping(String collectId,String [] deviceIds);
	/**
	 * 删除收藏摄像机
	 * @param deviceIds 摄像机id数组
	 */
	void deleteMapping(String [] deviceIds);
}
