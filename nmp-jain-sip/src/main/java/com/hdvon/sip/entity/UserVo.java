package com.hdvon.sip.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * <br>
 * <b>功能：</b>用户表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
public class UserVo implements Serializable{

	private static final long serialVersionUID = 1L;

    private String id;

    private String account;

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

    private java.util.Date lastLoginDate;

    private String isUserModify;

    private String isResourceAssigned;

    private String isRoleGranted;
    
    private String permissionplanId;

    private java.util.Date createTime;

    private java.lang.String updateUser;

    private String departmentId;
    
    private String filterDeptId;

    private String departmentName;

    private String depCodeSplit;

    private String parentDepName;

    private boolean isAdmin;


    private boolean isAllowOff;

    private String loginTime = "";

    private String loginStatus = "";

    private String curOperation = "";
    
    private Integer validType;
    
    private Integer lastLogin;

    String menuPermission;


}

