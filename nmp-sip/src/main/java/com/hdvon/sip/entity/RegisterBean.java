package com.hdvon.sip.entity;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

import lombok.Data;

/**
 * 注册用户对象
 * @author wanshaojian
 *
 */
@Data
public class RegisterBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//注册用户
	String registerCode;
	//注册状态
	Integer status;
	//有效期
	int expires;
	
	
	CountDownLatch latch;
	
	public RegisterBean(String registerCode,int expires) {
		this.registerCode = registerCode;
		this.expires = expires;
	}
}
