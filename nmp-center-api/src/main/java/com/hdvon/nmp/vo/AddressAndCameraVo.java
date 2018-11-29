package com.hdvon.nmp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <br>
 * <b>功能：</b>地址摄像机树 VO类<br>
 * <b>作者：</b>huangguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="addressAndCamera")
public class AddressAndCameraVo implements Serializable{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "地址名称(设备名称)")
    private String name;

    @ApiModelProperty(value = "当前地址父id")
    private String pid;
    
    @ApiModelProperty(value = "摄像机id")
    private String deviceId;
    
    @ApiModelProperty(value = "是否勾选，true 是，false 否")
    private boolean check;
    
    @ApiModelProperty(value = "已拥护摄像机操作权限值,'1,2,4,8'")
    private String permissionValue;
    
    @ApiModelProperty(value = "节点类型")
    private String nodeType;

}
