package com.hdvon.nmp.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class TimeingVedioLegVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8985980068459430373L;
	@ApiModelProperty(value = "定时录像设置时间段的开始时间秒数")
	private java.lang.Integer bgnTime;
	
	@ApiModelProperty(value = "定时录像设置时间段的结束时间秒数")
	private java.lang.Integer endTime;
}
