package com.hdvon.sip.entity;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

import lombok.Data;
/**
 * 心跳对象
 * @author wanshaojian
 *
 */
@Data
public class HearbeatBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String checkUserCode;
	/**
	 * 心跳发送状态
	 */
	private Integer status;
	
	private CountDownLatch latch;
	
	public HearbeatBean(String checkUserCode) {
		this.checkUserCode = checkUserCode;
	}
}