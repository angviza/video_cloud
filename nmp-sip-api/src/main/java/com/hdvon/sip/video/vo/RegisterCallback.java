package com.hdvon.sip.video.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class RegisterCallback implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String callId;//会话请求返回的标识
	
	private int statusCode;//状态码
	
	private long register_time; //回调当前时间
	
	private String register_errorCallId; //回调错误callId
	
	private  String username;//注册用户名
	

}
