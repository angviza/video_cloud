package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b> 实体类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_camera")
public class Camera implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 访问热度 db_column: hots 
     */
	private java.lang.Integer hots;

    /**
     * 设备id db_column: device_id 
     */
    @Column(name = "device_id")
	private java.lang.String deviceId;

    /**
     * 接入方式:1国标0非国标 db_column: access_mode 
     */
    @Column(name = "access_mode")
	private java.lang.Integer accessMode;

    /**
     * 注册用户名 db_column: registered_name 
     */
    @Column(name = "registered_name")
	private java.lang.String registeredName;

    /**
     * 注册用户密码 db_column: registered_pass 
     */
    @Column(name = "registered_pass")
	private java.lang.String registeredPass;

    /**
     * 注册服务器端口号 db_column: registered_server_port 
     */
    @Column(name = "registered_server_port")
	private java.lang.Integer registeredServerPort;

    /**
     * 创建时间 db_column: create_time 
     */
    @Column(name = "create_time")
	private java.util.Date createTime;

    /**
     * 修改时间 db_column: update_time 
     */
    @Column(name = "update_time")
	private java.util.Date updateTime;

    /**
     * 创建人 db_column: create_user 
     */
    @Column(name = "create_user")
	private java.lang.String createUser;

    /**
     * 修改人 db_column: update_user 
     */
    @Column(name = "update_user")
	private java.lang.String updateUser;


}

