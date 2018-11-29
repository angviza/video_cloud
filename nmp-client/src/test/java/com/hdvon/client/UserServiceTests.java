package com.hdvon.client;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.hdvon.client.service.IUserService;
import com.hdvon.client.vo.UserVo;

import cn.hutool.crypto.digest.DigestUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {
    @Autowired
    IUserService service;
    
    /**
     * 盐值
     */
    public static final String MD5_SALT = "!@#!@#%%@#R#HD";
    
//	@Test
//	public void getAddressCameraPermissionTreeTest() {
//		String depId ="3886151559020544";//超管
//
//		System.out.println("=========================根据部门id获取部门下的所有用户列表");
//		List<UserVo> list = service.getUserListByDepId(depId);
//		System.out.println("=========================【depId:"+depId+"】"+JSON.toJSONString(list));
//		
//
//	}
	
	@Test
	public void getLoginTest() {
		String username = "admin";
		String password = "1";
		System.out.println("=========================用户登录");
		UserVo list =  service.login(username, password);
		System.out.println("========================="+JSON.toJSONString(list));
		

	}
    
   
}
