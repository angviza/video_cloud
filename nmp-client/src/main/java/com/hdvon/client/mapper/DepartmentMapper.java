package com.hdvon.client.mapper;

import java.util.List;

import com.hdvon.client.config.mybatis.MyMapper;
import com.hdvon.client.entity.Department;
import com.hdvon.client.vo.DepartmentVo;
/**
 * 部门mapper接口
 * @author wanshaojian
 *
 */
public interface DepartmentMapper extends MyMapper<Department>{
	/**
	 * 根据用户获取部门信息
	 * @param userId
	 * @return
	 */
	List<Department> findRecord(String userId);
	/**
	 * 获取广州市区部门
	 * @return
	 */
	List<DepartmentVo> selectGzDepartment();
	
	/**
	 * 根据部门编码判断部门是否存在项目分组
	 * @param depCode
	 * @return
	 */
	int existProject(String depCode);
}
