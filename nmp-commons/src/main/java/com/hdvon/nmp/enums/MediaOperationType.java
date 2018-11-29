package com.hdvon.nmp.enums;
/**
 * 媒体操作类型枚举
 * @author wanshaojian
 * @since 2018-10-15
 */
public enum MediaOperationType {
	/*
	 * “Play”代表实时点播；“Playback”代表历史回放；“Download”代表文件下载；“Talk”代表语音对讲。
	 */
	PLAY("play"),PLAYBACK("playback"),DOWNLAOD("download"),TALK("talk");
	String value;
	
	private MediaOperationType(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
}
