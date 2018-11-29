package com.hdvon.client.enums;

/**
 * 索引库对应字段常量
 * @author Administrator
 *
 */
public enum AggFieldEnum {
	ORG_CODE("orgId"),ADDRESS_CODE("addressCode"),PROJECT_NAME("projectId"),GROUP_NAME("groupId");
	
	
	private AggFieldEnum(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}
	
}
