package com.hdvon.sip.entity;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

@Data
@XStreamAlias("Response")
public class PresetQueryBean implements Serializable{
	
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

	@XStreamAlias("SumNum")
	private Integer sumNum;
	
	@XStreamAlias("PresetList")
	private List<PresetItemBean> recordList;
	
    /**
     * 会话id
     */
	private String callId;
	/**
	 * 消息状态
	 */
	private String state;

	
	@Data
	@XStreamAlias("Item")
	public class PresetItemBean{
		@XStreamAlias("PresetID")
		private String presetID;
		
		@XStreamAlias("PresetName")
		private String presetName;
		
	}
}
