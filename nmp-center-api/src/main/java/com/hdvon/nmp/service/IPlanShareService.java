package com.hdvon.nmp.service;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>预案共享设置表 服务类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IPlanShareService{

	void savePlanShares(String planId, Integer shareStatus, List<String> departmentIdList, String flag);
}
