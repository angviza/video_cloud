package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>权限预案 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="PermissionPlan")
public class PermissionPlanVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "权限预案名称")
    private java.lang.String name;

    @ApiModelProperty(value = "权限预案状态（0禁用，1启用）")
    private java.lang.Integer status;

    @ApiModelProperty(value = "用户状态（1：白名单；2：黑名单；9：普通用户）")
    private java.lang.Integer userStatus;

    @ApiModelProperty(value = "预案开始时间")
    private java.util.Date bgnTime;

    @ApiModelProperty(value = "预案截止时间")
    private java.util.Date endTime;

    @ApiModelProperty(value = "说明")
    private java.lang.String description;
    
    @ApiModelProperty(value = "资源角色集合")
    private List<ResourceroleVo> resourceRoles;
    
    @ApiModelProperty(value = "资源角色集合")
    private List<UserVo> userVos;
    
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;

    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
    
    @ApiModelProperty(value = "删除标志 删除：0；正常：1")
    private java.lang.String deleteFlag;

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/


}

