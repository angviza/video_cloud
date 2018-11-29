package com.hdvon.nmp.mapper;

import com.hdvon.nmp.vo.SysroleVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import com.hdvon.nmp.entity.Sysrole;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

import java.util.List;
import java.util.Map;

public interface SysroleMapper extends Mapper<Sysrole> , MySqlMapper<Sysrole>{

    List<SysroleVo> querySysRolesByUserId(@Param("userIds") List<String> userIds);

    List<SysroleVo> querySysRoles(Map<String, Object> param);
    
    List<SysroleVo> selectSysrolesInPermissionPlan(Map<String, Object> map);
    
    List<SysroleVo> getUserAuthorizeSysRoleTree(@Param("userId") String userId);

	List<SysroleVo> selectRoleUserCount(Map<String, Object> param);
}