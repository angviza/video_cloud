package com.hdvon.nmp.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value ="LoginUser")
public class LoginUserVo implements Serializable {

    @ApiModelProperty(value = "tokenid")
    private String tokenid;

    @ApiModelProperty(value = "userId")
    private String userId;

    @ApiModelProperty(value = "用户帐号")
    private String account;

    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "优先级")
    private Integer level;

    @ApiModelProperty(value = "是否管理员",hidden = true)
    private boolean isAdmin = false;

    @ApiModelProperty(value = "权限菜单列表")
    private List<SysmenuVo> menuList;

    @ApiModelProperty(value = "部门id")
    private String departmentId;

    @ApiModelProperty(value = "所属部门名称")
    private String departmentName;
}
