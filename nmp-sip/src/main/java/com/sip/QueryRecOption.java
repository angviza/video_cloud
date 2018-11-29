package com.sip;

import lombok.Data;

@Data
public class QueryRecOption {
	
//	private String target="sip:38020000003000000020@192.168.2.3:5060";
//	private String to="sip:38020000001320000010@3802000000";
//	private String from="sip:38020000003000000020@3802000000";
//	private String cameraId="38020000001320000010";
//	private String startTime="2018-09-05T00:58:06";
//	private String endTime="2018-09-06T01:21:39";
//	private String type="all";
	/**
	 * 本地信令账号@IP:端口
	 */
	private String target;
	
	/**
	 * 目标信令通道@设备所在域
	 */
	private String to;
	
	/**
	 * 本地信令账号@域
	 */
	private String from;
	
	/**
	 * 通道ID
	 */
	private String cameraId;
	
	/**
	 * 格式为：2018-09-05T00:58:06
	 */
	private String startTime;
	
	/**
	 * 格式为：2018-09-05T00:58:06
	 */
	private String endTime;
	
	/**
	 * 默认填all （all 、alarm、manual）
	 */
	private String type;
	
	
}