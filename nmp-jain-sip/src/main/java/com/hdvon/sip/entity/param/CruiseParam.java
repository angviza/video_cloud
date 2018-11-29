package com.hdvon.sip.entity.param;

import java.io.Serializable;

import lombok.Data;
/**
 * 巡航控制请求参数
 * @author Administrator
 *
 */
@Data
public class CruiseParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 设备Code
	 */
	private String deviceID;
	
	/**
	 * 巡航控制类型
	 */
	private String type;
	
	/**
	 * 巡航组号
	 */
	private Integer groupNum;
	/**
	 * 预置位号
	 */
	private Integer presetNum;
	/**
	 * 巡航停留时间
	 */
	private Long stayTime;
	/**
	 * 巡航速度 
	 */
	private Integer speed; 
}
