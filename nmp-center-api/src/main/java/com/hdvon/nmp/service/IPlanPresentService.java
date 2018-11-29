package com.hdvon.nmp.service;

import com.hdvon.nmp.vo.PlanPresentVo;

/**
 * <br>
 * <b>功能：</b>预案关联预置位表 服务类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IPlanPresentService{
	
	/**
	 * 保存关联轮询或者上墙预案的预置位
	 * @param type  预案类型 wall:上墙预案，polling:轮巡预案
	 * @param planPresentVo
	 */
	void savePlanPresent(String type, PlanPresentVo planPresentVo);

}
