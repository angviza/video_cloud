package com.hdvon.nmp.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.vo.DepartmentProject;
import com.hdvon.nmp.vo.ProjectDepartmentVo;
import com.hdvon.nmp.vo.ProjectParamVo;
import com.hdvon.nmp.vo.ProjectVo;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;

/**
 * <br>
 * <b>功能：</b>项目信息 服务类<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IProjectService{
	
	/**
	 * 添加项目
	 * @param userVo
	 * @param project
	 */
	public void addProject(UserVo userVo, ProjectVo projectVo);
	
	/**
	 * 删除项目
	 * @param ids
	 */
	public void delProjectsByIds(List<String> ids);
	
	/**
	 * 查询项目信息
	 * @param id
	 * @return
	 */
	public ProjectVo getProjectById(ProjectDepartmentVo projectDepartmentVo);
	
	
	/**
	 * 分页查询项目列表
	 * @param pp
	 * @param search
	 * @return
	 */
	public PageInfo<ProjectVo> getProjectPages(PageParam pp, TreeNodeChildren treeNodeChildren, ProjectParamVo projectParamVo);
	


	/**
	 * 批量保存项目列表
	 * @param list
	 * @param titles
	 */
	public void batchInsertProjects(List<Map<String,Object>> list, String[] titles);
}
