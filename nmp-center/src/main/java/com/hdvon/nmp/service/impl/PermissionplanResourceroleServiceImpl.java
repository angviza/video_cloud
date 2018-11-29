package com.hdvon.nmp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.mapper.PermissionplanResourceroleMapper;
import com.hdvon.nmp.service.IPermissionplanResourceroleService;

/**
 * <br>
 * <b>功能：</b>权限预案关联资源角色表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class PermissionplanResourceroleServiceImpl implements IPermissionplanResourceroleService {

	@Autowired
	private PermissionplanResourceroleMapper permissionplanResourceroleMapper;

}
