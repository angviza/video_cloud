package com.hdvon.sip.enums;
/**
 * 录像控制枚举
 * @author Administrator
 *
 */
public enum TapeTypeEnum {
	/**
	 * 开始录像 结束录像
	 */
	START("start"),STOP("stop");
	
	private TapeTypeEnum(String value) {
		this.value = value;
	}
	private String value;
	
	
	public static TapeTypeEnum getObjectByKey(String value) {
		for(TapeTypeEnum em:TapeTypeEnum.values()) {
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
