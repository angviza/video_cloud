package com.sip;

import lombok.Data;

@Data
public class ControlOption {
	
	/**
	 * SIP: 本地信令账号@IP:端口，例：sip:34020000002000000001@192.168.2.169:5060
	 */
	private String target;
	/**
	 * SIP: 目标信令通道@设备所在域，例：sip:34020000001320000010@3402000000
	 */
	private String to;
	/**
	 * SIP：本地信令账号@域，例：sip:34020000001320000009@3402000000
	 */
	private String from;
	/**
	 * 通道ID，即设备ID
	 */
	private String cameraId;
	/**
	 * 方向速度
	 */
	private int iDSpeed;
	/**
	 * ZOOM速度
	 */
	private int iZSpeed;
	/**
	 * IRIS速度
	 */
	private int iISpeed;
	/**
	 * focus速度
	 */
	private int iFSpeed;
	/**
	 * 优先级
	 */
	private int iPriority=1;
	/**
	 *  控制方向
		上：1
		下：2
		左：3
		右：4
		左上：5
		左下：6
		右上：7
		右下：8
	 */
	private int direction;
	/**
	 *  控制镜头倍数，用于云台控制微调变焦
		缩小：1,
		放大：2
	 */
	private int zoom;
	/**
	 *  控制镜头光圈
		缩小：1,
		放大：2
	 */
	private int iris;
	/**
	 *  控制镜头焦距，用于云台控制微调调焦
		近：1,
		远：2
	 */
	private int focus;
	/**
	 * 是否取消摄像头控制
	   0 ：停止
	 */
	private int iStop=1;
}

