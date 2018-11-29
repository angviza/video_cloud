package com.hdvon.client.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * 用户对象
 * @author wnsojn 2018-2-24 7:00
 *
 */
@Data
public class UserVo implements Serializable{
    private String id;

    private String account;//用户帐号

    private String name;//用户姓名

    private String cardId;//身份证id

    private String mobilePhone;//手机号

    private String email;//邮箱
    
    private String duty;//职务

    private String description;//描述

    private Integer level;//优先级

    private String mac;//电脑mac地址

    private Integer enable;//是否有效（1.有效，0.无效）

    private Integer isPkiUser;//是否是PKI用户（1：是，0：否）

    private java.util.Date lastLoginDate;//最后一次登录时间

    private String isUserModify;//用户信息是否修改(1.是,0.否)

    private String isResourceAssigned;//是否分配资源(1:已分配；0：未分配)

    private String isRoleGranted;//是否授予角色(1:已授予；0：未授予)
    
    private String departmentId;//部门id
    
    private String departmentName;//部门名称

    private String depCode;//部门编码
    
}
