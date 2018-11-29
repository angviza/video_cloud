package com.hdvon.sip.entity.param;

import java.io.Serializable;

import lombok.Data;

/**
 * 录像查询请求参数
 * @author wanshaojian
 *
 */
@Data
public class RecordParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 设备id
	 */
	private String deviceID;
	/**
	 * 查询开始时间
	 */
	private String startTime;
	/**
	 * 查询结束时间
	 */
	private String endTime;


}
