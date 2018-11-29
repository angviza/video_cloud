package com.hdvon.sip.entity.param;

import java.io.Serializable;

import lombok.Data;
/**
 * 云台控制请求参数对象
 * @author wanshaojian
 *
 */
@Data
public class CloudParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 设备编码
	 */
	private String deviceID;

	
	/**
	 * 云台控制类型
	 * 值为'direction '表示控制方向
	 * 值为'zoom'  表示控制镜头变倍 
	 * 值为'iris' 表示控制光圈
	 * 值为'focus' 表示控制焦距
	 */
	private String type;
	/**
	 * 控制方向类型
	 */
	private String direction;
	/**
	 * 控制镜头类型
	 * 放大：1   缩小：2  停止： 0
	 */
	private Integer zoom;
	/**
	 * 控制光圈类型
	 * 放大：1   缩小：2  3：停止 0
	 */
	private Integer iris;
	/**
	 * 控制焦距类型
	 * 近：1  远：2  停止: 0
	 */
	private Integer focus;
	/**
	 * 速率
	 */
	private Integer speed;
}
