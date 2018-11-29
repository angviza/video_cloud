package com.sip;

import lombok.Data;

@Data
public class RegisterCred {
	
	/**
	 * 填*，不改
	 */
	private String realm;
	/**
	 * 本地信令账号
	 */
	private String username;
	/**
	 * 信令密码
	 */
	private String password;
	/**
	 * 加密方式。填Digest，不改
	 */
	private String schema;

}
