package com.hdvon.nmp.vo.sip;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>预置位设置请求参数 VO类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018/07/12<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="PresetOptionVo")
public class PresetOptionVo extends BaseOption implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//默认值1 
//	@ApiModelProperty(value = "优先级")
//	private Integer iPriority;
	
	//预置位控制类型
	@ApiModelProperty(value = "预置位控制类型 :1 设置预置位, 2 调用预置位,3  删除预置位 ")
	private Integer presetType;
	
	//预置位号
	@ApiModelProperty(value = "预置位id")
	private String presetId;
	
	//预置位号
	@ApiModelProperty(value = "预置位号, 设置预置位时不用传，其他都需要传入")
	private Integer presetNum;
	
//	@ApiModelProperty(value = "云台参数值")
//	private String value;
	
	@ApiModelProperty(value = "预置位名称")
	private String name;
	
	@ApiModelProperty(value = "摄像机ID")
	private String cameraId;
	
	@ApiModelProperty(value = "调用预置位控制接口入口类型：1视屏监控，2球机巡航预案")
	private Integer entranceType;
	
}
