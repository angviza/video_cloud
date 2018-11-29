package com.hdvon.client.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>权限预案 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_permission_plan")
public class PermissionPlan implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 权限预案名称 db_column: name 
     */
	private java.lang.String name;

    /**
     * 权限预案状态（0禁用，1启用） db_column: status 
     */
	private java.lang.Integer status;

    /**
     * 用户状态（1：白名单；2：黑名单；9：普通用户） db_column: user_status 
     */
    @Column(name = "user_status")
	private java.lang.Integer userStatus;

    /**
     * 预案开始时间 db_column: bgn_time 
     */
    @Column(name = "bgn_time")
	private java.util.Date bgnTime;

    /**
     * 预案截止时间 db_column: end_time 
     */
    @Column(name = "end_time")
	private java.util.Date endTime;

    /**
     * 说明 db_column: description 
     */
	private java.lang.String description;

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

