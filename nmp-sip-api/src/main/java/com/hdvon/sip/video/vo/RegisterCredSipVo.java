package com.hdvon.sip.video.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class RegisterCredSipVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
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
