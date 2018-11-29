package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>设备编码生成字典表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="DevicecodeDictionary")
public class DevicecodeDictionaryVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "父级id")
    private java.lang.String pid;
    
    @ApiModelProperty(value = "编码位置")
    private java.lang.String position;

    @ApiModelProperty(value = "对应位置的字典编码")
    private java.lang.String code;

    @ApiModelProperty(value = "字典编码名称")
    private java.lang.String name;

    @ApiModelProperty(value = "父字典编码")
    private java.lang.String pcode;

    @ApiModelProperty(value = "是否预留（1：是，0：否）")
    private java.lang.Integer isReserved;
    
    
    @ApiModelProperty(value = "节点等级")
    private Integer level;

    /*@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/


}

