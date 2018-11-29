package com.hdvon.sip.video.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class ResponseVideoVo  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String startTime;//起始时间
	
	private String endTime;//截止时间
	
	private String type;//类型
	
	private String fileSize;//文件大小
	

}
