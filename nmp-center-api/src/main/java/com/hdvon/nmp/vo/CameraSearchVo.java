package com.hdvon.nmp.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>摄像机访问历史报表 VO类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="CameraSearch")
public class CameraSearchVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	 @ApiModelProperty(value = "设备编码支持模糊")
	 private String sbbm;
		
	 @ApiModelProperty(value = "设备/区域/系统名称支持模糊")
     private String sbmc;
	 
	 @ApiModelProperty(value = "摄像机类型")
	 private Integer sxjlx;
	 
	 @ApiModelProperty(value = "所属编码器")
	 private String encoderServerId;
	 
	 @ApiModelProperty(value = "设备ip地址")
	 private String ipv4;
	 
	 @ApiModelProperty(value = "设备状态")
	 private Integer sbzt;
	 
	 @ApiModelProperty(value = "建设单位")
	 private String jsdw;

     @ApiModelProperty(value = "承建单位")
     private String cjdw;
     
     @ApiModelProperty(value = "设备厂商")
	 private Integer sbcs;
     
     @ApiModelProperty(value = "摄像机所属部门")
	 private Integer sxjssbm;
     
     @ApiModelProperty(value = "采集点类别")
	 private Integer cjdlb;
	    
	 @ApiModelProperty(value = "摄像机功能类型")
	 private Integer sxjgnlx;
     

}
