package com.hdvon.nmp.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel(value ="SigServerParam")
public class SigServerParamVo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7089006447600629428L;

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

    @ApiModelProperty(value = "启用状态(1启用，0停用)，默认停用")
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
    
    @ApiModelProperty(value = "地址编号")
    private java.lang.String addressCode;

    /*@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/


}
