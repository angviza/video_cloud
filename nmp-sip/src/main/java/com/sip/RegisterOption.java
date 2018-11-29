package com.sip;

import lombok.Data;

@Data
public class RegisterOption {
	
	/**
	 * sip:信令中心ID@信令中心域，例：sip:34020000002000000001@3402000000
	 */
	private String server;
	/**
	 * sip:本地信令账号@本地账号域，例：sip:34020000001320000009@3402000000
	 */
	private String from;
	/**
	 * sip:本地信令账号@信令中心域（提供第三方注册使用，本客户端填写本地信令账号），例：sip:34020000001320000009@3402000000
	 */
	private String to;
	/**
	 * sip:本地信令账号@下一跳信令的IP地址，（填本地IP地址）:下一跳信令的端口（填本地端口），例：sip:34020000001320000009@192.168.2.112:5060
	 */
	private String contact;
	/**
	 * sip:信令中心的路由IP地址:端口，例：sip:192.168.2.169:5060
	 */
	private String route;
	//private int expire;
	
}

