package com.hdvon.nmp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hdvon.nmp.entity.Camera;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.CameraNode;
import com.hdvon.nmp.vo.CameraParamVo;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.sip.CameraRegisteredParamVo;
import com.hdvon.nmp.vo.sip.UserDeviceParamVo;

import tk.mybatis.mapper.common.Mapper;

public interface CameraMapper extends Mapper<Camera> , MySqlMapper<Camera>{

    List<CameraVo> queryCamerasByEncodeId(@Param("encodeId") String encodeId);

    List<CameraVo> selectCameraByMapPage(CameraParamVo vo);

    List<CameraVo> selectCamerasByPollingId(@Param("pollingId") String pollingId);

    List<CameraVo> selectCamerasInWallChannel(Map<String, Object> map);
    
    List<CameraVo> selectCamerasInPollingPlan(Map<String, Object> map);

	List<UserDeviceParamVo> selectUserCameraPermission(Map<String, Object> param);

	List<CameraRegisteredParamVo> selectCameraRegisteredParam(String deviceId);

	List<CameraVo> selectByParam(Map<String, Object> param);
	
	List<CameraVo> seletCameraInfo(String deviceCode);

    /**
     * 摄像机简单对象查询（可扩展查询）
     * @param map
     * @return
     */
	List<CameraNode> selectCameraNode(Map<String,Object> map);

    /**
     * 查询已经授权的摄像机id列表
     * @param isAdmin 是否管理员
     * @param userId 用户id
     * @return
     */
    List<String> selectAuthCameraIds(@Param("isAdmin")boolean isAdmin , @Param("userId")String userId);
    
    List<CameraVo> selectCameraByParam(Map<String,Object> map);
    
    List<CameraVo> selectCamerasInGroups(@Param("groupIds")List<String> groupIds);

	List<String> selectDeviceIdByCamearId(Map<String, Object> param);
    /**
	 * 根据设备编码获取摄像机信息
	 * @param deviceCode 设备编码
	 * @return
	 */
	List<CameraVo> findCameraByDeviceCode(@Param("deviceCode")String deviceCode);
}