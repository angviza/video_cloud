package com.hdvon.nmp.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel(value ="MatrixParam")
public class MatrixParamVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6518424463797080125L;
	
	@ApiModelProperty(value = "设备名称")
	private String name;
	
	@ApiModelProperty(value = "设备编号")
	private String deviceNo;
	
	@ApiModelProperty(value = "注册用户")
	private String registerUser;
	
	@ApiModelProperty(value = "通道名称")
	private String channelName;
	
	@ApiModelProperty(value = "部门id")
	private String deptId;
	
	@ApiModelProperty(value = "部门编号")
	private String deptCode;


}
