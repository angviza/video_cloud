package com.hdvon.nmp.mapper;

import com.hdvon.nmp.vo.CameraParamVo;
import com.hdvon.nmp.vo.DeviceParamVo;
import com.hdvon.nmp.vo.DeviceVo;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hdvon.nmp.entity.Device;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

public interface DeviceMapper extends Mapper<Device> , MySqlMapper<Device>{
    DeviceParamVo selectCameraAndDeviceInfo(String cameraId);

    String selectMaxCodeBycode(String smbh);

	List<DeviceVo> selectByParam(Map<String, Object> param);

    List<DeviceVo> selectCamerasByGroupId(@Param("groupId")String groupId);
    
    List<Map<String,Object>> selectDeviceByMapPage(CameraParamVo vo);

    List<Map<String,Object>> selectDeviceByStorageId(Map<String,Object> map);
    
    int countCamerasByParam(CameraParamVo vo);

    DeviceVo selectDeviceBystoreCameraId(@Param("storeCameraId")String storeCameraId);

    DeviceVo selectDeviceByCameraId(@Param("cameraId")String cameraId);
}