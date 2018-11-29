package com.hdvon.sip.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * 预置位请求对象
 * @author wanshaojian
 *
 */
@Data
public class DevicePresetVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 设备编码
	 */
	String deviceCode;
	/**
	 * 请求状态
	 */
	Integer status;
	

	@Data
	public class PresetItem{
//		String 
	}
	
}
