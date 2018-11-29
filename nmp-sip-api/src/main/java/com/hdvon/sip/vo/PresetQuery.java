package com.hdvon.sip.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * 预置位请求对象
 * @author wanshaojian
 *
 */
@Data
public class PresetQuery implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 设备编码
	 */
	String deviceCode;
	/**
	 * 预置位类型
	 */
	PresetTypeEnum typeEnum;
	
	/**
	 * 预置位编号
	 */
	Integer presetNum;
	
	public enum PresetTypeEnum{
		//1：设置  2：调用  3: 删除
		SET("set","81"),TRANSFER("tran","82"),DELETE("del","83");
		private PresetTypeEnum(String key,String value) {
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
