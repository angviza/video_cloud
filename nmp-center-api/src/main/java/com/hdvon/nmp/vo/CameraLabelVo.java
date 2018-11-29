package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>摄像头录像标签表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="CameraLabel")
public class CameraLabelVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "标签名称")
    private java.lang.String name;

    @ApiModelProperty(value = "摄像机id")
    private java.lang.String cameraId;

    @ApiModelProperty(value = "录像开始时间")
    private Long startTime;

    @ApiModelProperty(value = "录像结束时间")
    private Long endTime;
    
    @ApiModelProperty(value = "设备id")
    private java.lang.String deviceId;
    
    @ApiModelProperty(value = "设备编码")
    private java.lang.String deviceCode;
   

    /*@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/


}

