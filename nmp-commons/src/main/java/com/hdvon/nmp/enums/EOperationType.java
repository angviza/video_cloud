package com.hdvon.nmp.enums;

import lombok.Getter;

/**
 * 
 * 功能：自定义菜单的操作类型。
 * 作者：zhanqf
 * 日期：2018年5月25日
 * 版权所有：广州弘度信息科技有限公司 版权所有(C) 2018
 *
 */
@Getter
public enum EOperationType {
	
	/**
	 * 增加。
	 */
	ADD("add"),
	
	/**
	 * 修改。
	 */
	EDIT("edit"),
	
	/**
	 * 删除。
	 */
	DEL("del"),
	
	/**
	 * 恢复。
	 */
	RENEW("renew");
	
	private String val;
	
	private EOperationType(String val) {
		this.val = val;
	}

}
