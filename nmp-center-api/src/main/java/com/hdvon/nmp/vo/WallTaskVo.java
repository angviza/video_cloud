package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <b>功能：</b>上墙任务主表 VO类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="WallTask")
public class WallTaskVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "任务名称")
    private java.lang.String name;
    
    @ApiModelProperty(value = "用户表id")
    private java.lang.String userId;

    @ApiModelProperty(value = "矩阵id")
    private java.lang.String matrixId;
    
}

