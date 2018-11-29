package com.hdvon.sip.enums;
/**
 * 控制光圈枚举
 * @author wanshaojian
 * @since 2018-10-15
 *
 */
public enum IrisEnum {
	/**
	 * 放大：1   缩小：2  3：停止 0
	 * 【对应光圈bit4指令编码】
	 */
	AMPLIFY(1,"48"),NARROW(2,"44"),STOP(0,"40");
	
	private IrisEnum(int key,String value) {
		this.key = key;
		this.value = value;
	}
	private int key;
	private String value;
	
	
	public static IrisEnum getObjectByKey(int key) {
		for(IrisEnum em:IrisEnum.values()) {
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
