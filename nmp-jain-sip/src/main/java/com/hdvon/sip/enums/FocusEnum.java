package com.hdvon.sip.enums;
/**
 * 控制焦距枚举
 * @author wanshaojian
 * @since 2018-10-15
 */
public enum FocusEnum {
	/**
	 * 近：1  远：2  停止: 0
	 * 	【对应调焦bit4指令编码】
	 */
	NEAR(1,"41"),FAR(2,"42"),STOP(0,"40");
	
	private FocusEnum(int key,String value) {
		this.key = key;
		this.value = value;
	}
	private int key;
	private String value;
	
	public static FocusEnum getObjectByKey(int key) {
		for(FocusEnum em:FocusEnum.values()) {
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
