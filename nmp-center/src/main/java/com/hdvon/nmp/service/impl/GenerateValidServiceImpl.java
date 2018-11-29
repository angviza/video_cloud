package com.hdvon.nmp.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.generate.FunTypeEnum;
import com.hdvon.nmp.mapper.AddressMapper;
import com.hdvon.nmp.mapper.BussinessGroupMapper;
import com.hdvon.nmp.mapper.CameraMapper;
import com.hdvon.nmp.mapper.DepartmentMapper;
import com.hdvon.nmp.mapper.EncoderServerMapper;
import com.hdvon.nmp.mapper.OrganizationMapper;
import com.hdvon.nmp.mapper.ProjectMapper;
import com.hdvon.nmp.service.IGenerateValidService;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.vo.AddressVo;
import com.hdvon.nmp.vo.BussinessGroupVo;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.DepartmentVo;
import com.hdvon.nmp.vo.EncoderServerVo;
import com.hdvon.nmp.vo.OrganizationVo;
import com.hdvon.nmp.vo.ProjectVo;
import com.hdvon.nmp.vo.ValidAttrVo;

import cn.hutool.core.util.StrUtil;

@Service
public class GenerateValidServiceImpl implements IGenerateValidService{
	@Autowired
	DepartmentMapper departmentMapper;
	
	@Autowired
	OrganizationMapper organizationMapper;
	
	@Autowired
	CameraMapper cameraMapper;
	
	@Autowired
	AddressMapper addressMapper;
	
	@Autowired
	EncoderServerMapper encoderServerMapper;
	
	@Autowired
	BussinessGroupMapper bussinessGroupMapper;
	
	@Autowired
	ProjectMapper projectMapper;
	
