package com.hdvon.sip.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * 录像请求对象
 * @author wanshaojian
 *
 */
 @Data
public class VideotapeQuery implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * 录像摄像机设备编码
	 */
	private String deviceCode;
	
	/**
	 * 存储服务器编码
	 */
	private String storageServerCode;
	
	/**
	 * 控制类型： 1：开始录像 2：停止录像
	 */
	private int cmdType;
	

}
