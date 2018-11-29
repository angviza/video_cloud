package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>编码器表 实体类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_encoder_server")
public class EncoderServer implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 编码器名称 db_column: NAME 
     */
    @Column(name = "NAME")
	private java.lang.String name;

    /**
     * 编码器ip db_column: ip 
     */
	private java.lang.String ip;

    /**
     * 编码器端口 db_column: PORT 
     */
    @Column(name = "PORT")
	private java.lang.Integer port;

    /**
     * 编码器用户名 db_column: username 
     */
	private java.lang.String username;

    /**
     * 编码器密码 db_column: PASSWORD 
     */
    @Column(name = "PASSWORD")
	private java.lang.String password;

    /**
     * 设备编号 db_column: devices_no 
     */
    @Column(name = "devices_no")
	private java.lang.String devicesNo;

    /**
     * deviceCj db_column: device_cj 
     */
    @Column(name = "device_cj")
	private java.lang.Integer deviceCj;

    /**
     * deviceType db_column: device_type 
     */
    @Column(name = "device_type")
	private java.lang.Integer deviceType;

    /**
     * 注册用户 db_column: register_user 
     */
    @Column(name = "register_user")
	private java.lang.String registerUser;

    /**
     * 注册用户密码 db_column: register_password 
     */
    @Column(name = "register_password")
	private java.lang.String registerPassword;

    /**
     * 注册ip db_column: register_ip 
     */
    @Column(name = "register_ip")
	private java.lang.String registerIp;

    /**
     * 注册端口号 db_column: register_port 
     */
    @Column(name = "register_port")
	private java.lang.Integer registerPort;

    /**
     * 总通道数 db_column: channels 
     */
	private java.lang.Integer channels;

    /**
     * 初始通道号 db_column: init_channel 
     */
    @Column(name = "init_channel")
	private java.lang.Integer initChannel;

    /**
     * 描述 db_column: description 
     */
	private java.lang.String description;

    /**
     * createTime db_column: create_time 
     */
    @Column(name = "create_time")
	private java.util.Date createTime;

    /**
     * createUser db_column: create_user 
     */
    @Column(name = "create_user")
	private java.lang.String createUser;

    /**
     * updateTime db_column: update_time 
     */
    @Column(name = "update_time")
	private java.util.Date updateTime;

    /**
     * updateUser db_column: update_user 
     */
    @Column(name = "update_user")
	private java.lang.String updateUser;


}

