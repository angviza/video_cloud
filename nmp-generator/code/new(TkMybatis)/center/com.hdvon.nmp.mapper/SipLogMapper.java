package com.hdvon.nmp.mapper;

import tk.mybatis.mapper.common.Mapper;
import com.hdvon.nmp.entity.SipLog;
import com.hdvon.nmp.mybatisExt.MySqlMapper;

public interface SipLogMapper extends Mapper<SipLog> , MySqlMapper<SipLog>{

}