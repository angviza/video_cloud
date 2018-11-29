package com.hdvon.sip.video.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class FileResponseVo  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String callId;//会话请求返回的标识
	
	private int statusCode;//状态码
	

}
