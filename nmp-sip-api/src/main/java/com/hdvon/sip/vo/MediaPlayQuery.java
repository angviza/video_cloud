package com.hdvon.sip.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 媒体点播查询对象
 * @author wanshaojian
 *
 */
@Data
public class MediaPlayQuery implements Serializable {
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


}
