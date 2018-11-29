package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;
import com.hdvon.nmp.entity.DevicecodeCode;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

public interface DevicecodeCodeMapper extends Mapper<DevicecodeCode> , MySqlMapper<DevicecodeCode>{

	String selectMaxCodeBycode(String baseCode);

}