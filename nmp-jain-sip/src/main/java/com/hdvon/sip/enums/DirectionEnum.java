package com.hdvon.sip.enums;
/**
 * 云台控制枚举类
 * @author wanshaojian
 * @since 2018-10-15
 *
 */
public enum DirectionEnum {
	/**
	 * 上：up  下：down   左：left   右：right   左上： upLeft  左下：downLeft  右上：upRight  右下：downRight 停止： stop
	 */
	STOP("stop","00"),UP("up","08"),DOWN("down","04"),LEFT("left","02"),RIGHT("right","01"),UPPER_LEFT("upLeft","0A"),LOWER_LEFT("downLeft","06"),UPPER_RIGHT("upRight","09"),LOWER_RIGHT("downRight","05");
	
	private DirectionEnum(String key,String value) {
		this.key = key;
		this.value = value;
	}
	private String key;  
	private String value;
	
	public static DirectionEnum getObjectByKey(String key) {
		for(DirectionEnum em:DirectionEnum.values()) {
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
