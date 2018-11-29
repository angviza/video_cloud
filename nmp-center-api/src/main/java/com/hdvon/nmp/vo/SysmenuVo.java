package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>系统功能表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="Sysmenu")
public class SysmenuVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "功能名称")
    private String name;

    @ApiModelProperty(value = "父id")
    private String pid;

    @ApiModelProperty(value = "请求地址")
    private String url;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "类型，1.菜单、2.操作、0.tab子菜单")
    private Integer type;

    @ApiModelProperty(value = "提交方式:POST、GET、PUT、DELETE")
    private String method;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "描述")
    private String description;
    
    @ApiModelProperty(value = "是否启用")
    private String enabled;

    @ApiModelProperty(value = "颜色")
    private java.lang.String color;

    @ApiModelProperty(value = "同级排序")
    private Integer orderby;

    @ApiModelProperty(value = "是否已关联(1.是，0.否)")
    private Integer isCheck;

}

