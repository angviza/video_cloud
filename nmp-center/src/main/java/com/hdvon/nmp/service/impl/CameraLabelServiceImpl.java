package com.hdvon.nmp.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.entity.Address;
import com.hdvon.nmp.entity.CameraLabel;
import com.hdvon.nmp.mapper.CameraLabelMapper;
import com.hdvon.nmp.mapper.CameraMapper;
import com.hdvon.nmp.service.ICameraLabelService;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.CameraLabelVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>摄像头录像标签表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class CameraLabelServiceImpl implements ICameraLabelService {

	@Autowired
	private CameraLabelMapper cameraLabelMapper;
	@Autowired
	private CameraMapper cameraMapper;

	@Override
	public void saveCameraLabel(UserVo user, CameraLabelVo vo) {
		CameraLabel label=Convert.convert(CameraLabel.class ,vo);

		label.setStartTime(new Date(Long.valueOf(vo.getStartTime()+"000")));
		label.setEndTime(new Date(Long.valueOf(vo.getEndTime()+"000")));
		if(StrUtil.isBlank(vo.getId())) {
			label.setId(IdGenerator.nextId());
			label.setCreateTime(new Date());
			label.setCreateUser(user.getAccount());
			cameraLabelMapper.insert(label);
		}else {
			label.setUpdateTime(new Date());
			label.setUpdateUser(user.getAccount());
			cameraLabelMapper.updateByPrimaryKeySelective(label);
		}
		
	}

	@Override
	public List<CameraLabelVo> getCameraLabel(Map<String, Object> map) {
		return cameraLabelMapper.selectCameraLabel(map);
	}

	@Override
	public void deleteByIds(List<String> ids) {
		 Example example = new Example(CameraLabel.class);
		 example.createCriteria().andIn("id",ids);
	     cameraLabelMapper.deleteByExample(example);
	}


}
