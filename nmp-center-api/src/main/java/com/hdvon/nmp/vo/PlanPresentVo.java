package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>预案关联预置位表 VO类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="PlanPresent")
public class PlanPresentVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "关联预案id")
    private java.lang.String planId;

    @ApiModelProperty(value = "关联预置位id")
    private java.lang.String presentId;

    @ApiModelProperty(value = "间隔时间（单位：秒）")
    private java.lang.Integer timeInterval;

    @ApiModelProperty(value = "预案中的预置位排序")
    private java.lang.Integer sort;
    
    @ApiModelProperty(value = "摄像机id")
    private java.lang.String cameraId;
    /**
     * 设备编码
     */
    private String cameraCode;
}

