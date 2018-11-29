package com.hdvon.sip.entity;

import java.io.Serializable;

import lombok.Data;
/**
 * sip bye 通用对象
 * @author wanshaojian
 *
 */
@Data
public class ByeBean extends BaseSipBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 设备编码
	 */
	private String callId;
	/**
	 * 状态
	 */
	private String state;

}
