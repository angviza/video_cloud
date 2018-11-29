package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>矩阵服务器表 实体类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_matrix")
public class Matrix implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 矩阵服务器名称 db_column: name 
     */
	private java.lang.String name;

    /**
     * 矩阵服务器ip db_column: ip 
     */
	private java.lang.String ip;

    /**
     * 矩阵服务器端口 db_column: port 
     */
	private java.lang.Integer port;

    /**
     * 设备编号 db_column: devices_no 
     */
    @Column(name = "devices_no")
	private java.lang.String devicesNo;

    /**
     * 注册用户 db_column: register_user 
     */
    @Column(name = "register_user")
	private java.lang.String registerUser;

    /**
     * 注册密码 db_column: register_password 
     */
    @Column(name = "register_password")
	private java.lang.String registerPassword;

    /**
     * 注册服务器ip db_column: register_server_ip 
     */
    @Column(name = "register_server_ip")
	private java.lang.String registerServerIp;

    /**
     * 注册服务器端口 db_column: register_server_port 
     */
    @Column(name = "register_server_port")
	private java.lang.Integer registerServerPort;

    /**
     * registerServerId db_column: register_server_id 
     */
    @Column(name = "register_server_id")
	private java.lang.String registerServerId;

    /**
     * registerServerDomain db_column: register_server_domain 
     */
    @Column(name = "register_server_domain")
	private java.lang.String registerServerDomain;

    /**
     * 说明 db_column: description 
     */
	private java.lang.String description;

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

