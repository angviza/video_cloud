package com.hdvon.sip.entity;

import java.io.Serializable;

import lombok.Data;
/**
 * 不需要websock时候，通用返回对象
 * @author wanshaojian
 *
 */
@Data
public class SipResultBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 会话id
	 */
	private String callId;
	/**
	 * 返回回话状态
	 */
	private String state;
	
	
	public SipResultBean(String callId,String state) {
		this.callId = callId;
		this.state = state;
	}
}
