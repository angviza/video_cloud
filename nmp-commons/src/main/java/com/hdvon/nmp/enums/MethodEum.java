package com.hdvon.nmp.enums;
/**
 * 方法类型枚举
 * @author wanshaojian
 * @since 2018-10-16
 */
public enum MethodEum {
	/**
	 * attach  登录
	 * detach 注销
	 * play 实时播放
	 * playback 录像回看
	 * terminate 停止播放
	 * cloud 云台控制
	 * cruise 巡航预案控制
	 * preset 预置位控制
	 * queryPreset 预置位查询
	 * wiper 雨刷控制
	 * record 录像控制
	 * queryRecord 录像查找
	 * playbackControl 回放控制
	 * download 下载
	 * cancel 取消发送
	 * keeplive 心跳
	 * homePosition 看守位控制
	 * dragZoom 3D拖放控制
	 */
	ATTACH("attach"),DETACH("detach"),PLAY("play"),PLAYBACK("playback"),TERMINATE("terminate"),CLOUD("PTZ"),CRUISE("cruise"),PRESET("preset"),QUERYPRESET("queryPreset"),
	WIPER("wiper"),RECORD("record"),QUERYRECORD("queryRecord"),PLAYBACK_CONTROL("playbackControl"),DOWNLOAD("download"),CANCEL("cancel"),KEEPLIVE("Keepalive")
	,HOMEPOSITION("homePosition"),DRAGZOOM("dragZoom"),SINGLEROADMANYHOUR("singleRoadManyHour");
	
	private MethodEum(String value) {
		this.value = value;
	}
	private String value;
	
	
	public static MethodEum getObjectByKey(String value) {
		for(MethodEum em:MethodEum.values()) {
			if(em.getValue().equals(value)) {
				return em;
			}
		};
		return null;
	}
	
	public String getValue() {
		return value;
	}
}
