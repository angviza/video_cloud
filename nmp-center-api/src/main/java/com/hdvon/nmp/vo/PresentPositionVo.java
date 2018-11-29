package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>预置位表 VO类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="PresentPosition")
public class PresentPositionVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "预置位名称")
    private java.lang.String name;

    @ApiModelProperty(value = "预置位编号")
    private java.lang.String presentNo;
    
    @ApiModelProperty(value = "是否守望位（1：是；0：否）")
    private java.lang.Integer isKeepwatch;

    @ApiModelProperty(value = "排序序号")
    private java.lang.Integer sort;

    @ApiModelProperty(value = "关联摄像机id")
    private java.lang.String cameraId;
    
    @ApiModelProperty(value = "关联球机巡航预案id")
    private java.lang.String planId;
    
    @ApiModelProperty(value = "预置位在预案中的排序序号")
    private java.lang.String presentSort;
    
    @ApiModelProperty(value = "预置位在预案中的时间间隔")
    private java.lang.String timeInterval;
    
    @ApiModelProperty(value = "是否关联该预置位 1:是 0:否")//用于预案、下拉选中
    private java.lang.String status;
    
    @ApiModelProperty(value = "设备编码")
    private String cameraCode;
    
    /*@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/


}

