package com.hdvon.sip.vo;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import lombok.Data;
/**
 * 预置位请求对象
 * @author wanshaojian
 *
 */
@Data
public class PresetVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 设备编码
	 */
	private String deviceCode;
	/**
	 * 预置位类型
	 */
	private String type;
	
	/**
	 * 预置位编号
	 */
	private Integer presetNum;
	
	/**
	 * 会话id
	 */
	private String callId;
	/**
	 * 请求状态
	 */
	private Integer status;
	

    /**
     * 将异步请求转换为同步请求
     */
	private CountDownLatch latch;
}
