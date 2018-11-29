package com.hdvon.nmp.generate.util;


public enum CheckTypeEnum {
	/**
	 * 校验字典值
	 */
	DIC("DIC"),
	
	/**
	 * 校验表头
	 */
	HEAD("HEAD"),
	
	/**
	 * 校验长度
	 */
	LENGTH("LENGTH"),
	/**
	 * 校验date类型
	 */
	DATE("DATE"),
	/**
	 * 非空校验
	 */
	NOTNULL("NOTNULL"),
	
	REGEX("REGEX"),
	TEXT("TEXT");
	
	private String val;
	
	private CheckTypeEnum(String val) {
		this.setVal(val);
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

}
