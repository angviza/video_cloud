package com.hdvon.nmp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.mapper.ProjectDepartmentMapper;
import com.hdvon.nmp.service.IProjectDepartmentService;
import com.hdvon.nmp.vo.OrganizationTreeVo;

/**
 * <br>
 * <b>功能：</b>项目与部门关联表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class ProjectDepartmentServiceImpl implements IProjectDepartmentService {

	@Autowired
	private ProjectDepartmentMapper projectDepartmentMapper;
	/*
	 * 获取行政区域树
	 * @param pid 父节点id
	 * @return
	 * @throws UserException
	 */
	@Override
	public List<OrganizationTreeVo> getProjectTree() {
		// TODO Auto-generated method stub
		List<OrganizationTreeVo> allList = new ArrayList<>();
		//获取顶级目录
		List<OrganizationTreeVo> toplist = projectDepartmentMapper.selectTopProject();
		for(OrganizationTreeVo vo:toplist) {
			allList.add(vo);
			
			getChilderNode(vo.getId(), allList);
		}
		return allList;
	}
	
	public void getChilderNode(String pid,List<OrganizationTreeVo> allList) {
		//获取顶级目录
		List<OrganizationTreeVo> childlist = projectDepartmentMapper.selectChildProject(pid);
		if(childlist.isEmpty()) {
			return;
		}
		for(OrganizationTreeVo vo:childlist) {
			if(vo.getIsProject()==0) {//判断当前部门是否存在项目
				int count = projectDepartmentMapper.existProject(vo.getDepCode());
				if(count == 0) {
					continue;
				}
				allList.add(vo);
				
				//递归获取下级元素
				getChilderNode(vo.getId(), allList);
			}else {
				allList.add(vo);
			}
			

		}
	}

}
