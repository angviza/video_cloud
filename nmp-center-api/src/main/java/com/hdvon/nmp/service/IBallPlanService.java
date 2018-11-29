package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.BallPlanParamVo;
import com.hdvon.nmp.vo.BallPlanVo;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.PlanShareVo;
import com.hdvon.nmp.vo.PresentPositionVo;
import com.hdvon.nmp.vo.UserVo;

/**
 * <br>
 * <b>功能：</b>球机巡航预案表 服务类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IBallPlanService{
	
	/**
	    * 保存球机巡航预案信息
	    * @param userVo
	    * @param ballPlanVo
	    */
	   void saveBallPlan(UserVo userVo, BallPlanParamVo ballPlanParamVo);
	   /**
	    * 分页查询球机巡航预案列表
	    * @param pp
	    * @param search
	    * @return
	    */
	   PageInfo<BallPlanVo> getBallPlanPages(PageParam pp, Map<String,Object> map);
	   /**
	    * 查询球机巡航预案列表
		 * @param ballPlanParamVo
		 * @param userVo
		 * @return
		 */
		List<BallPlanVo> getBallPlanList(BallPlanParamVo ballPlanParamVo, UserVo userVo);
	   /**
	    * 批量删除球机巡航预案
	    * @param ids
	    */
	   void delBallPlansByIds(List<String> ids);
	   /**
	    * 根据id查询单个球机巡航预案信息
	    * @param id
	    * @return
	    */
	   BallPlanVo getBallPlanById(String id);
		
		/**
	     * 保存球机巡航预案的共享部门关联
	     * @param ballplanId
	     * @param departmentIds
	     */
	    void saveBallplanShares(String wallplanId, List<String> departmentIds);
	    
	    /**
	     * 查询球机巡航预案的共享部门
	     * @param ballplanId
	     * @return
	     */
	    BallPlanVo getBallplanShares(String ballplanId);
	    
	    /**
	     * 查询球机巡航预案的预置位
	     * @param map
	     * @return
	     */
	    List<PresentPositionVo> getPresentPositionsInBallPlan(Map<String,Object> amp);
	    
	    /**
	     * 修改保存预案预置位列表信息
	     * @param presentPositions
	     */
	    void savePresentPositionToBallPlan(String ballplanId, List<String> presentPositions, List<String> timeIntervals, List<String> ballPresentIdList);
	    
	    /**
	     * 上移位或者下移位后更新数据库的排序
	     * @param ids
	     */
	    void shiftingUpDown(String ballplanId, List<String> ids);
	    
	    /**
	     * 查询巡航预案中的预置位立标并标记球机巡航预案中的预置位列表
	     * @param map
	     * @return
	     */
	    List<PresentPositionVo> getBallPresentsInCamera(Map<String,Object> amp);
	    

}
