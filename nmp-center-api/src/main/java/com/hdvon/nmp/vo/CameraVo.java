package com.hdvon.nmp.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>摄像机表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="Camera")
public class CameraVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "访问热度")
    private Integer hots;

    @ApiModelProperty(value = "摄像机ip地址")
    private java.lang.String cameraIp;

    @ApiModelProperty(value = "接入方式:1国标0非国标")
    private Integer accessMode;

    @ApiModelProperty(value = "地址树名称")
    private String addressName;

    @ApiModelProperty(value = "设备名称")
    private String sbmc;
    
    @ApiModelProperty(value = "设备编码")
    private String sbbm;
    
    @ApiModelProperty(value = "设备安装地址")
    private String azdz;

    @ApiModelProperty(value = "设备类别")
    private int sxjlx;

    @ApiModelProperty(value = "状态")
    private int sbzt;

    @ApiModelProperty(value = "所属编码器")
    private String encodeServerName;

    @ApiModelProperty(value = "所在分组名称")
    private String groupName;

    @ApiModelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "地址id")
    private String addressId;

    @ApiModelProperty(value = "编码器表id")
    private String encoderServerId;

    @ApiModelProperty(value = "摄像机工程id")
    private String projectId;

    @ApiModelProperty(value = "摄像机组织机构id")
    private String orgId;

    @ApiModelProperty(value = "摄像机所在分组id")
    private String cameraGroupId;

    @ApiModelProperty(value = "建设单位")
    private String jsdw;
    
    @ApiModelProperty(value = "承建单位单位")
    private String cjdw;

    @ApiModelProperty(value = "注册用户名")
    private String registeredName;
    
    @ApiModelProperty(value = "注册密码")
    private String registeredPass;
    
    @ApiModelProperty(value = "注册中心信令端口")
    private String registeredServerPort;

    @ApiModelProperty(value = "摄像机正常状态")
    private int zcztCount;
    
    @ApiModelProperty(value = "矩阵通道id")
    private String matrixChannelId;
    
    @ApiModelProperty(value = "矩阵通道id")
    private List<PresentPositionVo> presentPositions;
    
    @ApiModelProperty(value = "用户操作权限值")
    private String permissionValue;
    
    @ApiModelProperty(value = "关联预案的摄像机排序(可用于轮巡预案和上墙预案)")
    private Integer planSort;

    /**摄像机关联中间表*/

    /*@ApiModelProperty(value = "所在地址树名称")
    private String addressName;

    @ApiModelProperty(value = "编码器名称")
    private String encoderServerName;

    @ApiModelProperty(value = "所在摄像机工程名称")
    private String projectName;

    @ApiModelProperty(value = "所在摄像机组织机构名称")
    private String orgName;
    */

    /*@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/


}

