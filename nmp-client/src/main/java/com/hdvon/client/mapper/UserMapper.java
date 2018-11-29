package com.hdvon.client.mapper;

import java.util.List;

import com.hdvon.client.config.mybatis.MyMapper;
import com.hdvon.client.entity.User;
import com.hdvon.client.vo.UserVo;
/**
 * 用户mapper
 * @author wanshaojian
 *
 */
public interface UserMapper extends MyMapper<User>{
	/**
	 * 根据userIds获取用户列表
	 * @param userIds
	 * @return
	 */
	List<User> findListByIds(String userIds);
	/**
	 * 根据depId获取用户列表
	 * @param depId
	 * @return
	 */
	List<UserVo> findListByDepId(String depId);
	
	/**
	 * 根据资源角色获取所有的用户id
	 * @param roleId
	 * @return
	 */
	List<String> getUserIdsByRoleId(String roleId);
}
