package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.LimitLoginVo;
import com.hdvon.nmp.vo.UserVo;

/**
 * <br>
 * <b>功能：</b>用户登录限制登录表 服务类<br>
 * <b>作者：</b>huangguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface ILimitLoginService{
	
	PageInfo<LimitLoginVo> getLimitByPage(PageParam pageParam, Map<String, Object> param);

	LimitLoginVo limitLoginSave(LimitLoginVo vo,UserVo userVo);

	LimitLoginVo getInfo(String id);

	void deleteLimitIp(List<String> idList);

	List<LimitLoginVo> getLimitLoginList(Map<String, Object> param);

	int seletCount(Map<String, Object> param);

	void updateState(LimitLoginVo vo, UserVo userVo);

}
