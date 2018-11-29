package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>系统角色功能关联表 VO类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="SysroleSysmenu")
public class SysroleSysmenuVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "系统角色表id")
    private String sysroleId;

    @ApiModelProperty(value = "系统功能表id")
    private String sysmenuId;


}

