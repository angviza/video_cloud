package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>设备播放记录（临时）表 VO类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="CameraLog")
public class CameraLogVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private java.lang.String id;

    @ApiModelProperty(value = "用户id")
    private java.lang.String userId;

    @ApiModelProperty(value = "设备编码")
    private java.lang.String sbbm;

    @ApiModelProperty(value = "播放返回的id")
    private java.lang.String callId;

    @ApiModelProperty(value = "用户登录ip")
    private java.lang.String userIp;

    @ApiModelProperty(value = "播放方式 1 实时；2 录像，3 下载")
    private java.lang.String playType;

    @ApiModelProperty(value = "状态： 1 在线  0 离线")
    private java.lang.String state;

    /*@ApiModelProperty(value = "播放时间")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;*/


}

