package com.hdvon.client.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
/**
 * es用户摄像机资源对象
 * @author wanshaojian
 *
 */
@Data
public class EsCameraPermissionVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2977531820243351596L;
	
	private String id;//主键id
	
	
	private String cameraId;//摄像机id
	private Long deviceId; //设备id
	private String deviceName;//摄像机名称
	private String deviceCode;//摄像机编码
	private Integer deviceType;//摄像机类型
	private Integer status;//摄像机状态
	private Long orgId;//摄像机所属行政id
	private String orgName;//摄像机所属行政机构名称
	private String orgCode;//摄像机所属行政编码
	private Long addressId;//摄像机所属地址id
	private String addressName;//摄像机所属地址名称
	private String addressCode;//摄像机所属地址编码
	private Long projectId;//摄像机所属项目分组id
	private String projectName;//摄像机所属项目分组名称
	private String deviceCompany;//摄像机所属公司
	private String installAddress;//安装地址
	private String location;//坐标  纬度在前，经度在后
	private String encoderServerId;//编码器id
	private String encoderServerName;//编码器名称
	private String ip;//ip地址
	private Integer positionType;//位置类型
	private String constructionUnit;//建设单位
	private String urbanConstructionUnit;//城建单位
	private Integer collectionCategory;//采集点类别
	private Integer functionalType;//功能类型
	
	private String permissionValue;//用户摄像机权限
	
	private Double latitude;
	private Double Longitude;
	
	private Integer sxjssbm;//所属部门
	
	private Long userId;//摄像机许可用户id
	private String account;//用户名称

	private Date startDate;
	private Date endDate;
	/**
	 * 永久有效
	 */
	private Integer permanent;

}
