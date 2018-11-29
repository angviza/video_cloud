package com.hdvon.sip.vo;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import lombok.Data;

@Data
public class DeviceStatusVo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private String deviceCode;
	/**
	 * 查询结果标志 OK成功
	 */
	private String result;
	/**
	 * 是否在线 ONLINE 在线 OFFLINE 离线
	 */
	private String onLine;
	/**
	 * 是否正常工作 ON 是 OFF否
	 */
	private String status;
	/**
	 * 是否编码 ON 是 OFF否 
	 */
	private String encode;
	/**
	 * 是否录像 ON 是 OFF否
	 */
	private String record;
	
	/**
	 * 设备时间和日期
	 */
	private Date deviceDate;
	
	private CountDownLatch latch;
	/**
	 * 请求响应状态
	 */
	private Integer respStatus; 
}
