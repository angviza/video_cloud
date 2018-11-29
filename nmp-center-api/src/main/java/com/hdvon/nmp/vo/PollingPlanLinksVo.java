package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.List;

import com.hdvon.nmp.common.CameraPermissionVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value ="pollingPlanLinksVo")
public class PollingPlanLinksVo<T>  implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = -5761500250577310226L;
	
	@ApiModelProperty(value = "摄像机列表")
    private List<CameraNode> addressCameraList;

	@ApiModelProperty(value = "关联的分组列表")
    private List<CameragrouopVo> groupList;
	
	@ApiModelProperty(value = "地图选择摄像机列表")
    private List<CameraNode> mapCameraList;

}
