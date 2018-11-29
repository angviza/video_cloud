package com.hdvon.nmp.vo.sip;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>用户拥有摄像机权限 VO类<br>
 * <b>作者：</b>haungguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="UserDeviceParamVo")
public class UserDeviceParamVo extends BaseOption implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @ApiModelProperty(value = "用户拥有该摄像机的权限值")
	private String permissionVlaue;

}
