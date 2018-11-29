package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>摄像机表与摄像机分组的关联表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="CameraCameragroup")
public class CameraCameragroupVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "摄像机id")
    private String cameraId;

    @ApiModelProperty(value = "摄像机分组id")
    private String cameragroupId;

    @ApiModelProperty(value = "摄像机分组名称")
    private String cameragroupName;
    
    @ApiModelProperty(value = "设备名称")
    private java.lang.String cameraName;

    @ApiModelProperty(value = "设备编码")
    private java.lang.String cameraCode;
    
    @ApiModelProperty(value = "组内摄像机排序序号")
    private java.lang.Integer sort;
    
    @ApiModelProperty(value = "设备id")
    private java.lang.String deviceId;
}

