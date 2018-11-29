package com.hdvon.sip.enums;
/**
 * 回看控制类型枚举
 * @author wanshaojian
 *
 */
public enum PlaybackEnum {
	/**
	 * PLAY表示播放  
	 * PAUSE表示暂停
	 * RANDOMPLAY表示随机播放 
	 * TEARDOWN表示 停止
	 * MULTIPLE表示快进和慢进
	 */
	PLAY("play","PLAY"),RANDOM_PLAY("randomPlay","RANDOM_PLAY"),PAUSE("pause","PAUSE"),MULTIPLE("multiple","MULTIPLE"),TEARDOWN("terminate","TEARDOWN");
	
	private PlaybackEnum(String key,String value) {
		this.key = key;
		this.value = value;
	}
	private String key;
	private String value;
	
	
	public static PlaybackEnum getObjectByKey(String key) {
		for(PlaybackEnum em:PlaybackEnum.values()) {
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
