package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.TranspondServerParamVo;
import com.hdvon.nmp.vo.TranspondServerVo;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;

/**
 * <br>
 * <b>功能：</b>转发服务器 服务类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface ITranspondServerService{
		
	/**
	 * 添加转发服务器
	 * @param userVo
	 * @param transpondServerParamVo
	 * @param projectIds
	 */
	public void saveTranspondServer(UserVo userVo, TranspondServerParamVo transpondServerParamVo, List<String> projectIds);
	
	/**
	 * 删除转发服务器
	 * @param ids
	 */
	public void delTranspondServersByIds(List<String> ids);
	
	/**
	 * 查询转发服务器信息
	 * @param id
	 * @return transpondServerParamVo
	 */
	public TranspondServerVo getTranspondServerById(TranspondServerParamVo transpondServerParamVo);
	
	
	/**
	 * 分页查询转发服务器列表
	 * @param pp
	 * @param map
	 * @return
	 */
	public PageInfo<TranspondServerVo> getTranspondServerPages(PageParam pp, TreeNodeChildren treeNodeChildren, TranspondServerParamVo transpondServerParamVo);
	
	/**
	 * 查询转发服务器列表
	 * @param map
	 * @return
	 */
	public List<TranspondServerVo> getTranspondServerList(TreeNodeChildren treeNodeChildren, TranspondServerParamVo transpondServerParamVo);

	
	public String getMaxCodeBycode(Map<String, Object> map);

}
