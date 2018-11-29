package com.hdvon.sip.video.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>录像查询 VO类<br>
 * <b>作者：</b>haungguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="QueryRecOptionInputVo")
public class QueryRecOptionInputVo extends BaseOptionVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("录像开始时间")
	private Long startTime;
	
	@ApiModelProperty("录像开始时间")
	private Long endTime;
	
//	@ApiModelProperty("默认填all")
//	private String type="all";
	
}
