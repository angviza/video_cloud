package com.hdvon.sip.enums;
/**
 * message消息类型
 * @author wanshaojian
 *
 */
public enum MessageTypeEnum {
	/**
	 * Keepalive  心跳
	 * PresetQuery 日志位查询
	 * RecordInfo 录像查询
	 * DeviceControl 设备控制
	 * DeviceStatus 设备状态
	 * MediaStatus 媒体状态
	 * homePosition 看守位控制
	 * dragZoomOut 3D拖放控制
	 */
	KEEPALIVE("Keepalive","发送心跳"),PRESET_QUERY("PresetQuery","预置位查询"),RECORD_INFO("RecordInfo","录像查询"),
	DEVICE_CONTROL("DeviceControl","设备控制"),DEVICE_STATUS("DeviceStatus","设备状态"),MEDIA_STATUS("MediaStatus","媒体状态"),
	HOME_POSITION("homePosition","看守位控制"),DRAG_ZOOM("dragZoom","3D拖放控制");
	
	private MessageTypeEnum(String key,String value) {
		this.key = key;
		this.value = value;
	}
	private String key;
	private String value;
	
	public static MessageTypeEnum getObjectByKey(String key) {
		for(MessageTypeEnum em:MessageTypeEnum.values()) {
			if(em.getKey().equals(key)) {
				return em;
			}
		};
		return null;
	}
	public String getKey() {
		return key;
	}
	public String getValue() {
		return value;
	}
}
