package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>收藏夹关联表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="CollectMapping")
public class CollectMappingVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "关联收藏夹id")
    private java.lang.String collectId;

    @ApiModelProperty(value = "关联设备id")
    private java.lang.String deviceId;

    @ApiModelProperty(value = "设备名称")
    private java.lang.String deviceName;

    @ApiModelProperty(value = "设备编码")
    private java.lang.String deviceCode;

    @ApiModelProperty(value = "设备类型")
    private Integer deviceType;
    
    @ApiModelProperty(value = "跳转url")
    private String url;
    
}

