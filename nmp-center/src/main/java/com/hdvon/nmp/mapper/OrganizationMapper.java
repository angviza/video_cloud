package com.hdvon.nmp.mapper;

import com.hdvon.nmp.vo.OrganizationVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import com.hdvon.nmp.entity.Organization;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

import java.util.List;
import java.util.Map;

public interface OrganizationMapper extends Mapper<Organization> , MySqlMapper<Organization>{

    List<OrganizationVo> selectChildOrgsByPid(Map<String, Object> map);

    List<OrganizationVo> selectParentOrgsByPid(@Param("id") String id);

    List<OrganizationVo> selectOrganizationList(Map<String, Object> map);

    OrganizationVo selectOrganizationById(@Param("id") String id);
    
    OrganizationVo selectOrgById(@Param("id") String id);

	List<OrganizationVo> selectByParam(Map<String, Object> param);
}