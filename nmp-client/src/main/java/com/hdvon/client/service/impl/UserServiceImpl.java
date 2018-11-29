package com.hdvon.client.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.client.config.redis.BaseRedisDao;
import com.hdvon.client.entity.Department;
import com.hdvon.client.entity.User;
import com.hdvon.client.exception.UserException;
import com.hdvon.client.mapper.CameraPermissionMapper;
import com.hdvon.client.mapper.DepartmentMapper;
import com.hdvon.client.mapper.UserMapper;
import com.hdvon.client.service.IUserService;
import com.hdvon.client.vo.DepartmentVo;
import com.hdvon.client.vo.UserVo;
import com.hdvon.nmp.common.SystemConstant;

import cn.hutool.crypto.digest.DigestUtil;

/**
 * <br>
 * <b>功能：</b>用户服务实现<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class UserServiceImpl  implements IUserService {
	
    /**
     * 盐值
     */
    public static final String MD5_SALT = "!@#!@#%%@#R#HD";

	@Resource
	UserMapper userMapper;
	@Resource
	CameraPermissionMapper cameraPermissionMapper;
	@Resource
	DepartmentMapper departmentMapper;

	@Resource
	BaseRedisDao redisDao;

	@Override
	public UserVo login(String username, String password) throws UserException {
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			throw new UserException("账号密码不能为空");
		}
		password = getPasswordByMd5(password);
		User model =  new User();
		model.setAccount(username);
		model.setPassword(password);
		
		User record = userMapper.selectOne(model);
	    if(record == null) {
	    	throw new UserException("账号或密码错误");
	    }else if(record.getEnable() == null || record.getEnable().intValue() == 0){
	    	throw new UserException("该账号已停用，请联系管理员");
	    }
	    UserVo vo = new UserVo();
	    BeanUtils.copyProperties(record,vo);
	    //更新上次登录时间
	    record.setLastLoginDate(new Date());
	    userMapper.updateByPrimaryKeySelective(record);
	    //获取用户部门信息
	    List<Department> deptList = departmentMapper.findRecord(record.getId());
	    if(deptList!=null && deptList.size()>0) {
	    	Department dept = deptList.get(0);
	    	vo.setDepartmentId(dept.getId());
	    	vo.setDepartmentName(dept.getName());
	    	vo.setDepCode(dept.getDepCode());
	    } 
	    
		return vo;
	}

	/**
	 * 获取广州部门列表
	 * @return
	 * @throws UserException
	 */
	@Override
	public List<DepartmentVo> getDepartmentTree() throws UserException {
		// TODO Auto-generated method stub
		
		String key = SystemConstant.redis_client_department_key+"_4401";
		if(!redisDao.exists(key)) {
			
			List<DepartmentVo> list = departmentMapper.selectGzDepartment();
			
			int expireTime = 60*60+new Random().nextInt(30);
			redisDao.addList(key, list, expireTime);
			
			return list;
		}
		List<DepartmentVo> list = (List<DepartmentVo>) redisDao.getList(key).get(0);
		return list;
		 
	}
	
	
    /**
     * 生成包含盐值md5密码
     * @param password
     * @return
     */
    public static String getPasswordByMd5(String password){
        return DigestUtil.md5Hex(password.trim() + MD5_SALT);
    }

	/**
	 * 根据部门id获取用户列表
	 * @return
	 * @throws UserException
	 */
	@Override
	public List<UserVo> getUserListByDepId(String depId) throws UserException {
		// TODO Auto-generated method stub
		return userMapper.findListByDepId(depId);
	}

}
