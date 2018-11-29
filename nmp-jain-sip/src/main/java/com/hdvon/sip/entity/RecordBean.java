package com.hdvon.sip.entity;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

@Data
@XStreamAlias("Response")
public class RecordBean implements Serializable{
	
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
	
	@XStreamAlias("Name")
	private String name;
	
	@XStreamAlias("SumNum")
	private Integer sumNum;
	
	@XStreamAlias("RecordList")
	private List<RecordItemBean> recordList;
	
	/**
	 * 录像翻查的开始时间
	 */
	private String startTime;
	/**
	 * 录像翻查的结束时间
	 */
	private String endTime;
	/**
	 * 状态
	 */
	private String state;
    /**
     * 会话id
     */
	private String callId;

    /**
     * 记录当前下标
     */
	private int index = 0;
	
	@Data
	@XStreamAlias("Item")
	public class RecordItemBean{
		@XStreamAlias("DeviceID")
		private String deviceID;
		
		@XStreamAlias("Name")
		private String name;
		
		@XStreamAlias("FilePath")
		private String filePath;
		
		@XStreamAlias("Address")
		private String address;
		
		@XStreamAlias("StartTime")
		private String startTime;
		
		@XStreamAlias("EndTime")
		private String endTime;
		
		@XStreamAlias("Secrecy")
		private String secrecy;
		
		@XStreamAlias("Type")
		private String type;

		
		@XStreamAlias("FileSize")
		private Long fileSize;
		
		
		private String uri;

	}
}
