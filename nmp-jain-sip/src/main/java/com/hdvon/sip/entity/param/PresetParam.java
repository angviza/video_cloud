package com.hdvon.sip.entity.param;

import java.io.Serializable;

import lombok.Data;
/**
 * 预置位请求参数
 * @author wanshaojian
 *
 */
@Data
public class PresetParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 设备编码
	 */
	private String deviceID;
	/**
	 * 预置位控制类型
	 */
	private String type;
	
	/**
	 * 预置位编号
	 * 范围1-255
	 */
	private Integer presetNum;
	/**
	 * 预置位名称
	 */
	private String presetName;
}
