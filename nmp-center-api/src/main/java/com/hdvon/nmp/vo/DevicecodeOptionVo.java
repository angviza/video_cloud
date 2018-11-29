package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>设备编码生成器选项 VO类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="DevicecodeOption")
public class DevicecodeOptionVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "省（2位）")
    private java.lang.String province;

    @ApiModelProperty(value = "城市（2位）")
    private java.lang.String city;

    @ApiModelProperty(value = "地区（6位）")
    private java.lang.String area;

    @ApiModelProperty(value = "基层单位（2位）")
    private java.lang.String basicNnit;

    @ApiModelProperty(value = "行业 当code为08,09,13,14,15时，industryName必须有值（2位）")
    private java.lang.String industry;

    @ApiModelProperty(value = "类型（3位）")
    private java.lang.String type;

    @ApiModelProperty(value = "网络标识（1位）")
    private java.lang.String internet;

    @ApiModelProperty(value = "行业名称（6位）")
    private java.lang.String industryName;

    @ApiModelProperty(value = "类型为5时-公安机关监控类型（3位）")
    private java.lang.String contorlType;

    @ApiModelProperty(value = "-监控范围 ;公安网时必选（3位）")
    private java.lang.String region;

    @ApiModelProperty(value = "-投资单位(6位):网络类型为不为5,或者行业 当code为08,09,13,14,15时 ")
    private java.lang.String investment;

    @ApiModelProperty(value = "承建单位单位（6位）：类型为不为5时")
    private java.lang.String construction;

    @ApiModelProperty(value = "编码生成数量")
    private java.lang.Integer number;

    //扩展字段
    @ApiModelProperty(value = "设备编码")
    private String deviceCode;
    
    @ApiModelProperty(value = "是否使用（0-否，1-是）")
    private Integer status;
    
    
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;

    /*@ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/


}

