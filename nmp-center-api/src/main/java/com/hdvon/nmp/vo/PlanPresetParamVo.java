package com.hdvon.nmp.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value ="PlanPresetParamVo")
public class PlanPresetParamVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
    @ApiModelProperty(value = "球机巡航预案id")
    private String planId;

    @ApiModelProperty(value = "摄像机id")
    private String cameraId;
}
