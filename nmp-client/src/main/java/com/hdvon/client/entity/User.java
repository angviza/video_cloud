package com.hdvon.client.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * <br>
 * <b>功能：</b>用户表 实体类<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@Table(name="t_user")
public class User implements Serializable{
	@Id  
    @Column(name = "Id")
    private String id;

    private String account;

    private String password;
    
    private String name;

    private String cardId;

    private String mobilePhone;
    
    private String email;
    
    private String duty;

    private String description;

    private Integer level;

    private String mac;

    private Integer enable;

    private Integer isPkiUser;

    private Date lastLoginDate;

    private String isUserModify;

    private String isResourceAssigned;

    private String isRoleGranted;
    
}
