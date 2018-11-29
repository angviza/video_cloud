package com.hdvon.quartz.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * <br>
 * <b>功能：</b>摄像机报表统计Vo<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
public class ReportResponseVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	 private String cameraId;//摄像机id
	 
	 private String name;//设备名称
	 
	 private String deviceCode;//设备编码
	 
	 private Integer hotPoints;//统计时间的热度值
	 
	 private java.util.Date creatTime;
	

}
