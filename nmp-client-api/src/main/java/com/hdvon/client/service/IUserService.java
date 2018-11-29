package com.hdvon.client.service;

import java.util.List;

import com.hdvon.client.exception.UserException;
import com.hdvon.client.vo.DepartmentVo;
import com.hdvon.client.vo.UserVo;

public interface IUserService {
	/**
	 * 用户登录方法
	 * @param username 账户
	 * @param password 密码
	 * @return
	 * @throws UserException
	 */
	UserVo login(String username , String password) throws UserException;
	
	
	
	/**
	 * 获取广州部门列表
	 * @return
	 * @throws UserException
	 */
	List<DepartmentVo> getDepartmentTree() throws UserException;
	
	
	/**
	 * 根据部门id获取用户列表
	 * @return
	 * @throws UserException
	 */
	List<UserVo> getUserListByDepId(String depId) throws UserException;
}
