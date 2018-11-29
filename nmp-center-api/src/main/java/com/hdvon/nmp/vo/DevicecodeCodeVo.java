package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>设备编码器生成编码 VO类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="DevicecodeCode")
public class DevicecodeCodeVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "设备编码编号")
    private java.lang.String deviceCode;

    @ApiModelProperty(value = "devicecodeOptionId")
    private java.lang.String devicecodeOptionId;

    @ApiModelProperty(value = "是否使用（0-否，1-是）")
    private java.lang.Integer status;


}

