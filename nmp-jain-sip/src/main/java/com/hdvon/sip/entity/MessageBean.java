package com.hdvon.sip.entity;

import java.io.Serializable;

import lombok.Data;
/**
 * sip message通用对象
 * @author wanshaojian
 *
 */
@Data
public class MessageBean extends BaseSipBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 目标设备编码
	 */
	private String targetDeviceCode;
	
	/**
	 * sn编码
	 */
	private Long sn;
	/**
	 * 录像翻查的开始时间
	 */
	private String startTime;
	/**
	 * 录像翻查的结束时间
	 */
	private String endTime;
	
	/**
	 * sdpDate报文
	 */
	private String sdpData;

	
	/**
	 * 保存会话id
	 */
	private String callId;
	
	/**
	 * 预置位控制类型
	 */
	private String type;
	
	/**
	 * 预置位编号
	 * 范围1-255
	 */
	private Integer presetNum;
	/**
	 * 预置位名称
	 */
	private String presetName;

	
	/**
	 * 巡航组号
	 */
	private Integer groupNum;
	/**
	 * 巡航停留时间
	 */
	private Long stayTime;
	/**
	 * 巡航速度 
	 */
	private Integer speed; 
	
	/**
	 * 注册用户
	 */
	private String registerCode;
	/**
	 * 是否websocket
	 *   后台消息为 false
	 */
	private Boolean isWebsocket;
    
}
