package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.CameraConnections;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

public interface CameraConnectionsMapper extends Mapper<CameraConnections> , MySqlMapper<CameraConnections> {
	
	public List<Map<String, Object>> selectMonitorConnectionsRank(int rank);
	
	public List<Map<String, Object>> selectReplayConnectionsRank(int rank);
	
	public List<Map<String, Object>> selectDownloadConnectionsRank(int rank);
}