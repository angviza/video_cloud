package com.hdvon.nmp.mapper;

import com.hdvon.nmp.vo.DepartmentProject;
import com.hdvon.nmp.vo.ProjectDepartmentVo;
import com.hdvon.nmp.vo.ProjectVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import com.hdvon.nmp.entity.Project;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

import java.util.List;
import java.util.Map;

public interface ProjectMapper extends Mapper<Project> , MySqlMapper<Project>{

    ProjectVo selectProjectInfoById(@Param("projectDepartmentVo") ProjectDepartmentVo projectDepartmentVo);

    List<ProjectVo> selectProjectList(Map<String, Object> map);

}