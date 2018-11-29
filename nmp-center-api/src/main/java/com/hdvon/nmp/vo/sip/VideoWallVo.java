package com.hdvon.nmp.vo.sip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value ="VideoWallVo")
public class VideoWallVo implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -153167745608705004L;

	@ApiModelProperty(value = "通道编号")
	private String channelNos;
	
	@ApiModelProperty(value = "设备编码")
	private String deviceCodes;
	
	@ApiModelProperty(value = "设备编码列表")
	private List<String> deviceCodeList;

	@ApiModelProperty(value = "矩阵通道id列表")
	private List<String> channelNoList;
	
	@ApiModelProperty(value = "轮巡翻屏间隔时间")
	private Integer timeInterval;
	
	@ApiModelProperty(value = "逗号隔开的多个任务id")
	private String taskIds;
	
	@ApiModelProperty(value = "任务id列表")
	private List<String> taskIdList;
	
	@ApiModelProperty(value = "录像开始时间")
	private Long startTime ;
	
	@ApiModelProperty(value = "录像结束时间")
	private Long endTime ;
	
	public void convertToList() {
		if(StrUtil.isNotBlank(this.deviceCodes)) {
			String[] deviceCodeArr = this.deviceCodes.split(",");
			this.deviceCodeList = Arrays.asList(deviceCodeArr);
		}else {
			this.deviceCodeList = new ArrayList<String>();
		}

		if(StrUtil.isNotBlank(this.channelNos)) {
			String[] channelNoArr = this.channelNos.split(",");
			this.channelNoList = Arrays.asList(channelNoArr);
		}else {
			this.channelNoList = new ArrayList<String>();
		}
		
		if(StrUtil.isNotBlank(this.taskIds)) {
			String[] taskIdArr = this.taskIds.split(",");
			this.taskIdList = Arrays.asList(taskIdArr);
		}else {
			this.taskIdList = new ArrayList<String>();
		}

	}
}
