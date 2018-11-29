package com.hdvon.nmp.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>业务分组关联摄像机 VO类<br>
 * <b>作者：</b>huangguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
public class CameraBussinessGroupVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "摄像id")
    private String cameraId;
	
	@ApiModelProperty(value = "摄像设备id")
    private String deviceId;
	
	@ApiModelProperty(value = "业务分组id")
    private String buessId;
	
	@ApiModelProperty(value = "设备编号(码)")
    private String sbbm;
	
	@ApiModelProperty(value = "设备名称")
    private String sbmc;
    
    @ApiModelProperty(value = "设备类别")
    private String sxjlx;
    
    @ApiModelProperty(value = "状态")
    private String sbzt;
	
}
