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
public class UserParamVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "用户帐号")
    private String account;

    @ApiModelProperty(value = "用户姓名",required = true)
    private String name;

    @ApiModelProperty(value = "身份证id")
    private String cardId;

    @ApiModelProperty(value = "手机号")
    private String mobilePhone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "帐号密码")
    private String password;

    @ApiModelProperty(value = "职务")
    private String duty;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "优先级")
    private Integer level;

    @ApiModelProperty(value = "电脑mac地址")
    private String mac;

    @ApiModelProperty(value = "是否有效（1.有效，0.无效）",required = true)
    private Integer enable;

    @ApiModelProperty(value = "是否是PKI用户（1：是，0：否）")
    private Integer isPkiUser;

    @ApiModelProperty(value = "确认帐号密码")
    private String confirmPassword;

    @ApiModelProperty(value = "所属部门id",required = true)
    private String departmentId;
    
    
   /* @ApiModelProperty(value = "可选部门json,格式[{'departmentId':'2','value':'1,2'}]")
    private String departmentJson;*/
    
}

