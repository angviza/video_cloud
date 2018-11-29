package com.hdvon.nmp.mapper;

import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.SipLog;
import com.hdvon.nmp.mybatisExt.MySqlMapper;
import com.hdvon.nmp.vo.sip.SipLogVo;

import tk.mybatis.mapper.common.Mapper;

public interface SipLogMapper extends Mapper<SipLog> , MySqlMapper<SipLog> {

	List<SipLogVo> selectByParam(Map<String, String> param);

}