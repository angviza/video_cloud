package com.hdvon.nmp.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.mapper.*;
import com.hdvon.nmp.service.IUserSysmenuService;
import com.hdvon.nmp.entity.UserSysmenu;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.UserSysmenuVo;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <br>
 * <b>功能：</b>用户自定义菜单关联表Service<br>
 * <b>作者：</b>zhanqf<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
@Slf4j
public class UserSysmenuServiceImpl implements IUserSysmenuService {
	
	@Autowired
	private UserSysmenuMapper usMapper;

	@Override
	public UserSysmenuVo findByUseridAndMenuid(String userId, String menuId) {
		Example exm = new Example(UserSysmenu.class);
		exm.createCriteria()
			.andEqualTo("userId", userId)
			.andEqualTo("sysmenuId", menuId);
		UserSysmenu menu = this.usMapper.selectOneByExample(exm);
		if (menu == null) {
			return null;
		} // if
		UserSysmenuVo vo = new UserSysmenuVo();
		BeanUtils.copyProperties(menu, vo);
		return vo;
	}

	@Override
	public Boolean existByUseridAndMenuid(String userId, String menuId) {
		Example exm = new Example(UserSysmenu.class);
		exm.createCriteria()
			.andEqualTo("userId", userId)
			.andEqualTo("sysmenuId", menuId);
		return this.usMapper.selectCountByExample(exm) > 0;
	}

	@Override
	public Integer save(UserSysmenuVo vo) {
		UserSysmenu menu = new UserSysmenu();
		BeanUtils.copyProperties(vo, menu);
		menu.setId(IdGenerator.nextId());
		return this.usMapper.insert(menu);
	}

	@Override
	public Integer delete(String userId, String menuId) {
		Example exm = new Example(UserSysmenu.class);
		exm.createCriteria()
			.andEqualTo("userId", userId)
			.andEqualTo("sysmenuId", menuId);
		return this.usMapper.deleteByExample(exm);
	}

	@Override
	public Integer modify(UserSysmenuVo vo) {
		Example exm = new Example(UserSysmenu.class);
		exm.createCriteria()
			.andEqualTo("userId", vo.getUserId())
			.andEqualTo("sysmenuId", vo.getSysmenuId());
		UserSysmenu menu = this.usMapper.selectOneByExample(exm);
		if (menu == null) {
			return 0;
		} // if
		BeanUtils.copyProperties(vo, menu);
		return this.usMapper.updateByExample(menu, exm);
	}

	@Override
	public Integer deleteByUserid(String userId) {
		Example exm = new Example(UserSysmenu.class);
		exm.createCriteria()
			.andEqualTo("userId", userId);
		return this.usMapper.deleteByExample(exm);
	}
	
}
