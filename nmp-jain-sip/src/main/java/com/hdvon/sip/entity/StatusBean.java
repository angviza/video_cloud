package com.hdvon.sip.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class StatusBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private String cmdType;
	
	private Long sn;
	
	private String deviceID;
	
	
	private String notifyType;
}
