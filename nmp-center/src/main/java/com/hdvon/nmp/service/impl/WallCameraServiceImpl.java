package com.hdvon.nmp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.entity.WallCamera;
import com.hdvon.nmp.mapper.CameraMapper;
import com.hdvon.nmp.mapper.WallCameraMapper;
import com.hdvon.nmp.service.IWallCameraService;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.WallCameraVo;

import cn.hutool.core.bean.BeanUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>上墙轮巡的摄像机表Service<br>
 * <b>作者：</b>zhanqf<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class WallCameraServiceImpl implements IWallCameraService {

	@Autowired
	private WallCameraMapper wallCameraMapper;
	
	@Autowired
	private CameraMapper cameraMapper;

	@Override
	public List<WallCameraVo> findByRotateId(String id) {
		Example exm = new Example(WallCamera.class);
		exm.createCriteria().andEqualTo("rotateId", id);
		List<WallCamera> eoLst = this.wallCameraMapper.selectByExample(exm);
		if ((eoLst == null) ||
				eoLst.isEmpty()) {
			return null;
		} // if
		List<WallCameraVo> voLst = new ArrayList<>();
		for (WallCamera eo: eoLst) {
			WallCameraVo vo = new WallCameraVo();
			BeanUtil.copyProperties(eo, vo);
			voLst.add(vo);
		} // for
		return voLst;
	}

	@Override
	public void deleteByRotateId(String id) {
		Example exm = new Example(WallCamera.class);
		exm.createCriteria().andEqualTo("rotateId", id);
		this.wallCameraMapper.deleteByExample(exm);
	}

	@Override
	public void saveList(
			String rotateId,
			List<String> idLst) {
		if ((idLst == null) ||
				idLst.isEmpty()) {
			return;
		} // if
		List<WallCamera> eoLst = new ArrayList<>();
		for (int index = 1, size = idLst.size(); index <= size; ++ index) {
			String cameraId = idLst.get(index - 1);
			WallCamera eo = new WallCamera();
			eoLst.add(eo);
			// setter
			eo.setId(IdGenerator.nextId());
			eo.setCameraId(cameraId);
			eo.setRotateId(rotateId);
			eo.setQueue(index);
		} // for
		this.wallCameraMapper.insertList(eoLst);
	}

}
