package com.hdvon.sip.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import lombok.Data;

/**
 * 媒体资源对象
 * 
 * @author Administrator
 *
 */
@Data
public class MediaDownloadVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	/**
	 * 媒体流发送者设备编码
	 */
	private String deviceCode;

	/**
	 * 发送协议
	 */
	private String transportType;

	/**
	 * 媒体流接收者设备编码
	 */
	private String receiveCode;

	/**
	 * 媒体流接受IP地址
	 */
	private String receiveIp;
	
	/**
	 * 媒体流接受端端口
	 */
	private Integer receivePort;


	/**
	 * 视频播放状态
	 */
	private Integer status;

	/**
	 * 以下参数为视频回看和下载参数 开始时间
	 */
	private Date startDate;

	/**
	 * 以下参数为视频回看和下载参数 结束时间
	 */
	private Date endDate;

	/**
	 * 以下参数为视频回看和下载参数 视音频文件的URI
	 */
	private String uri;
	
	/**
	 * 对应客户端事务call-ID
	 */
	private String callId;


	private CountDownLatch latch;
}
