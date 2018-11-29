package com.hdvon.sip.video.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <br>
 * <b>功能：</b>云台控制参数 VO类<br>
 * <b>作者：</b>huangguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="ControlOptionInputVo")
public class ControlOptionInputVo extends BaseOptionVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	//步长
	@ApiModelProperty(value = "步长")
	private Integer step;
	
	//优先级：默认值为1 
	@ApiModelProperty(value = "优先级")
	private Integer iPriority;
	
	//控制方向
	@ApiModelProperty(value = "控制摄像头方法 1 上,2 下,3 左 ,4 右 ,5 左上,6 左下,7 右上,8 右下")
	private Integer direction;
	
	//控制镜头倍数 缩小：1,放大：2
	@ApiModelProperty(value = "控制镜头倍数(变焦) ;缩小：1,放大：2 ")
	private Integer zoom;
	
	//控制镜头光圈 缩小：1,放大：2
	@ApiModelProperty(value = "控制镜头光圈 ;缩小：1,放大：2 ")
	private Integer iris;
	
	//控制镜头焦距  近：1,远：2
	@ApiModelProperty(value = "控制镜头焦距(调焦) ;近：1,远：2 ")
	private Integer focus;
	
	//是否取消摄像头控制
	@ApiModelProperty(value = "是否取消摄像头控制 ;是： 0,否：1 ")
	private Integer isCancel;
}
