package com.hdvon.sip.vo;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

import lombok.Data;
/**
 * 巡航预案控制
 * @author wanshaojian
 *
 */
@Data
public class CruiseVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 设备Code
	 */
	private String deviceCode;
	
	/**
	 * 类型
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
	/**
	 * 会话id
	 */
	private String callId;
	/**
	 * 响应结果
	 */
	private Integer status;

    /**
     * 将异步请求转换为同步请求
     */
	private CountDownLatch latch;
}
