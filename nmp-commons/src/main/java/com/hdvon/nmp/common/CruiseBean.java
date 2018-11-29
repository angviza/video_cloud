package com.hdvon.nmp.common;

import java.io.Serializable;

import lombok.Data;

/**
 * 球机巡航预案对象
 * @author wanshaojian
 *
 */
@Data
public class CruiseBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 消息id
	 */
	private String msgID;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 设备Code
	 */
	private String deviceID;
	
	/**
	 * 巡航控制类型
	 *  add 表示 加入巡航点 
 	 *	delete 表示 删除一个巡航点  1:preset必填
 	 *            删除所有presetNm 为空
 	 *  setSpeed 设置巡航速度
	 *  setStopover 设置巡航停留时间
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
