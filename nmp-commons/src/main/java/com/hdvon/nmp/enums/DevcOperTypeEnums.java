package com.hdvon.nmp.enums;

import lombok.Getter;

@Getter
public enum DevcOperTypeEnums {
	/**
	 * 实时播放
	 */
	实时播放("1"),
	
	/**
	 * 录像回放
	 */
	录像回放("2"),
	
	/**
	 * 录像下载
	 */
	录像下载("3"),
	
	/**
	 * 云台控制
	 */
	云台控制("4");
	
	private String value;
	
	private DevcOperTypeEnums(String value) {
		this.value = value;
	}

}