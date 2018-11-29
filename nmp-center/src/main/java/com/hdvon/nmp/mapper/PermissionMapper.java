package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.Permission;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.PermissionVo;

public interface PermissionMapper extends Mapper<Permission> , MySqlMapper<Permission>{

    Integer selectSumValue();

	List<PermissionVo> selectByParam(Map<String, Object> param);
}