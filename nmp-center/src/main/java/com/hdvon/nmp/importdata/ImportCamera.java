package com.hdvon.nmp.importdata;

import java.io.Serializable;
import java.util.List;

import com.hdvon.nmp.entity.Camera;
import com.hdvon.nmp.entity.CameraCameragroup;
import com.hdvon.nmp.entity.CameraMapping;
import com.hdvon.nmp.entity.Device;

import lombok.Data;

@Data
public class ImportCamera   implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -7666014203651132577L;
	
	private String deviceIds;//用逗号隔开的多个设备id，kafka推送消息，同步摄像机信息到es的参数
	
	private List<Device> devices;
	
	private List<Camera> cameras;
	
	private List<CameraMapping> cameraMappings;
	
	private List<CameraCameragroup> cameraCameraGroups;

}
