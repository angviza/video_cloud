package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>摄像机分组表
 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="Cameragrouop")
public class CameragrouopVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "分组名称")
    private String name;

    @ApiModelProperty(value = "分组父id")
    private String pid;
    
    @ApiModelProperty(value = "编号")
    private java.lang.String code;

    @ApiModelProperty(value = "父编号")
    private java.lang.String pcode;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否私有(1.是，0.否)")
    private Integer isPrivate;

    /*@ApiModelProperty(value = "分组创建人")
    private String creatorId;*/

    @ApiModelProperty(value = "同级排序")
    private Integer orderby;

    /*@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/

    @ApiModelProperty(value = "所属上级名称")
    private String parentName;

    @ApiModelProperty(value = "预案勾选状态")
    private Integer status;

}

