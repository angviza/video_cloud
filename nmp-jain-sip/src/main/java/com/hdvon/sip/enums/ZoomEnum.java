package com.hdvon.sip.enums;
/**
 * 控制镜头变倍枚举
 * @author wanshaojian
 * @since 2018-10-15
 */
public enum ZoomEnum {
	/**
	 * 放大：1   缩小：2  停止： 0
	 * 【对应变焦bit4指令编码】
	 */
	AMPLIFY(1,"10"),NARROW(2,"20"),STOP(0,"00");
	
	private ZoomEnum(int key,String value) {
		this.key = key;
		this.value = value;
	}
	private int key;
	private String value;
	
	public static ZoomEnum getObjectByKey(int key) {
		for(ZoomEnum em:ZoomEnum.values()) {
			if(em.getKey() == key) {
				return em;
			}
		};
		return null;
	}
	
	public int getKey() {
		return key;
	}
	public String getValue() {
		return value;
	}
}
