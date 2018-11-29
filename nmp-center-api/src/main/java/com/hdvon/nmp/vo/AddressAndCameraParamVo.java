package com.hdvon.nmp.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b> 业务分组地址摄像机树 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="addressAndCameraParam")
public class AddressAndCameraParamVo implements Serializable{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "地址名称(设备名称)")
    private String sbmc;
    
    @ApiModelProperty(value = "设备编码")
    private String sbbm;

    @ApiModelProperty(value = "当前地址父id")
    private String pid;
    
    @ApiModelProperty(value = "摄像机id")
    private String deviceId;
    
    @ApiModelProperty(value = "摄像机是否被关联分组 null:未关联,不为null已被关联")
    private String businessGroupId;
    
    

}
