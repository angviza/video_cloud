package com.hdvon.client.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.hdvon.client.entity.Address;
import com.hdvon.client.entity.CameraGroup;
import com.hdvon.client.entity.Permission;
import com.hdvon.client.entity.User;
import com.hdvon.client.exception.UserException;
import com.hdvon.client.mapper.AddressMapper;
import com.hdvon.client.mapper.CameraGroupMapper;
import com.hdvon.client.mapper.DepartmentMapper;
import com.hdvon.client.mapper.PermissionMapper;
import com.hdvon.client.mapper.UserMapper;
import com.hdvon.nmp.common.SystemConstant;
/**
 * 公共帮助类
 * @author wanshaojian
 *
 */
@Component
public class CommonService {
	@Resource
	UserMapper userMapper;
	
	@Resource
	DepartmentMapper departmentMapper;
	
	@Resource
	PermissionMapper permissionMapper;
	
	@Resource
	AddressMapper addressMapper;
	
	
	@Resource
	CameraGroupMapper cameraGroupMapper;
	
	private static final Integer MAX_CODE_NUM = 100;
	
	private static final Integer TEN_CODE_NUM = 10;
	
	private static final String DEFALUT_PID = "0";
	
	
	/**
	 * 判断用户是否是超级管理员
	 * @param userId
	 * @return
	 * @throws UserException
	 */
	public boolean isSuperAdmin(String userId) throws UserException{
		//根据用户id获取用户信息
		User userRecord = userMapper.selectByPrimaryKey(userId);
		if(userRecord == null) {
			throw new UserException("该用户在系统中不存在");
		}
		//判断用户是否超级管理员
		if(SystemConstant.super_Account.equals(userRecord.getAccount())) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 判断用户是否是超级管理员
	 * @param userId
	 * @return
	 * @throws UserException
	 */
	public User findRecord(String userId) throws UserException{
		//根据用户id获取用户信息
		return userMapper.selectByPrimaryKey(userId);
	}
	/**
	 * 根据用户id集合获取用户列表
	 * @param userId
	 * @return
	 * @throws UserException
	 */
	public List<User> findUserListByIds(String userIds) throws UserException{
		//根据用户id获取用户信息
		return userMapper.findListByIds(userIds);
	}
	
	/**
	 * 获取摄像机资源权限值
	 * @return
	 */
	public String getPermissionValue() {
		//获取摄像机所有权限
		List<Permission> permissionList = permissionMapper.selectAll();
		
		List<Integer> permissionValueList = permissionList.stream().map(Permission::getValue).filter(x -> x !=null ).collect(Collectors.toList());
		
		return StringUtils.join(permissionValueList.toArray(), ",");  

	}
	/**
	 * 生成地址模板
	 * @param pid
	 */
	public String genAddressCode(String pid) {
		String parentCode = "";
		if(pid != DEFALUT_PID) {
			//获取父节点对象
			Address parent = addressMapper.selectByPrimaryKey(pid);
			parentCode = parent.getCode();
		}
		//根据pid获取当前节点的子节点
		Address model = new Address();
		model.setPid(pid);
		
		
		List<Address> list = addressMapper.select(model);
		int num = 0;
		
		for(Address item:list){
			String code = item.getCode();
			int len = item.getCode().length();
			if(pid != "0") {
				code = code.substring(len-3,len);
			}
			if(num<Integer.valueOf(code)) {
				num = Integer.valueOf(code);
			}
		}
				
		return parentCode+genCode(num+1);
	}
	/**
	 * 生成地址模板
	 * @param pid
	 */
	public String genCameraGroupCode(String pid) {
		String parentCode = "";
		if(pid != DEFALUT_PID) {
			//获取父节点对象
			CameraGroup parent = cameraGroupMapper.selectByPrimaryKey(pid);
			parentCode = parent.getCode();
		}

		//根据pid获取当前节点的子节点
		CameraGroup model = new CameraGroup();
		model.setPid(pid);
		
		List<CameraGroup> list = cameraGroupMapper.select(model);
		int num = 0;
		
		for(CameraGroup item:list){
			String code = item.getCode();
			int len = item.getCode().length();
			if(pid != "0") {
				code = code.substring(len-3,len);
			}
			if(num<Integer.valueOf(code)) {
				num = Integer.valueOf(code);
			}
		}
				
		return parentCode+genCode(num+1);
	}
	/**
	 * 更新地址编码
	 */
	public void updateAddressCode() {
		Address model = new Address();
		model.setPid("0");
		//获取
		List<Address> list = addressMapper.select(model);
		
		//首位编号以001编号开始
		int i=1;
		for(Address entity:list) {
			String code = genCode(i);
			
			entity.setCode(code);
			//修改数据
			addressMapper.updateByPrimaryKey(entity);
			
			//递归修改子级节点
			updateChildAddressCode(entity.getId(), code);
			
			i++;
		}
	}
	
	/**
	 * 递归修改子级地址编码
	 * @param pid
	 * @param parentCode
	 */
	private void updateChildAddressCode(String pid,String parentCode) {
		Address model = new Address();
		model.setPid(pid);
		//获取
		List<Address> list = addressMapper.select(model);
		if(list == null || list.isEmpty()) {
			return;
		}
		//首位编号以001编号开始
		int i=1;
		for(Address entity:list) {
			String code = parentCode+genCode(i);
					
			entity.setCode(code);
			//修改数据
			addressMapper.updateByPrimaryKey(entity);
			
			//递归修改子级节点
			updateChildAddressCode(entity.getId(), code);
			
			i++;
		}
	}
	
	
	/**
	 * 更新地址编码
	 */
	public void updateCameraGroupCode() {
		CameraGroup model = new CameraGroup();
		model.setPid("0");
		//获取
		List<CameraGroup> list = cameraGroupMapper.select(model);
		
		//首位编号以001编号开始
		int i=1;
		for(CameraGroup entity:list) {
			String code = genCode(i);
			
			entity.setCode(code);
			//修改数据
			cameraGroupMapper.updateByPrimaryKey(entity);
			
			//递归修改子级节点
			updateChildCameraGroupCode(entity.getId(), code);
			
			i++;
		}
	}
	
	/**
	 * 递归修改子级地址编码
	 * @param pid
	 * @param parentCode
	 */
	private void updateChildCameraGroupCode(String pid,String parentCode) {
		CameraGroup model = new CameraGroup();
		model.setPid(pid);
		//获取
		List<CameraGroup> list = cameraGroupMapper.select(model);
		if(list == null || list.isEmpty()) {
			return;
		}
		//首位编号以001编号开始
		int i=1;
		for(CameraGroup entity:list) {
			String code = parentCode+genCode(i);
			
			entity.setCode(code);
			//修改数据
			cameraGroupMapper.updateByPrimaryKey(entity);
			
			//递归修改子级节点
			updateChildCameraGroupCode(entity.getId(), code);
			
			i++;
		}
	}
	private String genCode(int i) {
		if(i>=TEN_CODE_NUM && i<MAX_CODE_NUM) {
			return "1"+i;
		}else if(i<TEN_CODE_NUM) {
			return "10"+i;
		}else {
			return String.valueOf(i);
		}
	}
}
