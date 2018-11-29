package com.hdvon.nmp.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.entity.UserPicture;
import com.hdvon.nmp.mapper.UserPictureMapper;
import com.hdvon.nmp.service.IUserPictureService;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.UserPictureVo;

import tk.mybatis.mapper.entity.Example;
/**
 * <br>
 * <b>功能：</b>用户头像管理Service<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class IUserPictureServiceImpl implements IUserPictureService{

	@Autowired
	private UserPictureMapper userPictureMapper;
	
	@Override
	public void save(UserPictureVo picture) {
		// TODO Auto-generated method stub
		UserPicture bean = new UserPicture();
		BeanUtils.copyProperties(picture, bean);
		
		UserPicture record = findRecord(picture.getUserId());
		if(record == null) {
			bean.setCreateTime(new Date());
			bean.setId(IdGenerator.nextId());
			
			userPictureMapper.insertSelective(bean);
		}else {
			bean.setId(record.getId());
			bean.setUpdateTime(new Date());
			
			userPictureMapper.updateByPrimaryKey(bean);
		}
		
		
	}

	@Override
	public UserPictureVo queryRecord(String userId) {
		// TODO Auto-generated method stub
		UserPicture picture = findRecord(userId);
		if(picture == null) {
			return null;
		}
		UserPictureVo vo = new UserPictureVo();
		BeanUtils.copyProperties(picture, vo);
		return vo;
	}

	private UserPicture findRecord(String userId) {
		// TODO Auto-generated method stub
		Example example = new Example(UserPicture.class);
		example.createCriteria().andEqualTo("userId", userId);
		List<UserPicture> list =userPictureMapper.selectByExample(example);
		if(list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}
}
