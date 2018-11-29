package com.hdvon.nmp.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.CollectVo;
import com.hdvon.nmp.vo.UserVo;

/**
 * <br>
 * <b>功能：</b>收藏表 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface ICollectService{

	/**
	 * 获取用户收藏列表
	 * @param account 用户账号
	 * @return
	 */
	List<CollectVo> getList(UserVo user);
	

	/**
	 * 获取用户收藏列表
	 * @param account 用户账号
	 * @return
	 */
	PageInfo<CollectVo> getCollectByPage(UserVo user, PageParam pageParam);
	
	
	/**
	 * 判断收藏夹是否存在
	 * @param name 收藏夹名称
	 * @param account 用户账号
	 */
	boolean existsCollect(String name,UserVo user);
	
	/**
	 * 新建收藏夹
	 * @param record
	 * @param account 用户账号
	 */
	void saveCollect(CollectVo record,UserVo user);
	/**
	 * 删除收藏夹
	 * @param record
	 */
	void deleteCollect(CollectVo record);
	
	/**
	 * 删除收藏夹
	 * @param record
	 */
	void batchDeleteCollect(String[] ids);
}
