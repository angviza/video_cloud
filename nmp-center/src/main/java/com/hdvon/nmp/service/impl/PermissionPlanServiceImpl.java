package com.hdvon.nmp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.common.WebConstant;
import com.hdvon.nmp.entity.PermissionPlan;
import com.hdvon.nmp.entity.PermissionplanInfoEl;
import com.hdvon.nmp.entity.PermissionplanResourcerole;
import com.hdvon.nmp.entity.PermissionplanSysrole;
import com.hdvon.nmp.entity.PermissionplanUser;
import com.hdvon.nmp.entity.WallPlan;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.PermissionPlanMapper;
import com.hdvon.nmp.mapper.PermissionplanInfoElMapper;
import com.hdvon.nmp.mapper.PermissionplanResourceroleMapper;
import com.hdvon.nmp.mapper.PermissionplanSysroleMapper;
import com.hdvon.nmp.mapper.PermissionplanUserMapper;
import com.hdvon.nmp.mapper.ResourceroleMapper;
import com.hdvon.nmp.mapper.SysroleMapper;
import com.hdvon.nmp.mapper.UserDepartmentDataMapper;
import com.hdvon.nmp.mapper.UserMapper;
import com.hdvon.nmp.service.IPermissionPlanService;
import com.hdvon.nmp.util.BeanHelper;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.DepartmentUserTreeVo;
import com.hdvon.nmp.vo.DictionaryTypeVo;
import com.hdvon.nmp.vo.PermissionPlanParamVo;
import com.hdvon.nmp.vo.PermissionPlanVo;
import com.hdvon.nmp.vo.ResourceroleVo;
import com.hdvon.nmp.vo.SysroleVo;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>权限预案Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class PermissionPlanServiceImpl implements IPermissionPlanService {

	@Autowired
	private PermissionPlanMapper permissionPlanMapper;
	
	@Autowired
	private PermissionplanUserMapper permissionplanUserMapper;
	
	@Autowired
	private PermissionplanSysroleMapper permissionplanSysroleMapper;
	
	@Autowired
	private PermissionplanResourceroleMapper permissionplanResourceroleMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private SysroleMapper sysroleMapper;
	
	@Autowired
	private ResourceroleMapper resourceroleMapper;
	
	@Autowired
	private UserDepartmentDataMapper userDepartmentDataMapper;
	
	@Autowired
	private PermissionplanInfoElMapper permissionplanInfoElMapper;

	@Override
	public void savePermissionPlan(UserVo userVo, PermissionPlanParamVo permissionPlanParamVo) {
		PermissionPlan permissionPlan = Convert.convert(PermissionPlan.class,permissionPlanParamVo);
		if(StrUtil.isNotBlank(permissionPlanParamVo.getId())) {//修改
			Example we = new Example(WallPlan.class);
			we.createCriteria().andEqualTo("name", permissionPlanParamVo.getName()).andNotEqualTo("id", permissionPlanParamVo.getId());
			int countName = permissionPlanMapper.selectCountByExample(we);
			if(countName > 0) {
				throw new ServiceException("预案名称已经存在！");
			}
			
			permissionPlan.setUpdateTime(new Date());
			permissionPlan.setUpdateUser(userVo.getAccount());
			
			permissionPlanMapper.updateByPrimaryKeySelective(permissionPlan);//修改预案信息
			
		}else {//新增
			Example ppe = new Example(PermissionPlan.class);
			ppe.createCriteria().andEqualTo("name", permissionPlanParamVo.getName());
			int countName = permissionPlanMapper.selectCountByExample(ppe);
			if(countName > 0) {
				throw new ServiceException("预案名称已经存在！");
			}
			if("1".equals(permissionPlanParamVo.getType())) {
				permissionPlan.setUserStatus(9);
			}
			permissionPlan.setId(IdGenerator.nextId());
			permissionPlan.setCreateTime(new Date());
			permissionPlan.setCreateUser(userVo.getAccount());
			permissionPlan.setUpdateTime(new Date());
			permissionPlan.setUpdateUser(userVo.getAccount());
			permissionPlan.setDeleteFlag(WebConstant.FIELD_DETELEFLAG_NO);
			
			permissionPlanMapper.insertSelective(permissionPlan);//保存权限预案
		}
		
	}

	@Override
	public PageInfo<PermissionPlanVo> getPermissionPlanPages(PageParam pp, Map<String, Object> map) {
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
	/*	Example e = new Example(PermissionPlan.class);
		Example.Criteria criteria = e.createCriteria();
		if(map.get("id")!=null) {
			criteria.andEqualTo("id", map.get("id").toString());
		}
		if(map.get("name") != null) {
			criteria.andLike("name", "%"+map.get("name").toString()+"%");
		}
		if(map.get("status") != null) {
			criteria.andEqualTo("status", map.get("status").toString());
		}
		if(map.get("userStatus") != null) {
			criteria.andEqualTo("userStatus", map.get("userStatus").toString());
		}
		if(map.get("type") != null) {
			if("1".equals(map.get("type"))) {
				criteria.andEqualTo("userStatus", 9);
			}else if("2".equals(map.get("type"))) {
				criteria.andNotEqualTo("userStatus", 9);
			}
		}
		List<PermissionPlan> permissionPlans = permissionPlanMapper.selectByExample(e);
		PageInfo<PermissionPlan> entityPageInfo = new PageInfo<PermissionPlan>(permissionPlans);
		int entityTotal = (int) entityPageInfo.getTotal();
		int entityPageSize = entityPageInfo.getPageSize();
		int entityPageNum = entityPageInfo.getPageNum();
		
		List<PermissionPlanVo> permissionPlanVos = BeanHelper.convertList(PermissionPlanVo.class, permissionPlans);
		PageInfo<PermissionPlanVo> voPageInfo = new PageInfo<PermissionPlanVo>(permissionPlanVos);
		voPageInfo.setTotal(entityTotal);
		voPageInfo.setPageSize(entityPageSize);
		voPageInfo.setPageNum(entityPageNum);
		*/
		List<PermissionPlanVo> voPageInfo=permissionPlanMapper.selectByParam(map);
		return new PageInfo<PermissionPlanVo>(voPageInfo);
	}

	@Override
	public List<PermissionPlanVo> getPermissionPlanList(Map<String, Object> map) {
		/*Example e = new Example(PermissionPlan.class);
		Example.Criteria criteria = e.createCriteria();
		if(map.get("id")!=null) {
			criteria.andEqualTo("id", map.get("id").toString());
		}
		if(map.get("name") != null) {
			criteria.andLike("name", "%"+map.get("name").toString()+"%");
		}
		if(map.get("status") != null) {
			criteria.andEqualTo("status", map.get("status").toString());
		}
		List<PermissionPlan> permissionPlans = permissionPlanMapper.selectByExample(e);
    	return BeanHelper.convertList(PermissionPlanVo.class, permissionPlans);
    	*/
		List<PermissionPlanVo> permissionPlans=permissionPlanMapper.selectByParam(map);
		return permissionPlans;
	}
	

	@Override
	public void delPermissionPlansByIds(List<String> ids) {
		Example ppeStatus = new Example(PermissionPlan.class);
		ppeStatus.createCriteria().andIn("id", ids).andEqualTo("status", "1");
		int countStatus = permissionPlanMapper.selectCountByExample(ppeStatus);
		if(countStatus>0) {
			throw new ServiceException("启用的预案不能删除！");
		}
		
		Example ppe = new Example(WallPlan.class);
		ppe.createCriteria().andIn("id", ids);
		permissionPlanMapper.deleteByExample(ppe);//删除权限预案
		
		Example ppu = new Example(PermissionplanUser.class);
		ppu.createCriteria().andIn("perssionplanId",ids);
		permissionplanUserMapper.deleteByExample(ppu);//删除权限预案与用户的关联
		
		Example pps = new Example(PermissionplanSysrole.class);
		pps.createCriteria().andIn("id", ids);
		permissionplanSysroleMapper.deleteByExample(pps);//删除权限预案与系统角色的关联
		
		Example ppr = new Example(PermissionplanResourcerole.class);
		pps.createCriteria().andIn("id", ids);
		permissionplanSysroleMapper.deleteByExample(ppr);//删除权限预案与资源角色的关联
		
//		Example el = new Example(PermissionPlan.class);
//		el.createCriteria().andIn("id", ids);
//		permissionPlanMapper.deleteByExample(el);
	}

	@Override
	public PermissionPlanVo getPermissionPlanById(String id) {
		PermissionPlan permissionPlan = permissionPlanMapper.selectByPrimaryKey(id);
		PermissionPlanVo permissionPlanVo = Convert.convert(PermissionPlanVo.class,permissionPlan);
		return permissionPlanVo;
	}

	@Override
	public void saveRelateUsers(String permissionPlanId, List<String> userIdList) {
		Example e = new Example(PermissionplanUser.class);
		e.createCriteria().andEqualTo("perssionplanId", permissionPlanId);
		permissionplanUserMapper.deleteByExample(e);//删除修改前权限预案与用户的关联
		
		List<PermissionplanUser> list = new ArrayList<PermissionplanUser>();
		for(String userId : userIdList) {
			if(StrUtil.isNotBlank(userId)) {
				PermissionplanUser ppu = new PermissionplanUser();
				ppu.setId(IdGenerator.nextId());
				ppu.setPerssionplanId(permissionPlanId);
				ppu.setUserId(userId);
				list.add(ppu);
			}
		}
		if(! list.isEmpty()) {
			permissionplanUserMapper.insertList(list);//添加修改后权限预案与用户的关联
		}
		
	}

	@Override
	public void saveRelateSysroles(String permissionPlanId, List<String> sysroleIdList) {
		Example e = new Example(PermissionplanSysrole.class);
		e.createCriteria().andEqualTo("permissionplanId", permissionPlanId);
		permissionplanSysroleMapper.deleteByExample(e);//删除修改前权限预案与系统角色的关联
		
		List<PermissionplanSysrole> list = new ArrayList<PermissionplanSysrole>();
		for(String sysroleId : sysroleIdList) {
			PermissionplanSysrole pps = new PermissionplanSysrole();
			pps.setId(IdGenerator.nextId());
			pps.setPermissionplanId(permissionPlanId);
			pps.setSysroleId(sysroleId);
		}
		permissionplanSysroleMapper.insertList(list);
	}

	@Override
	public void saveRelateResourceroles(String permissionPlanId, List<String> resourceroleIdList) {
		Example e = new Example(PermissionplanResourcerole.class);
		e.createCriteria().andEqualTo("permissionplanId", permissionPlanId);
		permissionplanResourceroleMapper.deleteByExample(e);//删除修改前权限预案与用户的关联
		
		List<PermissionplanResourcerole> list = new ArrayList<PermissionplanResourcerole>();
		for(String resourceroleId : resourceroleIdList) {
			if(StrUtil.isNotBlank(resourceroleId)) {
				PermissionplanResourcerole ppr = new PermissionplanResourcerole();
				ppr.setId(IdGenerator.nextId());
				ppr.setPermissionplanId(permissionPlanId);
				ppr.setResourceroleId(resourceroleId);
				list.add(ppr);
			}
		}
		if(! list.isEmpty()) {
			permissionplanResourceroleMapper.insertList(list);
		}
	}

	@Override
	public List<UserVo> getRelateUsers(Map<String, Object> map) {
		List<UserVo> userVos = userMapper.selectUsersInPermissionPlan(map);
		return userVos;
	}

	@Override
	public List<SysroleVo> getRelateSysroles(Map<String, Object> map) {
		List<SysroleVo> sysroleVos = sysroleMapper.selectSysrolesInPermissionPlan(map);
		return sysroleVos;
	}

	@Override
	public List<ResourceroleVo> selectResourcerolesByPermissionPlanId(UserVo userVo,String permissionPlanId) {
	    Map<String,Object> map = new HashMap<>();
	    map.put("permissionplanId",permissionPlanId);
		List<ResourceroleVo> resourceroleVos = resourceroleMapper.selectByParam(map);
		return resourceroleVos;
	}

	@Override
	public List<UserVo> getRelateUsersByPermissionPlanId(UserVo userVo,String permissionplanId) {
        Map<String, Object> map = new HashMap<>();
        map.put("permissionplanId",permissionplanId);
		List<UserVo> userList = userMapper.selectUsersByParam(map);
		return userList;
	}

}
