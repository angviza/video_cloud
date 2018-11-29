package com.hdvon.nmp.vo;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>用户操作行为统计表 VO类<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="RepUseranalysis")
public class RepUseranalysisVo implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private java.lang.String id;

    @ApiModelProperty(value = "用户账号")
    private java.lang.String account;

    @ApiModelProperty(value = "用户在线时长")
    private java.lang.Integer onlineTotal;

    @ApiModelProperty(value = "用户登录次数")
    private java.lang.Integer loiginTotal;

    @ApiModelProperty(value = "实时监控次数")
    private java.lang.Integer inviteTotal;

    @ApiModelProperty(value = "线索翻查次数")
    private java.lang.Integer replayTotal;

    @ApiModelProperty(value = "录像下载次数")
    private java.lang.Integer downloadTotal;

    @ApiModelProperty(value = "controlTotal")
    private java.lang.Integer controlTotal;

    @ApiModelProperty(value = "预留")
    private java.lang.String otherTotal;

    @ApiModelProperty(value = "统计最小时间")
    private java.util.Date startTime;

    @ApiModelProperty(value = "用户行为统计时间")
    private java.util.Date creatTime;
    
    //扩展
    @ApiModelProperty(value = "操作设备总数量")
    private java.lang.Integer total;
    
    @ApiModelProperty(value = "用户id")
    private java.lang.String userId;
    
    @ApiModelProperty(value = "所属部门")
    private java.lang.String departmentName;
    


}

