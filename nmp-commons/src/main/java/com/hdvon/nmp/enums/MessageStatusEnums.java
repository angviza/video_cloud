package com.hdvon.nmp.enums;

import lombok.Getter;

@Getter
public enum MessageStatusEnums {
	
	/**
	 * 未处理
	 */
	未处理(-1),
	
	/**
	 * 处理中
	 */
	处理中(0),
	
	/**
	 * 已处理
	 */
	已处理(1);
	
	private Integer value;
	
	private MessageStatusEnums(Integer value) {
		this.value = value;
	}
}
