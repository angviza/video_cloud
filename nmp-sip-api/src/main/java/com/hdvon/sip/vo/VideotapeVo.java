package com.hdvon.sip.vo;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

import lombok.Data;
/**
 * 录像对象
 * @author wanshaojian
 *
 */
 @Data
public class VideotapeVo implements Serializable{

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
	 * 对应客户端事务call-ID
	 */
	private String callId;

	/**
	 * 录像状态
	 */
	private Integer status;
	
	private CountDownLatch latch;
}
