package com.hdvon.sip.enums;
/**
 * 快进和慢退的播放速率倍数枚举类
 * Scale头域的取值范围，
 * @author wanshaojian
 * @since 2018-10-15
 */
public enum ScaleEnum {
	/**
	 * 播放速度，
	 * 		基本取值：1表示0.25倍、2表示0.5倍、3表示1倍、4表示2倍、5表示4倍 6表示8倍
	 */
	QUARTER_TIMES("1",0.25),AHALF_TIMES("2",0.5),ONE_TIMES("3",1.0),TWO_TIMES("4",2.0),FOUR_TIMES("5",4.0),EIGHT_TIMES("6",8.0);
	
	private ScaleEnum(String key,Double value) {
		this.key = key;
		this.value = value;
	}
	
	
	private String key;
	private Double value;
	
	public static ScaleEnum getObjectByKey(String key) {
		for(ScaleEnum em:ScaleEnum.values()) {
			if(em.getKey().equals(key)) {
				return em;
			}
		};
		return null;
	}

	public String getKey() {
		return key;
	}
	public Double getValue() {
		return value;
	}
}
