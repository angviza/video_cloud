package com.hdvon.client.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.client.entity.User;
import com.hdvon.client.exception.CameraException;
import com.hdvon.client.exception.UserException;
import com.hdvon.client.mapper.TreeMapper;
import com.hdvon.client.service.CommonService;
import com.hdvon.client.service.ITreeService;
import com.hdvon.client.vo.CameraPermissionVo;
import com.hdvon.nmp.common.SystemConstant;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * <br>
 * <b>功能：</b>摄像机行政区域树服务接口<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018-6-20<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class TreeServiceImpl implements ITreeService {

	@Autowired
	TreeMapper treeMapper;
	@Resource
	CommonService commonService;
	
	@Override
	public List<CameraPermissionVo> getAddressTree() throws CameraException {
		// TODO Auto-generated method stub
		return treeMapper.findAddressTree();
	}

	@Override
	public List<CameraPermissionVo> getOrganizationTree() throws CameraException {
		// TODO Auto-generated method stub
		return treeMapper.findOrganizationTree();
	}

	@Override
	public List<CameraPermissionVo> getProjectTree() throws CameraException {
		// TODO Auto-generated method stub
//		List<CameraPermissionVo> dataList = new ArrayList<>();
//		// 获取项目的顶级部门
//		List<CameraPermissionVo> list = treeMapper.findTopDept();
//		dataList.addAll(list);
//		//递归获取下级节点
//		for(CameraPermissionVo vo:list) {
//			getChildProject(vo.getId(), dataList);
//		}
		List<CameraPermissionVo> dataList = treeMapper.findProjectTree();
		return dataList;
	}
	
	public void getChildProject(String pid,List<CameraPermissionVo> dataList) throws CameraException {
		// TODO Auto-generated method stub
		// 获取项目的顶级部门
		Map<String, Object> modelMap = new HashMap<>(16);
		modelMap.put("pid", pid);
		modelMap.put("isAdmin", 1);
		
		List<CameraPermissionVo> list = treeMapper.findChildProjectOrDept(modelMap);
		if(list == null || list.isEmpty()) {
			return;
		}	
		dataList.addAll(list);
		for(CameraPermissionVo vo:list) {
			getChildProject(vo.getId(), dataList);
		}
	}

	@Override
	public List<CameraPermissionVo> getCameraGroupTree(String userId) throws CameraException {
		// TODO Auto-generated method stub
		//判断用户是否超级管理员
		User user = commonService.findRecord(userId);
		if(user == null) {
			throw new UserException("该用户在系统中不存在");
		}
		Map<String,Object> modelMap = new HashMap<>(16);
		modelMap.put("userName", userId);
		int isAdmin = SystemConstant.super_Account.equals(user.getAccount()) ? 1 :0;
		modelMap.put("isAdmin", isAdmin);
		
		return treeMapper.findCameraGroupTree(modelMap);
	}

}
