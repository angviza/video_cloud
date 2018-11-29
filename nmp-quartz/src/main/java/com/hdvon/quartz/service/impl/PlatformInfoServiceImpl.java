package com.hdvon.quartz.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.quartz.entity.PlatformInfo;
import com.hdvon.quartz.mapper.PlatformInfoMapper;
import com.hdvon.quartz.service.IPlatformInfoService;
import com.hdvon.quartz.vo.PlatformInfoVo;
import cn.hutool.core.convert.Convert;

/**
 * <br>
 * <b>功能：</b>平台使用情况服务实现类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class PlatformInfoServiceImpl implements IPlatformInfoService {

	@Autowired
	private PlatformInfoMapper platformInfoMapper;

	@Override
	public List<Map<String, Object>> getPlatInfoUseList(Map<String, Object> map) {
		return platformInfoMapper.selectPlatInfoUseList(map);
	}

	@Override
	public void batchInsertList(List<PlatformInfoVo> list) {
		
		List<PlatformInfo> infos = new ArrayList<PlatformInfo>();
		
		for (PlatformInfoVo vo : list) {
			
			PlatformInfo info = Convert.convert(PlatformInfo.class, vo);
			infos.add(info);
		}
			
		platformInfoMapper.insertList(infos);
		
	}

}
