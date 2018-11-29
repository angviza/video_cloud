package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>网管服务器管理 VO类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="GatewayServer")
public class GatewayServerVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "网关服务器名称")
    private java.lang.String name;

    @ApiModelProperty(value = "网关服务器编码")
    private java.lang.String code;

    @ApiModelProperty(value = "网关服务器ip地址")
    private java.lang.String ip;

    @ApiModelProperty(value = "网关服务器端口")
    private java.lang.Integer port;

    @ApiModelProperty(value = "网关服务器域名")
    private java.lang.String domainName;

    @ApiModelProperty(value = "网关服务器操作系统登录用户名")
    private java.lang.String userName;

    @ApiModelProperty(value = "网关服务器操作系统登录密码")
    private java.lang.String password;

    @ApiModelProperty(value = "注册用户名")
    private java.lang.String registerUser;

    @ApiModelProperty(value = "注册用户密码")
    private java.lang.String registerPass;

    @ApiModelProperty(value = "注册服务器IP")
    private java.lang.String registerIp;

    @ApiModelProperty(value = "注册服务器端口")
    private java.lang.Integer registerPort;

    @ApiModelProperty(value = "注册服务器域名")
    private java.lang.String registerDomain;

    @ApiModelProperty(value = "所属地址树")
    private java.lang.String addressId;

    @ApiModelProperty(value = "设备描述")
    private java.lang.String description;

    @ApiModelProperty(value = "启用状态")
    private java.lang.Integer enabled;

    @ApiModelProperty(value = "版本号")
    private java.lang.String versionNo;
    
    @ApiModelProperty(value = "网关服务器关联摄像机集合")
    private java.util.List<GatewayserverMappingVo> GatewayserverMappingVos;
    
    @ApiModelProperty(value = "地址名称")
    private java.lang.String addressName;
    
    /*@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/


}

