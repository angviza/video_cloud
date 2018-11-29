package com.hdvon.nmp.service;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.CameragrouopVo;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;
import org.omg.CORBA.UserException;

import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>摄像机分组表
 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface ICameragrouopService{

    /**
     * 按分页查询分组列表
     * @param cameragrouopVo
     * @param pageParam
     * @return
     */
    public PageInfo<CameragrouopVo> getGroupListByPage(CameragrouopVo cameragrouopVo ,TreeNodeChildren treeNodeChildren, PageParam pageParam);


    /**
     * 根据id查询分组信息
     * @param id
     * @return
     */
    public CameragrouopVo getGroupById(String id);

    /**
     * 保存分组
     * @param cameragrouopVo
     */
    public void saveGroup(UserVo userVo , CameragrouopVo cameragrouopVo);

    /**
     * 删除分组
     * @param userVo
     * @param groupIds
     */
    public void deleteGroup(UserVo userVo , List<String> groupIds);
    
	/**
	 * 查询关联上墙预案的摄像机自定义分组列表
     * @param userVo
     * @param wallplanId
     * @param channelId
	 * @return
	 * @throws UserException
	 */
	List<CameragrouopVo> getRelateWallPlanCameraGroupTree(UserVo userVo , String wallplanId , String channelId);
	

	/**
	 * 批量保存摄像机自定义分组
	 * @param list
	 * @param titles
	 */
	void batchInsertCameragroups(List<Map<String,Object>> list,String[] titles);

	/**
	 * 处理id pid关系
	 * @param list
	 * @param titles
	 */
	List<Map<String,Object>> transpCodeToId(List<Map<String,Object>> list,String[] titles);

	/**
	 * 导出地址数据
	 */
	List<CameragrouopVo> exportGrpup(String seach,TreeNodeChildren treeNodeChildren);

	/**
	 * 保存当前用户单个自定义分组中可见摄像机的关联
	 * @param userVo
	 * @param cameargroupId
	 * @param cameraIdList
	 */
	void relateCamerasToCameargroup(UserVo userVo, String cameargroupId, List<String> cameraIdList);
	
	public int getCameragroupCountByPid(String id);
}
