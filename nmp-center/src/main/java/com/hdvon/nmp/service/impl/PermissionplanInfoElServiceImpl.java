package com.hdvon.nmp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.entity.PermissionPlan;
import com.hdvon.nmp.entity.PermissionplanInfoEl;
import com.hdvon.nmp.mapper.PermissionPlanMapper;
import com.hdvon.nmp.mapper.PermissionplanInfoElMapper;
import com.hdvon.nmp.service.IPermissionplanInfoElService;
import com.hdvon.nmp.util.snowflake.IdGenerator;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>权限预案信息表Service<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class PermissionplanInfoElServiceImpl implements IPermissionplanInfoElService {

	@Autowired
	private PermissionplanInfoElMapper permissionplanInfoElMapper;
	
	@Autowired
	private PermissionPlanMapper permissionPlanMapper;

	@Override
	public void deletePlanInfoElsByParam(Map<String, Object> map) {
		Example elExa = new Example(PermissionplanInfoEl.class);
		if(map.get("permissionplanId") != null && StrUtil.isNotBlank(map.get("permissionplanId").toString())) {
			elExa.createCriteria().andEqualTo("planId", map.get("permissionplanId").toString());
		}
		permissionplanInfoElMapper.deleteByExample(elExa);
	}

	@Override
	public void savePlanInfoEls(String permissionplanId, List<String> resourceroleIdList, List<String> userIdList) {
		PermissionPlan permissionplan = permissionPlanMapper.selectByPrimaryKey(permissionplanId);
		
		List<PermissionplanInfoEl> list = new ArrayList<>();
		for(String roleId : resourceroleIdList) {
			for(String userId : userIdList) {
				PermissionplanInfoEl permissionplanInfoEl = new PermissionplanInfoEl();
				permissionplanInfoEl.setId(IdGenerator.nextId());
				permissionplanInfoEl.setPlanId(permissionplanId);
				permissionplanInfoEl.setResourceroleId(roleId);
				permissionplanInfoEl.setUserId(userId);
				permissionplanInfoEl.setStatus(permissionplan.getStatus());
				permissionplanInfoEl.setPlanType(permissionplan.getUserStatus());
				permissionplanInfoEl.setBgnTime(permissionplan.getBgnTime());
				permissionplanInfoEl.setEndTime(permissionplan.getEndTime());
				list.add(permissionplanInfoEl);
			}
		}
		permissionplanInfoElMapper.insertList(list);
	}

}
