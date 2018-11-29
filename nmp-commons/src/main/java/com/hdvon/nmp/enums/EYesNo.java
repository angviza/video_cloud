package com.hdvon.nmp.enums;

import lombok.Getter;

/**
 * 
 * 功能：表示是否的整型枚举。
 * 作者：zhanqf
 * 日期：2018年5月25日
 * 版权所有：广州弘度信息科技有限公司 版权所有(C) 2018
 *
 */
@Getter
public enum EYesNo {
	
	/**
	 * 是。
	 */
	YES(1),
	
	/**
	 * 否。
	 */
	NO(0);
	
	private Integer val;
	
	private EYesNo(Integer val) {
		this.val = val;
	}

}
