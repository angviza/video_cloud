package com.hdvon.nmp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.entity.Address;
import com.hdvon.nmp.entity.DevicecodeCode;
import com.hdvon.nmp.mapper.DevicecodeCodeMapper;
import com.hdvon.nmp.service.IDevicecodeCodeService;
import com.hdvon.nmp.vo.DevicecodeOptionVo;

import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>设备编码器生成编码Service<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class DevicecodeCodeServiceImpl implements IDevicecodeCodeService {

	@Autowired
	private DevicecodeCodeMapper devicecodeCodeMapper;

	@Override
	public String getMaxCodeBycode(String baseCode) {
		return devicecodeCodeMapper.selectMaxCodeBycode(baseCode);
	}

	@Override
	public void deleteCode(List<String> deviceCodeList) {
		Example example = new Example(DevicecodeCode.class);
		example.createCriteria().andIn("id",deviceCodeList);
        devicecodeCodeMapper.deleteByExample(example);
		
	}


}
