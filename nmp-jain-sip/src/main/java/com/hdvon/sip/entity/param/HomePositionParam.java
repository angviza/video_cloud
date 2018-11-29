package com.hdvon.sip.entity.param;

import java.io.Serializable;

import lombok.Data;
/**
 * 看守位控制请求对象
 * @author wanshaojian
 *
 */
@Data
public class HomePositionParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 设备编号
	 */
	private String deviceID;
	/**
	 * 看守位使能1：开启，0：关闭
	 */
	private Integer enabled;
	/**
	 * 自动归位时间间隔
	 */
	private Integer resetTime;
	/**
	 * 预置位编号
	 */
	private Integer presetNum;
}
