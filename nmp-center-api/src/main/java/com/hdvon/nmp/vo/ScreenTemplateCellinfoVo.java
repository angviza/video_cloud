package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>分屏管理单元屏信息表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="ScreenTemplateCellinfo")
public class ScreenTemplateCellinfoVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "关联模板表id")
    private java.lang.String templateId;

    @ApiModelProperty(value = "行号")
    private java.lang.Integer rowNo;

    @ApiModelProperty(value = "合并行数")
    private java.lang.Integer rowSpan;

    @ApiModelProperty(value = "列号")
    private java.lang.Integer colNo;

    @ApiModelProperty(value = "合并列数")
    private java.lang.Integer colSpan;

    /*@ApiModelProperty(value = "createTime")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "updateTime")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "createUser")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "updateUser")
    private java.lang.String updateUser;*/


}

