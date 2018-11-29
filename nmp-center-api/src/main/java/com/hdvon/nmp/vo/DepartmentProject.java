package com.hdvon.nmp.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>部门项目树 (项目树)VO类<br>
 * <b>作者：</b>huangguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="departmentProject")
public class DepartmentProject implements Serializable{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "父id")
    private String pid;
    
    @ApiModelProperty(value = "项目id 为null是为部门树，否则是项目id")
    private String projectId;
    
    @ApiModelProperty(value = "项目编号")
    private String code;
    
    @ApiModelProperty(value = "节点类型")
    private String nodeType;
}
