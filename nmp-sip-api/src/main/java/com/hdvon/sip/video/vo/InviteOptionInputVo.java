package com.hdvon.sip.video.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>视频监控请求参数 VO类<br>
 * <b>作者：</b>haungguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="InviteOptionInputVo")
public class InviteOptionInputVo extends BaseOptionVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/*@ApiModelProperty(value = "设备id")
	private String deviceId;*/
	
	@ApiModelProperty(value = "设备编码,轮巡用")
	private String deviceCodes;
	
	@ApiModelProperty(value = "播放器插件端口号,轮巡用")
	private String ports ;
	
	@ApiModelProperty(value = "播放器插件端口号")
	private Integer port ;
	
	@ApiModelProperty(value = "请求方式  1代表实时点播；2代表历史回放；3代表文件下载；4代表语音对讲",required=true)
	private String playType ;
	
	/*@ApiModelProperty(value = "开始时间,回放或下载时必填")
	private String startTime ;
	
	@ApiModelProperty(value = "结束时间,回放或下载必填")
	private String endTime ;*/
	
	@ApiModelProperty(value = "开始时间,回放或下载时必填")
	private Long startTime ;
	
	@ApiModelProperty(value = "结束时间,回放或下载必填")
	private Long endTime ;
	
	@ApiModelProperty(value = "矩阵通道id")
	private String channelId;
	
	@ApiModelProperty(value = "分段数")
	private Integer number;

	@ApiModelProperty(value = "播放协议 TCP,UDP,默认是使用户个人中心设置的视频协议方式")
	private String protocol;
	
	@ApiModelProperty(value = "客户端ip地址")
	private String clientIp;

	@ApiModelProperty(value = "矩阵通道id")
	private String channelIds;

	@ApiModelProperty(value = "设备编码列表")
	private List<String> deviceCodeList;

	@ApiModelProperty(value = "矩阵通道id列表")
	private List<String> channelIdList;
    
	@ApiModelProperty(value = "轮巡翻屏间隔时间")
	private Integer timeInterval;
	
	@ApiModelProperty(value = "矩阵编号（国标）")
	private String matrixCode;

	public void convertToList() {
		if(StrUtil.isNotBlank(this.deviceCodes)) {
			String[] deviceCodeArr = this.deviceCodes.split(",");
			this.deviceCodeList = Arrays.asList(deviceCodeArr);
		}else {
			this.deviceCodeList = new ArrayList<String>();
		}

		if(StrUtil.isNotBlank(this.channelIds)) {
			String[] channelIdArr = this.channelIds.split(",");
			this.channelIdList = Arrays.asList(channelIdArr);
		}else {
			this.channelIdList = new ArrayList<String>();
		}

	}
}
