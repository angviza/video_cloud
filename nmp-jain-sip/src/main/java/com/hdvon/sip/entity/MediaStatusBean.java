package com.hdvon.sip.entity;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

@Data
@XStreamAlias("Response")
public class MediaStatusBean implements Serializable{
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
	
	
	@XStreamAlias("NotifyType")
	private String notifyType;
}
