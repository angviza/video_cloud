package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b> VO类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="CameraConnections")
public class CameraConnectionsVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.Long id;

    @ApiModelProperty(value = "deviceCode")
    private java.lang.String deviceCode;

    @ApiModelProperty(value = "monitorConnections")
    private java.lang.Long monitorConnections;

    @ApiModelProperty(value = "replayConnections")
    private java.lang.Long replayConnections;

    @ApiModelProperty(value = "downloadConnections")
    private java.lang.Long downloadConnections;


}

