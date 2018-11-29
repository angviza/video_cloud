package com.hdvon.nmp.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.DevicecodeCode;
import com.hdvon.nmp.entity.DevicecodeOption;
import com.hdvon.nmp.mapper.DevicecodeCodeMapper;
import com.hdvon.nmp.mapper.DevicecodeOptionMapper;
import com.hdvon.nmp.service.IDevicecodeOptionService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.DevicecodeOptionVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.convert.Convert;

/**
 * <br>
 * <b>功能：</b>设备编码生成器选项Service<br>
 * <b>作者：</b>huanggx<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class DevicecodeOptionServiceImpl implements IDevicecodeOptionService {

	@Autowired
	private DevicecodeOptionMapper devicecodeOptionMapper;
	@Autowired
	private DevicecodeCodeMapper devicecodeCodeMapper;
	

	@Override
	public void saveDeviceCode(List<BigDecimal> list, DevicecodeOptionVo param,UserVo userVo,String isUser) {
		DevicecodeOption option = Convert.convert(DevicecodeOption.class,param);
		String optionId=IdGenerator.nextId();
		option.setId(optionId);
		option.setArea(param.getArea());
		option.setCreateUser(userVo.getAccount());
		option.setCreateTime(new Date());
		
		List<DevicecodeCode> deviceCode=new ArrayList<DevicecodeCode>();
		for(BigDecimal code:list) {
			DevicecodeCode device =new DevicecodeCode();
			device.setId(IdGenerator.nextId());
			device.setDevicecodeOptionId(optionId);
			device.setDeviceCode(code.toString());
			if("1".equals(isUser)) {
				device.setStatus(1);//使用状态
			}else {
				device.setStatus(0);//未使用状态
			}
			
			deviceCode.add(device);
		}
		
		devicecodeOptionMapper.insert(option);
		devicecodeCodeMapper.insertList(deviceCode);
		
	}


	@Override
	public PageInfo<DevicecodeOptionVo> getOptionPage(PageParam pageParam, Map<String, String> param) {
		 PageHelper.startPage(pageParam.getPageNo(),pageParam.getPageSize());
		 List<DevicecodeOptionVo> pageList=devicecodeOptionMapper.selectByParam(param);
		 return new PageInfo<>(pageList);
	}


	@Override
	public DevicecodeOptionVo getInfo(String devieCodeId) {
		 Map<String,String> param=new HashMap<String,String>();
		 param.put("devieCodeId", devieCodeId);
		 List<DevicecodeOptionVo> pageList=devicecodeOptionMapper.selectByParam(param);
		 if(pageList.size()>0) {
			 return pageList.get(0); 
		 }
		return null;
	}

}
