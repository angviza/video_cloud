package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>资源角色与摄像头权限关联表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="ResourceroleCameraPermission")
public class ResourceroleCameraPermissionVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "资源角色id")
    private String resouceroleId;

    @ApiModelProperty(value = "摄像机id")
    private String cameraId;

    @ApiModelProperty(value = "摄像机编码")
    private String deviceCode;

    @ApiModelProperty(value = "摄像机权限值[1,2,4,8]")
    private String permissionValue;

    @ApiModelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "设备名称")
    private String name;

}

