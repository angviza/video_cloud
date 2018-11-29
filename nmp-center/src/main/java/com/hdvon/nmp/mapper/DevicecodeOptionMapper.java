package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.DevicecodeOption;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.DevicecodeOptionVo;

public interface DevicecodeOptionMapper extends Mapper<DevicecodeOption> , MySqlMapper<DevicecodeOption>{

	List<DevicecodeOptionVo> selectByParam(Map<String, String> param);

}