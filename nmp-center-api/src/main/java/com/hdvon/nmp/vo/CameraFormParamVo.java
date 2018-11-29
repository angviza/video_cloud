package com.hdvon.nmp.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b> 设备高级搜索条件VO类<br>
 * <b>作者：</b>huangguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="CameraFormParamVo")
public class CameraFormParamVo implements Serializable{
	private static final long serialVersionUID = 1L;

	
    @ApiModelProperty(value = "用户id")
	private String userId;//用户id
	
	@ApiModelProperty(value = "查询字符串 适用 设备名称 设备编码  地址  所属项目 所属新政区域 地址树 自定义分组")
	private String queryStr;//查询字符串 适用 设备名称 设备编码  地址  所属项目 所属新政区域 地址树 自定义分组  
	
	
	@ApiModelProperty(value = "摄像机名称")
    private String deviceName;//摄像机名称
    
	@ApiModelProperty(value = "行政编码")
    private String orgCode;//行政编码
    
	@ApiModelProperty(value = "项目编号集合")
    private String projectId;//项目编号集合
    
	@ApiModelProperty(value = "地址编码")
    private String addressCode;//地址编码
    
	@ApiModelProperty(value = "自定义分组编码")
    private String groupCode;//自定义分组编码
    
	@ApiModelProperty(value = "摄像机id")
    private String cameraId;//摄像机id
    
	@ApiModelProperty(value = "摄像机id")
    private String deviceCode;//摄像机id
    
	@ApiModelProperty(value = "编码器名称")
	private String encoderServerName;//编码器名称
	
	@ApiModelProperty(value = "ip地址")
	private String ip;//ip地址
	
	@ApiModelProperty(value = "位置类型")
	private Integer positionType;//位置类型
	
	@ApiModelProperty(value = "建设单位")
	private String constructionUnit;//建设单位
	
	@ApiModelProperty(value = "城建单位")
	private String urbanConstructionUnit;//城建单位
	
	@ApiModelProperty(value = "采集点类别")
	private Integer collectionCategory;//采集点类别
	
	@ApiModelProperty(value = "功能类型")
	private Integer functionalType;//功能类型
	
	@ApiModelProperty(value = "设备厂商")
	private String deviceCompany;//设备厂商
	
	@ApiModelProperty(value = "摄像机类型")
	private Integer deviceType;//摄像机类型
	
	@ApiModelProperty(value = "摄像机状态")
	private Integer status;//摄像机状态
	
	@ApiModelProperty(value = "新增区域名称")
	private String orgName;//新增区域名称
	
	@ApiModelProperty(value = "所属设备编码")
	private String encoderServerId;
	
	@ApiModelProperty(value = "所属部门")
	private String departmentId;

    

}
