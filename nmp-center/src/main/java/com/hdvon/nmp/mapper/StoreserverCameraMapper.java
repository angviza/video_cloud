package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hdvon.nmp.common.CameraPermissionVo;
import com.hdvon.nmp.entity.StoreserverCamera;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.StoreserverCameraVo;

public interface StoreserverCameraMapper extends Mapper<StoreserverCamera> , MySqlMapper<StoreserverCamera>{
	
	List<StoreserverCameraVo> selectCamerasByIds(@Param("deviceIds") List<String> deviceIds);
	
	List<StoreserverCameraVo> selectUserStoreserverCamera(Map<String,Object> map);
}