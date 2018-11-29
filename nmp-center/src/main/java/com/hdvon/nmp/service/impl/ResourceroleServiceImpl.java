package com.hdvon.nmp.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.entity.Resourcerole;
import com.hdvon.nmp.entity.User;
import com.hdvon.nmp.entity.UserResourcerole;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.*;
import com.hdvon.nmp.service.IResourceroleService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.ResourceroleVo;
import com.hdvon.nmp.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * <br>
 * <b>功能：</b>资源角色表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class ResourceroleServiceImpl implements IResourceroleService {

	@Autowired
	private ResourceroleMapper resourceroleMapper;
    @Autowired
    private ResourceroleCameraPermissionMapper resourceroleCameraPermissionMapper;

    @Autowired
    private UserResourceroleMapper userResourceroleMapper;
    
    @Autowired
    private UserDepartmentDataMapper userDepartmentDataMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public PageInfo<ResourceroleVo> getResRoleListByPage(ResourceroleVo resourceroleVo, PageParam pageParam) {
        PageHelper.startPage(pageParam.getPageNo(), pageParam.getPageSize());

        Map<String,Object> param = new HashMap<>();
        param.put("pidOrId",resourceroleVo.getId());
        param.put("likeName",resourceroleVo.getName().trim());

        List<ResourceroleVo> list = resourceroleMapper.selectByParam(param);
        return new PageInfo<>(list);

    }
    
    private void getNodeChildren(List<ResourceroleVo> list, List<String> curIds, List<ResourceroleVo> result, int count) {
    	for(int i=0;i<list.size();i++) {
    		ResourceroleVo resourceroleVo = list.get(i);
    		if(count == 0) {//取当前节点
    			if(curIds.contains(resourceroleVo.getId())) {
    				count++;
    				result.add(resourceroleVo);
    				list.remove(resourceroleVo);
    				getNodeChildren(list, curIds, result, count);
    			}
    		}else {//取子节点
				if(curIds.contains(resourceroleVo.getPid())) {
        			result.add(resourceroleVo);
        			list.remove(resourceroleVo);
        			curIds.add(resourceroleVo.getId());
        			getNodeChildren(list, curIds, result, count);
        		}
    		}
    		
    	}
    }
    

    @Override
    public ResourceroleVo getResRoleById(String id) {
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        List<ResourceroleVo> list = resourceroleMapper.selectByParam(map);
        if(list.size() > 0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public void saveResRole(UserVo userVo, ResourceroleVo resourceroleVo){
        Resourcerole resouceRole = Convert.convert(Resourcerole.class,resourceroleVo);
        Example example = new Example(Resourcerole.class);

        //根节点
        if(StrUtil.isNotEmpty(resouceRole.getPid()) && !"0".equals(resouceRole.getPid())){
            example.clear();
            example.createCriteria().andEqualTo("id", resouceRole.getPid());
            int count = resourceroleMapper.selectCountByExample(example);
            if(count == 0){
                throw new ServiceException("上级资源角色不存在");
            }
        }else{
        	resouceRole.setPid("0");
        }

        if(StrUtil.isBlank(resouceRole.getId())){
            // 新增
            example.clear();
            example.createCriteria().andEqualTo("name", resouceRole.getName());
            int count = resourceroleMapper.selectCountByExample(example);
            if(count > 0){
                throw new ServiceException("资源角色名称已存在");
            }

            resouceRole.setId(IdGenerator.nextId());
            Date date = new Date();
            resouceRole.setCreateTime(date);
            resouceRole.setCreateUser(userVo.getAccount());
            resouceRole.setUpdateTime(date);
            resouceRole.setUpdateUser(userVo.getAccount());
            resouceRole.setIsMenuAssigned(0);
            resouceRole.setIsPermAssigned(0);
            resourceroleMapper.insertSelective(resouceRole);
        }else{
            // 修改
            Resourcerole oldResourcerole = resourceroleMapper.selectByPrimaryKey(resourceroleVo.getId());
            if(oldResourcerole != null && "0".equals(oldResourcerole.getPid()) && !resourceroleVo.getPid().equals(oldResourcerole.getPid())) {
                throw new ServiceException("根节点的上级角色节点不能修改");
            }
            example.clear();
            example.createCriteria().andEqualTo("name", resouceRole.getName()).andNotEqualTo("id",resourceroleVo.getId());
            int count = resourceroleMapper.selectCountByExample(example);
            if(count > 0){
                throw new ServiceException("这个资源角色名称已被其他使用");
            }

            resouceRole.setUpdateUser(userVo.getId());
            resouceRole.setUpdateTime(new Date());
            resourceroleMapper.updateByPrimaryKeySelective(resouceRole);
        }
    }

    @Override
    public void saveResPermission(String resourceRoleId, String permissionContent){

    }

    @Override
    public void deleteResRole(UserVo userVo, List<String> ids){
        if(ids != null && ids.size() > 0){
            //查询资源角色是否存在子节点
            Example resourceroleChildrenExample = new Example(Resourcerole.class);
            resourceroleChildrenExample.createCriteria().andIn("pid", ids);
            int roleCount = resourceroleMapper.selectCountByExample(resourceroleChildrenExample);
            if(roleCount > 0) {
                throw new ServiceException("资源角色存在子资源角色，不能删除！");
            }


//            Example cameraPermissionExample = new Example(ResourceroleCameraPermission.class);
//            cameraPermissionExample.createCriteria().andIn("resouceroleId", ids);
//            int addressCount = resourceroleCameraPermissionMapper.selectCountByExample(cameraPermissionExample);
//            if(addressCount > 0){
//                throw new ServiceException("资源角色存在地址树关联，不能删除！");
//            }

            for(String id : ids){
                Resourcerole temp = resourceroleMapper.selectByPrimaryKey(id);
                if(temp != null){
                    //查询用户是否有关联
                    if(temp.getIsMenuAssigned() == 1){
                        throw new ServiceException("系统角色已经向用户授权");
                    }
                    //查询地址树是否有关联
                    if(temp.getIsPermAssigned() == 1){
                        throw new ServiceException("系统角色已经分配系统菜单权限");
                    }
                }
            }
            //查询用户是否有关联
//            Example userResourceroleExample = new Example(UserResourcerole.class);
//            userResourceroleExample.createCriteria().andIn("resourceroleId", ids);
//            int userCount = userResourceroleMapper.selectCountByExample(userResourceroleExample);
//            if(userCount > 0){
//                throw new ServiceException("资源角色存在用户关联，不能删除！");
//            }

            //批量删除资源角色
            Example example = new Example(Resourcerole.class);
            example.createCriteria().andIn("id", ids);
            resourceroleMapper.deleteByExample(example);
        }
    }

	@Override
	public void saveUsersToResourceroleId(UserVo loginUser, String resourceroleId, List<String> userIds) {
        Example example = new Example(UserResourcerole.class);
        //查找当前角色关联的用户
        example.clear();
        example.createCriteria().andEqualTo("resourceroleId",resourceroleId);
        List<UserResourcerole> temp = userResourceroleMapper.selectByExample(example);
        List<String> tempUserIds = new ArrayList<>();
        for(UserResourcerole obj : temp){
            tempUserIds.add(obj.getUserId());
        }

        //删除资源角色下的用户
        example.clear();
        Example.Criteria criteria = example.createCriteria().andEqualTo("resourceroleId",resourceroleId);
        if(!loginUser.isAdmin()){
            //普通用户根据可管理部门进行过滤删除
            List<String> manageUserIds = userMapper.selectAuthUserIds(loginUser.getId());
            criteria.andIn("userId",manageUserIds);
        }
        userResourceroleMapper.deleteByExample(example);

        //添加资源角色下用户
        int isMenuAssigned = 0;
        if(userIds != null){
            // 去重
            tempUserIds.removeAll(userIds);
            tempUserIds.addAll(userIds);

            //查询普通用户可管理部门下的所有用户id，并取出用户id的交集
            if(!loginUser.isAdmin()){
                List<String> manageUserIds = userMapper.selectAuthUserIds(loginUser.getId());
                userIds.retainAll(manageUserIds);
            }

            for(String userId : userIds) {
                UserResourcerole userResour = new UserResourcerole();
                userResour.setId(IdGenerator.nextId());
                userResour.setResourceroleId(resourceroleId);
                userResour.setUserId(userId);
                isMenuAssigned =1;
                userResourceroleMapper.insertSelective(userResour);
            }
        }

        // 更新用户授权状态
        for(String tempId :tempUserIds){
            example.clear();
            example.createCriteria().andEqualTo("userId",tempId);
            int tempCount= userResourceroleMapper.selectCountByExample(example);
            if(tempCount > 0){
                User user = new User();
                user.setId(tempId);
                user.setIsResourceAssigned("1");
                userMapper.updateByPrimaryKeySelective(user);
            } else {
                User user = new User();
                user.setId(tempId);
                user.setIsResourceAssigned("0");
                userMapper.updateByPrimaryKeySelective(user);
            }
        }

        //更新isMenuAssigned
        Resourcerole resourcerole = new Resourcerole();
        resourcerole.setIsMenuAssigned(isMenuAssigned);
        resourcerole.setId(resourceroleId);
        resourceroleMapper.updateByPrimaryKeySelective(resourcerole);
	}

	/**
	 * 根据条件查询角色关联的用户ids
	 */
	@Override
	public List<String> getUserIdsByParam(Map<String, Object> param) {
		List<String> userIds =resourceroleMapper.selectUserIdsByParam(param);
		return userIds;
	}

	@Override
	public List<ResourceroleVo> getResourceByParam(Map<String, Object> param) {
		return resourceroleMapper.selectResourceByParam(param);
	}
}
