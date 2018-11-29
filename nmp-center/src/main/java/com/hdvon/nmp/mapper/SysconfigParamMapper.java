package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.SysconfigParam;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.SysconfigParamVo;

public interface SysconfigParamMapper extends Mapper<SysconfigParam> , MySqlMapper<SysconfigParam>{

	List<SysconfigParamVo> selectByParam(Map<String, Object> param);

}