package com.hdvon.nmp.mapper;

import com.hdvon.nmp.vo.ResourceroleVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import com.hdvon.nmp.entity.Resourcerole;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

import java.util.List;
import java.util.Map;

public interface ResourceroleMapper extends Mapper<Resourcerole> , MySqlMapper<Resourcerole>{

//    List<ResourceroleVo> queryResourceRolesByUserId(@Param("userIds") List<String> userIds);

//    List<ResourceroleVo> queryResourceRoles(Map<String, Object> param);
    
//	List<ResourceroleVo> selectResourcerolesInPermissionPlan(Map<String, Object> map);
	
//	List<ResourceroleVo> selectResourcerolesByPermissionPlanId(String permissionPlanId);

	List<ResourceroleVo> selectByParam(Map<String, Object> param);

	List<String> selectUserIdsByParam(Map<String, Object> param);

	List<ResourceroleVo> selectResourceByParam(Map<String, Object> param);
	
//	ResourceroleVo selectResourceroleInfoById(@Param("id")String id);
	
//	String queryParentName(@Param("pid")String pid);
}