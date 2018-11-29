package com.hdvon.sip.vo;

public class VideoControlType {
	
	/**
	 * 录像控制命令类型
	 * @author wanshaojian
	 *
	 */
	public enum VideotapeEnum{
		/**
		 * START开始   STOP 停止 
		 */
		START("START"),STOP("STOP");
		private VideotapeEnum(String value) {
			this.value = value;
		}
		
		private String value;

		public String getValue() {
			return value;
		}
	}
	/**
	 * 录像控制命令类型
	 * @author wanshaojian
	 *
	 */
	public enum VideoMethodEnum{
		/**
		 * PLAY播放   PAUSE暂停  RANDOMPLAY 随机播放 TEARDOWN 停止  MULTIPLE 快进和慢进 
		 */
		PLAY("PLAY"),RANDOM_PLAY("RANDOM_PLAY"),PAUSE("PAUSE"),MULTIPLE("MULTIPLE"),TEARDOWN("TEARDOWN");
		private VideoMethodEnum(String value) {
			this.value = value;
		}
		
		private String value;

		public String getValue() {
			return value;
		}
	}
	/**
	 * 快进和慢退的播放速率倍数枚举类
	 * Scale头域的取值范围，
	 * @author wanshaojian
	 *
	 */
	public enum ScaleEnum {
		QUARTER_TIMES(0.25),AHALF_TIMES(0.5),ONE_TIMES(1.0),TWO_TIMES(2.0),FOUR_TIMES(4.0);
		
		private ScaleEnum(Double value) {
			this.value = value;
		}
		
		private Double value;

		public Double getValue() {
			return value;
		}
		
	}
}
