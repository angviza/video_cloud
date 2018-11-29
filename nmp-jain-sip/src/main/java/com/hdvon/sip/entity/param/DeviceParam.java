package com.hdvon.sip.entity.param;

import java.io.Serializable;

import lombok.Data;

/**
 * 设备查询请求参数
 * @author wanshaojian
 *
 */
@Data
public class DeviceParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 设备id
	 */
	private String deviceID;
}
