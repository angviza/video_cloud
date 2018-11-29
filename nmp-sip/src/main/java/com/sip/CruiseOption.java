package com.sip;

import lombok.Data;

@Data
public class CruiseOption {
	
	/**
	private String target="sip:38020000003000000020@192.168.2.3:5060";
	private String to="sip:38020000001320000010@3802000000";
	private String from="sip:38020000003000000020@3802000000";
	private String cameraId="38020000001320000010";
	
	private int iPriority = 10;
	public int cmdType = 1;
	
	public int groupNum = 1;
	public int presetNum = 2;
	public int stayTime = 10;
	public int speed = 3021;
	**/
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
	 * 优先级
	 */
	private int iPriority = 1;
	/**
	 * 巡航控制类型   加入巡航点：1； 删除一个巡航点：2； 设置巡航速度：3； 设置巡航停留时间：4； 开始巡航：5； 停止巡航：0(使用云台控制的停止命令)
	 */
	private int cmdType;
	/**
	 * 巡航组号 取值范围：0-255
	 */
	private int groupNum;
	/**
	 * 预置位号  1-255 （0为预留， 当cmdType = 2,删除操作时才能填写0，表示删除整组预置位）
	 */
	private int presetNum;
	/**
	 * 巡航停留时间 单位：s
	 */
	private int stayTime;
	/**
	 * 巡航速度
	 */
	private int speed;
}