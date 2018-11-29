package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.ProjectMappingVo;
import com.hdvon.nmp.vo.SigServerParamVo;
import com.hdvon.nmp.vo.SigServerVo;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;

/**
 * <br>
 * <b>功能：</b>信令中心服务器表 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface ISigServerService{
	
	/**
	 * 添加信令服务器
	 * @param userVo
	 * @param sigServer
	 */
	public void saveSigServer(UserVo userVo, SigServerParamVo sigServerParamVo, List<String> projectIds);
	
	/**
	 * 删除信令服务器
	 * @param ids
	 */
	public void delSigServersByIds(List<String> ids);
	
	/**
	 * 查询信令服务器信息
	 * @param id
	 * @return
	 */
	public SigServerVo getSigServerById(SigServerParamVo sigServerParamVo);
	
	
	/**
	 * 分页查询信令服务器列表
	 * @param pp
	 * @param search
	 * @return
	 */
	public PageInfo<SigServerVo> getSigServerPages(PageParam pp, TreeNodeChildren treeNodeChildren, SigServerParamVo sigServerParamVo);
	
	/**
	 * 查询信令服务器列表
	 * @param map
	 * @return
	 */
	public List<SigServerVo> getSigServerList(TreeNodeChildren treeNodeChildren, SigServerParamVo sigServerParamVo);

	public String getMaxCodeBycode(Map<String, Object> map);
}
