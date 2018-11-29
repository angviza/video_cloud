package com.hdvon.sip.entity;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

@Data
@XStreamAlias("Response")
public class DeviceStatusBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("CmdType")
	private String cmdType;
	
	@XStreamAlias("SN")
	private Long sn;
	
	@XStreamAlias("DeviceID")
	private String deviceID;
	
	
	@XStreamAlias("Result")
	private String result;
	
	@XStreamAlias("Online")
	private String onLine;
	
	@XStreamAlias("Status")
	private String status;
	
	@XStreamAlias("Encode")
	private String encode;
	
	@XStreamAlias("Record")
	private String record;
	
	
	@XStreamAlias("DeviceTime")
	private String deviceTime;
}
