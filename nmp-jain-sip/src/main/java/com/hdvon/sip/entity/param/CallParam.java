package com.hdvon.sip.entity.param;

import java.io.Serializable;

import lombok.Data;
/**
 * 根据callId进行取消或者停止播放
 * @author wanshaojian
 *
 */
@Data
public class CallParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 会话id
	 */
	private String callId;
	/**
	 * 设备编码
	 */
	private String deviceID;
}
