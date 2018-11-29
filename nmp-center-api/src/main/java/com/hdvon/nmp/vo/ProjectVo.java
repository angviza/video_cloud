package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>项目信息 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="Project")
public class ProjectVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "项目名称")
    private String name;

    @ApiModelProperty(value = "项目编号")
    private String code;

    @ApiModelProperty(value = "项目类型（1，新建项目；2：升级改造项目；9,其他项目'）")
    private java.lang.String type;
    
    @ApiModelProperty(value = "项目类点")
    private Integer classType;

    @ApiModelProperty(value = "项目描述")
    private String description;

    @ApiModelProperty(value = "同级排序")
    private Integer orderby;

    @ApiModelProperty(value = "联系人")
    private String contactor;

    @ApiModelProperty(value = "电话")
    private String teleno;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "建设部门id")
    private String constructorDepId;
    
    @ApiModelProperty(value = "建设部门名称")
    private String construtorName;

    @ApiModelProperty(value = "承建部门id")
    private String builderDepId;
    
    @ApiModelProperty(value = "承建部门名称")
    private String builderName;
    
    @ApiModelProperty(value = "承建部门名称")
    private String sigServerId;

    /*@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/


}

