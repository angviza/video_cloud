package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b> VO类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="ServicesInfo")
public class ServicesInfoVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.Long id;

    @ApiModelProperty(value = "ipAddress")
    private java.lang.String ipAddress;

    @ApiModelProperty(value = "port")
    private java.lang.Integer port;

    @ApiModelProperty(value = "name")
    private java.lang.String name;

    @ApiModelProperty(value = "code")
    private java.lang.String code;
    
    @ApiModelProperty(value = "type")
    private java.lang.String type;

    @ApiModelProperty(value = "enabled")
    private java.lang.Integer enabled;

    @ApiModelProperty(value = "serverStatus")
    private java.lang.Integer serverStatus;

    @ApiModelProperty(value = "userName")
    private java.lang.String userName;

    @ApiModelProperty(value = "password")
    private java.lang.String password;

    @ApiModelProperty(value = "cpuUseRate")
    private java.lang.String cpuUseRate;

    @ApiModelProperty(value = "memoryUseRate")
    private java.lang.String memoryUseRate;

    @ApiModelProperty(value = "networkUseRate")
    private java.lang.String networkUseRate;

    @ApiModelProperty(value = "diskUseRate")
    private java.lang.String diskUseRate;

    @ApiModelProperty(value = "connectivity")
    private java.lang.String connectivity;

    @ApiModelProperty(value = "packetLostRate")
    private java.lang.String packetLostRate;

    @ApiModelProperty(value = "averageDelay")
    private java.lang.Long averageDelay;

    @ApiModelProperty(value = "onlineStatus")
    private java.lang.Integer onlineStatus;
    
    @ApiModelProperty(value = "onlineState")
    private java.lang.String onlineState;

    @ApiModelProperty(value = "concurrentRequest")
    private java.lang.Long concurrentRequest;

    @ApiModelProperty(value = "connections")
    private java.lang.Long connections;

    @ApiModelProperty(value = "requestFailure")
    private java.lang.Long requestFailure;

    /*@ApiModelProperty(value = "createTime")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "updateTime")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "createUser")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "updateUser")
    private java.lang.String updateUser;*/


}

