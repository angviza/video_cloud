package com.hdvon.sip.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix="sip")
@PropertySources({
        @PropertySource(value = {"classpath:sip.properties"},ignoreResourceNotFound = true),
        @PropertySource(value={"file:sip.properties"},ignoreResourceNotFound = true)
})
@Getter
@Setter
public class SipConfig {
	//信令服务器IP
	private String serverIp;
	
	//信令服务器端口	
	private Integer serverPort;
	
	//sip客户端IP
	private String clientIp;
	
	//sip客户端默认端口
	private Integer clientPort;
	
	//媒体视频流接受设备用户
	private String receiverCode;
	
	//默认摄像机设备编码【媒体流发送者设备编码】
	private String defaultDeviceCode;
	
	//sip注册账号列表
	private String userlist;
	
	//sip默认注册账号
	private String defaultUserName;
	
	//sip注册账号鉴权默认密码
	private String defaultPassword;
	
	//sip有效时长
	private Integer expire;
	
	//sip用户通道
	private String domain;
	
	//存储服务器账号
	private String defaultStorageServerCode;
	
	
	//sip连接超时时间(已秒为单位)
	private int connectionTimeout;
	
	/**
	 * 预置位列表调用间隔时间
	 */
	private Long presetListInvokeInterval;
	
	
	private boolean defaultFlag;
}
