package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>信令中心服务器表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="SigServer")
public class SigServerVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "name")
    private java.lang.String name;

    @ApiModelProperty(value = "code")
    private java.lang.String code;

    @ApiModelProperty(value = "ip")
    private java.lang.String ip;

    @ApiModelProperty(value = "port")
    private java.lang.Integer port;

    @ApiModelProperty(value = "domainName")
    private java.lang.String domainName;

    @ApiModelProperty(value = "中心信令服务器操作系统登录用户名")
    private java.lang.String userName;

    @ApiModelProperty(value = "中心信令服务器操作系统登录密码")
    private java.lang.String password;

    @ApiModelProperty(value = "所属地址id")
    private java.lang.String addressId;

    @ApiModelProperty(value = "设备描述")
    private java.lang.String description;

    @ApiModelProperty(value = "启用状态(1启用，0停用，-1删除)，默认停用")
    private java.lang.Integer enabled;

    @ApiModelProperty(value = "服务状态（1在线，0离线）默认离线")
    private java.lang.Integer serverStatus;

    @ApiModelProperty(value = "cpu状态（默认0%）")
    private java.lang.String cpuStatus;

    @ApiModelProperty(value = "内存状态（默认0%）")
    private java.lang.String memoryStatus;

    @ApiModelProperty(value = "网络状态（默认0%）")
    private java.lang.String networkStatus;

    @ApiModelProperty(value = "版本号")
    private java.lang.String versionNo;
    
    @ApiModelProperty(value = "项目列表")
    private List<ProjectMappingVo> projectMappingVos;
    
    @ApiModelProperty(value = "所属地址名称")
    private java.lang.String addressName;
    
    @ApiModelProperty(value = "系统状态阈值")
    private java.lang.String threshold = "80%";

    /*@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/


}

