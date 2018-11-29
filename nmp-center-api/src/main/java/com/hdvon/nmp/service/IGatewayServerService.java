package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.GatewayServerParamVo;
import com.hdvon.nmp.vo.GatewayServerVo;
import com.hdvon.nmp.vo.SigServerParamVo;
import com.hdvon.nmp.vo.SigServerVo;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;

/**
 * <br>
 * <b>功能：</b>网管服务器管理 服务类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IGatewayServerService{
	
	/**
	 * 添加网关服务器
	 * @param userVo
	 * @param gatwayServer
	 */
	public void saveGatewayServer(UserVo userVo, GatewayServerParamVo gatewayServerParamVo, List<String> projectIds);
	
	/**
	 * 删除网关服务器
	 * @param ids
	 */
	public void delGatewayServersByIds(List<String> ids);
	
	/**
	 * 查询网关服务器信息
	 * @param id
	 * @return
	 */
	public GatewayServerVo getGatewayServerById(String id);
	
	
	/**
	 * 分页查询网关服务器列表
	 * @param pp
	 * @param search
	 * @return
	 */
	public PageInfo<GatewayServerVo> getGatewayServerPages(PageParam pp, TreeNodeChildren treeNodeChildren, GatewayServerParamVo gatewayServerParamVo);
	
	/**
	 * 查询网关服务器列表
	 * @param map
	 * @return
	 */
	public List<GatewayServerVo> getGatewayServerList(TreeNodeChildren treeNodeChildren,GatewayServerParamVo gatewayServerParamVo);

	public String getMaxCodeBycode(Map<String, Object> map);

}
