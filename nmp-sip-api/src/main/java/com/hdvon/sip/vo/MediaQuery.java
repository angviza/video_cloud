package com.hdvon.sip.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * <br>
 * <b>功能：</b>录像查询 VO类<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018-08-22<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
/**
 * 录像查询对象
 * @author wanshaojian
 *
 */
@Data
@ApiModel(value ="QueryVideo")
public class MediaQuery implements Serializable{

	@ApiModelProperty(value = "接受端ip")
	String receiveIp;
		
	@ApiModelProperty(value = "设备编码")
	String deviceCode;
	
	@ApiModelProperty(value = "查询起始时间")
	Date startTime;
	
	@ApiModelProperty(value = "查询结束时间")
	Date endTime;


    
    
}
