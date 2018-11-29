package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.List;

import com.hdvon.nmp.common.CameraPermissionVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value ="ChannelCameraTreeGroup")
public class PlanCameraTreeGroupVo<T>  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5761500250577310226L;

	@ApiModelProperty(value = "未分组的摄像机列表")
    private List<T> cameras;
	
	@ApiModelProperty(value = "关联的分组列表")
    private List<CameragrouopVo> groups;
}
