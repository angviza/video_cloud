package com.hdvon.sip.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 媒体资源对象
 * 
 * @author Administrator
 *
 */
@Data
public class MediaPlayVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	/**
	 * 媒体流发送者设备编码
	 */
	private String deviceCode;

	/**
	 * 发送协议
	 */
	private String transportType;

	/**
	 * 媒体流接收者设备编码
	 */
	private String receiveCode;

	/**
	 * 媒体流接受IP地址
	 */
	private String receiveIp;

	/**
	 * 媒体流接受客户端端口
	 */
	private int receivePort;

	/**
	 * 视频播放状态
	 */
	private Integer status;
	
	/**
	 * 对应客户端事务call-ID
	 */
	private String callId;
	
	/**
	 * 注册用户
	 */
	private String registerCode;
	
	
}
