package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>球机巡航预案表 VO类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="BallPlan")
public class BallPlanVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "关联摄像机id")
    private java.lang.String cameraId;

    @ApiModelProperty(value = "预案名称")
    private java.lang.String name;

    @ApiModelProperty(value = "是否启用状态（1：启用；0：禁用）")
    private java.lang.Integer status;

    @ApiModelProperty(value = "共享设置状态（0:私有，仅自己可见；1全局共享，所有人可见；2部门共享，本部门用户可见；3指定部门共享，对指定的部门用户可见）")
    private java.lang.Integer shareStatus;

    @ApiModelProperty(value = "预案开始时间")
    private java.util.Date bgnTime;

    @ApiModelProperty(value = "预案结束时间")
    private java.util.Date endTime;

    @ApiModelProperty(value = "说明")
    private java.lang.String description;
    
    @ApiModelProperty(value = "摄像机名称")
    private java.lang.String cameraName;
    
    @ApiModelProperty(value = "摄像机类型")
    private java.lang.String cameraType;
    
    @ApiModelProperty(value = "摄像机国标编号")
    private java.lang.String cameraNo;

    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;

    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/

    @ApiModelProperty(value = "共享设置部门树")
    private List<DepartmentVo> departmentVos;
}

