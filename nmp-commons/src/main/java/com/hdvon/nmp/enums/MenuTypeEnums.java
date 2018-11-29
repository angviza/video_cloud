package com.hdvon.nmp.enums;

import lombok.Getter;

@Getter
public enum MenuTypeEnums {
	/**
	 * 视频监控
	 */
	视频监控(0),
	
	/**
	 * 线索翻查
	 */
	线索翻查(1),
	
	/**
	 * 案事件管理
	 */
	案事件管理(2),
	
	/**
	 * 综合运维管理
	 */
	综合运维管理(3);
	
	private Integer value;
	
	private MenuTypeEnums(Integer value) {
		this.value = value;
	}

}