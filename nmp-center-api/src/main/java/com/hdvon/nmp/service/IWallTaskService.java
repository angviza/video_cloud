package com.hdvon.nmp.service;

import java.util.List;

import com.hdvon.nmp.vo.MatrixVo;
import com.hdvon.nmp.vo.UserVo;
import com.hdvon.nmp.vo.WallTaskParamVo;
import com.hdvon.nmp.vo.WallTaskVo;

/**
 * <br>
 * <b>功能：</b>上墙轮巡主表 服务类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IWallTaskService{
	
	public List<WallTaskVo> findAll();
	
	public WallTaskVo findOne(String id);
	
	public void delete(List<String> taskIds);
	
	public void save(UserVo userVo, MatrixVo matrixVo, WallTaskParamVo wallTaskParamVo);

	public List<WallTaskVo> getWallTaskList(WallTaskVo wallTaskVo);
}