	private static final String[] perCols = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P",
			"Q","R","S","T","U","V","W","X","Y","Z"};
	
	@Override
	public Map<String,List<String>> checkTreeNodeExists(Map<String, List<String>> treeData, String treeType, ValidAttrVo treeAttr, Integer cursRow, String isVirtual) {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		
		List<String> codeList = treeData.get("codeList");
		List<String> nameList = treeData.get("nameList");
		List<String> pcodeList = treeData.get("pcodeList");
		
		Map<String,Object> codeMap = new HashMap<String,Object>();
		Map<String,Object> nameMap = new HashMap<String,Object>();
		Map<String,Object> pcodeMap = new HashMap<String,Object>();
		
		codeMap.put("codeList", codeList);
		nameMap.put("nameList", nameList);
		pcodeMap.put("pcodeList", pcodeList);
		
		
		
		if(TreeType.DEPARTMENT.getVal().equals(treeType)) {//校验部门编号、名称是否已经存在
			Map<String,Object> xzqhMap = new HashMap<String,Object>();
			List<String> xzqhList = treeData.get("xzqhList");
			xzqhMap.put("xzqhList", xzqhList);
			
			if(codeList != null && codeList.size()>0) {//编号是否已经存在
				List<DepartmentVo> deptCodeVos = departmentMapper.selectChildDepartmentsByPid(codeMap);
				if(deptCodeVos != null) {
					List<Integer> list = new ArrayList<Integer>();
					for(DepartmentVo deptVo : deptCodeVos) {
						int rowNo = codeList.indexOf(deptVo.getDepCode());
						if(rowNo >= 0) {
							list.add(rowNo);
						}
					}
					if(list.size()>0) {
						Collections.sort(list);
						String errorMsg = generateErrorContent(list, treeAttr.getCodeCol(), treeType,"code", FunTypeEnum.DEPARTMENT.getVal(), cursRow);
						throw new ServiceException(errorMsg);
					}
				}
			}
			if(nameList != null && nameList.size()>0) {//名称是否已经存在
				List<DepartmentVo> deptNameVos = departmentMapper.selectChildDepartmentsByPid(nameMap);
				if(deptNameVos != null) {
					List<Integer> list = new ArrayList<Integer>();
					for(DepartmentVo deptVo : deptNameVos) {
						int rowNo = nameList.indexOf(deptVo.getName());
						if(rowNo >= 0) {
							list.add(rowNo);
						}
					}
					if(list.size()>0) {
						Collections.sort(list);
						String errorMsg = generateErrorContent(list, treeAttr.getNameCol(), treeType, "name", FunTypeEnum.DEPARTMENT.getVal(), cursRow);
						throw new ServiceException(errorMsg);
					}
				}
			}
			if(pcodeList != null && pcodeList.size()>0) {
				Map<String,Object> paramMap = new HashMap<String,Object>();
				List<DepartmentVo> deptPCodeVos = departmentMapper.selectChildDepartmentsByPid(paramMap);
				if(deptPCodeVos != null) {
					List<Integer> list = new ArrayList<Integer>();
					List<String> existList = new ArrayList<String>();
					for(DepartmentVo departmentVo : deptPCodeVos) {
						existList.add(departmentVo.getDepCode());
					}
					for(int i=0;i<pcodeList.size();i++) {
						String pcode = pcodeList.get(i);
						if(!"0".equals(pcode) && !existList.contains(pcode) && !codeList.contains(pcode)) {//不是根节点，数据库中没有父编号且excel编号中没有该父编号
							list.add(i);
						}
						
						if(list.size()==10) {
							break;
						}
					}
					if(list.size()>0) {
						Collections.sort(list);
						String errorMsg = generateErrorContent(list, treeAttr.getPcodeCol(), null, "pcode",FunTypeEnum.DEPARTMENT.getVal(), cursRow);
						throw new ServiceException(errorMsg);
					}
				}
			}
			if(xzqhList != null && xzqhList.size()>0) {
				String[] xzqhIdArr = new String[xzqhList.size()];
				List<String> xzqhIdList = Arrays.asList(xzqhIdArr);
				Map<String,Object> param = new HashMap<String,Object>();
				//param.put("nameList", xzqhList);
				List<OrganizationVo> grganizationVos = organizationMapper.selectByParam(param);
				if(grganizationVos != null) {
					List<Integer> list = new ArrayList<Integer>();
					Map<Integer, List<Integer>> idIndexMap = new HashMap<Integer, List<Integer>>();
					List<String> existList = new ArrayList<String>();
					for(OrganizationVo organizationVo : grganizationVos) {
						existList.add(organizationVo.getName());
					}
					nameTransToId(existList, xzqhList, list, idIndexMap);
					if(list.size()>0) {
						Collections.sort(list);
						String errorMsg = generateErrorContent(list, treeAttr.getXzqhCol(), null, "xzqh",FunTypeEnum.DEPARTMENT.getVal(), cursRow);
						throw new ServiceException(errorMsg);
					}else {
						for(Map.Entry<Integer, List<Integer>> entry : idIndexMap.entrySet()) {
							Integer key = entry.getKey();
							List<Integer> value = entry.getValue();
							OrganizationVo organizationVo = grganizationVos.get(key);
							for(int i=0;i<value.size();i++) {
								xzqhIdList.set(value.get(i),  organizationVo.getId());
							}
						}
						result.put("xzqhIdList", xzqhIdList);
					}
				}
			}
		}else if(TreeType.ORG.getVal().equals(treeType)) {
			
			Map<String,Object> bussMap = new HashMap<String,Object>();
			List<String> bussList = treeData.get("bussList");
			bussMap.put("bussList", bussList);
			
			
			
			if(codeList != null && codeList.size()>0) {//编号是否已经存在
				List<OrganizationVo> orgCodeVos = organizationMapper.selectChildOrgsByPid(codeMap);
				if(orgCodeVos != null) {
					List<Integer> list = new ArrayList<Integer>();
					for(OrganizationVo orgVo : orgCodeVos) {
						int rowNo = codeList.indexOf(orgVo.getOrgCode());
						if(rowNo >= 0) {
							list.add(rowNo);
						}
					}
					if(list.size()>0) {
						Collections.sort(list);
						String errorMsg = generateErrorContent(list, treeAttr.getCodeCol(), treeType, "code", FunTypeEnum.ORG.getVal(), cursRow);
						throw new ServiceException(errorMsg);
					}
				}
			}
			if(nameList != null && nameList.size()>0) {//名称是否已经存在
				List<OrganizationVo> orgNameVos = organizationMapper.selectChildOrgsByPid(nameMap);
				if(orgNameVos != null) {
					List<Integer> list = new ArrayList<Integer>();
					for(OrganizationVo orgVo : orgNameVos) {
						int rowNo = nameList.indexOf(orgVo.getName());
						if(rowNo >= 0) {
							list.add(rowNo);
						}
					}
					if(list.size()>0) {
						Collections.sort(list);
						String errorMsg = generateErrorContent(list, treeAttr.getNameCol(), treeType, "name",FunTypeEnum.ORG.getVal(), cursRow);
						throw new ServiceException(errorMsg);
					}
				}
			}
			if(pcodeList != null && pcodeList.size()>0) {
				Map<String,Object> paramMap = new HashMap<String,Object>();
				List<OrganizationVo> orgPCodeVos = organizationMapper.selectChildOrgsByPid(paramMap);
				if(orgPCodeVos != null) {
					List<Integer> list = new ArrayList<Integer>();
					List<String> existList = new ArrayList<String>();
					for(OrganizationVo orgVo : orgPCodeVos) {
						existList.add(orgVo.getOrgCode());
					}
					for(int i=0;i<pcodeList.size();i++) {
						String pcode = pcodeList.get(i);
						if(!"0".equals(pcode) && !existList.contains(pcode) && !codeList.contains(pcode)) {//不是根节点，数据库中没有父编号且excel编号中没有该父编号
							list.add(i);
						}
						if(list.size()==10) {
							break;
						}
					}
					if(list.size()>0) {
						Collections.sort(list);
						String errorMsg = generateErrorContent(list, treeAttr.getPcodeCol(), null, "pcode",FunTypeEnum.ORG.getVal(), cursRow);
						throw new ServiceException(errorMsg);
					}
				}
			}
			if("1".equals(isVirtual)) {
				String[] bussIdArr = new String[bussList.size()];
				List<String> bussIdList = Arrays.asList(bussIdArr);
				Map<String,Object> param = new HashMap<String,Object>();
				//param.put("nameList", bussList);
				List<BussinessGroupVo> bussinessGroupVos = bussinessGroupMapper.selectByParam(param);
				if(bussinessGroupVos != null) {
					List<Integer> list = new ArrayList<Integer>();
					Map<Integer, List<Integer>> idIndexMap = new HashMap<Integer, List<Integer>>();
					List<String> existList = new ArrayList<String>();
					for(BussinessGroupVo bussinessGroupVo : bussinessGroupVos) {
						existList.add(bussinessGroupVo.getName());
					}
					nameTransToId(existList, bussList, list, idIndexMap);
					if(list.size()>0) {
						Collections.sort(list);
						String errorMsg = generateErrorContent(list, treeAttr.getBussCol(), null, "bussiness",FunTypeEnum.ORG.getVal(), cursRow);
						throw new ServiceException(errorMsg);
					}else {
						for(Map.Entry<Integer, List<Integer>> entry : idIndexMap.entrySet()) {
							Integer key = entry.getKey();
							List<Integer> value = entry.getValue();
							BussinessGroupVo bussinessGroupVo = bussinessGroupVos.get(key);
							for(int i=0;i<value.size();i++) {
								bussIdList.set(value.get(i),  bussinessGroupVo.getId());
							}
						}
						result.put("bussIdList", bussIdList);
					}
				}
			}
		}
		return result;
	}
	/**
	 * 数字列号转excel列号
	 * @param col 列索引，从0开始
	 * @return
	 */
	private static String transToExcelCol(int col) {
		StringBuffer excelCol = new StringBuffer();
		if(col/perCols.length == 0) {
			excelCol.append(perCols[col]);
		}else if((col+1)/perCols.length>(perCols.length+1)){
			throw new ServiceException("最多支持"+(perCols.length*perCols.length+1)+"列，请检查");
		}else{
			excelCol.append(perCols[col/perCols.length-1]).append(perCols[col%perCols.length]);
		}
		return excelCol.toString();
	}
	/**
	 * 生成异常信息
	 * @param rowNos
	 * @param colNo
	 * @param treeType
	 * @param columnType
	 * @return
	 */
	private String generateErrorContent(List<Integer> rowNos, Integer colNo, String treeType, String columnType, String funType, Integer cursRow) {
		StringBuffer sb = new StringBuffer();
		sb.append("第"+transToExcelCol(colNo)+"列第");
		for(int i=0;i<rowNos.size();i++) {
			if(i<10) {
				sb.append((rowNos.get(i)+1+cursRow));
				if(rowNos.size()-1>i) {
					sb.append("、");
				}
			}else {
				sb.append((rowNos.get(i)+1+cursRow)+"...");
				break;
			}
		}
		sb.append("行");
		if(FunTypeEnum.ORG.getVal().equals(funType) || FunTypeEnum.DEPARTMENT.getVal().equals(funType)) {
			if("code".equals(columnType)) {
				sb.append("编号已存在！");
			}else if("name".equals(columnType)){
				sb.append("名称已存在！");
			}else if("pcode".equals(columnType)){
				sb.append("父编号不存在！");
			} if("bussiness".equals(columnType)) {
				sb.append("【所属业务分组】不存在！");
			}else if("xzqh".equals(columnType)) {
				sb.append("【所属行政区划】不存在！");
			}
		}else if(FunTypeEnum.CAMERA.getVal().equals(funType)) {
			if("sbbm".equals(columnType)) {
				sb.append("【设备编码】已经存在！");
			}else if("sbmc".equals(columnType)) {
				sb.append("【设备名称】已经存在！");
			}else if("xzqh".equals(columnType)) {
				sb.append("【所属行政区划】不存在！");
			}else if("address".equals(columnType)) {
				sb.append("【所属地址】不存在！");
			}else if("encoder".equals(columnType)) {
				sb.append("【所属编码器】不存在！");
			}else if("bussiness".equals(columnType)) {
				sb.append("【所属业务分组】不存在！");
			}else if("project".equals(columnType)) {
				sb.append("【所属项目】不存在！");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 摄像机关联信息的名称转换成存在的或者不存在的id索引集合
	 * @param existList
	 * @param xzqhList
	 * @param list
	 * @param idIndexs
	 */
	private void nameTransToId(List<String> existList, List<String> xzqhList, List<Integer> list, Map<Integer,List<Integer>> idIndexMap) {
		
		Map<String,List<Integer>> map = new HashMap<String,List<Integer>>();
		
		for(int i=0;i<xzqhList.size();i++) {
			
			if(map.get(xzqhList.get(i)) != null) {
				map.get(xzqhList.get(i)).add(i);
			}else {
				List<Integer> xzqhIndex = new ArrayList<Integer>();
				xzqhIndex.add(i);
				map.put(xzqhList.get(i), xzqhIndex);
			}
		}
		for(Map.Entry<String, List<Integer>> entry : map.entrySet()) {
			String key = entry.getKey();
			List<Integer> value = entry.getValue();
			if(StrUtil.isBlank(key)) {
				continue;
			}
			if(!existList.contains(key)) {
				list.addAll(map.get(key));
			}else {
				int voIndex = existList.indexOf(key);
				idIndexMap.put(voIndex, value);
			}
			if(list.size()==10) {
				break;
			}
		}
	}
	@Override
	public Map<String, List<String>> checkCameraAttrExists(List<Map<String, String>> cameraData, ValidAttrVo treeAttr, Integer cursRow) {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		List<String> sbbmList = new ArrayList<String>();
		List<String> sbmcList = new ArrayList<String>();
		
		List<String> xzqhList = new ArrayList<String>();
		List<String> addressList = new ArrayList<String>();
		List<String> encoderList = new ArrayList<String>();
		List<String> bussList = new ArrayList<String>();
		List<String> projectList = new ArrayList<String>();
		
		for(Map<String,String> map : cameraData) {
			sbbmList.add(map.get("sbbm"));
			sbmcList.add(map.get("sbmc"));
			
			xzqhList.add(map.get("xzqh"));//行政区划名称
			addressList.add(map.get("addressId"));//地址名称
			encoderList.add(map.get("encoderServerId"));//编码器名称
			bussList.add(map.get("bussGroupId"));//业务分组名称
			projectList.add(map.get("projectId"));//项目名称
		}
		
		String[] xzqhIdArr = new String[xzqhList.size()];
		String[] addressIdArr = new String[addressList.size()];
		String[] encoderIdArr = new String[encoderList.size()];
		String[] bussIdArr = new String[bussList.size()];
		String[] projectIdArr = new String[projectList.size()];
		List<String> xzqhIdList = Arrays.asList(xzqhIdArr);
		List<String> addressIdList = Arrays.asList(addressIdArr);
		List<String> encoderIdList = Arrays.asList(encoderIdArr);
		List<String> bussIdList = Arrays.asList(bussIdArr);
		List<String> projectIdList = Arrays.asList(projectIdArr);
		
		if(sbbmList.size()>0) {
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("sbbmList", sbbmList);
			List<CameraVo> cameraVos = cameraMapper.selectCameraByParam(param);
			if(cameraVos != null) {
				List<Integer> list = new ArrayList<Integer>();
				for(CameraVo cameraVo : cameraVos) {
					int rowNo = sbbmList.indexOf(cameraVo.getSbbm());
					if(rowNo >= 0) {
						list.add(rowNo);
					}
					if(list.size()==10) {
						break;
					}
				}
				if(list.size()>0) {
					Collections.sort(list);
					String errorMsg = generateErrorContent(list, treeAttr.getSbbmCol(), null, "sbbm",FunTypeEnum.CAMERA.getVal(), cursRow);
					throw new ServiceException(errorMsg);
				}
			}
		}
		if(sbmcList.size()>0){
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("sbmcList", sbmcList);
			List<CameraVo> cameraVos = cameraMapper.selectCameraByParam(param);
			if(cameraVos != null) {
				List<Integer> list = new ArrayList<Integer>();
				for(CameraVo cameraVo : cameraVos) {
					int rowNo = sbmcList.indexOf(cameraVo.getSbmc());
					if(rowNo >= 0) {
						list.add(rowNo);
					}
					if(list.size()==10) {//只提示前10个异常
						break;
					}
				}
				if(list.size()>0) {
					Collections.sort(list);
					String errorMsg = generateErrorContent(list, treeAttr.getSbmcCol(), null, "sbmc",FunTypeEnum.CAMERA.getVal(), cursRow);
					throw new ServiceException(errorMsg);
				}
			}
		}
		if(xzqhList.size()>0) {
			Map<String,Object> param = new HashMap<String,Object>();
			List<OrganizationVo> orgVos = organizationMapper.selectByParam(param);
			if(orgVos != null) {
				List<Integer> list = new ArrayList<Integer>();
				Map<Integer, List<Integer>> idIndexMap = new HashMap<Integer, List<Integer>>();
				List<String> existList = new ArrayList<String>();
				for(int i=0;i<orgVos.size();i++) {
					OrganizationVo orgVo = orgVos.get(i);
					existList.add(orgVo.getName());
				}
				nameTransToId(existList, xzqhList, list, idIndexMap);
				if(list.size()>0) {
					Collections.sort(list);
					String errorMsg = generateErrorContent(list, treeAttr.getXzqhCol(), null, "xzqh",FunTypeEnum.CAMERA.getVal(), cursRow);
					throw new ServiceException(errorMsg);
				}else {
					for(Map.Entry<Integer, List<Integer>> entry : idIndexMap.entrySet()) {
						Integer key = entry.getKey();
						List<Integer> value = entry.getValue();
						OrganizationVo orgVo = orgVos.get(key);
						for(int i=0;i<value.size();i++) {
							 xzqhIdList.set(value.get(i), orgVo.getId());
						}
					}
					result.put("xzqhIdList", xzqhIdList);
				}
			}
		}
		if(addressList.size()>0) {
			Map<String,Object> param = new HashMap<String,Object>();
			List<AddressVo> addressVos = addressMapper.selectByParam(param);
			if(addressVos != null) {
				List<Integer> list = new ArrayList<Integer>();
				Map<Integer, List<Integer>> idIndexMap = new HashMap<Integer, List<Integer>>();
				List<String> existList = new ArrayList<String>();
				for(AddressVo addressVo : addressVos) {
					existList.add(addressVo.getName());
				}
				nameTransToId(existList, addressList, list, idIndexMap);
				if(list.size()>0) {
					Collections.sort(list);
					String errorMsg = generateErrorContent(list, treeAttr.getAddressCol(), null, "address",FunTypeEnum.CAMERA.getVal(), cursRow);
					throw new ServiceException(errorMsg);
				}else {
					for(Map.Entry<Integer, List<Integer>> entry : idIndexMap.entrySet()) {
						Integer key = entry.getKey();
						List<Integer> value = entry.getValue();
						AddressVo addressVo = addressVos.get(key);
						for(int i=0;i<value.size();i++) {
							addressIdList.set(value.get(i), addressVo.getId());
						}
					}
					result.put("addressIdList", addressIdList);
				}
			}
		}
		if(encoderList.size()>0) {
			Map<String,Object> param = new HashMap<String,Object>();
			//param.put("nameList", encoderList);
			List<EncoderServerVo> encoderServerVos = encoderServerMapper.selectEncoderServerList(param);
			if(encoderServerVos != null) {
				List<Integer> list = new ArrayList<Integer>();
				Map<Integer, List<Integer>> idIndexMap = new HashMap<Integer, List<Integer>>();
				List<String> existList = new ArrayList<String>();
				for(EncoderServerVo encoderServerVo : encoderServerVos) {
					existList.add(encoderServerVo.getName());
				}
				nameTransToId(existList, encoderList, list, idIndexMap);
				if(list.size()>0) {
					Collections.sort(list);
					String errorMsg = generateErrorContent(list, treeAttr.getEncoderCol(), null, "encoder",FunTypeEnum.CAMERA.getVal(), cursRow);
					throw new ServiceException(errorMsg);
				}else {
					for(Map.Entry<Integer, List<Integer>> entry : idIndexMap.entrySet()) {
						Integer key = entry.getKey();
						List<Integer> value = entry.getValue();
						EncoderServerVo encoderServerVo = encoderServerVos.get(key);
						for(int i=0;i<value.size();i++) {
							encoderIdList.set(value.get(i),  encoderServerVo.getId());
						}
					}
					result.put("encoderIdList", encoderIdList);
				}
			}
		}
		if(bussList.size()>0) {
			Map<String,Object> param = new HashMap<String,Object>();
			//param.put("nameList", bussList);
			List<BussinessGroupVo> bussinessGroupVos = bussinessGroupMapper.selectByParam(param);
			if(bussinessGroupVos != null) {
				List<Integer> list = new ArrayList<Integer>();
				Map<Integer, List<Integer>> idIndexMap = new HashMap<Integer, List<Integer>>();
				List<String> existList = new ArrayList<String>();
				for(BussinessGroupVo bussinessGroupVo : bussinessGroupVos) {
					existList.add(bussinessGroupVo.getName());
				}
				nameTransToId(existList, bussList, list, idIndexMap);
				if(list.size()>0) {
					Collections.sort(list);
					String errorMsg = generateErrorContent(list, treeAttr.getBussCol(), null, "bussiness",FunTypeEnum.CAMERA.getVal(), cursRow);
					throw new ServiceException(errorMsg);
				}else {
					for(Map.Entry<Integer, List<Integer>> entry : idIndexMap.entrySet()) {
						Integer key = entry.getKey();
						List<Integer> value = entry.getValue();
						BussinessGroupVo bussinessGroupVo = bussinessGroupVos.get(key);
						for(int i=0;i<value.size();i++) {
							bussIdList.set(value.get(i),  bussinessGroupVo.getId());
						}
					}
					result.put("bussIdList", bussIdList);
				}
			}
		}
		if(projectList.size()>0) {//校验所属项目是否存在，且必须要存在
			Map<String,Object> param = new HashMap<String,Object>();
			//param.put("nameList", projectList);
			List<ProjectVo> projectVos = projectMapper.selectProjectList(param);
			if(projectVos != null) {
				List<Integer> list = new ArrayList<Integer>();//不存在的项目在excel中的索引集合
				Map<Integer, List<Integer>> idIndexMap = new HashMap<Integer, List<Integer>>();
				List<String> existList = new ArrayList<String>();
				for(ProjectVo projectVo : projectVos) {
					existList.add(projectVo.getName());
				}
				nameTransToId(existList, projectList, list, idIndexMap);
				if(list.size()>0) {
					Collections.sort(list);
					String errorMsg = generateErrorContent(list, treeAttr.getProjectCol(), null, "project",FunTypeEnum.CAMERA.getVal(), cursRow);
					throw new ServiceException(errorMsg);
				}else {
					for(Map.Entry<Integer, List<Integer>> entry : idIndexMap.entrySet()) {
						Integer key = entry.getKey();
						List<Integer> value = entry.getValue();
						ProjectVo projectVo = projectVos.get(key);
						for(int i=0;i<value.size();i++) {
							projectIdList.set(value.get(i),  projectVo.getId());
						}
					}
					result.put("projectIdList", projectIdList);
				}
			}
		}
		return result;
		
	}

}
