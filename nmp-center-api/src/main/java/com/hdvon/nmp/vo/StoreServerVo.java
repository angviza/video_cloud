package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>存储服务器 VO类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="StoreServer")
public class StoreServerVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "存储服务器名称")
    private java.lang.String name;

    @ApiModelProperty(value = "存储服务器编号")
    private java.lang.String code;

    @ApiModelProperty(value = "存储服务器ip")
    private java.lang.String ip;

    @ApiModelProperty(value = "存储服务器端口")
    private java.lang.Integer port;

    @ApiModelProperty(value = "存储服务器操作系统登录用户名")
    private java.lang.String userName;

    @ApiModelProperty(value = "存储服务器操作系统登录密码")
    private java.lang.String password;

    @ApiModelProperty(value = "注册服务器用户名")
    private java.lang.String registerUser;

    @ApiModelProperty(value = "注册服务器密码")
    private java.lang.String registerPass;

    @ApiModelProperty(value = "注册服务器ip")
    private java.lang.String registerIp;

    @ApiModelProperty(value = "注册服务器端口")
    private java.lang.Integer registerPort;

    @ApiModelProperty(value = "注册服务器域名")
    private java.lang.String registerDomain;

    @ApiModelProperty(value = "存储类型")
    private java.lang.Integer storeType;
    
    @ApiModelProperty(value = "录像默认保存天数")
    private java.lang.Integer defaultDays;

    @ApiModelProperty(value = "关联地址树id")
    private java.lang.String addressId;

    @ApiModelProperty(value = "服务状态（1在线，0离线,-1删除）默认离线")
    private java.lang.Integer serverStatus;

    @ApiModelProperty(value = "启用状态(1启用，0停用)，默认停用")
    private java.lang.Integer enabled;

    @ApiModelProperty(value = "说明")
    private java.lang.String description;

    @ApiModelProperty(value = "版本号")
    private java.lang.String versionNo;
    
    @ApiModelProperty(value = "项目列表")
    private List<StoreserverMappingVo> storeserverMappingVos;
    
    @ApiModelProperty(value = "所属地址名称")
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

