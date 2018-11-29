package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.UserLoginInfo;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

public interface UserLoginInfoMapper extends Mapper<UserLoginInfo> , MySqlMapper<UserLoginInfo> {
	
	public List<Map<String, Object>> selectOnlineUsersList(Map<String, Object> map);
	
	public Long selectOnlineUsersCount(Map<String, Object> map);
}