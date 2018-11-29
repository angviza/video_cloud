package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>转发服务器 实体类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_transpond_server")
public class TranspondServer implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 转发服务器名称 db_column: name 
     */
	private java.lang.String name;

    /**
     * 转发服务器编码 db_column: code 
     */
	private java.lang.String code;

    /**
     * 转发服务器ip db_column: ip 
     */
	private java.lang.String ip;

    /**
     * 转发服务器端口号 db_column: port 
     */
	private java.lang.Integer port;

    /**
     * 转发服务器操作系统登录用户名 db_column: user_name 
     */
    @Column(name = "user_name")
	private java.lang.String userName;

    /**
     * 转发服务器操作系统登录密码 db_column: password 
     */
	private java.lang.String password;

    /**
     * 注册服务器用户名 db_column: register_user 
     */
    @Column(name = "register_user")
	private java.lang.String registerUser;

    /**
     * 注册服务器密码 db_column: register_pass 
     */
    @Column(name = "register_pass")
	private java.lang.String registerPass;

    /**
     * 注册服务器ip db_column: register_ip 
     */
    @Column(name = "register_ip")
	private java.lang.String registerIp;

    /**
     * 注册服务器端口 db_column: register_port 
     */
    @Column(name = "register_port")
	private java.lang.Integer registerPort;

    /**
     * 注册服务器域名 db_column: register_domain 
     */
    @Column(name = "register_domain")
	private java.lang.String registerDomain;

    /**
     * 关联地址树id db_column: address_id 
     */
    @Column(name = "address_id")
	private java.lang.String addressId;

    /**
     * 服务状态（1在线，0离线）默认离线 db_column: server_status 
     */
    @Column(name = "server_status")
	private java.lang.Integer serverStatus;

    /**
     * 启用状态(1启用，0停用,-1 删除)，默认停用 db_column: enabled
     */
	private java.lang.Integer enabled;

    /**
     * 说明 db_column: description 
     */
	private java.lang.String description;

    /**
     * 版本号 db_column: version_no 
     */
    @Column(name = "version_no")
	private java.lang.String versionNo;

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

