package com.hdvon.nmp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.entity.UserDepartment;
import com.hdvon.nmp.mapper.UserDepartmentMapper;
import com.hdvon.nmp.service.IUserDepartmentService;

import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>用户与部门关联表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class UserDepartmentServiceImpl implements IUserDepartmentService {

	@Autowired
	private UserDepartmentMapper userDepartmentMapper;

	@Override
	public String findDepartmentidByUserid(String userId) {
		Example exm = new Example(UserDepartment.class);
		exm.createCriteria()
			.andEqualTo("userId", userId);
		UserDepartment dpr = this.userDepartmentMapper.selectOneByExample(exm);
		if (dpr == null) {
			return null;
		} // if
		return dpr.getDepId();
	}
	
	

}
