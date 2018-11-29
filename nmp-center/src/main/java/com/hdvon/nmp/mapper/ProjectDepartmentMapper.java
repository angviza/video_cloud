package com.hdvon.nmp.mapper;

import java.util.List;

import com.hdvon.nmp.entity.ProjectDepartment;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.OrganizationTreeVo;

import tk.mybatis.mapper.common.Mapper;

public interface ProjectDepartmentMapper extends Mapper<ProjectDepartment> , MySqlMapper<ProjectDepartment>{
	/**
	 * 获取项目分组顶级目录【更加项目去获取其顶级部门】
	 * @param modelMap
	 * @return
	 */
	List<OrganizationTreeVo> selectTopProject();
	
	/**
	 * 根据项目pid获取下级内容
	 * @param modelMap
	 * @return
	 */
	List<OrganizationTreeVo> selectChildProject(String pid);
	
	/**
	 * 根据部门编码判断部门是否存在项目分组
	 * @param depCode
	 * @return
	 */
	int existProject(String depCode);
}