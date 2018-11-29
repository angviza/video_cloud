package com.hdvon.nmp.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.mapper.UserLoginInfoMapper;
import com.hdvon.nmp.service.IUserLoginInfoService;
import com.hdvon.nmp.util.PageParam;

/**
 * <br>
 * <b>功能：</b>Service<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class UserLoginInfoServiceImpl implements IUserLoginInfoService {

	@Autowired
	private UserLoginInfoMapper userLoginInfoMapper;

	@Override
	public PageInfo<Map<String, Object>> getOnlineUsersPage(Map<String, Object> map, PageParam pageParam) {
		
		PageHelper.startPage(pageParam.getPageNo(), pageParam.getPageSize());
		List<Map<String, Object>> list = userLoginInfoMapper.selectOnlineUsersList(map);
		
		return new PageInfo<>(list);
	}

	@Override
	public Long getOnlineUsersCount(Map<String, Object> map) {
		return userLoginInfoMapper.selectOnlineUsersCount(map);
	}

	@Override
	public List<Map<String, Object>> getOnlineUsersList(Map<String, Object> param) {
		return userLoginInfoMapper.selectOnlineUsersList(param);
	}

}
