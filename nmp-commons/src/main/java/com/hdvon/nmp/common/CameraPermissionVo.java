package com.hdvon.nmp.common;

import java.io.Serializable;

import lombok.Data;
/**
 * 摄像机许可资源树对象
 * @author wnsojn 2018-2-24 7:00
 *
 */
@Data
public class CameraPermissionVo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8141033928546543945L;

	private String id;//地址id

    private String name;//地址名称

    private String pid;//当前地址父id
    
    private String deviceId;//设备id
    
    private String cameraId;//摄像机id
    
    private String deviceCode;//设备编码
    
    private String deviceType;//摄像机类型
    
    private String permissionValue;//摄像机权限值[1,2,4,8]
    
    private Integer isProject;//是否是项目
    
    private String depCode;//部门编码
    
    private String status;//是否关联状态
    
    private String nodeType;//节点状态
    
}
