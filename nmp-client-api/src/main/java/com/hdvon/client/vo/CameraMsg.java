package com.hdvon.client.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * 更新设备发送同步消息实体
 * @author wanshaojian
 *
 */
@Data
public class CameraMsg implements Serializable{
	private String id;//操作用户id
	private String content;
	private int type;//操作类型 1:新增  2:修改  3:删除
	private String deviceIds;//设备id集合
	
	private String msgId;//消息id
	
}
