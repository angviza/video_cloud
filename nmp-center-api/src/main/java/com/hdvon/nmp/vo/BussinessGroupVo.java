package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>业务分组 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="BussinessGroup")
public class BussinessGroupVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "业务分组名称")
    private String name;

    @ApiModelProperty(value = "业务分组编号")
    private String code;

    @ApiModelProperty(value = "摄像机关联状态(0：未关联，1：已关联)")
    private Integer status;
    
    @ApiModelProperty(value = "描述")
    private String description;

    /*@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;*/

    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;

    @ApiModelProperty(value = "创建人")
    private java.lang.String createUser;

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/


}

