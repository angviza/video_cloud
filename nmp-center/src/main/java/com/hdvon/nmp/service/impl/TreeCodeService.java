package com.hdvon.nmp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdvon.nmp.config.code.CodeConfig;
import com.hdvon.nmp.entity.Address;
import com.hdvon.nmp.entity.Cameragrouop;
import com.hdvon.nmp.entity.Department;
import com.hdvon.nmp.entity.Organization;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.AddressMapper;
import com.hdvon.nmp.mapper.CameragrouopMapper;
import com.hdvon.nmp.mapper.DepartmentMapper;
import com.hdvon.nmp.mapper.OrganizationMapper;
import com.hdvon.nmp.mapper.TreeNodeMapper;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.vo.TreeNode;
import com.hdvon.nmp.vo.TreeNodeDepartment;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;


@Component
public class TreeCodeService {
	@Resource
	AddressMapper addressMapper;
	
	@Resource
	CameragrouopMapper cameraGrouopMapper;
	
	@Resource
	DepartmentMapper departmentMapper;
	
	@Resource
	OrganizationMapper organizationMapper;
	
	@Resource
    private TreeNodeMapper treeNodeMapper;
	
	@Autowired
    CodeConfig codeConfig;
	
	private static final String DEFAULT_ROOT_CODE = "4400";
	
	private static final Integer MAX_CODE_NUM = 100;
	
	private static final Integer TEN_CODE_NUM = 10;
	
	private static final String DEFALUT_PID = "0";
	
	/**
	 * 生成部门编号
	 * @param pid
	 * @return
	 */
	public String genDepartmentCode(String pid) {
		String parentCode = "";
		List<Department> list = null;
		List<String> existCodes = new ArrayList<String>();
		if(pid != DEFALUT_PID) {
			//获取父节点对象
			Department parent = departmentMapper.selectByPrimaryKey(pid);
			parentCode = parent.getDepCode();
		}else {//根节点
			Example rootIdExample = new Example(Department.class);
			rootIdExample.createCriteria().andEqualTo("pid", pid);
			list = departmentMapper.selectByExample(rootIdExample);
			if(list == null || list.size()==0) {//没有根节点
				return DEFAULT_ROOT_CODE;
			}else {//有根节点
				for(Department dept : list) {
					existCodes.add(dept.getDepCode());
				}
				parentCode = codeConfig.getRootCode().substring(0, 2);
				String genCode = genDeptCode(parentCode, existCodes);
				return genCode;
			}
		}
		//根据pid获取当前节点的子节点
		Department model = new Department();
		model.setPid(pid);
		
		
		list = departmentMapper.select(model);
		for(Department dept : list) {
			existCodes.add(dept.getDepCode());
		}
		String genCode = genDeptCode(parentCode, existCodes);
		return genCode;
	}
	
	/**
	 * 生成部门编号
	 * @return
	 */
	private String genDeptCode(String parentCode, List<String> existCodes) {
		if(StrUtil.isBlank(parentCode)) {//根节点的父编号为空
			
		}
		String genCode = "";
		for(int i=0;i<MAX_CODE_NUM;i++) {
			String tempCode = "";
			if(i<TEN_CODE_NUM) {
				tempCode = parentCode + "0" + i;
			}else{
				tempCode = parentCode + i;
			}
			if(!existCodes.contains(tempCode)) {
				genCode = tempCode;
			}
			if(StrUtil.isNotBlank(genCode)) {
				return genCode;
			}
		}
		
		throw new ServiceException("每一级部门最多生成"+MAX_CODE_NUM+"个部门编号！");
	}
	/**
	 * 生成地址编号
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
	 * 生成自定义分组编号
	 * @param pid
	 */
	public String genCameraGroupCode(String pid) {
		String parentCode = "";
		if(pid != DEFALUT_PID) {
			//获取父节点对象
			Cameragrouop parent = cameraGrouopMapper.selectByPrimaryKey(pid);
			parentCode = parent.getCode();
		}

		//根据pid获取当前节点的子节点
		Cameragrouop model = new Cameragrouop();
		model.setPid(pid);
		
		List<Cameragrouop> list = cameraGrouopMapper.select(model);
		int num = 0;
		
		for(Cameragrouop item:list){
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
	 * 生成自定义编号
	 * @param pid
	 */
	public String genSplitCode(String pid, String curCode, String type) {
		String parentSplitCode = "";
		//获取父节点对象
		if(TreeType.DEPARTMENT.getVal().equals(type)) {
			Department parent = departmentMapper.selectByPrimaryKey(pid);
			parentSplitCode = parent.getDepCodeSplit();
		}else if(TreeType.ORG.getVal().equals(type)) {
			Organization parent = organizationMapper.selectByPrimaryKey(pid);
			parentSplitCode = parent.getOrgCodeSplit();
		}	
		return parentSplitCode+(parentSplitCode.endsWith(codeConfig.getSplitCodeSuffix()) ? "" : codeConfig.getSplitCodeSuffix())+"-"+curCode;
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

	/**
	 * 获取地址根节点的最大编码
	 * @return
	 */
	public String genAddressPCode() {
		Map<String,String> param =new HashMap<String,String>();
		param.put("pid", "0");
		String max=addressMapper.selectMaxCode(param);
		if(StrUtil.isNotBlank(max)) {
			return (Integer.valueOf(max)+1)+"";
		}else {
			return "101";
		}
	}

	/**
	 * 获取自定义分组根节点最大编码
	 * @return
	 */
	public String genCameraGroupPCode() {
		Map<String,String> param =new HashMap<String,String>();
		param.put("pid", "0");
		String max=cameraGrouopMapper.selectMaxCode(param);
		if(StrUtil.isNotBlank(max)) {
			return (Integer.valueOf(max)+1)+"";
		}else {
			return "101";
		}
	}
	
	/**
	 * 查询
	 * @param orgCodeSplit
	 * @param type
	 * @return
	 */
	public List<TreeNode> getOrgChildNodesByCode(String orgCodeSplit, String type){
		 Map<String,Object> param = new HashMap<>();
		 param.put("code", orgCodeSplit);
		 param.put("type", type);
		return treeNodeMapper.selectChildNodesByCode(param);
	}
}
