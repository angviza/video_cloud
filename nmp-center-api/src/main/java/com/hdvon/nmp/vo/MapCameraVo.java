package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
@Data
public class MapCameraVo implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8174707079461435178L;

	private PollingPlanVo pollingPlanVo;
	
	private List<CameraVo> cameraVos;
}
