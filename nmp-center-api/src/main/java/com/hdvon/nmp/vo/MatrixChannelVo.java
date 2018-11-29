package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>数字矩阵通道 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="MatrixChannel")
public class MatrixChannelVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "矩阵通道名称")
    private java.lang.String name;

    @ApiModelProperty(value = "矩阵通道设备编号")
    private java.lang.String devicesNo;
    
    @ApiModelProperty(value = "description")
    private java.lang.String description;

    @ApiModelProperty(value = "matrixId")
    private java.lang.String matrixId;

    @ApiModelProperty(value = "matrixName")
    private java.lang.String matrixName;

    @ApiModelProperty(value = "轮询摄像机集合")
    private List<CameraVo> cameras;
    
    //用于视屏上墙
    @ApiModelProperty(value = "摄像机编号")
	private java.lang.String cameraCode;

    @ApiModelProperty(value = "摄像机名称")
    private java.lang.String cameraName;
    
    @ApiModelProperty(value = "是否录像上墙（1：录像上墙；0：视屏上墙）")
    private java.lang.Integer isplayback;

    /*@ApiModelProperty(value = "createTime")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "updateTime")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "createUser")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "updateUser")
    private java.lang.String updateUser;*/


}

