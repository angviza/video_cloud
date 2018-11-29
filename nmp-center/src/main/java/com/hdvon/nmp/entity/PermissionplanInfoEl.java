package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>权限预案信息表 实体类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_permissionplan_info_el")
public class PermissionplanInfoEl implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private java.lang.String id;

    /**
     * 权限预案id db_column: plan_id 
     */
    @Column(name = "plan_id")
	private java.lang.String planId;

    /**
     * 预案关联资源角色id db_column: resourcerole_id 
     */
    @Column(name = "resourcerole_id")
	private java.lang.String resourceroleId;

    /**
     * 预案关联用户id db_column: user_id 
     */
    @Column(name = "user_id")
	private java.lang.String userId;

    /**
     * 权限预案状态（0禁用，1启用） db_column: status 
     */
	private java.lang.Integer status;

    /**
     * 预案类型（1：白名单；2：黑名单；9：普通用户） db_column: plan_type 
     */
    @Column(name = "plan_type")
	private java.lang.Integer planType;

    /**
     * 预案开始时间 db_column: bgn_time 
     */
    @Column(name = "bgn_time")
	private java.util.Date bgnTime;

    /**
     * 预案结束时间 db_column: end_time 
     */
    @Column(name = "end_time")
	private java.util.Date endTime;


}

