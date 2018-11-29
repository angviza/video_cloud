package com.hdvon.nmp.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.Sysrole;
import com.hdvon.nmp.entity.SysroleSysmenu;
import com.hdvon.nmp.entity.User;
import com.hdvon.nmp.entity.UserSysrole;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.*;
import com.hdvon.nmp.service.ISysroleService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.DepartmentUserTreeVo;
import com.hdvon.nmp.vo.SysroleVo;
import com.hdvon.nmp.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * <br>
 * <b>功能：</b>系统角色表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class SysroleServiceImpl implements ISysroleService {

	@Autowired
	private SysroleMapper sysroleMapper;

    @Autowired
    private SysroleSysmenuMapper sysroleSysmenuMapper;

    @Autowired
    private UserSysroleMapper userSysroleMapper;
    
    @Autowired
    private UserDepartmentDataMapper userDepartmentDataMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageInfo<SysroleVo> getSysRoleListByPage(SysroleVo sysroleVo, PageParam pageParam) {
        PageHelper.startPage(pageParam.getPageNo(), pageParam.getPageSize());

        Map<String,Object> param = new HashMap<>();
        param.put("pidOrId",sysroleVo.getId());
        param.put("likeName",sysroleVo.getName());
        List<SysroleVo> list = sysroleMapper.querySysRoles(param);

         return new PageInfo<>(list);
    }

    
    private void getNodeChildren(List<SysroleVo> list, List<String> curIds, List<SysroleVo> result, int count) {
    	for(int i=0;i<list.size();i++) {
    		SysroleVo sysroleVo = list.get(i);
    		if(count == 0) {//取当前节点
    			if(curIds.contains(sysroleVo.getId())) {
    				count++;
    				result.add(sysroleVo);
    				list.remove(sysroleVo);
    				getNodeChildren(list, curIds, result, count);
    			}
    		}else {//取子节点
				if(curIds.contains(sysroleVo.getPid())) {
        			result.add(sysroleVo);
        			list.remove(sysroleVo);
        			curIds.add(sysroleVo.getId());
        			getNodeChildren(list, curIds, result, count);
        		}
    		}
    		
    	}
    }

    @Override
    public SysroleVo getSysRoleById(String id) {
        if(StrUtil.isBlank(id)){
            return null;
        }
        Map<String,Object> param = new HashMap<>();
        param.put("id",id);
        List<SysroleVo> list = sysroleMapper.querySysRoles(param);
        if(list.size() > 0 ){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public void saveSysrole(UserVo userVo, SysroleVo sysroleVo){
        Sysrole sysrole = Convert.convert(Sysrole.class,sysroleVo);
        Example example = new Example(Sysrole.class);

        // 根节点
        if(StrUtil.isNotEmpty(sysrole.getPid()) && !"0".equals(sysrole.getPid())){
            example.clear();
            example.createCriteria().andEqualTo("id", sysrole.getPid());
            int count = sysroleMapper.selectCountByExample(example);
            if(count == 0){
                throw new ServiceException("上级系统角色不存在");
            }
        }else{
            sysrole.setPid("0");
        }

        if(StrUtil.isBlank(sysrole.getId())){
            // 新增
            example.clear();
            example.createCriteria().andEqualTo("name", sysrole.getName());
            int count = sysroleMapper.selectCountByExample(example);
            if(count > 0){
                throw new ServiceException("系统角色名称已存在");
            }

            sysrole.setId(IdGenerator.nextId());
            Date date = new Date();
            sysrole.setCreateUser(userVo.getAccount());
            sysrole.setCreateTime(date);
            sysrole.setUpdateUser(userVo.getAccount());
            sysrole.setUpdateTime(date);
            sysrole.setIsMenuAssigned(0);
            sysrole.setIsPermAssigned(0);
            sysroleMapper.insertSelective(sysrole);
        }else{
            // 修改
            Sysrole oldSysrole = sysroleMapper.selectByPrimaryKey(sysroleVo.getId());
            if(oldSysrole != null && "0".equals(oldSysrole.getPid()) && !sysroleVo.getPid().equals(oldSysrole.getPid())) {
                throw new ServiceException("根节点的上级角色节点不能修改");
            }

            example.clear();
            example.createCriteria().andEqualTo("name", sysrole.getName()).andNotEqualTo("id",sysrole.getId());
            int count = sysroleMapper.selectCountByExample(example);
            if(count > 0){
                throw new ServiceException("这个系统角色名称已被其他使用");
            }

            sysrole.setUpdateUser(userVo.getId());
            sysrole.setUpdateTime(new Date());
            sysroleMapper.updateByPrimaryKeySelective(sysrole);
        }
    }

    @Override
    @Transactional
    public void saveSysroleMenu(String roleId, List<String> menuIdList) {
        Sysrole role = sysroleMapper.selectByPrimaryKey(roleId);
        if(role == null){
            throw new ServiceException("此系统角色不存在");
        }

        //先取消原有菜单关联
        Example example = new Example(SysroleSysmenu.class);
        example.createCriteria().andEqualTo("sysroleId", roleId);
        sysroleSysmenuMapper.deleteByExample(example);

        //关联新菜单
        int isPermAssigned = 0;
        if(menuIdList != null){
            for (String menuId : menuIdList) {
                SysroleSysmenu sysroleSysmenu = new SysroleSysmenu();
                if(StrUtil.isNotEmpty(menuId)){
                    sysroleSysmenu.setId(IdGenerator.nextId());
                    sysroleSysmenu.setSysroleId(roleId);
                    sysroleSysmenu.setSysmenuId(menuId);
                    isPermAssigned = 1;
                    sysroleSysmenuMapper.insertSelective(sysroleSysmenu);
                }
            }
        }

        //更新isPermAssigned
        Sysrole sysrole = new Sysrole();
        sysrole.setIsPermAssigned(isPermAssigned);
        sysrole.setId(roleId);
        sysroleMapper.updateByPrimaryKeySelective(sysrole);
    }

    @Override
    public void deleteSysrole(UserVo userVo,List<String> ids){
        if(ids != null && ids.size() > 0){

            //检查角色是否有子角色
            Example sysroleExample = new Example(Sysrole.class);
            sysroleExample.createCriteria().andIn("pid", ids);
            int childCount = sysroleMapper.selectCountByExample(sysroleExample);
            if(childCount > 0){
                throw new ServiceException("系统角色存在子角色关联");
            }

            //查询菜单是否有关联
//            Example sysroleSysmenuExample = new Example(SysroleSysmenu.class);
//            sysroleSysmenuExample.createCriteria().andIn("sysroleId", ids);
//            int menuCount = sysroleSysmenuMapper.selectCountByExample(sysroleSysmenuExample);
//            if(menuCount > 0){
//                throw new ServiceException("系统角色存在菜单关联");
//            }

            for(String id : ids){
                Sysrole temp = sysroleMapper.selectByPrimaryKey(id);
                if(temp != null){
                    //查询用户是否有关联
                    if(temp.getIsMenuAssigned() == 1){
                        throw new ServiceException("系统角色已经向用户授权");
                    }
                    //查询菜单是否有关联
                    if(temp.getIsPermAssigned() == 1){
                        throw new ServiceException("系统角色已经分配系统菜单权限");
                    }
                }
            }

            //查询用户是否有关联
//            Example userSysroleExample = new Example(UserSysrole.class);
//            userSysroleExample.createCriteria().andIn("roleId", ids);
//            int userCount = userSysroleMapper.selectCountByExample(userSysroleExample);
//            if(userCount > 0){
//                throw new ServiceException("系统角色存在用户关联");
//            }

            //批量删除系统角色
            Example example = new Example(Sysrole.class);
            example.createCriteria().andIn("id", ids);
            sysroleMapper.deleteByExample(example);
        }
    }

	@Override
	public List<DepartmentUserTreeVo> getRelatedDeptUserTree(Map<String, Object> map) {
		List<DepartmentUserTreeVo> list = userDepartmentDataMapper.getSysroleDeptUserTree(map);
		return list;
	}

	@Override
	public void relateUsersToSysroleId(UserVo userVo, String sysroleId, List<String> userIds) {
        Example example = new Example(UserSysrole.class);
        // 查找当前角色关联的用户
        example.clear();
        example.createCriteria().andEqualTo("roleId",sysroleId);
        List<UserSysrole> temp = userSysroleMapper.selectByExample(example);
        List<String> tempUserIds = new ArrayList<>();
        for(UserSysrole obj : temp){
            tempUserIds.add(obj.getUserId());
        }

        example.clear();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId",sysroleId);
        if(!userVo.isAdmin()){
            //普通用户根据可管理部门进行过滤删除
            List<String> manageUserIds = userMapper.selectAuthUserIds(userVo.getId());
            criteria.andIn("userId",manageUserIds);
        }
        userSysroleMapper.deleteByExample(example);

		List<UserSysrole> list = new ArrayList<UserSysrole>();
		int isMenuAssigned = 0;
		if(userIds != null ){
            // 去重
            tempUserIds.removeAll(userIds);
            tempUserIds.addAll(userIds);

            for(String userId : userIds) {
                UserSysrole userSysrole = new UserSysrole();
                userSysrole.setId(IdGenerator.nextId());
                userSysrole.setRoleId(sysroleId);
                userSysrole.setUserId(userId);
                list.add(userSysrole);
                isMenuAssigned = 1;

                // 更新用户授权状态
                User user = new User();
                user.setId(userId);
                user.setIsRoleGranted("1");
                userMapper.updateByPrimaryKeySelective(user);
            }
            userSysroleMapper.insertList(list);
        }

        // 更新用户授权状态
        for(String tempId :tempUserIds){
            example.clear();
            example.createCriteria().andEqualTo("userId",tempId);
            int tempCount= userSysroleMapper.selectCountByExample(example);
            if(tempCount > 0){
                User user = new User();
                user.setId(tempId);
                user.setIsRoleGranted("1");
                userMapper.updateByPrimaryKeySelective(user);
            } else {
                User user = new User();
                user.setId(tempId);
                user.setIsRoleGranted("0");
                userMapper.updateByPrimaryKeySelective(user);
            }
        }

        //更新isMenuAssigned
        Sysrole sysrole = new Sysrole();
        sysrole.setIsMenuAssigned(isMenuAssigned);
        sysrole.setId(sysroleId);
        sysroleMapper.updateByPrimaryKeySelective(sysrole);
	}

	@Override
	public List<SysroleVo> getUserAuthorizeSysRoleTree(String userId) {
		
		return sysroleMapper.getUserAuthorizeSysRoleTree(userId);
	}

	@Override
	public PageInfo<SysroleVo> getRoleUserCount(Map<String, Object> param, PageParam pageParam) {
	    PageHelper.startPage(pageParam.getPageNo(),pageParam.getPageSize());
        List<SysroleVo> list = sysroleMapper.selectRoleUserCount(param);
        return new PageInfo<>(list);
	}

}
