package com.hdvon.nmp.vo.video;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserOperLogVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String userId;
	
	private String callId;
	
	private String localIp;
	
	private String deviceCode;
	
	private String playType;
	
	private String operType;
	
	private String content;
	
	private String url;
	
	private long start;
	
	private Integer menuType;
	
	private String token;
	
}
