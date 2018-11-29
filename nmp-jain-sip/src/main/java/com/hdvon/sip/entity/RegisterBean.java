package com.hdvon.sip.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * sip register 通用对象
 * @author wanshaojian
 *
 */
@Data
public class RegisterBean extends BaseSipBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//注册用户
	String registerCode;
	//状态
	String state;
	
	//有效期
	int expires;
	
	
	public RegisterBean(String registerCode,int expires) {
		this.registerCode = registerCode;
		this.expires = expires;
	}
}
