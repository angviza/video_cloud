package com.hdvon.nmp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.mapper.EncodeserverMappingMapper;
import com.hdvon.nmp.service.IEncodeserverMappingService;

/**
 * <br>
 * <b>功能：</b>编码器关联中间表(t_encodeserver_mapping)Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class EncodeserverMappingServiceImpl implements IEncodeserverMappingService {

	@Autowired
	private EncodeserverMappingMapper encodeserverMappingMapper;

}
