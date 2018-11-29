package com.hdvon.nmp.mapper;

import com.hdvon.nmp.entity.EncoderServer;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.EncoderServerVo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface EncoderServerMapper extends Mapper<EncoderServer> , MySqlMapper<EncoderServer>{

    List<EncoderServerVo> selectEncoderServerList(Map<String, Object> paramMap);
    
	String selectMaxCodeByParam(Map<String, Object> map);
}