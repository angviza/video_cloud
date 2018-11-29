package com.hdvon.nmp.vo.sip;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <br>
 * <b>功能：</b>录像控制参数 VO类<br>
 * <b>作者：</b>haungguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Data
@ApiModel(value ="PalyDownload")
public class PlayDownload extends BaseOption implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "控制类型：1：开始录像2：停止录像")
	private Integer cmdType;
	
//	@ApiModelProperty(value = "端口号,前端无需传值，后台自动生成")
//	private Integer port ;
	

}
