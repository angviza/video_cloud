package com.hdvon.sip.entity;

import java.io.Serializable;

import lombok.Data;
/**
 * 点播返回实体
 * @author wanshaojian
 *
 */
@Data
public class InviteEntity implements Serializable{
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
	 * 整个SDP消息体
	 */
	private String sdp;
	/**
	 * 接受流的端口
	 */
	private int port;
	
	/**
	 * 下载倍数
	 */
	private int speed;
}
