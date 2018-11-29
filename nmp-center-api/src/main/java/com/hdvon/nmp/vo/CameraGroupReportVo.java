package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>摄像机组报表统计Vo<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018/8/7<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="CameraGroupReportVo")
public class CameraGroupReportVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "摄像机分组ID")
	private String id;
	
	@ApiModelProperty(value = "摄像机分组名称")
	private String name;
	
	@ApiModelProperty(value = "操作日期     日期格式：yyyy-MM-dd")
	private String date;
	 
	@ApiModelProperty(value = "访问热度值")
	private Integer hotPoints;
	
}
