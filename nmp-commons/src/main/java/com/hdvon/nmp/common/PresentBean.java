package com.hdvon.nmp.common;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * 预置位对象
 * @author wanshaojian
 *
 */
@Data
public class PresentBean  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 消息id
	 */
	private String msgID;
	/**
	 * 设备编码
	 */
	private String deviceID;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 预置位控制类型   add 新增  delete 删除
	 */
	private String type;
	/**
	 * 预置位编码
	 */
	private Integer presetNum;
	/**
	 * 预置位名称
	 */
	private String presetName; 
}
