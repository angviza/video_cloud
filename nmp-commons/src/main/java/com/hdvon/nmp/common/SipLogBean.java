package com.hdvon.nmp.common;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * kafka sip日志消息对象
 * @author Administrator
 *
 */
@Data
@Builder
public class SipLogBean implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 请求业务id
	 */
	private String transactionID;
	
	/**
	 * 会话id
	 */
	private String callID;
	
	/**
	 * 记录id
	 */
	private String sipId;
	
    /**
     * 用户id
     */
    private String userId;
    
    /**
     * 登录token
     */
    private String token;
    
    /**
     * 请求方法
     */
    private String method;
    /**
     * 请求地址
     */
    private String reqIp;
    /**
     * 设备id
     */
    private String deviceID;
    
    /**
     * 请求时间
     */
    private Date reqDate;
    
    /**
     * 请求参数
     */
    private String param;
}
