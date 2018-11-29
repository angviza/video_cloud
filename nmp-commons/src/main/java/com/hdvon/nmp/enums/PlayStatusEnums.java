package com.hdvon.nmp.enums;

import lombok.Getter;

@Getter
public enum PlayStatusEnums {
	/**
	 * 在线
	 */
	在线("1"),
	
	/**
	 * 离线
	 */
	离线("0");
	
	private String value;
	
	private PlayStatusEnums(String value) {
		this.value = value;
	}
}
