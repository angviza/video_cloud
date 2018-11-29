package com.sip;

import lombok.Data;

@Data
public class PresetOption {
	
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
	 * 通道ID，设备ID
	 */
	private String cameraId;
	/**
	 * 优先级
	 */
	private int iPriority = 1;
	/**
	 * 预置位控制类型
	 */
	private int presetType;
	/**
	 * 预置位号
	 */
	private int presetNum;
}
