package com.hdvon.sip.enums;
/**
 * 录像下载速度枚举类
 * Scale头域的取值范围，
 * @author wanshaojian
 * @since 2018-10-15
 */
public enum DownloadSpeedEnum {
	/**
	 * 播放速度，1倍、2倍、4倍 8倍
	 */
	ONE_TIMES(1),TWO_TIMES(2),FOUR_TIMES(4),EIGHT_TIMES(8);
	
	private DownloadSpeedEnum(int value) {
		this.value = value;
	}
	
	
	private int value;
	
	public static DownloadSpeedEnum getObjectByKey(int value) {
		for(DownloadSpeedEnum em:DownloadSpeedEnum.values()) {
			if(em.getValue() == value) {
				return em;
			}
		};
		return null;
	}

	public int getValue() {
		return value;
	}
}
