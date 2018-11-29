package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>信令中心服务器表 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_sig_server")
public class SigServer implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * name db_column: name 
     */
	private java.lang.String name;

    /**
     * code db_column: code 
     */
	private java.lang.String code;

    /**
     * ip db_column: ip 
     */
	private java.lang.String ip;

    /**
     * port db_column: port 
     */
	private java.lang.Integer port;

    /**
     * domainName db_column: domain_name 
     */
    @Column(name = "domain_name")
	private java.lang.String domainName;

    /**
     * 中心信令服务器操作系统登录用户名 db_column: user_name 
     */
    @Column(name = "user_name")
	private java.lang.String userName;

    /**
     * 中心信令服务器操作系统登录密码 db_column: password 
     */
	private java.lang.String password;

    /**
     * 所属地址id db_column: address_id 
     */
    @Column(name = "address_id")
	private java.lang.String addressId;

    /**
     * 设备描述 db_column: description 
     */
	private java.lang.String description;

    /**
     * 启用状态(1启用，0停用，-1 删除)，默认停用 db_column: enabled
     */
	private java.lang.Integer enabled;

    /**
     * 服务状态（1在线，0离线）默认离线 db_column: server_status 
     */
    @Column(name = "server_status")
	private java.lang.Integer serverStatus;

    /**
     * cpu状态（默认0%） db_column: cpu_status 
     */
    @Column(name = "cpu_status")
	private java.lang.String cpuStatus;

    /**
     * 内存状态（默认0%） db_column: memory_status 
     */
    @Column(name = "memory_status")
	private java.lang.String memoryStatus;

    /**
     * 网络状态（默认0%） db_column: network_status 
     */
    @Column(name = "network_status")
	private java.lang.String networkStatus;

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

