package com.hdvon.sip.entity;

import java.io.Serializable;

import lombok.Data;
/**
 * 公共请求参数
 * @author Administrator
 *
 */
@Data
public class BaseSipBean implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    /**
     * 版本号
     */
    protected String version;
    /**
     * 事务id
     */
    protected String transactionID;
    /**
     * 用户token
     */
    protected String token;
    /**
     * 请求方法
     */
    protected String method;
    /**
     * websockt的Id
     */
    protected String wsId;
    
    /**
     * 用户id
     */
    private String userId;
    
    /**
     * 请求IP
     */
    private String reqIp;
    
    /**
     * 请求参数
     */
    private String param;
    
    /**
     * 请求时间  以毫秒计算
     */
    protected long reqTime;
}
