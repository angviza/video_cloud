package com.hdvon.nmp.generate.util;

public enum FunTypeEnum {
	ORG("ORG"),
	DEPARTMENT("DEPARTMENT"),
	CAMERA("CAMERA");
	
	private String val;
	
	private FunTypeEnum(String val) {
		this.setVal(val);
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}
}
