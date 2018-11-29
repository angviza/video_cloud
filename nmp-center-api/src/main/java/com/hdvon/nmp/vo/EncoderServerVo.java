package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>编码器表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="EncoderServer")
public class EncoderServerVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "编码器名称")
    private String name;

    @ApiModelProperty(value = "编码器ip")
    private String ip;

    @ApiModelProperty(value = "编码器端口")
    private Integer port;

    @ApiModelProperty(value = "编码器用户名")
    private String username;

    @ApiModelProperty(value = "编码器密码")
    private String password;

    @ApiModelProperty(value = "设备编号")
    private String devicesNo;
    
    @ApiModelProperty(value = "设备厂家")
    private java.lang.Integer deviceCj;

    @ApiModelProperty(value = "设备类型")
    private java.lang.Integer deviceType;

    @ApiModelProperty(value = "注册用户")
    private String registerUser;

    @ApiModelProperty(value = "注册用户密码")
    private String registerPassword;

    @ApiModelProperty(value = "注册ip")
    private String registerIp;

    @ApiModelProperty(value = "注册端口号")
    private Integer registerPort;
    
    @ApiModelProperty(value = "总通道数")
    private java.lang.Integer channels;

    @ApiModelProperty(value = "初始通道号")
    private java.lang.Integer initChannel;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "createTime")
    private java.util.Date createTime;

    /*@ApiModelProperty(value = "createUser")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "updateTime")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "updateUser")
    private java.lang.String updateUser;*/

    @ApiModelProperty(value = "所属地址id")
    private String addressId;

    @ApiModelProperty(value = "所属地址名称")
    private String addressName;
    
    @ApiModelProperty(value = "所属地址编号")
    private String addressCode;
    
    @ApiModelProperty(value = "所属项目id")
    private String projectId;
    
    @ApiModelProperty(value = "所属项目名称")
    private String projectName;
    
    @ApiModelProperty(value = "所属项目编号")
    private String projectCode;
    
    public void convertProjectId() {
    	if(this.projectId != null) {
    		if(this.projectId.substring(0,2).equals("p_")) {
        		this.projectId = projectId.substring(2,projectId.length());
        	}
    	}
    }
}

