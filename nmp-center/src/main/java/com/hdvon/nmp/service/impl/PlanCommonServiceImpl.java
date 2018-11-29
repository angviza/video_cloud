package com.hdvon.nmp.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.BallPlan;
import com.hdvon.nmp.entity.PollingPlan;
import com.hdvon.nmp.entity.PollingplanCamera;
import com.hdvon.nmp.entity.WallPlan;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.*;
import com.hdvon.nmp.service.IPlanCommonService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.CameraNode;
import com.hdvon.nmp.vo.DeviceVo;
import com.hdvon.nmp.vo.PlanCommonVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>处理所有类型的预案 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
@Slf4j
public class PlanCommonServiceImpl implements IPlanCommonService {
    @Autowired
    private PollingPlanMapper pollingPlanMapper;
    @Autowired
    private BallPlanMapper ballPlanMapper;
    @Autowired
    private WallPlanMapper wallPlanMapper;
    @Autowired
    private PollingplanCameraMapper pollingplanCameraMapper;
    @Autowired
    private CameraMapper cameraMapper;

    @Override
    public PageInfo<PlanCommonVo> getPlanCommonList(PageParam pp, String name, String type) {
        if(StrUtil.isBlank(type)){
            type = "all";
        }
        if(!"all".equals(type) &&  !"ball".equals(type) && !"polling".equals(type) && !"wall".equals(type)){
            throw new ServiceException("不支持的预案类型");
        }

        PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("type",type);
        List<PlanCommonVo> list = pollingPlanMapper.selectAllPlanList(map);
        return new PageInfo<>(list);
    }

    @Override
    public boolean updatePlanCommon(String id, String type, String status) {
        if(StrUtil.isBlank(id)){
            throw new ServiceException("id不允许为空");
        }else if(StrUtil.isBlank(type)){
            throw new ServiceException("类型不允许为空");
        }else if(StrUtil.isBlank(status)){
            throw new ServiceException("状态不允许为空");
        }else if(!"1".equals(status) && !"0".equals(status)){
            throw new ServiceException("状态参数错误");
        }
        Integer newStatus = Integer.parseInt(status);
        if("polling".equals(type)){
            PollingPlan pollingPlan = new PollingPlan();
            pollingPlan.setId(id);
            pollingPlan.setStatus(newStatus);
            pollingPlanMapper.updateByPrimaryKeySelective(pollingPlan);
        }else if("ball".equals(type)){
            BallPlan ballPlan = new BallPlan();
            ballPlan.setId(id);
            ballPlan.setStatus(newStatus);
            ballPlanMapper.updateByPrimaryKeySelective(ballPlan);
        }else if("wall".equals(type)){
            WallPlan wallPlan = new WallPlan();
            wallPlan.setId(id);
            wallPlan.setStatus(newStatus);
            wallPlanMapper.updateByPrimaryKeySelective(wallPlan);
        }else{
            throw new ServiceException("不支持的预案类型");
        }
        return true;
    }

    @Override
    public List<CameraNode> getPlanCameraList(String id, String type) {
        if(StrUtil.isBlank(id)){
            throw new ServiceException("id不允许为空");
        }else if(StrUtil.isBlank(type)) {
            throw new ServiceException("类型不允许为空");
        }
        if("polling".equals(type) || "ball".equals(type) || "wall".equals(type)){
            Map<String,Object> map = new HashMap<>();
            map.put("planId",id);
            map.put("planType",type);
            List<CameraNode> list = cameraMapper.selectCameraNode(map);
            return list;
        }else{
            throw new ServiceException("不支持的预案类型");
        }
    }
}
