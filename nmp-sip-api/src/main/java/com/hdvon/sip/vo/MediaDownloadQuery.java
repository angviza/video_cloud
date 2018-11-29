package com.hdvon.sip.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 视频回看查询对象
 * @author wanshaojian
 *
 */
@Data
public class MediaDownloadQuery implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "媒体流发送者设备编码")
	protected String deviceCode;

	@ApiModelProperty(value = "协议方式")
	protected String transportType;

	@ApiModelProperty(value = "媒体流接收者设备编码")
	protected String receiveCode;

	@ApiModelProperty(value = "媒体流接受IP地址")
	protected String receiveIp;

	@ApiModelProperty(value = "sip客户端端口")
	protected int clientPort;

    @ApiModelProperty(value = "开始时间")
    private Date startDate;
    
    @ApiModelProperty(value = "结束时间")
    private Date endDate;
    
    @ApiModelProperty(value = "视音频文件的URI")
    private String uri;
    
}
