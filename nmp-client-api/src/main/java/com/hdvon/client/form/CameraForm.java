package com.hdvon.client.form;

import java.io.Serializable;

import lombok.Data;

@Data
public class CameraForm implements Serializable{
	private String userId;//用户id
	
	private String queryStr;//查询字符串 适用 设备名称 设备编码  地址  所属项目 所属新政区域 地址树 自定义分组  
	
	
    private String deviceName;//摄像机名称
    
    private String orgCode;//行政编码
    
    private String projectId;//项目编号集合
    
    private String addressCode;//地址编码
    
    private String groupCode;//自定义分组编码
    
    private String cameraId;//摄像机id
    
    private String deviceCode;//摄像机id
    
	private String encoderServerName;//编码器名称
	
	private String ip;//ip地址
	
	private Integer positionType;//位置类型
	
	private String constructionUnit;//建设单位
	
	private String urbanConstructionUnit;//城建单位
	
	private Integer collectionCategory;//采集点类别
	
	private Integer functionalType;//功能类型
	
	private Integer deviceCompany;//设备厂商
	
	private Integer deviceType;//摄像机类型
	
	private Integer status;//摄像机状态
	
	private String orgName;//新增区域名称
	
	private Long deviceId;//设备id
	
	private String encoderServerId;//编码器id
	
	private Integer sxjssbm;//所属部门
	
	private boolean isHighFilter;//是否高级搜索

}
