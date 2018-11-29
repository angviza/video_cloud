package com.hdvon.nmp.util;

import lombok.Getter;

@Getter
public enum TreeType {
	/**
	 * 地址树。
	 */
	ADDRESS("address"),
	/**
	 * 组织机构树
	 */
	ORG("org"),
	/**
	 * 项目分组树
	 */
	PROJECT("project"),
	/**
	 * 自定义分组树
	 */
	GROUP("group"),
	/**
	 * 部门树
	 */
	DEPARTMENT("department");
	
	private String val;
	
	private TreeType(String val) {
		this.val = val;
	}
}
