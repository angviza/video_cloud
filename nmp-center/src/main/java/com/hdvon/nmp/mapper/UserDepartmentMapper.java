package com.hdvon.nmp.mapper;

import com.hdvon.nmp.vo.UserDepartmentVo;
import tk.mybatis.mapper.common.Mapper;
import com.hdvon.nmp.entity.UserDepartment;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

import java.util.List;
import java.util.Map;

public interface UserDepartmentMapper extends Mapper<UserDepartment> , MySqlMapper<UserDepartment>{
    List<UserDepartmentVo> selectDepartUser(Map<String, Object> param);
}