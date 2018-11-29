package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.List;

import com.hdvon.nmp.common.CameraPermissionVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value ="UserPermissionCamera")
public class UserPermissionCameraVo implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -7666952584315152300L;
	
	@ApiModelProperty(value = "地址列表")
	private List<AddressVo> addressVos;

    @ApiModelProperty(value = "当前用户在关联的对象中可见摄像机列表")
    private List<CameraPermissionVo> cameraPermisssionVos;
    
    @ApiModelProperty(value = "当前用户地址摄像机树")
    private List<CameraPermissionVo> cameraPermisssionTree;
	
}
