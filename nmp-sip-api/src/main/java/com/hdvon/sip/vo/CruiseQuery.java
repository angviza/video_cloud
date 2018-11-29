package com.hdvon.sip.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * 巡航预案控制
 * @author wanshaojian
 *
 */
@Data
public class CruiseQuery implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 设备Code
	 */
	private String deviceCode;
	
	/**
	 * 类型
	 */
	private CruiseTypeEnum typeEnum;
	
	/**
	 * 巡航组号
	 */
	private Integer groupNum;
	/**
	 * 预置位号
	 */
	private Integer presetNum;
	/**
	 * 巡航停留时间
	 */
	private Long stayTime;
	/**
	 * 巡航速度 
	 */
	private Integer speed; 
	
	/**
	 * 类型枚举
	 * @author wanshaojian
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
		ADD("add","84"),DEL("del","85"),SET_SPEED("setspeed","86"),SET_STOPOVER("stopover","87"),START("start","88"),STOP("stop","40");
		
		private CruiseTypeEnum(String key,String value) {
			this.key = key;
			this.value = value;
		}
		private String key;
		private String value;
		
		public String getKey() {
			return key;
		}
		public String getValue() {
			return value;
		}
		
		
	}

	
}
