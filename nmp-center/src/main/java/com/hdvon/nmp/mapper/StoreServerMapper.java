package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.common.CameraPermissionVo;
import com.hdvon.nmp.entity.StoreServer;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.StoreServerParamVo;
import com.hdvon.nmp.vo.StoreServerVo;

public interface StoreServerMapper extends Mapper<StoreServer> , MySqlMapper<StoreServer>{
	
	StoreServerVo selectStoreServerByParam(Map<String,Object> map);
	
	List<StoreServerVo> selectStoreServersList(Map<String,Object> map);
	
	List<CameraPermissionVo> selectUserStoreserverCamera(Map<String,Object> map);
	
	void deleteCamerasByStoreserverId(String storeserverId,String userId);

	String selectMaxCodeBycode(Map<String, Object> map);
}