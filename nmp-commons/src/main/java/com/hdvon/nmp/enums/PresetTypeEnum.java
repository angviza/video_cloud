package com.hdvon.nmp.enums;
/**
 * 预置位设置类型枚举
 * @author wanshaojian
 * @since 2018-10-15
 *
 */
public enum PresetTypeEnum {
	//add：设置  2：调用  3: 删除
	SET("add","81"),TRANSFER("call","82"),DELETE("delete","83");
	private PresetTypeEnum(String key,String value) {
		this.key = key;
		this.value = value;
	}
	private String key;
	private String value;
	
	public static PresetTypeEnum getObjectByKey(String key) {
		for(PresetTypeEnum em:PresetTypeEnum.values()) {
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
