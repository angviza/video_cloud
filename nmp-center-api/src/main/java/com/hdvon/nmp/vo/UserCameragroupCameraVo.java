package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.List;

import com.hdvon.nmp.common.CameraPermissionVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value ="UserCameragroupCamera")
public class UserCameragroupCameraVo implements Serializable{


    /**
	 * 
	 */
	private static final long serialVersionUID = -2499395991837710820L;

	@ApiModelProperty(value = "地址列表")
	private List<AddressVo> addressVos;

    @ApiModelProperty(value = "当前用户在自定义分组中可见摄像机列表")
    private List<CameraPermissionVo> cameraPermisssionVos;
}
