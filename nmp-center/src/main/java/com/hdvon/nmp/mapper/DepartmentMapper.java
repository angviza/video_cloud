package com.hdvon.nmp.mapper;

import com.hdvon.nmp.vo.DepartmentVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import com.hdvon.nmp.entity.Department;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

import java.util.List;
import java.util.Map;

public interface DepartmentMapper extends Mapper<Department> , MySqlMapper<Department>{
    List<DepartmentVo> selectChildDepartmentsByPid(Map<String, Object> param);

    List<DepartmentVo> selectDepartMentList(String projectId);

    DepartmentVo selectDepartmentInfoById(@Param("id") String id);

	List<DepartmentVo> selectDepartmentByUserId(String userId);
	
	List<DepartmentVo> selectShowDeptTreeByPlanId(Map<String,Object> map);

	List<DepartmentVo> selectByParam(Map<String, Object> param);

	List<DepartmentVo> selectDepartmentUserCount(Map<String, Object> param);
}