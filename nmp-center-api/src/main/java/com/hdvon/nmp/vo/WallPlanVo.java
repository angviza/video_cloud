package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>上墙预案 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="WallPlan")
public class WallPlanVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "关联矩阵id")
    private java.lang.String matrixId;

    @ApiModelProperty(value = "上墙预案名称")
    private java.lang.String name;

    @ApiModelProperty(value = "预案状态（1：启用；0：停止）")
    private java.lang.Integer status;

    @ApiModelProperty(value = "共享状态（0:私有，仅自己可见；1全局共享，所有人可见；2部门共享，本部门用户可见；3指定部门共享，对指定的部门用户可见）")
    private java.lang.Integer shareStatus;

    @ApiModelProperty(value = "开始时间")
    private java.util.Date bgnTime;

    @ApiModelProperty(value = "结束时间")
    private java.util.Date endTime;

    @ApiModelProperty(value = "说明")
    private java.lang.String description;
    
    @ApiModelProperty(value = "矩阵通道id对应的摄像机列表的map集合")
    private List<Map<String,List<String>>> idList;
    
    @ApiModelProperty(value = "矩阵通道集合")
    private List<MatrixChannelVo> matrixChannels;
    
    @ApiModelProperty(value = "关联矩阵名称")
    private java.lang.String matrixName;

    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;

    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;

    @ApiModelProperty(value = "创建人")
    private java.lang.String createUser;

    @ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;
    
    @ApiModelProperty(value = "更新用户所属部门")
    private java.lang.String deptId;
    
    @ApiModelProperty(value = "共享部门列表")
    private List<PlanShareVo> planShareVos;
    
    @ApiModelProperty(value = "共享设置部门树")
    private List<DepartmentVo> departmentVos;
}

