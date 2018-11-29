package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>报警设备表 VO类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="AlarmServer")
public class AlarmServerVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "设备名称")
    private java.lang.String name;

    @ApiModelProperty(value = "设备编码")
    private java.lang.String code;

    @ApiModelProperty(value = "设备ip")
    private java.lang.String ip;

    @ApiModelProperty(value = "经度")
    private Double jd;

    @ApiModelProperty(value = "纬度")
    private Double wd;

    @ApiModelProperty(value = "关联地址id")
    private java.lang.String addressId;

    @ApiModelProperty(value = "关联摄像机id")
    private java.lang.String cameraId;

    @ApiModelProperty(value = "关联预置位id")
    private java.lang.String presentId;

    @ApiModelProperty(value = "addressName")
    private java.lang.String addressName;

    @ApiModelProperty(value = "cameraName")
    private java.lang.String cameraName;

    @ApiModelProperty(value = "presentName")
    private java.lang.String presentName;

    @ApiModelProperty(value = "description")
    private java.lang.String description;

    @ApiModelProperty(value = "启用状态(1启用，0停用)，默认停用")
    private java.lang.Integer enabled;
    
    @ApiModelProperty(value = "项目列表")
    private List<AlarmserverMappingVo> alarmserverMappingVos;
    
    /*@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/


}

