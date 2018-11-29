package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>上墙轮巡的矩阵通道表 VO类<br>
 * <b>作者：</b>zhanqf<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="WallChannel")
public class WallChannelVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "上墙轮巡主表id")
    private java.lang.String rotateId;

    @ApiModelProperty(value = "矩阵通道id")
    private java.lang.String channelId;

    @ApiModelProperty(value = "顺序")
    private java.lang.Integer queue;

    @ApiModelProperty(value = "当前播放的摄像机id")
    private java.lang.String cameraId;

    @ApiModelProperty(value = "调用jni接口返回的callid")
    private java.lang.String callId;

}

