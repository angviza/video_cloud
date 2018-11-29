package com.hdvon.nmp.common;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
/**
 * 预置位列表对象
 * @author wanshaojian
 *
 */
@Data
public class PresentListBean implements Serializable{
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
	
	private List<PresentBean> itemList;
}
