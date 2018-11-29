package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>存储服务器关联摄像机 VO类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="StoreserverCamera")
public class StoreserverCameraVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "关联摄像机id")
    private java.lang.String cameraId;

    @ApiModelProperty(value = "关联存储服务器id")
    private java.lang.String storeserverId;

    @ApiModelProperty(value = "摄像机名称")
    private java.lang.String cameraName;

    @ApiModelProperty(value = "摄像机编号")
    private java.lang.String cameraNo;

    @ApiModelProperty(value = "摄像机类型")
    private java.lang.Integer cameraType;

    @ApiModelProperty(value = "摄像机录像状态")
    private java.lang.Integer status;

    @ApiModelProperty(value = "录像保存天数")
    private java.lang.Integer keepDays;
    
    @ApiModelProperty(value = "定时录像开始日期")
    private java.lang.String bgnDate;

}

