package com.hdvon.nmp.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectParamVo  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3997959605540165690L;


	@ApiModelProperty(value = "部门id")
	private String deptId;

    @ApiModelProperty(value = "部门编号")
    private String deptCode;
    
    @ApiModelProperty(value = "项目名称")
    private String projectName;
}
