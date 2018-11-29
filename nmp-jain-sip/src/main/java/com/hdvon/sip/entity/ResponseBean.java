package com.hdvon.sip.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * websocket通知前端页面的消息类
 * @author wanshaojian
 * @Date 2018-10-16
 * 版权所有：广州弘度信息科技有限公司 版权所有(C) 2018
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 版本号
     */
    private String version;
    /**
     * 事务id
     */
    private String transactionID;
    /**
     * websockt的Id
     */
    private String wsId;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 用户token
     */
    private String token;

    /**
     * 请求参数
     */
    private T result;
    
    
    /**
     * 返回错误信息
     */
    private ErrorBean error;
    


}
