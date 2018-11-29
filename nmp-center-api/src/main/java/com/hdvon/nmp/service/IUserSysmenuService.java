package com.hdvon.nmp.service;

import com.hdvon.nmp.vo.UserSysmenuVo;

/**
 * <br>
 * <b>功能：</b>用户自定义菜单关联表 服务类<br>
 * <b>作者：</b>zhanqf<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IUserSysmenuService {
	
	/**
	 * 根据用户id删除自定义菜单数据。
	 * @param userId
	 * @return
	 */
	Integer deleteByUserid(String userId);
	
	/**
	 * 根据用户id、菜单id查询。
	 * @param userId
	 * @param menuId
	 * @return
	 */
	UserSysmenuVo findByUseridAndMenuid(
			String userId,
			String menuId);
	
	/**
	 * 根据用户id、菜单id判断是否存在。
	 * @param userId
	 * @param menuId
	 * @return
	 */
	Boolean existByUseridAndMenuid(
			String userId,
			String menuId);
	
	/**
	 * 保存。
	 * @param vo
	 * @return
	 */
	Integer save(UserSysmenuVo vo);
	
	/**
	 * 根据用户id、菜单id删除自定义菜单数据。
	 * @param userId
	 * @param menuId
	 * @return
	 */
	Integer delete(
			String userId,
			String menuId);
	
	/**
	 * 修改。
	 * @param vo
	 * @return
	 */
	Integer modify(UserSysmenuVo vo);
	
}
