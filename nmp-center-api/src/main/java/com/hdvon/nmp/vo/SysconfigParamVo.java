package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>系统配置 VO类<br>
 * <b>作者：</b>huangguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="Sysconfig")
public class SysconfigParamVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private java.lang.String id;

    @ApiModelProperty(value = "配置项名称")
    private java.lang.String name;

    @ApiModelProperty(value = "英文名称(标识)")
    private java.lang.String enName;

    @ApiModelProperty(value = "配置项值")
    private java.lang.String value;

    @ApiModelProperty(value = "是否开启（1.开启，0.关闭）")
    private java.lang.String state;

    @ApiModelProperty(value = "描述信息")
    private java.lang.String description;

   
    //关联字段
    @ApiModelProperty(value = "配置项中文名")
    private java.lang.String chname;
    
    @ApiModelProperty(value = "配置项字典值")
    private java.lang.String dictionaryId;
    
    /*@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/


}

