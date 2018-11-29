package com.hdvon.nmp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <br>
 * <b>功能：</b>地址表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="Address")
public class AddressVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "地址名称")
    private String name;

    @ApiModelProperty(value = "同级排序")
    private Integer orderby;

    @ApiModelProperty(value = "当前地址父id")
    private String pid;
    
    @ApiModelProperty(value = "编号")
    private java.lang.String code;

    @ApiModelProperty(value = "父编号")
    private java.lang.String pcode;

    @ApiModelProperty(value = "描述")
    private String description;
    
    @ApiModelProperty(value = "是否选中状态（1是0否）")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;

    /*@ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/

    @ApiModelProperty(value = "所属上级名称")
    private String parentName;
    
    @ApiModelProperty(value = "节点类型")
    private String nodeType;

}

