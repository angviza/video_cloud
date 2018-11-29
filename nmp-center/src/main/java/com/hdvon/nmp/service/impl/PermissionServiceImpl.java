package com.hdvon.nmp.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.entity.Permission;
import com.hdvon.nmp.mapper.PermissionMapper;
import com.hdvon.nmp.service.IPermissionService;
import com.hdvon.nmp.util.BeanHelper;
import com.hdvon.nmp.vo.PermissionVo;

import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>操作权限表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class PermissionServiceImpl implements IPermissionService {

	@Autowired
	private PermissionMapper permissionMapper;

	@Override
	public List<PermissionVo> getPermissionList() {
		/*Example example = new Example(Permission.class);
		example.setOrderByClause("value");
		List<Permission> permission = permissionMapper.selectByExample(example);
		List<PermissionVo> list = BeanHelper.convertList(PermissionVo.class, permission);
		*/
        Map<String,Object> param = new HashMap<String, Object>();  
		List<PermissionVo> list=permissionMapper.selectByParam(param);
		return list;
	}

}
