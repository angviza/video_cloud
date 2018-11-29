package com.hdvon.sip.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * websocket请求消息类型
 * @author zhuxiaojin
 * @Date 2018-10-16
 * 版权所有：广州弘度信息科技有限公司 版权所有(C) 2018
 */
@Data
public class RequestBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 版本号
     */
    private String version;
    
    /**
     * 请求方法
     */
    private String method;
    /**
     * websockt的Id
     */
    private String wsId;
    /**
     * 事务id
     */
    private String transactionID;
    /**
     * 用户token
     */
    private String token;
    /**
     * 请求时间  以毫秒计算
     */
    private long reqTime;
    
    /**
     * 请求参数
     */
    private T param;

}
