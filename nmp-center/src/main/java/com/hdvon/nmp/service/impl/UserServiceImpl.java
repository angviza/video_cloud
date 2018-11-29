package com.hdvon.nmp.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.common.WebConstant;
import com.hdvon.nmp.config.redis.BaseRedisDao;
import com.hdvon.nmp.dto.UserDto;
import com.hdvon.nmp.entity.*;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.*;
import com.hdvon.nmp.service.IUserService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <br>
 * <b>功能：</b>用户表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserMapper userMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

	@Autowired
	private UserDepartmentMapper userDepartmentMapper;

	@Autowired
	private UserResourceroleMapper userResourceroleMapper;

	@Autowired
	private UserSysroleMapper userSysroleMapper;

	@Autowired
	private ResourceroleMapper resourceroleMapper;

	@Autowired
	private SysroleMapper sysroleMapper;

    @Autowired
    private SysmenuMapper sysmenuMapper;
    
    @Autowired
    private UserSysmenuMapper userSysmenuMapper;
    
    @Autowired
	private UserDepartmentDataMapper userDepartmentDataMapper;
    
    @Resource
	BaseRedisDao redisDao;

	@Override
	public void saveUser(UserVo loginUser, UserParamVo userParamVo){
		User user = Convert.convert(User.class,userParamVo);

		if(StrUtil.isBlank(user.getId())) {//添加用户
            if(StrUtil.isBlank(userParamVo.getId())) {
                Example userExample = new Example(User.class);
                userExample.createCriteria().andEqualTo("account", user.getAccount()).andNotEqualTo("enable", -1);//是否存在该账户名的有效用户
                List<User> users = userMapper.selectByExample(userExample);
                if(users != null && users.size() > 0) {
                    throw new ServiceException("用户账号已经存在!");
                }
            }
			user.setId(IdGenerator.nextId());
			Date date = new Date();
			user.setCreateTime(date);
			user.setCreateUser(loginUser.getAccount());
			user.setUpdateTime(date);
			user.setUpdateUser(loginUser.getAccount());
			user.setEnable(userParamVo.getEnable());
			user.setIsPkiUser(0);
            user.setIsUserModify("0");
            user.setIsResourceAssigned("0");
            user.setIsRoleGranted("0");
			userMapper.insertSelective(user);
		}else {//修改用户信息
			//权限判断
			if(!loginUser.isAdmin()){
				if(!loginUser.getId().equals(user.getId()) && user.getLevel().intValue() <= loginUser.getLevel().intValue()){
					throw new ServiceException("您优先级不够，无法修改该用户:"+user.getName());
				}
			}

            user.setUpdateTime(new Date());
			user.setUpdateUser(loginUser.getAccount());
            user.setIsUserModify("1");
			userMapper.updateByPrimaryKeySelective(user);
		}
        //添加用户部门关联
		Example udExample = new Example(UserDepartment.class);
		udExample.createCriteria().andEqualTo("userId", user.getId());
		int count = userDepartmentMapper.selectCountByExample(udExample);

        UserDepartment userDepartment = new UserDepartment();
        userDepartment.setUserId(user.getId());
        userDepartment.setDepId(userParamVo.getDepartmentId());
		if(count == 0 ){//添加用户组织机构关联
            userDepartment.setId(IdGenerator.nextId());
            userDepartmentMapper.insertSelective(userDepartment);
        }else{//修改组织机构关联
            Example userDepartmentExample = new Example(UserDepartment.class);
            userDepartmentExample.createCriteria().andEqualTo("userId", user.getId());
            userDepartmentMapper.updateByExampleSelective(userDepartment, userDepartmentExample);
        }

	}

	@Override
	public void updateUserStatus(UserVo loginUser, UserVo userVo){
        if(userVo != null){
            User user = new User();
            if(StrUtil.isNotEmpty(userVo.getId()) && userVo.getEnable() != null){
                user.setId(userVo.getId());
                user.setEnable(userVo.getEnable());
                user.setUpdateTime(new Date());
                user.setUpdateUser(loginUser.getAccount());
                userMapper.updateByPrimaryKeySelective(user);
            }
        }
    }

	@Override
	public void delUsers(UserVo loginUserVo , List<String> ids) {
        //删除用户
        Example ue = new Example(User.class);
        ue.createCriteria().andIn("id", ids);
		//权限判断
		if(!loginUserVo.isAdmin()){
			List<User> userlist = userMapper.selectByExample(ue);
			validate(loginUserVo,userlist);
		}

        //删除逻辑使用假删除，怕影响真实数据，防止一些误删或者非法删除，方便恢复
        User user = new User();
        user.setEnable(-1);
        userMapper.updateByExampleSelective(user,ue);

        log.info("用户["+loginUserVo.getAccount()+"]删除了用户列表："+ids.toString());
	}

	@Override
	public UserVo getUserInfoById(String id) {
	    Map<String,Object> param = new HashMap<>();
	    if(StrUtil.isBlank(id)){
	       throw new ServiceException("用户id不允许为空");
        }
	    param.put("userId",id);
		List<UserVo> list = userMapper.selectUsersByParam(param);
        if(list.size() > 0){
            return list.get(0);
        }
		return null;
	}

    @Override
    public List<UserVo> getUsersByParam(UserDto userDto){
        Map<String,Object> param = new HashMap<>();
        param.put("sysroleId",userDto.getSysroleId());
        param.put("resourceRoleId",userDto.getResourceRoleId());
        param.put("account", userDto.getAccount());
        param.put("name", userDto.getName());
        param.put("departmentId",userDto.getDepartmentId());
        param.put("userIds",userDto.getUserIds());
        List<UserVo> list = userMapper.selectUsersByParam(param);
        return list;
    }

	@Override
    public PageInfo<UserVo> getUsersByPage(PageParam pp, TreeNodeChildren treeNodeChildren, UserDto userDto){
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
        Map<String,Object> param = new HashMap<>();
        param.put("sysroleId",userDto.getSysroleId());
        param.put("resourceRoleId",userDto.getResourceRoleId());
        param.put("account", userDto.getAccount());
        param.put("name", userDto.getName());
        if(treeNodeChildren != null) {
        	 param.put("deptIds",treeNodeChildren.getDeptNodeIds());
        }
        param.put("userIds",userDto.getUserIds());
        List<UserVo> list = userMapper.selectUsersByParam(param);
        return new PageInfo<>(list);
	}

	@Override
	public void updateUserPassword(String id , String password ) {
        User user = userMapper.selectByPrimaryKey(id);
        if(user == null) {
            throw new ServiceException("用户不存在");
        }
        User newUser = new User();
        newUser.setId(id);
        newUser.setPassword(password);
		userMapper.updateByPrimaryKeySelective(newUser);
	}

	@Override
	public void grantResourceRole(UserVo uservo,List<String> userIds, List<String> resourceRoles) {
		//权限判断
    	Example userExample = new Example(User.class);
		userExample.createCriteria().andIn("id", userIds);
		if(!uservo.isAdmin()){
			List<User> userlist = userMapper.selectByExample(userExample);
			validate(uservo,userlist);
		}

		Example delUre = new Example(UserResourcerole.class);
		delUre.createCriteria().andIn("userId",userIds);

		//查找用户关联的资源角色
		List<UserResourcerole> temp = userResourceroleMapper.selectByExample(delUre);
		List<String> tempResRoleIds = new ArrayList<>();
		for(UserResourcerole obj : temp){
			tempResRoleIds.add(obj.getResourceroleId());
		}

		//删除修改前的资源角色关联
		userResourceroleMapper.deleteByExample(delUre);

        //批量保存用户资源角色关联
		List<UserResourcerole> resourceroles = new ArrayList<>();
		if(resourceRoles != null){
			// 去重
			tempResRoleIds.removeAll(resourceRoles);
			tempResRoleIds.addAll(resourceRoles);

			for(String userId : userIds) {
				for(String roleId : resourceRoles) {
					if(StrUtil.isBlank(userId) || StrUtil.isBlank(roleId)){
						continue;
					}
					UserResourcerole resourceRole = new UserResourcerole();
					resourceRole.setId(IdGenerator.nextId());
					resourceRole.setUserId(userId.trim());
					resourceRole.setResourceroleId(roleId.trim());
					resourceroles.add(resourceRole);
				}
			}
		}
		if(resourceroles.size() > 0){
			//userResourceroleMapper.insertBatch(resourceroles);
			userResourceroleMapper.insertList(resourceroles);
		}

		// 更新资源角色授权状态
		for(String tempId : tempResRoleIds){
			delUre.clear();
			delUre.createCriteria().andEqualTo("resourceroleId",tempId);
			int tempCount= userResourceroleMapper.selectCountByExample(delUre);
			if(tempCount > 0){
				Resourcerole resourcerole = new Resourcerole();
				resourcerole.setIsMenuAssigned(1);
				resourcerole.setId(tempId);
				resourceroleMapper.updateByPrimaryKeySelective(resourcerole);
			} else {
				Resourcerole resourcerole = new Resourcerole();
				resourcerole.setIsMenuAssigned(0);
				resourcerole.setId(tempId);
				resourceroleMapper.updateByPrimaryKeySelective(resourcerole);
			}
		}

        //设置用户是否已授予资源角色
        User record = new User();
		if(resourceroles.size() == 0) {
            record.setIsResourceAssigned("0");
		}else {
            record.setIsResourceAssigned("1");
		}
		userMapper.updateByExampleSelective(record,userExample);
	}

	@Override
	public void grantSysRole(UserVo uservo,List<String> userIds, List<String> sysroleVos) {
		//权限判断
		Example userExample = new Example(User.class);
		userExample.createCriteria().andIn("id", userIds);
		if(!uservo.isAdmin()){
			List<User> userlist = userMapper.selectByExample(userExample);
			validate(uservo,userlist);
		}

		Example delUse = new Example(UserSysrole.class);
		delUse.createCriteria().andIn("userId", userIds);
		//查找用户关联的系统角色
		List<UserSysrole> temp = userSysroleMapper.selectByExample(delUse);
		List<String> tempRoleIds = new ArrayList<>();
		for(UserSysrole obj : temp){
			tempRoleIds.add(obj.getRoleId());
		}
		// 删除用户关联的系统角色记录
		userSysroleMapper.deleteByExample(delUse);

        //批量保存用户系统角色关联
		List<UserSysrole> sysRoles = new ArrayList<>();
		if(sysroleVos != null){
			// 去重
			tempRoleIds.removeAll(sysroleVos);
			tempRoleIds.addAll(sysroleVos);

			for(String userId : userIds) {
				for(String roleId : sysroleVos) {
					if(StrUtil.isBlank(userId) || StrUtil.isBlank(roleId)){
						continue;
					}
					UserSysrole sysRole = new UserSysrole();
					sysRole.setId(IdGenerator.nextId());
					sysRole.setUserId(userId.trim());
					sysRole.setRoleId(roleId.trim());
					sysRoles.add(sysRole);
				}
			}
		}
        if(sysRoles.size() > 0){
            //userSysroleMapper.insertBatch(sysRoles);
        	userSysroleMapper.insertList(sysRoles);
        }

		// 更新系统角色授权状态
		for(String tempId : tempRoleIds){
			delUse.clear();
			delUse.createCriteria().andEqualTo("roleId",tempId);
			int tempCount= userSysroleMapper.selectCountByExample(delUse);
			if(tempCount > 0){
				Sysrole sysrole = new Sysrole();
				sysrole.setIsMenuAssigned(1);
				sysrole.setId(tempId);
				sysroleMapper.updateByPrimaryKeySelective(sysrole);
			} else {
				Sysrole sysrole = new Sysrole();
				sysrole.setIsMenuAssigned(0);
				sysrole.setId(tempId);
				sysroleMapper.updateByPrimaryKeySelective(sysrole);
			}
		}

        //设置用户是否已授予系统角色
        User record = new User();
        if(sysRoles.size() == 0) {
            record.setIsRoleGranted("0");
        }else {
            record.setIsRoleGranted("1");
        }
		userMapper.updateByExampleSelective(record,userExample);
	}

	@Override
	public List<ResourceroleVo> getResourceRolesByUserId(List<String> userIds) {
	    Map<String,Object> map = new HashMap<>();
	    map.put("userIds",userIds);
		List<ResourceroleVo> resourceroleVos = resourceroleMapper.selectByParam(map);
		if(resourceroleVos != null){
			List<ResourceroleVo> templist = new ArrayList<>();
			//批量处理时去重角色对象
			resourceroleVos.stream().forEach(p -> {
				if (!templist.contains(p)) {
					templist.add(p);
				}
			});
			return templist;
		}
		return null;
	}

	@Override
	public List<SysroleVo> getSysrolesByUserId(List<String> userIds) {
		List<SysroleVo> sysroleVos = sysroleMapper.querySysRolesByUserId(userIds);
		if(sysroleVos != null){
			List<SysroleVo> templist = new ArrayList<>();
			//批量处理时去重角色对象
			sysroleVos.stream().forEach(p -> {
				if (!templist.contains(p)) {
					templist.add(p);
				}
			});
			return templist;
		}
		return null;
	}

	@Override
	public List<UserVo> getUserByAccount(String account) {
		Example ue = new Example(User.class);
		ue.createCriteria().andEqualTo("account", account);
		List<User> list = userMapper.selectByExample(ue);
		if(list == null || list.size()==0) {
			return Collections.EMPTY_LIST;
		}
		List<UserVo> userVos = new ArrayList<>();
		list.stream().forEach(user->{
			UserVo userVo = new UserVo();
			BeanUtils.copyProperties(user, userVo);
            //是否管理员，暂定为admin可看所有权限
            userVo.setAdmin(user.getAccount().toLowerCase().equals("admin"));
			userVos.add(userVo);
		});
		return userVos;
	}

    @Override
    public UserVo login(String username , String password) {
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            throw new ServiceException("账号密码不能为空");
        }

        // 自定义登录，获取用户详情
        Map<String, Object> param = new HashMap<>();
        param.put("account",username.trim());
        param.put("enable", -1);
        param.put("password",password.trim());
        List<UserVo> list = userMapper.login(param);

        if(list.size() == 0){
            throw new ServiceException("账号或密码错误");
        }
        UserVo login_user = list.get(0);
        if(login_user.getEnable() == null || login_user.getEnable().intValue() == 0){
            throw new ServiceException("该账号已停用，请联系管理员");
        }
        //更新上次登录时间
        User user = Convert.convert(User.class, login_user);
        user.setId(login_user.getId());
        user.setLastLoginDate(new Date());
        userMapper.updateByPrimaryKeySelective(user);

        // UserVo userVo = Convert.convert(UserVo.class, login_user);
        login_user.setAdmin(login_user.getAccount().toLowerCase().equals("admin"));//是否管理员，暂定为admin可看所有权限
        return login_user;
    }

	@Override
	public List<UserAddrCameraPermissionVo> getAddrCameraPermission(String userId) {
		return userMapper.selectAddrCameraPermission(userId);
	}

	@Override
	public List<SysmenuVo> optionMenu(String uid) {
        if (StrUtil.isBlank(uid)) {
            return null;
        } // if
        User user = userMapper.selectByPrimaryKey(uid);
        if (user == null) {
            return null;
        } // if
        Map<String,Object> param = new HashMap<>();
        param.put("userId",uid);
        // 管理员返回所有
        if (user.getAccount().toLowerCase().equals("admin")) {
            param.put("isAdmin","true");
        } // if
        // 用户有权限的系统菜单。
        List<SysmenuVo> voLst = sysmenuMapper.selectMenuByUserId(param);
        if ((voLst == null) ||
        		voLst.isEmpty()) {
        	return null;
        } // if
        // 信息整合。
		for (SysmenuVo vo: voLst) {
			Example exm = new Example(UserSysmenu.class);
			exm.createCriteria().andEqualTo("userId", uid).andEqualTo("sysmenuId", vo.getId());
			UserSysmenu us = this.userSysmenuMapper.selectOneByExample(exm);
			if (us != null) {
				vo.setPid(us.getSysmenuPid());
				vo.setName(us.getSysmenuName());
			} // if
		} // for
        return voLst;
	}

	@Override
	public void departmentToUser(UserVo uservo, List<String> userIdList, String[] departmentIds) {
    	//权限判断
		Example userExample= new Example(User.class);
		userExample.createCriteria().andIn("id", userIdList);
		if(!uservo.isAdmin()){
			List<User> userlist = userMapper.selectByExample(userExample);
			validate(uservo,userlist);
		}

		//清空用户可选部门信息
		Example example= new Example(UserDepartmentData.class);
		example.createCriteria().andIn("userId", userIdList);
		userDepartmentDataMapper.deleteByExample(example);
		ArrayList<UserDepartmentData> departList = new ArrayList<>();
		if(departmentIds.length>0) {
			//验证格式是否正确
	        for(String departmentId:departmentIds) {
	        	if(StrUtil.isBlank(departmentId)) {
	        		continue;
	        	}
	        	for(String userId :userIdList) {
	        		UserDepartmentData data =new UserDepartmentData();
	        		data.setId(IdGenerator.nextId());
		        	data.setDepartmentId(departmentId);
    				data.setUserId(userId);
    				departList.add(data);
    			}
	        }
	        //批量新增
	        if(departList.size() >0) {
	        	userDepartmentDataMapper.insertList(departList);
	        }
		}
	}

	/**
	 * 用户可管理部门树
	 */
	@Override
	public List<DepartmentVo> getDepartmentUser(String userId) {
	    Map<String,Object> map  = new HashMap<>();
	    if(userId == null){
	        userId = "";
        }
	    map.put("departmentUserId",userId);
		return departmentMapper.selectByParam(map);
	}
	/**
	 * 用户锁屏
	 * @param userId 用户id
	 * @param token token
	 * @return
	 */
	@Override
	public boolean lockScreen(String userId, String token) {
		// TODO Auto-generated method stub
		try {
			String key = WebConstant.LOCK_SCREEN_KEY + userId;
			redisDao.set(key, token, 1800L);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	/**
	 * 用户解锁
	 * @param userId 用户id
	 * @return
	 */
	@Override
	public boolean unLockScreen(String userId) {
		// TODO Auto-generated method stub
		try {
			String key = WebConstant.LOCK_SCREEN_KEY + userId;
			if(redisDao.exists(key)) {
				redisDao.remove(key);
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	@Override
	public void saveUserValidType(Integer type, UserVo user) {
		// TODO Auto-generated method stub
		
		//根据用户id获取用户信息
		User record = userMapper.selectByPrimaryKey(user.getId());
		if(record == null) {
			return;
		}
		record.setValidType(type);
		userMapper.updateByPrimaryKey(record);
		
	}

	@Override
	public PageInfo<UserVo> getStaticUser(Map<String, Object> param,PageParam pageParam) {
		PageHelper.startPage(pageParam.getPageNo(), pageParam.getPageSize());
        List<UserVo> list = userMapper.selectStaticUserByParam(param);
        return new PageInfo<>(list);
	}

	@Override
	public List<Map<String, Object>> exportStaticUser(Map<String, Object> param) {
		List<Map<String, Object>> listMap=new ArrayList<Map<String,Object>>();
		List<UserVo> list = userMapper.selectStaticUserByParam(param);
		for(UserVo vo :list) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("name", vo.getName());
			map.put("account", vo.getAccount());
			map.put("card_id", vo.getCardId());
			map.put("departmentName", vo.getDepartmentName());
			map.put("mobile_phone", vo.getMobilePhone());
			if(vo.getLastLoginDate() !=null) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				map.put("last_login_date", format.format(vo.getLastLoginDate()));
			}else {
				map.put("last_login_date", "");
			}
			map.put("lastLogin", vo.getLastLogin());
			listMap.add(map);
		}
		return listMap;
	}
	
	/**
	 * 部门用户详情
	 */
	@Override
	public PageInfo<UserVo> getUsersByDepart(PageParam pageParam, Map<String, Object> param) {
		PageHelper.startPage(pageParam.getPageNo(), pageParam.getPageSize());
        List<UserVo> list = userMapper.selectDepartmentUserCount(param);
        return new PageInfo<>(list);
	}
	
	/**
	 * 导出部门用户
	 */
	@Override
	public List<Map<String,Object>> exportDepartmentUser(Map<String, Object> param) {
		List<Map<String, Object>> listMap=new ArrayList<Map<String,Object>>(); 
		List<UserVo> list = userMapper.selectDepartmentUserCount(param);
		 for(UserVo vo :list) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("name", vo.getName());
			map.put("account", vo.getAccount());
			map.put("card_id", vo.getCardId());
			map.put("departmentName", vo.getDepartmentName());
			map.put("mobile_phone", vo.getMobilePhone());
			if(vo.getLastLoginDate() !=null) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				map.put("last_login_date", format.format(vo.getLastLoginDate()));
			}else {
				map.put("last_login_date", "");
			}
			map.put("lastLogin", vo.getLastLogin());
			listMap.add(map);
			}
		return listMap;
	}

	@Override
	public List<Map<String, Object>> exportRoleUser(Map<String, Object> param) {
		List<Map<String,Object>> list = userMapper.selectRoleUserCount(param);
		return list;
	}
	
	@Override
	public List<UserVo> getUsersByResourceroleId(String resouceroleId) {
		Map<String,Object> param = new HashMap<>();
        param.put("resourceRoleId",resouceroleId);
        List<UserVo> list = userMapper.selectUsersByParam(param);
		return list;
	}
	
	private void validate(UserVo uservo, List<User> userlist){
		for (User user : userlist) {
			if(!uservo.getId().equals(user.getId()) && user.getLevel().intValue() <= uservo.getLevel().intValue()){
				throw new ServiceException("您优先级不够，无法操作该用户:"+user.getName());
			}
		}
	}

	@Override
	public UserVo getUserById(String id) {
		
		User user = userMapper.selectByPrimaryKey(id);
		if (null != user) {
			
			UserVo vo = Convert.convert(UserVo.class, user);
			return vo;
		}
		
		return null;
		
	}

	/**
	 * 更新用户登时长
	 */
	@Override
	public void updateByParam(UserVo userVo) {
		if(userVo !=null) {
			User user = Convert.convert(User.class,userVo);
			user.setLastUpdateDate(new Date());
			userMapper.updateByPrimaryKeySelective(user);
		}
	}

}
