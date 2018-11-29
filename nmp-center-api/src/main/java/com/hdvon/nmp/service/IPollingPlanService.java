package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.common.CameraPermissionVo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.*;

/**
 * <br>
 * <b>功能：</b>轮询预案 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IPollingPlanService{

	 /**
     * 保存轮询预案信息
     * @param userVo
     * @param pollingPlanParamVo
     * @param cameraIds 地图界面创建预案关联多个摄像机id
     * @param isMap 是否地图上创建编辑预案
     */
    void savePollingPlan(UserVo userVo, PollingPlanParamVo pollingPlanParamVo, String cameraIds, boolean isMap);
    /**
     * 分页查询轮询预案列表
     * @param pp
     * @param search
     * @return
     */
    PageInfo<PollingPlanVo> getPollingPlanPages(PageParam pp, PollingPlanParamVo pollingPlanParamVo , UserVo userVo);
    /**
     * 查询轮询预案列表
     * @param search
     * @return
     */
    List<PollingPlanVo> getPollingPlanList(PollingPlanParamVo pollingPlanParamVo , UserVo userVo);
    /**
     * 批量删除轮询预案
     * @param ids
     */
    void delPollingPlansByIds(List<String> ids);
    /**
     * 根据id查询单个轮询预案信息
     * @param id
     * @return
     */
    PollingPlanVo getPollingPlanById(String id);

    /**
     * 保存轮询预案关联的摄像机关联
     * @param pollingplanId 轮巡预案id
     * @param cameraIdList 摄像机id
     * @param cameraGroupIdList 摄像机分组id
     * @param mapCameraIdList 地图摄像机id
     */
    void savePollingRelateCameras(String pollingplanId, List<String> cameraIdList, List<String> cameraGroupIdList , List<String> mapCameraIdList);


    /**
     * 查询轮询预案下单个分组下的摄像机列表
     * @param pollingplanId
     * @param groupId
     * @return
     */
    List<CameraVo> getPollingGroupCameras(String pollingplanId);
    
    /**
     * 查询轮询预案的共享部门
     * @param pollingplanId
     * @return
     */
    PollingPlanVo getPollingplanShares(String pollingplanId);
    
    /**
     * 查询轮询预案下所有的摄像机
     * @param wallplanId
     * @return
     */
    List<CameraVo> getCamerasByPollingplanId(String pollingplanId);
    
    /**
     * 根据预案id查询预案关联的摄像机
     * @return
     */
    List<PollingplanCameraVo> getPollingPlanRelatedCameras(Map<String,Object> map);
    
    /**
     * 查询轮巡预案关联数据，包括地址树己选的摄像机、分组列表、地图己选的摄像机
     * @param pollingplanId
     * @return
     */
    PollingPlanLinksVo getPollingPlanLinks(String pollingplanId, UserVo userVo);
    
    /**
     * 修改轮巡预案关联摄像机的排序
     * @param pollingplanId
     * @param curCameraId
     * @param cameraId
     */
    void changeCameraSort(String pollingplanId, String curCameraId, String cameraId);
}
