package com.hdvon.nmp.generate.util;

public enum CheckRegxEnum {
	/**
	 * 校验IP格式
	 */
	IP("IP"),
	/**
	 * 校验设备编码规则
	 */
	SBBM("SBBM"),
	/**
	 * 校验int类型规则
	 */
	INT("INT"),
	
	/**
	 * 校验double类型规则
	 */
	DOUBLE("DOUBLE"),
	
	/**
	 * 校验虚拟组织编码
	 */
	VIRTUAL_CODE("VirtualCode"),
	/**
	 * 校验经度格式
	 */
	JD("JD"),
	/**
	 * 校验纬度格式
	 */
	WD("WD"),
	/**
	 * 固话
	 */
	PHONE("PHONE"),
	/**
	 * 手机号
	 */
	MOBILE("MOBILE");
	
	private String val;
	
	private CheckRegxEnum(String val) {
		this.setVal(val);
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}
}
