package com.hdvon.nmp.enums;

import lombok.Getter;

/**
 * 上墙轮巡操作的枚举。
 * 功能：
 * 作者：zhanqf
 * 日期：2018年6月4日
 * 版权所有：广州弘度信息科技有限公司 版权所有(C) 2018
 *
 */
@Getter
public enum EWallOperation {
	
	/**
	 * 开始。
	 * 新增上墙。
	 */
	START("START"),
	
	/**
	 * 取消所有。
	 */
	CANCEL_ALL("CANCEL_ALL"),
	
	/**
	 * 取消单个。
	 */
	CANCEL_ONE("CANCEL_ONE"),
	
	/**
	 * 暂停。
	 */
	PAUSE("PAUSE"),
	
	/**
	 * 播放。
	 */
	PLAY("PLAY");
	
	private String val;
	
	private EWallOperation(String val) {
		this.val = val;
	}

}
