package com.hdvon.sip.video.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class CallbackResponseVo implements Serializable {
private static final long serialVersionUID = 1L;
	
	private int errorCode;//错误代码
	
	private int statusCode;//状态码
	
	private String callId;//会话请求返回的标识
	
	private String localIp;//应用服务器的IP地址
	
	private int localPort;//应用服务器的端口号
	
	private String localTransport;//应用服务器的传输协议
	 
	private String remoteIp;//远程媒体服务器的IP地址
	
	private int remotePort;//远程媒体服务器的端口号
	
	private String remoteTransport;//远程媒体服务器的传输协议
	
	private String errorDesc;//错误描述
	
	private String presetList;//预置为查询结果
	
	private Object recordList;//录像查询结果
}
