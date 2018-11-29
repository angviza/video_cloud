package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.vo.PresentPositionVo;
import com.hdvon.nmp.vo.UserVo;
import com.hdvon.nmp.vo.sip.PresetOptionVo;

/**
 * <br>
 * <b>功能：</b>预置位表 服务类<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IPresentPositionService{
	
	/**
     * 保存预置位信息
     * @param userVo
     * @param presentPositionVo
     */
    void savePresentPosition(UserVo userVo, PresentPositionVo presentPositionVo);
    /**
     * 查询预置位列表
     * @param map
     * @return
     */
    List<PresentPositionVo> getPresentPositionList(Map<String,Object> map);
    /**
     * 批量删除预置位
     * @param ids
     */
    void delPresentPositionsByIds(List<String> ids);
    /**
     * 根据id查询单个预置位信息
     * @param id
     * @return
     */
    PresentPositionVo getPresentPositionById(String id);
    /**
     * 查询预案下的所有预置位
     * @param planId 预案id
     * @return
     */
    List<PresentPositionVo> getPresentPositionsInPlan(String planId);

    /**
     * 查询多个摄像机下的预置位
     * @param paramMap
     * @return
     */
    List<PresentPositionVo> getPresentsInCameras(Map<String,Object> paramMap);
    
    /**
     * 查询报警设备关联摄像机中的预置位，并选中关联预置位
     * @param map
     * @return
     */
    List<PresentPositionVo> getAlarmPresentsInCamera(Map<String,Object> map);

    /**
     * 通过调用c++添加预置点
     * @param userVo
     * @param vo
     */
	void savePresent(UserVo userVo, PresetOptionVo vo,String cameraId);

	/**
	 * 检查参数是否正确
	 * @return
	 */
	String checkParam(PresetOptionVo vo);

	/**
	 * 获取最大预置位号
	 * @param vo
	 * @return
	 */
	int getMaxPresetNum(PresetOptionVo vo);

	/**
	 * 删除预置点
	 * @param vo
	 * @param cameraId
	 */
	void delPresent(PresetOptionVo vo);

	/**
	 * 编码是否存在
	 * @param vo
	 * @param cameraId
	 */
	int getCountByParam(PresetOptionVo vo, String cameraId);

	/**
	 *
	 * @param param
	 * @return
	 */
	List<PresentPositionVo> selectPresetList(Map<String,Object> param);

    /**
     * 查询最大预置位编号
     * @return
     */
    String getMaxPresetNo(String cameraId);
    
	/**
	 * 设置重新设置守望位
	 * @param presentId
	 */
	void resetKeepwatch(String cameraId, String presentId);
	
	/**
	 * 摄像机剩下的可以设置的预置位编号
	 * @param cameraId
	 * @return
	 */
	List<Integer> getEnablePresentPositions(String cameraId);
	
	
	/**
	 * 批量新增预值点
	 * @param userVo
	 * @param list
	 * @param cameraId
	 */
	void savePresentPosition(UserVo userVo, List<PresentPositionVo> list, String cameraId);
}
