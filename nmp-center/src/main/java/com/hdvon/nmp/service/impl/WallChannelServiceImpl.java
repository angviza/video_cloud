package com.hdvon.nmp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.entity.WallChannel;
import com.hdvon.nmp.mapper.WallChannelMapper;
import com.hdvon.nmp.service.IWallChannelService;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.WallChannelVo;

import cn.hutool.core.bean.BeanUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>上墙轮巡的矩阵通道表Service<br>
 * <b>作者：</b>zhanqf<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class WallChannelServiceImpl implements IWallChannelService {

	@Autowired
	private WallChannelMapper wallChannelMapper;

	@Override
	public List<WallChannelVo> findByRotateId(String id) {
		Example exm = new Example(WallChannel.class);
		exm.createCriteria().andEqualTo("rotateId", id);
		List<WallChannel> eoLst = this.wallChannelMapper.selectByExample(exm);
		if ((eoLst == null) ||
				eoLst.isEmpty()) {
			return null;
		} // if
		List<WallChannelVo> voLst = new ArrayList<>();
		for (WallChannel eo: eoLst) {
			WallChannelVo vo = new WallChannelVo();
			BeanUtil.copyProperties(eo, vo);
			voLst.add(vo);
		} // for
		return voLst;
	}

	@Override
	public void updateCameraId(String id, String cameraId) {
		WallChannel eo = this.wallChannelMapper.selectByPrimaryKey(id);
		eo.setCameraId(cameraId);
		this.wallChannelMapper.updateByPrimaryKey(eo);
	}

	@Override
	public void deleteByRotateId(String id) {
		Example exm = new Example(WallChannel.class);
		exm.createCriteria().andEqualTo("rotateId", id);
		this.wallChannelMapper.deleteByExample(exm);
	}

	@Override
	public void saveList(
			String rotateId,
			List<String> idLst) {
		if ((idLst == null) ||
				idLst.isEmpty()) {
			return;
		} // if
		List<WallChannel> eoLst = new ArrayList<>();
		for (int index = 1, size = idLst.size(); index <= size; ++ index) {
			String channelId = idLst.get(index - 1);
			WallChannel eo = new WallChannel();
			eoLst.add(eo);
			// setter
			eo.setId(IdGenerator.nextId());
			eo.setChannelId(channelId);
			eo.setRotateId(rotateId);
			eo.setQueue(index);
		} // for
		this.wallChannelMapper.insertList(eoLst);
	}

}
