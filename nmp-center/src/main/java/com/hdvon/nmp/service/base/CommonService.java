package com.hdvon.nmp.service.base;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdvon.nmp.entity.UserLog;
import com.hdvon.nmp.mapper.SysmenuMapper;
import com.hdvon.nmp.mapper.UserLogMapper;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.SysmenuVo;
import com.hdvon.nmp.vo.UserLogVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.util.StrUtil;

@Component
public class CommonService {

	@Autowired
    private SysmenuMapper sysmenuMapper;
	
	@Autowired
	private UserLogMapper userLogMapper;
	  
    
    public void saveUserLog(UserLogVo logVO) {
    	
		UserLog userLog = new UserLog();
		if (null == logVO.getId()) {
			userLog.setId(IdGenerator.nextId());
			userLog.setAccount(logVO.getAccount());
			userLog.setMenuId(logVO.getMenuId());
			userLog.setContent(logVO.getContent());
			userLog.setName(logVO.getName());
			userLog.setType(logVO.getType());
			userLog.setTokenId(logVO.getTokenId());
			userLog.setOperationObject(logVO.getOperationObject());
			userLog.setOperationTime(logVO.getOperationTime());
			userLog.setResponseTime(logVO.getResponseTime());
			userLogMapper.insertSelective(userLog);
		} else {
			userLog.setOperationTime(logVO.getOperationTime());
			userLog.setResponseTime(logVO.getResponseTime());
			userLog.setUpdateTime(new Date());
			userLogMapper.updateByPrimaryKeySelective(userLog);
		}
	}

}
