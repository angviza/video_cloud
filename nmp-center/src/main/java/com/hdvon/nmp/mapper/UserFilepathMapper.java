package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.UserFilepath;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.UserFilepathVo;

public interface UserFilepathMapper extends Mapper<UserFilepath> , MySqlMapper<UserFilepath>{

	List<UserFilepathVo> selectByParam(Map<String, String> map);

}