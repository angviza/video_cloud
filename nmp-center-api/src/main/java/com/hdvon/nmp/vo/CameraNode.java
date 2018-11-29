package com.hdvon.nmp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value ="CameraNode",description = "摄像机")
public class CameraNode implements Serializable {
    @ApiModelProperty(value = "树节点id")
    private String id;

    @ApiModelProperty(value = "树节点名称")
    private String name;

    @ApiModelProperty(value = "摄像机id")
    private String cameraId;

    @ApiModelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "设备编码")
    private String deviceCode;

    @ApiModelProperty(value = "经度")
    private Double jd;

    @ApiModelProperty(value = "纬度")
    private Double wd;

    @ApiModelProperty(value = "摄像机类型")
    private Integer deviceType;
}
