package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>轮询预案关联中间表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="PollingplanCamera")
public class PollingplanCameraVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "关联轮询预案表id")
    private java.lang.String pollingplanId;

    @ApiModelProperty(value = "关联摄像机分组id")
    private java.lang.String cameraId;
    
    @ApiModelProperty(value = "关联设备id")
    private java.lang.String deviceId;
    
    @ApiModelProperty(value = "关联摄像机编码")
    private java.lang.String deviceCode;
    
    @ApiModelProperty(value = "关联摄像机表名称")
    private java.lang.String name;

    @ApiModelProperty(value = "自定义分组id（冗余字段）")
    private java.lang.String cameragroupId;

    @ApiModelProperty(value = "自定义分组名称（冗余字段）")
    private java.lang.String cameragroupName;

    @ApiModelProperty(value = "排序序号")
    private java.lang.Integer sort;
}

