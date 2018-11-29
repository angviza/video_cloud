package com.hdvon.nmp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <br>
 * <b>功能：</b>用户表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="User")
public class OnlineUserVo extends UserVo implements Serializable{

    @ApiModelProperty(value = "是否允许强制下线")
    private boolean isAllowOff = false;

    @ApiModelProperty(value = "登录时长")
    private String loginTime = "";

    @ApiModelProperty(value = "用户状态")
    private String loginStatus = "";

    @ApiModelProperty(value = "当前操作资源")
    private String curOperation = "";
}

