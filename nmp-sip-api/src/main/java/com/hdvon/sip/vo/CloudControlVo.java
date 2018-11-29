package com.hdvon.sip.vo;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

import lombok.Data;
/**
 * 云台控制vo
 * @author wanshaojian
 *
 */
@Data
public class CloudControlVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		
	
	/**
	 * 设备Code
	 */
	private String deviceCode;
	
	/**
	 * 云台控制类型
	 */
	private String type;
	/**
	 * 控制方向
	 */
	private String direction;
	
	/**
	 * 控制镜头变倍
	 */
	private String zoom;
	
	/**
	 * 控制光圈缩小放大
	 */
	private String iris;
	
	
	/**
	 * 控制焦距近和远
	 */
	private String focus;
	
	
	/**
	 * 步长
	 * 	1:类型为     方向速度
	 *  2:类型为    镜头变倍速度  
	 *  3:类型为    镜头变倍速度 
	 *  4:类型为    镜头变倍速度  
	 */
	private Integer stepSize;
	
	/**
	 * 会话id
	 */
	private String callId;
	/**
	 * 响应状态
	 */
	private Integer status;

    /**
     * 将异步请求转换为同步请求
     */
	private CountDownLatch latch;
	
}
