package com.hdvon.nmp.mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.UserDepartmentData;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.DepartmentUserTreeVo;

import tk.mybatis.mapper.common.Mapper;

public interface UserDepartmentDataMapper extends Mapper<UserDepartmentData> , MySqlMapper<UserDepartmentData>{
	List<DepartmentUserTreeVo> getUserAuthorizeDeptTree(String userId);

//	List<DepartmentUserTreeVo> getPermissionPlanUserAuthorizeDeptTree(Map<String,Object> map);
	
//	List<DepartmentUserTreeVo> getResourceroleDeptUserTree(Map<String,Object> map);
	
	List<DepartmentUserTreeVo> getSysroleDeptUserTree(Map<String,Object> map);

//	List<DepartmentUserTreeVo> selectDepartmentUser(String userId);
}