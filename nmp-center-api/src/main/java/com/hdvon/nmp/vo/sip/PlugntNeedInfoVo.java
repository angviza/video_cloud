package com.hdvon.nmp.vo.sip;

import java.io.Serializable;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>视频监控插件需要参数 VO类<br>
 * <b>作者：</b>haungguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="PlugntNeedInfoVo")
public class PlugntNeedInfoVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "插件播放需要的ip地址")
	private String clientIp;
	
	@ApiModelProperty(value = "插件播放需要的端口")
	private String clientProt;
	
/*	@ApiModelProperty(value = "媒体流发送者ip地址")
	private String senderIp ;

	@ApiModelProperty(value = "媒体流发送者端口")
	private String senderPort ;*/
}
