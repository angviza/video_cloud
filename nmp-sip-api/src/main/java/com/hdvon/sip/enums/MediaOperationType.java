package com.hdvon.sip.enums;
/**
 * 请求媒体流的操作类型
 * @author wanshaojian
 *
 */
public enum MediaOperationType {
	/*
	 * “Play”代表实时点播；“Playback”代表历史回放；“Download”代表文件下载；“Talk”代表语音对讲。
	 */
	PLAY("Play"),PLAYBACK("Playback"),DOWNLAOD("Download"),TALK("Talk");
	String value;
	
	private MediaOperationType(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	
	
}
