package com.hdvon.client.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * 摄像机许可资源树对象
 * @author wnsojn 2018-2-24 7:00
 *
 */
@Data
public class CameraPermissionVo implements Serializable{
    private String id;//地址id

    private String name;//地址名称
    
    private String cameraId;//摄像机id

    private String pid;//当前地址父id
    
    private String code;//编码
    
    private String deviceId;//设备id
    
    private String deviceCode;//设备编码
    
    private String deviceType;//摄像机类型
    
    private String permissionValue;//摄像机权限值[1,2,4,8]
    
    private Integer isProject;//是否是项目
    
    private String depCode;//部门编码
    
    private double latitude;//纬度
    
    private double longitude;//经度
    
    private String status;//状态 是否存在摄像机
    
    
}
