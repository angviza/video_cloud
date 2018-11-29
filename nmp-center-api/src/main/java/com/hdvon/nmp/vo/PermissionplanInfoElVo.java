package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>权限预案信息表 VO类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="PermissionplanInfoEl")
public class PermissionplanInfoElVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "权限预案id")
    private java.lang.String planId;

    @ApiModelProperty(value = "预案关联资源角色id")
    private java.lang.String resourceroleId;

    @ApiModelProperty(value = "预案关联用户id")
    private java.lang.String userId;

    @ApiModelProperty(value = "权限预案状态（0禁用，1启用）")
    private java.lang.Integer status;

    @ApiModelProperty(value = "预案类型（1：白名单；2：黑名单；9：普通用户）")
    private java.lang.Integer planType;

    @ApiModelProperty(value = "预案开始时间")
    private java.util.Date bgnTime;

    @ApiModelProperty(value = "预案结束时间")
    private java.util.Date endTime;


}

