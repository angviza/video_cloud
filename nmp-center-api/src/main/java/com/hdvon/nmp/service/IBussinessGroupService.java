package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.BussinessGroupVo;
import com.hdvon.nmp.vo.CameraBussinessGroupVo;
import com.hdvon.nmp.vo.CameraNode;
import com.hdvon.nmp.vo.UserVo;

/**
 * <br>
 * <b>功能：</b>业务分组 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IBussinessGroupService{

	void saveBussinessGroup(UserVo userVo, BussinessGroupVo vo);

	PageInfo<BussinessGroupVo> getBussinessGroupByPage(BussinessGroupVo vo, PageParam pageParam);

	void deleteBussinessGroup(List<String> buessIdList);

	PageInfo<CameraBussinessGroupVo> getBuessByCameraPage(Map<String,Object> param, PageParam pageParam);

    /**
     * 保存业务分组关联摄像
     * @param buessId
     * @param cameraIdList
     */
	void saveRelateCamera(UserVo userVo,String buessId, List<String> cameraIdList);

    /**
     * 获取业务分组关联摄像
     * @param userVo
     * @param bussGroupId
     * @return
     */
    List<CameraNode> getRelateCamera(UserVo userVo, String bussGroupId);


	BussinessGroupVo getBuessByCameraPage(String id);

	PageInfo<CameraBussinessGroupVo> getBuessByCameraList(PageParam pageParam,Map<String,Object> param);

	List<BussinessGroupVo> getBussinessGroupList();

}
