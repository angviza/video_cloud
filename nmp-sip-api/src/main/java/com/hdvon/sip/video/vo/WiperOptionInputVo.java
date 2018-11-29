package com.hdvon.sip.video.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>录像控制参数 VO类<br>
 * <b>作者：</b>haungguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="WiperOptionInputVo")
public class WiperOptionInputVo extends BaseOptionVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "控制类型：1：开2：关")
	private Integer cmdType;

}
