package com.hdvon.nmp.enums;

import lombok.Getter;

@Getter
public enum MessageTypeEnums {
	
	/**
	 * 用户摄像机授权
	 */
	用户摄像机授权(1),
	
	/**
	 * 摄像机权限预案
	 */
	摄像机权限预案(2),
	
	/**
	 * 摄像机分组
	 */
	摄像机分组(3),
	
	/**
	 * 摄像机地址树
	 */
	设备(4);
	
	private Integer value;
	
	private MessageTypeEnums(Integer value) {
		this.value = value;
	}
	
}
