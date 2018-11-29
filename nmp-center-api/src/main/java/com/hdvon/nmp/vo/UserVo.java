package com.hdvon.nmp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <br>
 * <b>功能：</b>用户表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="User")
public class UserVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "用户帐号")
    private String account;

    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "身份证id")
    private String cardId;

    @ApiModelProperty(value = "手机号")
    private String mobilePhone;

    @ApiModelProperty(value = "邮箱")
    private String email;
/*
    @ApiModelProperty(value = "帐号密码")
    private String password;
*/
    @ApiModelProperty(value = "职务")
    private String duty;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "优先级")
    private Integer level;

    @ApiModelProperty(value = "电脑mac地址")
    private String mac;

    @ApiModelProperty(value = "是否有效（1.有效，0.无效）")
    private Integer enable;

    @ApiModelProperty(value = "是否是PKI用户（1：是，0：否）")
    private Integer isPkiUser;

    @ApiModelProperty(value = "最后一次登录时间")
    private java.util.Date lastLoginDate;
    
    @ApiModelProperty(value = "登录时更新时间")
    private java.util.Date lastUpdateDate;

    @ApiModelProperty(value = "用户信息是否修改(1.是,0.否)")
    private String isUserModify;

    @ApiModelProperty(value = "是否分配资源(1:已分配；0：未分配)")
    private String isResourceAssigned;

    @ApiModelProperty(value = "是否授予角色(1:已授予；0：未授予)")
    private String isRoleGranted;
    
    @ApiModelProperty(value = "权限预案id")
    private String permissionplanId;

    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;

    /*@ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/

    @ApiModelProperty(value = "部门id")
    private String departmentId;
    
    @ApiModelProperty(value = "部门id,用于用户管理界面的部门查询条件")
    private String filterDeptId;

    @ApiModelProperty(value = "所属部门名称")
    private String departmentName;

    @ApiModelProperty(value = "所属部门编码")
    private String depCodeSplit;

    @ApiModelProperty(value = "所属部门上级名称")
    private String parentDepName;

    @ApiModelProperty(value = "是否管理员" , hidden = true)
    private boolean isAdmin = false;


    @ApiModelProperty(value = "是否允许强制下线")
    private boolean isAllowOff = false;

    @ApiModelProperty(value = "登录时长")
    private String loginTime = "";

    @ApiModelProperty(value = "用户状态")
    private String loginStatus = "";

    @ApiModelProperty(value = "当前操作资源")
    private String curOperation = "";
    
    @ApiModelProperty(value = "用户验证方式")
    private Integer validType;
    
    @ApiModelProperty(value = "最近一次登录到现在隔天数")
    private Integer lastLogin;

    @ApiModelProperty(value = "菜单权限字符串")
    String menuPermission;

}

