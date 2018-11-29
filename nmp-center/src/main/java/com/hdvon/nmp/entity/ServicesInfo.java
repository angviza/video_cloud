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
@Table(name = "t_services_info")
public class ServicesInfo implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.Long id;

    /**
     * ipAddress db_column: ip_address 
     */
    @Column(name = "ip_address")
	private java.lang.String ipAddress;

    /**
     * port db_column: port 
     */
	private java.lang.Integer port;

    /**
     * name db_column: name 
     */
	private java.lang.String name;

    /**
     * code db_column: code 
     */
	private java.lang.String code;

    /**
     * enabled db_column: enabled 
     */
	private java.lang.Integer enabled;

    /**
     * serverStatus db_column: server_status 
     */
    @Column(name = "server_status")
	private java.lang.Integer serverStatus;

    /**
     * userName db_column: user_name 
     */
    @Column(name = "user_name")
	private java.lang.String userName;

    /**
     * password db_column: password 
     */
	private java.lang.String password;

    /**
     * cpuUseRate db_column: cpu_use_rate 
     */
    @Column(name = "cpu_use_rate")
	private java.lang.String cpuUseRate;

    /**
     * memoryUseRate db_column: memory_use_rate 
     */
    @Column(name = "memory_use_rate")
	private java.lang.String memoryUseRate;

    /**
     * networkUseRate db_column: network_use_rate 
     */
    @Column(name = "network_use_rate")
	private java.lang.String networkUseRate;

    /**
     * diskUseRate db_column: disk_use_rate 
     */
    @Column(name = "disk_use_rate")
	private java.lang.String diskUseRate;

    /**
     * connectivity db_column: connectivity 
     */
	private java.lang.String connectivity;

    /**
     * packetLostRate db_column: packet_lost_rate 
     */
    @Column(name = "packet_lost_rate")
	private java.lang.String packetLostRate;

    /**
     * averageDelay db_column: average_delay 
     */
    @Column(name = "average_delay")
	private java.lang.Long averageDelay;

    /**
     * onlineStatus db_column: online_status 
     */
    @Column(name = "online_status")
	private java.lang.Integer onlineStatus;

    /**
     * concurrentRequest db_column: concurrent_request 
     */
    @Column(name = "concurrent_request")
	private java.lang.Long concurrentRequest;

    /**
     * connections db_column: connections 
     */
	private java.lang.Long connections;

    /**
     * requestFailure db_column: request_failure 
     */
    @Column(name = "request_failure")
	private java.lang.Long requestFailure;

    /**
     * createTime db_column: create_time 
     */
    @Column(name = "create_time")
	private java.util.Date createTime;

    /**
     * updateTime db_column: update_time 
     */
    @Column(name = "update_time")
	private java.util.Date updateTime;

    /**
     * createUser db_column: create_user 
     */
    @Column(name = "create_user")
	private java.lang.String createUser;

    /**
     * updateUser db_column: update_user 
     */
    @Column(name = "update_user")
	private java.lang.String updateUser;


}

