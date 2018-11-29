package com.hdvon.nmp.entity;

import java.io.Serializable;
import lombok.Data;
import javax.persistence.*;

/**
 * <br>
 * <b>功能：</b>用户表 实体类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name = "t_user")
public class User implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * id db_column: id 
     */
    @Id
	private String id;

    /**
     * 用户帐号 db_column: account 
     */
	private String account;

    /**
     * 用户姓名 db_column: name 
     */
	private String name;

    /**
     * 身份证id db_column: card_id 
     */
    @Column(name = "card_id")
	private String cardId;

    /**
     * 手机号 db_column: mobile_phone 
     */
    @Column(name = "mobile_phone")
	private String mobilePhone;

    /**
     * 邮箱 db_column: email 
     */
	private String email;

    /**
     * 帐号密码 db_column: password 
     */
	private String password;

    /**
     * 职务 db_column: duty 
     */
	private String duty;

    /**
     * 描述 db_column: description 
     */
	private String description;

    /**
     * 优先级 db_column: level 
     */
	private Integer level;

    /**
     * 电脑mac地址 db_column: mac 
     */
	private String mac;

    /**
     * 是否有效（1.有效，0.无效） db_column: enable 
     */
	private Integer enable;

    /**
     * 是否是PKI用户（1：是，0：否） db_column: is_pki_user 
     */
    @Column(name = "is_pki_user")
	private Integer isPkiUser;

    /**
     * 最后一次登录时间 db_column: last_login_date 
     */
    @Column(name = "last_login_date")
	private java.util.Date lastLoginDate;
    
    /**
     * 登录时更新时间 db_column: last_update_date 
     */
    @Column(name = "last_update_date")
	private java.util.Date lastUpdateDate;

    /**
     * 用户信息是否修改(1.是,0.否) db_column: is_user_modify 
     */
    @Column(name = "is_user_modify")
	private String isUserModify;

    /**
     * 是否分配资源(1:已分配；0：未分配) db_column: is_resource_assigned 
     */
    @Column(name = "is_resource_assigned")
	private String isResourceAssigned;

    /**
     * 是否授予角色(1:已授予；0：未授予) db_column: is_role_granted 
     */
    @Column(name = "is_role_granted")
	private String isRoleGranted;

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
	private String createUser;

    /**
     * 修改人 db_column: update_user 
     */
    @Column(name = "update_user")
	private String updateUser;

    /**
     * 用户验证方式 db_column: valid_type 
     */
    @Column(name = "valid_type")
	private java.lang.Integer validType;

}

