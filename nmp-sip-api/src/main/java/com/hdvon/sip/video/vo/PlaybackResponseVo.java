package com.hdvon.sip.video.vo;

import lombok.Data;

@Data
public class PlaybackResponseVo extends CallbackResponseVo{
	
	private static final long serialVersionUID = 1L;
	
	// 回放开始时间
	private Long startTime;
	
	//回放结束时间
	private Long endTime;

}
