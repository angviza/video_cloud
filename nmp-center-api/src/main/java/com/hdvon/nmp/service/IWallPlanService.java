package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.common.CameraPermissionVo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.*;

/**
 * <br>
 * <b>功能：</b>上墙预案 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
/**
 * @author Administrator
 *
 */
public interface IWallPlanService{
	 /**
	    * 保存上墙预案信息
	    * @param userVo
	    * @param wallPlanVo
	    */
	   void saveWallPlan(UserVo userVo, WallPlanParamVo wallPlanParamVo);
	   /**
	    * 分页查询上墙预案列表
	    * @param pp
	    * @param search
	    * @return
	    */
	   PageInfo<WallPlanVo> getWallPlanPages(PageParam pp, Map<String,Object> map);
	   /**
	    * 查询上墙预案列表
		 * @param wallPlanParamVo
		 * @param userVo
		 * @return
		 */
		List<WallPlanVo> getWallPlanList(WallPlanParamVo wallPlanParamVo, UserVo userVo);
	   /**
	    * 批量删除上墙预案
	    * @param ids
	    */
	   void delWallPlansByIds(List<String> ids);
	   /**
	    * 根据id查询单个上墙预案信息
	    * @param id
	    * @return
	    */
	   WallPlanVo getWallPlanById(String id);
	   /**
		 * 查询上墙预案单个通道下需要轮询的摄像机列表
		 * @param map
		 * @return
		 */
		List<CameraVo> getWallChannelCameraList(Map<String,Object> map);
		
		/**
		 * 保存通道与摄像机的关联
		 * @param cameraIdList
		 */
		void saveChannelRelateCameras(String wallplanId, String channelId, List<String> cameraIdList, List<String> cameraGroupIdList, List<String> mapCameraIdList);
	    
	    /**
	     * 查询上墙预案的共享部门
	     * @param wallplanId
	     * @return
	     */
	    WallPlanVo getWallplanShares(String wallplanId);

        /**
         * 查询上墙预案单个通道关联的所有摄像机
         * @param userVo
         * @param wallplanId
         * @param channelId
         * @return
         */
	    List<CameraNode> getWallChannelRelatedCameras(UserVo userVo, String wallplanId , String channelId);
	    
	    /**
	     * 查询上墙预案关联的所有摄像机
	     * @param wallplanId
	     * @return
	     */
	    WallPlanVo getWallChannelCameras(String wallplanId);
	    
	    /**
	     * 上墙预案单个通道关联摄像机移位，调整摄像机的排序
	     * @param wallplanId
	     * @param matrixchannelId
	     * @param curCameraId
	     * @param cameraId
	     */
	    void changeCameraSort(String wallplanId, String matrixchannelId, String curCameraId, String cameraId);
}
