package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>矩阵服务器表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="Matrix")
public class MatrixVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "矩阵服务器名称")
    private java.lang.String name;

    @ApiModelProperty(value = "矩阵服务器ip")
    private java.lang.String ip;

    @ApiModelProperty(value = "矩阵服务器端口")
    private java.lang.Integer port;

    @ApiModelProperty(value = "设备编号")
    private java.lang.String devicesNo;

    @ApiModelProperty(value = "注册用户")
    private java.lang.String registerUser;

    @ApiModelProperty(value = "注册密码")
    private java.lang.String registerPassword;

    @ApiModelProperty(value = "注册服务器ip")
    private java.lang.String registerServerIp;

    @ApiModelProperty(value = "注册服务器端口")
    private java.lang.Integer registerServerPort;
    
    @ApiModelProperty(value = "注册服务器id")
    private java.lang.String registerServerId;

    @ApiModelProperty(value = "注册服务器域名")
    private java.lang.String registerServerDomain;

    @ApiModelProperty(value = "说明")
    private java.lang.String description;
    
    @ApiModelProperty(value = "所属部门id")
    private java.lang.String departmentId;
    
    @ApiModelProperty(value = "所属部门名称")
    private java.lang.String departmentName;
    
    @ApiModelProperty(value = "是否关联通道(1:已关联，0：未关联)")
    private java.lang.String status;

    /*@ApiModelProperty(value = "createTime")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "updateTime")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "createUser")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "updateUser")
    private java.lang.String updateUser;*/


}

