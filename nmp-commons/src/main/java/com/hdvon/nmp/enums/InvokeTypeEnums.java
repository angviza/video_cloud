package com.hdvon.nmp.enums;

import lombok.Getter;

@Getter
public enum InvokeTypeEnums {
	/**
	 * 信令注册
	 */
	信令注册(0),
	
	/**
	 * 实时播放
	 */
	实时播放(1),
	
	/**
	 * 云台控制
	 */
	云台控制(2),
	
	/**
	 * 回放控制
	 */
	回放控制(3),
	
	/**
	 * 预置位控制
	 */
	预置位控制(4),
	
	
	/**
	 * 巡航预案控制
	 */
	巡航预案控制(5),
	
	/**
	 * 录像查询
	 */
	录像查询(6);
	
	private Integer value;
	
	private InvokeTypeEnums(Integer value) {
		this.value = value;
	}

}