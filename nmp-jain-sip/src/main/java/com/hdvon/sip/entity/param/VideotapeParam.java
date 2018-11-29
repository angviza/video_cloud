package com.hdvon.sip.entity.param;

import java.io.Serializable;

import lombok.Data;
/**
 * 录像控制请求参数
 * @author wanshaojian
 *
 */
 @Data
public class VideotapeParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * 录像摄像机设备编码
	 */
	private String deviceID;
	
	
	/**
	 * 录像类型： start表示开始录像   stop表示停止录像
	 */
	private String type;
	

}
