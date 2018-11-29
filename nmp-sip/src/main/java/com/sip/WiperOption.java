package com.sip;

import lombok.Data;

@Data
public class WiperOption {
	
//	private String target="sip:38020000003000000020@192.168.2.3:5060";
//	private String to="sip:38020000001320000010@3802000000";
//	private String from="sip:38020000003000000020@3802000000";
//	private String cameraId="38020000001320000010";
//	public int cmdType = 1;
//	private int iPriority = 10;
	
	/**
	 * SIP: 本地信令账号@IP:端口
	 */
	private String target;
	
	/**
	 * SIP: 目标信令通道@设备所在域
	 */
	private String to;
	
	/**
	 * SIP：本地信令账号@域
	 */
	private String from;
	
	/**
	 * 通道ID
	 */
	private String cameraId;
	
	/**
	 * 控制类型：//1：开  //2：
	 */
	public int cmdType;
	
	/**
	 * 优先级
	 */
	private int iPriority;
	
	
	
}