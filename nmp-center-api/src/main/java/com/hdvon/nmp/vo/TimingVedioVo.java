package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>定时录像表 VO类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="TimingVedio")
public class TimingVedioVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "关联id")
    private java.lang.String storeCameraId;

    @ApiModelProperty(value = "星期几（星期一：1；星期二：2；星期三：3；星期四：4；星期五：5；星期六：6；星期日：7）")
    private java.lang.Integer dayOfWeek;

    @ApiModelProperty(value = "一天中某一段录像时间段的开始时间")
    private java.util.Date bgnTime;

    @ApiModelProperty(value = "一天中某一段录像时间段的结束时间")
    private java.util.Date endTime;

    /*@ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;*/

    /*@ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;*/

    /*@ApiModelProperty(value = "创建人")
    private java.lang.String createUser;*/

    /*@ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;*/


}

