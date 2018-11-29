package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b> VO类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="SipLog")
public class SipLogVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "sip操作日志id")
    private java.lang.String id;

    @ApiModelProperty(value = "请求业务id")
    private java.lang.String transactionId;

    @ApiModelProperty(value = "会话id")
    private java.lang.String callId;

    @ApiModelProperty(value = "用户id")
    private java.lang.String userId;

    @ApiModelProperty(value = "请求方法")
    private java.lang.String method;

    @ApiModelProperty(value = "操作内容")
    private java.lang.String content;

    @ApiModelProperty(value = "请求地址")
    private java.lang.String reqIp;

    @ApiModelProperty(value = "设备编码")
    private java.lang.String deviceId;

    @ApiModelProperty(value = "请求参数")
    private java.lang.String param;

    @ApiModelProperty(value = "播放状态")
    private java.lang.String playStatus;

    @ApiModelProperty(value = "设备状态")
    private java.lang.String deviceStatus;

    @ApiModelProperty(value = "请求时间")
    private java.util.Date reqTime;

    /*@ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;*/


}

