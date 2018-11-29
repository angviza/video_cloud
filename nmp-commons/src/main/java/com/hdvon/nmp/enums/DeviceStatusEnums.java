package com.hdvon.nmp.enums;

import lombok.Getter;

@Getter
public enum DeviceStatusEnums {
	/**
	 * 正常
	 */
	正常("1"),
	
	/**
	 * 异常
	 */
	异常("0");
	
	private String value;
	
	private DeviceStatusEnums(String value) {
		this.value = value;
	}
	
}
