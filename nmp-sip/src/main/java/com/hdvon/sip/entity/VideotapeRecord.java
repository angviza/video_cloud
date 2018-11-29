package com.hdvon.sip.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

@Data
@XStreamAlias("Response")
public class VideotapeRecord {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("CmdType")
	private String cmdType;
	
	@XStreamAlias("SN")
	private Long sn;
	
	@XStreamAlias("DeviceID")
	private String deviceCode;
	
	
	@XStreamAlias("Result")
	private String result;
}
