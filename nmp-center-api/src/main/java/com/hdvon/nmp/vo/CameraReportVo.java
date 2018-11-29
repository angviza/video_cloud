package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>摄像机访问历史报表 VO类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="CameraReport")
public class CameraReportVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "设备编码")
    private java.lang.String sbbm;

    @ApiModelProperty(value = "设备名称")
    private java.lang.String sbmc;

    @ApiModelProperty(value = "该时间段访问总数")
    private java.lang.Integer total;

    @ApiModelProperty(value = "统计时间")
    private java.util.Date creatTime;


}

