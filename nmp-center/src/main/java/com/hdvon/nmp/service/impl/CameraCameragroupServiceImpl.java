package com.hdvon.nmp.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.mapper.CameraCameragroupMapper;
import com.hdvon.nmp.mapper.CameraMapper;
import com.hdvon.nmp.service.ICameraCameragroupService;
import com.hdvon.nmp.vo.CameraNode;
import com.hdvon.nmp.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>摄像机表与摄像机分组的关联表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class CameraCameragroupServiceImpl implements ICameraCameragroupService {

	@Autowired
	private CameraCameragroupMapper cameraCameragroupMapper;
    @Autowired
    private CameraMapper cameraMapper;


    @Override
	public List<CameraNode> selectByGroup(UserVo userVo, String cameragroupId) {
        Map<String, Object> param = new HashMap<>();
        Map<String,Object> map = new HashMap<>();
        map.put("isAdmin",userVo.isAdmin());
        map.put("userId",userVo.getId());
        if(cameragroupId == null){
            cameragroupId = "";
        }
        param.put("cameragroupId",cameragroupId);
		return cameraMapper.selectCameraNode(param);
	}


    /**
     * 获取自定义分组下所以摄像机
     */
	@Override
	public List<CameraNode> selectCamearByGroup(String cameargroupId) {
		 Map<String, Object> param = new HashMap<>();
         Map<String,Object> map = new HashMap<>();
         map.put("isAdmin",1);// 管理员
         param.put("cameragroupId",cameargroupId);
		 return cameraMapper.selectCameraNode(param);
	}

}
