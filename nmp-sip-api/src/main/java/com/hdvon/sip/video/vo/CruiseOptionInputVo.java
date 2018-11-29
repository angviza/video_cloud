package com.hdvon.sip.video.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>巡航预案控制请求参数 VO类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018/7/20<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="CruiseOptionInputVo")
public class CruiseOptionInputVo extends BaseOptionVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//巡航控制类型   加入巡航点：1；删除一个巡航点：2；设置巡航速度：3；设置巡航停留时间：4；开始巡航：5；停止巡航：0(使用云台控制的停止命令)
	@ApiModelProperty(value = "巡航控制类型   加入巡航点：1； 删除一个巡航点：2； 设置巡航速度：3； 设置巡航停留时间：4； 开始巡航：5； 停止巡航：0(使用云台控制的停止命令)")
	private Integer cmdType;
	
	//巡航组号 取值范围：0-255
	@ApiModelProperty(value = "巡航组号   取值范围：0-255，暂时可以先写成1即可")
	private Integer groupNum;
	
	//预置位号  1-255 （0为预留， 当cmdType = 2,删除操作时才能填写0，表示删除整组预置位）
	@ApiModelProperty(value = "预置位号  1-255 （0为预留， 当cmdType = 2,删除操作时才能填写0，表示删除整组预置位）")
	private Integer presetNum;
	
	//巡航停留时间 单位：s
	@ApiModelProperty(value = "巡航停留时间   单位：s")
	private Integer stayTime;
	
	//优先级：默认值为1 
	@ApiModelProperty(value = "优先级")
	private Integer iPriority;
	
	//巡航速度
	@ApiModelProperty(value = "巡航速度")
	private Integer speed;
}
