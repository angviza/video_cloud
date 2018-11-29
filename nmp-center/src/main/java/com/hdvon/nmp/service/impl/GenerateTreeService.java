package com.hdvon.nmp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.DepartmentMapper;
import com.hdvon.nmp.mapper.OrganizationMapper;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.vo.DepartmentVo;
import com.hdvon.nmp.vo.OrganizationVo;
import com.hdvon.nmp.vo.ValidAttrVo;

@Component
public class GenerateTreeService {
	
	@Autowired
	DepartmentMapper departmentMapper;
	
	@Autowired
	OrganizationMapper organizationMapper;
	/**
	 * 校验树节点编号、名称是否已经存在数据库
	 * @param map
	 * @param treeType
	 * @param treeAttr
	 */
	public void checkTreeNodeExists(Map<String,List<String>> map, String treeType, ValidAttrVo treeAttr) {
		List<String> codeList = map.get("codeList");
		List<String> nameList = map.get("nameList");
		List<String> pcodeList = map.get("pcodeList");
		
		Map<String,Object> codeMap = new HashMap<String,Object>();
		Map<String,Object> nameMap = new HashMap<String,Object>();
		Map<String,Object> pcodeMap = new HashMap<String,Object>();
		
		codeMap.put("codeList", codeList);
		nameMap.put("nameList", nameList);
		pcodeMap.put("pcodeList", pcodeList);
		
		if(TreeType.DEPARTMENT.getVal().equals(treeType)) {//校验部门编号、名称是否已经存在
			if(codeList != null && codeList.size()>0) {//编号是否已经存在
				List<DepartmentVo> deptVos = departmentMapper.selectChildDepartmentsByPid(codeMap);
				if(deptVos != null) {
					List<Integer> list = new ArrayList<Integer>();
					for(DepartmentVo deptVo : deptVos) {
						int rowNo = codeList.indexOf(deptVo.getDepCode());
						if(rowNo >= 0) {
							list.add(rowNo);
						}
					}
					Collections.sort(list);
					String errorMsg = generateErrorContent(list, treeAttr.getCodeCol(), treeType, "code");
					throw new ServiceException(errorMsg);
				}
			}
			if(nameList != null && nameList.size()>0) {//名称是否已经存在
				List<DepartmentVo> deptVos = departmentMapper.selectChildDepartmentsByPid(nameMap);
				if(deptVos != null) {
					List<Integer> list = new ArrayList<Integer>();
					for(DepartmentVo deptVo : deptVos) {
						int rowNo = nameList.indexOf(deptVo.getName());
						if(rowNo >= 0) {
							list.add(rowNo);
						}
					}
					Collections.sort(list);
					String errorMsg = generateErrorContent(list, treeAttr.getNameCol(), treeType, "name");
					throw new ServiceException(errorMsg);
				}
			}
		}else if(TreeType.ORG.getVal().equals(treeType)) {
			if(codeList != null && codeList.size()>0) {//编号是否已经存在
				List<OrganizationVo> orgVos = organizationMapper.selectChildOrgsByPid(codeMap);
				if(orgVos != null) {
					List<Integer> list = new ArrayList<Integer>();
					for(OrganizationVo orgVo : orgVos) {
						int rowNo = codeList.indexOf(orgVo.getOrgCode());
						if(rowNo >= 0) {
							list.add(rowNo);
						}
					}
					Collections.sort(list);
					String errorMsg = generateErrorContent(list, treeAttr.getCodeCol(), treeType, "code");
					throw new ServiceException(errorMsg);
				}
			}
			if(nameList != null && nameList.size()>0) {//名称是否已经存在
				List<OrganizationVo> orgVos = organizationMapper.selectChildOrgsByPid(nameMap);
				if(orgVos != null) {
					List<Integer> list = new ArrayList<Integer>();
					for(OrganizationVo orgVo : orgVos) {
						int rowNo = nameList.indexOf(orgVo.getName());
						if(rowNo >= 0) {
							list.add(rowNo);
						}
					}
					Collections.sort(list);
					String errorMsg = generateErrorContent(list, treeAttr.getNameCol(), treeType, "name");
					throw new ServiceException(errorMsg);
				}
			}
		}
	}
		/**
		 * 生成异常信息
		 * @param rowNos
		 * @param colNo
		 * @param treeType
		 * @param columnType
		 * @return
		 */
		private String generateErrorContent(List<Integer> rowNos, Integer colNo, String treeType, String columnType) {
			StringBuffer sb = new StringBuffer();
			sb.append("第"+(colNo+1)+"列第");
			for(int i=0;i<rowNos.size();i++) {
				if(i<10) {
					sb.append(rowNos.get(i)+"、");
				}else {
					sb.append(rowNos.get(i)+"...");
					break;
				}
			}
			sb.append("行");
			if("code".equals(columnType)) {
				sb.append("编号已存在！");
			}else if("name".equals(columnType)){
				sb.append("名称已存在！");
			}
			return sb.toString();
		}
}
