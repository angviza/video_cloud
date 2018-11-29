package com.hdvon.nmp.service;


import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.CameraNode;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.DeviceVo;
import com.hdvon.nmp.vo.PlanCommonVo;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>处理所有类型的预案 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IPlanCommonService {

    /**
     * 查询所有预案列表
     * @param pp 分页
     * @param name 根据预案名称查询
     * @param type 根据预案类型查询
     * @return
     */
    public PageInfo<PlanCommonVo> getPlanCommonList(PageParam pp, String name, String type);

    /**
     * 更新预案状态
     * @param id
     * @param type
     * @param status
     */
    boolean updatePlanCommon(String id , String type , String status);


    /**
     * 获取预案相关摄像机
     * @param id
     * @param type
     * @return
     */
    List<CameraNode> getPlanCameraList(String id , String type );
}
