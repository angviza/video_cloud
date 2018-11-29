package com.hdvon.sip.entity.param;

import java.io.Serializable;

import lombok.Data;

/**
 * 录像下载请求参数
 * @author wanshaojian
 *
 */
@Data
public class DownloadParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 设备id
	 */
	private String deviceID;
	/**
	 * 发送协议
	 */
    private String protocol;
	/**
	 * 发送协议类型
	 */
	private String transport;


	/**
	 * 媒体流接受IP地址
	 */
	private String host;
	
	/**
	 * 媒体流接受端端口
	 */
	private Integer port;

	/**
	 * 以下参数为视频回看和下载参数 开始时间
	 */
	private String startTime;

	/**
	 * 以下参数为视频回看和下载参数 结束时间
	 */
	private String endTime;

	/**
	 * 以下参数为视频回看和下载参数 视音频文件的URI
	 */
	private String uri;
	
	/**
	 * 下载倍数
	 */
	private Integer speed;
}
