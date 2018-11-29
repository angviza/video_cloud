package com.hdvon.sip.entity;

import java.io.Serializable;

import lombok.Data;
/**
 * sip invate 通用对象
 * @author wanshaojian
 *
 */
@Data
public class InviteBean extends BaseSipBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 设备编码
	 */
	private String deviceID;
	/**
	 * 协议方式
	 */
	private String transport;
	/**
	 * 设备编码
	 */
	private String callId;
	/**
	 * 状态
	 */
	private String state;
	/**
	 * 编码
	 */
	private String encoder;
	/**
	 * 接受流的ip
	 */
	private String host;
	/**
	 * 接受流的端口
	 */
	private int port;
	
	/**
	 * 下载倍数
	 */
	private int speed;
}
