package com.hdvon.sip.entity.param;

import java.io.Serializable;

import lombok.Data;
/**
 * 实时点播请求对象
 * @author wanshaojian
 *	1:protocol 为28181协议参数必填参数
 *          transport UDP, TCP-Active或TCP-Passive
 *			host 接收地址
 *			port 接收端口
 *			encode	String类型	转动需要用到
 *  2：protocol 为rtsp协议参数必填参数
 *     transport	TCP, UDP
 *     url	rtsp://…..
 */
@Data
public class InviteParam implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 设备编码
	 */
	private String deviceID;
	/**
	 * 
	 */
    private String protocol;
	/**
	 * 媒体流接受者ip
	 */
	private String host;
	/**
	 * 媒体流接受者端口
	 */
	private Integer port;
	/**
	 * 协议方式
	 */
	private String transport;

	/**
	 * 转码
	 */
	private String encode;
	/**
	 * 播放地址
	 */
	private String uri;
	/**
	 * 超时时间
	 */
	private Long expire;
}
