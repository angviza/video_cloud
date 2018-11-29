package com.hdvon.nmp.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatrixScreenVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "矩阵通道名称")
	private java.lang.String name;

    @ApiModelProperty(value = "矩阵通道设备编号")
    private java.lang.String channelNo;
    
    @ApiModelProperty(value = "摄像机编号")
	private java.lang.String cameraCode;

    @ApiModelProperty(value = "摄像机名称")
    private java.lang.String cameraName;
    
    @ApiModelProperty(value = "是否录像上墙（1：录像上墙；0：视屏上墙）")
    private java.lang.Integer isplayback;
}
