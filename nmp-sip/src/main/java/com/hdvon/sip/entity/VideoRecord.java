package com.hdvon.sip.entity;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

@Data
@XStreamAlias("Response")
public class VideoRecord implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("CmdType")
	private String cmdType;
	
	@XStreamAlias("SN")
	private Long sn;
	
	@XStreamAlias("DeviceID")
	private String deviceId;
	
	@XStreamAlias("Name")
	private String name;
	
	@XStreamAlias("SumNum")
	private Integer sumNum;
	
	@XStreamAlias("RecordList")
	private List<VideoItem> recordList;
	
	
	@Data
	@XStreamAlias("Item")
	public class VideoItem{
		@XStreamAlias("DeviceID")
		private String deviceId;
		
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
	}
}
