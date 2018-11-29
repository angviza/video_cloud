package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b> 实体类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_sip_log")
public class SipLog implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * sip操作日志id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 请求业务id db_column: transaction_id 
     */
    @Column(name = "transaction_id")
	private java.lang.String transactionId;

    /**
     * 会话id db_column: call_id 
     */
    @Column(name = "call_id")
	private java.lang.String callId;

    /**
     * 用户id db_column: user_id 
     */
    @Column(name = "user_id")
	private java.lang.String userId;

    /**
     * 请求方法 db_column: method 
     */
	private java.lang.String method;

    /**
     * 操作内容 db_column: content 
     */
	private java.lang.String content;

    /**
     * 请求地址 db_column: req_ip 
     */
    @Column(name = "req_ip")
	private java.lang.String reqIp;

    /**
     * 设备编码 db_column: device_id 
     */
    @Column(name = "device_id")
	private java.lang.String deviceId;

    /**
     * 请求参数 db_column: param 
     */
	private java.lang.String param;

    /**
     * 播放状态 db_column: play_status 
     */
    @Column(name = "play_status")
	private java.lang.String playStatus;

    /**
     * 设备状态 db_column: device_status 
     */
    @Column(name = "device_status")
	private java.lang.String deviceStatus;

    /**
     * 请求时间 db_column: req_time 
     */
    @Column(name = "req_time")
	private java.util.Date reqTime;

    /**
     * 更新时间 db_column: update_time 
     */
    @Column(name = "update_time")
	private java.util.Date updateTime;


}

