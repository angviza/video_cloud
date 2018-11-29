package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>摄像机报表统计Vo<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="ReportResponseVo")
public class ReportResponseVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "设备id")
	private String id;
	
	@ApiModelProperty(value = "设备名称")
	private String name;
	 
	@ApiModelProperty(value = "设备编码")
	private String deviceCode;
	 
	@ApiModelProperty(value = "访问热度值")
	private Integer hotPoints;
	 
	@ApiModelProperty(value = "操作时间")
	private java.util.Date creatTime;
	

}
