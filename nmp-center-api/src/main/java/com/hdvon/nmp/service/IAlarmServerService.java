package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.AlarmServerParamVo;
import com.hdvon.nmp.vo.AlarmServerVo;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;

/**
 * <br>
 * <b>功能：</b>报警设备表 服务类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IAlarmServerService{

	/**
	 * 添加报警设备服务器
	 * @param userVo
	 * @param alarmServer
	 */
	public void saveAlarmServer(UserVo userVo, AlarmServerParamVo alarmServerParamVo, List<String> projectIdList);
	
	/**
	 * 删除报警设备
	 * @param ids
	 */
	public void delAlarmServersByIds(List<String> ids);
	
	/**
	 * 查询报警服务器信息
	 * @param alarmServerParamVo
	 * @return
	 */
	public AlarmServerVo getAlarmServerById(AlarmServerParamVo alarmServerParamVo);
	
	
	/**
	 * 分页查询报警设备列表
	 * @param pp
	 * @param search
	 * @return
	 */
	public PageInfo<AlarmServerVo> getAlarmServerPages(PageParam pp, TreeNodeChildren treeNodeChildren, AlarmServerParamVo alarmServerParamVo);
	
	/**
	 * 查询报警设备列表
	 * @param map
	 * @return
	 */
	public List<AlarmServerVo> getAlarmServerList(TreeNodeChildren treeNodeChildren, AlarmServerParamVo alarmServerParamVo);

	public String getMaxCodeBycode(Map<String, Object> map);
}
