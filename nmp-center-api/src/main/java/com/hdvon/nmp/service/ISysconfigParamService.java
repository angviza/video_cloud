package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.SysconfigParamVo;
import com.hdvon.nmp.vo.UserVo;

/**
 * <br>
 * <b>功能：</b>系统配置 服务类<br>
 * <b>作者：</b>huangguanxin<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface ISysconfigParamService{

	/**
	 * 分页查询系统配置
	 * @param pageParam
	 * @param param
	 * @return
	 */
	PageInfo<SysconfigParamVo> getSysConfigByPage(PageParam pageParam, Map<String, Object> param);
	
	/**
	 * 保存系统配置
	 * @param vo
	 * @param userVo
	 */
	void save(SysconfigParamVo vo, UserVo userVo);
	
	/**
	 * 系统配置详情
	 * @param id
	 * @return
	 */

	SysconfigParamVo getInfo(String id);

	/**
	 * 删除系统配置
	 * @param idList
	 */
	void deleteByIds(List<String> idList);
	

	List<SysconfigParamVo> getSysConfigByParam(Map<String, Object> param);

}
