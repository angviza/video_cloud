package com.hdvon.sip.entity;

import java.io.Serializable;

import lombok.Data;
/**
 * 错误码对象
 * @author wanshaojian
 *
 */
@Data
public class ErrorBean implements Serializable{
	/**
	 * 错误码
	 */
	private String code;
	/**
	 * 错误消息
	 */
	private String message;
}
