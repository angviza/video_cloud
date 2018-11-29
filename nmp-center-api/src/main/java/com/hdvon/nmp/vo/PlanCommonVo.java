package com.hdvon.nmp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <br>
 * <b>功能：</b>预案通用类 VO类，目前用于地图搜索<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="plan")
public class PlanCommonVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "预案名称")
    private java.lang.String name;

    @ApiModelProperty(value = "预案类型：polling:轮巡预案、ball:球机轮巡、wall:上墙预案")
    private java.lang.String type;

    @ApiModelProperty(value = "状态（是否启用：1启用，0停止）")
    private java.lang.String status;

    @ApiModelProperty(value = "预案描述")
    private java.lang.String description;

}
