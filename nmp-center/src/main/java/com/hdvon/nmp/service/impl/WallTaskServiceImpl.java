package com.hdvon.nmp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.entity.WallTask;
import com.hdvon.nmp.mapper.WallTaskMapper;
import com.hdvon.nmp.service.IWallTaskService;
import com.hdvon.nmp.util.BeanHelper;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.MatrixVo;
import com.hdvon.nmp.vo.UserVo;
import com.hdvon.nmp.vo.WallTaskParamVo;
import com.hdvon.nmp.vo.WallTaskVo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * <br>
 * <b>功能：</b>上墙轮巡主表Service<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class WallTaskServiceImpl implements IWallTaskService {

	@Autowired
	private WallTaskMapper wallTaskMapper;

	@Override
	public List<WallTaskVo> findAll() {
		List<WallTask> eoLst = this.wallTaskMapper.selectAll();
		if ((eoLst == null) ||
				eoLst.isEmpty()) {
			return null;
		} // if
		List<WallTaskVo> voLst = new ArrayList<>();
		for (WallTask eo: eoLst) {
			WallTaskVo vo = new WallTaskVo();
			BeanUtil.copyProperties(eo, vo);
			voLst.add(vo);
		} // for
		return voLst;
	}

	@Override
	public WallTaskVo findOne(String id) {
		WallTask eo = this.wallTaskMapper.selectByPrimaryKey(id);
		WallTaskVo vo = new WallTaskVo();
		BeanUtil.copyProperties(eo, vo);
		return vo;
	}

	@Override
	public void delete(List<String> taskIds) {
		Example taskExample = new Example(WallTask.class);
		taskExample.createCriteria().andIn("id", taskIds);
		wallTaskMapper.deleteByExample(taskExample);
	}

	@Override
	public void save(UserVo userVo, MatrixVo matrixVo, WallTaskParamVo wallTaskParamVo) {
		WallTask wt = new WallTask();
		wt.setId(IdGenerator.nextId());
		wt.setName(wallTaskParamVo.getName());
		wt.setUserId(userVo.getId());
		wt.setMatrixId(matrixVo.getId());
		this.wallTaskMapper.insert(wt);
	}

	@Override
	public List<WallTaskVo> getWallTaskList(WallTaskVo wallTaskVo) {
		Example wallTaskExample = new Example(WallTask.class);
		Criteria criteria = wallTaskExample.createCriteria();
		if(StrUtil.isNotBlank(wallTaskVo.getUserId())) {
			criteria.andEqualTo("userId",wallTaskVo.getUserId());
		}
		if(StrUtil.isNotBlank(wallTaskVo.getMatrixId())) {
			criteria.andEqualTo("matrixId",wallTaskVo.getMatrixId());
		}	
		List<WallTask> list = wallTaskMapper.selectByExample(wallTaskExample);
		List<WallTaskVo> wallTaskVos = BeanHelper.convertList(WallTaskVo.class, list);
		return wallTaskVos;
	}

}
