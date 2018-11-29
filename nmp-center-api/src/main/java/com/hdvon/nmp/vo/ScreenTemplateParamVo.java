package com.hdvon.nmp.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class ScreenTemplateParamVo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8331006168546052146L;

	@ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "模板名称")
    private java.lang.String name;

    @ApiModelProperty(value = "行数")
    private java.lang.Integer rows;

    @ApiModelProperty(value = "列数")
    private java.lang.Integer cols;

    @ApiModelProperty(value = "说明")
    private java.lang.String description;

    /*@ApiModelProperty(value = "createTime")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "updateTime")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "createUser")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "updateUser")
    private java.lang.String updateUser;*/

}
