package com.hdvon.nmp.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.mapper.CameraConnectionsMapper;
import com.hdvon.nmp.service.ICameraConnectionsService;

/**
 * <br>
 * <b>功能：</b>Service<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class CameraConnectionsServiceImpl implements ICameraConnectionsService {

	@Autowired
	private CameraConnectionsMapper cameraConnectionsMapper;

	@Override
	public List<Map<String, Object>> getMonitorConnectionsRank(int rank) {
		return cameraConnectionsMapper.selectMonitorConnectionsRank(rank);
	}

	@Override
	public List<Map<String, Object>> getReplayConnectionsRank(int rank) {
		return cameraConnectionsMapper.selectReplayConnectionsRank(rank);
	}

	@Override
	public List<Map<String, Object>> getDownloadConnectionsRank(int rank) {
		return cameraConnectionsMapper.selectDownloadConnectionsRank(rank);
	}

}
