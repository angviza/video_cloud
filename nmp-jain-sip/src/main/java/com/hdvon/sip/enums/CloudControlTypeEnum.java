package com.hdvon.sip.enums;
/**
 * 控制类型枚举
 * @author wanshaojian
 * @since 2018-10-15
 *
 */
public enum CloudControlTypeEnum {
	/**
	 * DIRECTION  控制方向
	 * ZOOM  控制镜头变倍
	 * IRIS 控制光圈
	 * FOCUS 控制焦距
	 */
	DIRECTION("direction"),ZOOM("zoom"),IRIS("iris"),FOCUS("focus");
	

	
	private CloudControlTypeEnum(String value) {
		this.value = value;
	}
	private String value;
	
	public static CloudControlTypeEnum getObjectByKey(String value) {
		for(CloudControlTypeEnum em:CloudControlTypeEnum.values()) {
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
