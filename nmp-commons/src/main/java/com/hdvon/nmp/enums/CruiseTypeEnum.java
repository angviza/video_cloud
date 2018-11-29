package com.hdvon.nmp.enums;
/**
 * 巡航预案设置设置类型枚举
 * @author wanshaojian
 * @since 2018-10-15
 *
 */
public enum CruiseTypeEnum {
	/**
	 * add  加入巡航点
	 * del  删除一个巡航点
	 * setspeed 设置巡航速度
	 * stopover 设置巡航停留时间
	 * start 开始巡航
	 * stop 停止巡航
	 */
	ADD("add","84"),DEL("delete","85"),SET_SPEED("setSpeed","86"),SET_STOPOVER("setStopover","87"),START("start","88"),STOP("stop","40");
	
	private CruiseTypeEnum(String key,String value) {
		this.key = key;
		this.value = value;
	}
	private String key;
	private String value;
	
	public static CruiseTypeEnum getObjectByKey(String key) {
		for(CruiseTypeEnum em:CruiseTypeEnum.values()) {
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
