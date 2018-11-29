package com.hdvon.nmp.enums;

/**
 * <br>
 * <b>功能：</b>sip操作内容枚举类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public enum MethodDetailEnums {
	
	PLAY("play", "视频实时播放"),TERMINATE("terminate", "停止播放"),RECORD("record", "实时录像控制"),CLOUD("PTZ", "摄像头方向控制"),CRUISE("cruise", "球机巡航预案控制"),
	PRESET("preset", "预置位设置控制"),WIPER("wiper", "雨刷开关控制"),PLAYBACK("playback", "视频录像回放"),PLAYBACK_CONTROL("playbackControl", "录像回放控制"),
	SINGLEROADMANYHOUR("singleRoadManyHour", "视频录像单路多时回放"),DOWNLOAD("download", "录像文件下载"),QUERYPRESET("queryPreset", "查看预置位"),
	QUERYRECORD("queryRecord", "查看录像"),HOMEPOSITION("homePosition", "摄像头守望位控制"),DRAGZOOM("dragZoom", "3D拖放控制"),;
	
	private String key;
	private String value;
	
	private MethodDetailEnums(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public static MethodDetailEnums getInfoByKey(String key) {
		for(MethodDetailEnums em : MethodDetailEnums.values()) {
			if(em.getKey().equals(key)) {
				return em;
			}
		}
		return null;
	}
	
	public static MethodDetailEnums getInfoByValue(String val) {
		for(MethodDetailEnums em : MethodDetailEnums.values()) {
			if(em.getValue().equals(val)) {
				return em;
			}
		}
		return null;
	}

	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
}
