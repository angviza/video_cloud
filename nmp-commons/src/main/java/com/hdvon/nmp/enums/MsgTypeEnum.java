package com.hdvon.nmp.enums;

public enum MsgTypeEnum {
	/**
	 * CONTRACT_TOPIC1		kafaka消息生产者的发送类别为CONTRACT_TOPIC2
	 * ES同步消息
	 * 		USER_CAMERA_KEY		指定kafaka消息的 key为USER_CAMERA
	 * 		PLAN_CAMERA_KEY		指定kafaka消息的 key为PLAN_CAMERA
	 * 		CAMERA_GROUP_KEY		指定kafaka消息的 key为CAMERA_GROUP
	 * 		USERROLE_CAMERA      指定kafaka消息的 key为USERROLE_CAMERA
	 * 预置位同步消息
	 *      PERSET 预置位同步消息
	 * 巡航预案同步消息
	 *      CRUISE 巡航预案同步消息     
	 */
	ES_USER_MSG(1,"USER_CAMERA"),ES_PLLAN_MSG(2,"PLAN_CAMERA"),ES_GROUP_MSG(3,"CAMERA_GROUP"),ES_USERROLE_MSG(4,"USERROLE_CAMERA"),ES_CAMERA_MSG(5,"CAMERA"),PRESET_MSG(6,"PRESET_TOPIC"),PRESET_QUERY_MSG(7,"PRESET_QUERY_TOPIC"),CRUISE_MSG(8,"CRUISE_TOPIC");
	
	private int key;
	private String value;
	
	private MsgTypeEnum(int key,String value) {
		this.key = key;
		this.value = value;
	}
	
	public static MsgTypeEnum getMsgTypeEnumByKey(int key) {
		for(MsgTypeEnum em:MsgTypeEnum.values()) {
			if(em.getKey() == key) {
				return em;
			}
		}
		return null;
	}
	
	public static MsgTypeEnum getMsgTypeEnumByValue(String val) {
		for(MsgTypeEnum em:MsgTypeEnum.values()) {
			if(em.getValue().equals(val)) {
				return em;
			}
		}
		return null;
	}

	public int getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
}
