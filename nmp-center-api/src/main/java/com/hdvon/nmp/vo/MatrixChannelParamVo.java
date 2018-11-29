package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value ="MatrixChannelParam")
public class MatrixChannelParamVo implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 2954278615449283812L;

	 @ApiModelProperty(value = "id")
	    private java.lang.String id;

	    @ApiModelProperty(value = "矩阵通道名称")
	    private java.lang.String name;

	    @ApiModelProperty(value = "矩阵通道设备编号")
	    private java.lang.String devicesNo;
	    
	    @ApiModelProperty(value = "description")
	    private java.lang.String description;

	    @ApiModelProperty(value = "matrixId")
	    private java.lang.String matrixId;
	    
	    @ApiModelProperty(value = "轮询摄像机集合")
	    private java.lang.String cameras;

	    /*@ApiModelProperty(value = "createTime")
	    private java.util.Date createTime;*/

	    /*@ApiModelProperty(value = "updateTime")
	    private java.util.Date updateTime;*/

	    /*@ApiModelProperty(value = "createUser")
	    private java.lang.String createUser;*/

	    /*@ApiModelProperty(value = "updateUser")
	    private java.lang.String updateUser;*/
}
