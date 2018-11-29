package com.hdvon.nmp.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value ="AlarmServerParam")
public class AlarmServerParamVo implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -2630391702960999642L;
	@ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "设备名称")
    private java.lang.String name;

    @ApiModelProperty(value = "设备编码")
    private java.lang.String code;

    @ApiModelProperty(value = "设备ip")
    private java.lang.String ip;

    @ApiModelProperty(value = "经度")
    private Double jd;

    @ApiModelProperty(value = "纬度")
    private Double wd;

    @ApiModelProperty(value = "关联地址id")
    private java.lang.String addressId;

    @ApiModelProperty(value = "关联摄像机id")
    private java.lang.String cameraId;

    @ApiModelProperty(value = "关联预置位id")
    private java.lang.String presentId;

    @ApiModelProperty(value = "description")
    private java.lang.String description;
    
    @ApiModelProperty(value = "启用状态(1启用，0停用)，默认停用")
    private java.lang.Integer enabled;
    
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
