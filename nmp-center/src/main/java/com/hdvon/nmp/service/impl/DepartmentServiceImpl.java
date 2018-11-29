package com.hdvon.nmp.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hdvon.nmp.vo.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.config.code.CodeConfig;
import com.hdvon.nmp.entity.Department;
import com.hdvon.nmp.entity.DepartmentMatrix;
import com.hdvon.nmp.entity.Organization;
import com.hdvon.nmp.entity.ProjectDepartment;
import com.hdvon.nmp.entity.UserDepartment;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.DepartmentMapper;
import com.hdvon.nmp.mapper.DepartmentMatrixMapper;
import com.hdvon.nmp.mapper.ProjectDepartmentMapper;
import com.hdvon.nmp.mapper.TreeNodeMapper;
import com.hdvon.nmp.mapper.UserDepartmentDataMapper;
import com.hdvon.nmp.mapper.UserDepartmentMapper;
import com.hdvon.nmp.service.IDepartmentService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.TreeType;
import com.hdvon.nmp.util.snowflake.IdGenerator;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>部门表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class DepartmentServiceImpl implements IDepartmentService {

	@Autowired
	private DepartmentMapper departmentMapper;
	
	@Autowired
	DepartmentMatrixMapper departmentMatrixMapper;
	
	@Autowired
	ProjectDepartmentMapper projectDepartmentMapper;
	
	@Autowired
	UserDepartmentMapper userDepartmentMapper;
	
	@Autowired
	UserDepartmentDataMapper userDepartmentDataMapper;
	
	@Autowired
    private TreeCodeService treeCodeService;
	
	@Autowired
    private TreeNodeMapper treeNodeMapper;
	
	@Autowired
    CodeConfig codeConfig;
	
	@Autowired
    private GenerateTreeService generateTreeService;

	@Override
	public PageInfo<DepartmentVo> getDeptPages(PageParam pp, TreeNodeChildren treeNodeChildren, DepartmentVo deptVo) {
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
        Map<String,Object> param = new HashMap<>();
        param.put("search",deptVo.getName());
        if(treeNodeChildren.getDeptNodeIds().size()>0) {
        	param.put("id", deptVo.getId());
        }
        param.put("deptIds", treeNodeChildren.getDeptNodeIds());
		List<DepartmentVo> list = departmentMapper.selectChildDepartmentsByPid(param);
    	return new PageInfo<>(list);
	}

    @Override
    public void saveDepartment(UserVo userVo, DepartmentVo departmentVo) {
        Department department = Convert.convert(Department.class,departmentVo);

        //检查部门名称
        if(StrUtil.isNotBlank(department.getName())){
            Example example = new Example(Department.class);
            Example.Criteria dc = example.createCriteria();
            dc.andEqualTo("name",department.getName());
            if(StrUtil.isNotBlank(department.getId())){
                dc.andNotEqualTo("id",department.getId());
            }
            int count = departmentMapper.selectCountByExample(example);
            if(count > 0){
                throw new ServiceException("部门名称已经存在！");
            }
        }

        //检查父级节点
        Department parentDept = null;
        if(StrUtil.isNotBlank(department.getPid()) && !"0".equals(department.getPid())){//子节点
            parentDept = departmentMapper.selectByPrimaryKey(department.getPid());
            if(parentDept == null) {
                throw new ServiceException("用于添加子部门的部门不存在！");
            }
        }else{//根节点
            department.setPid("0");
            //department.setParentDepCode("0");
        }
        
        //检查部门编码
        if(StrUtil.isNotBlank(department.getDepCode())){
            Example example = new Example(Department.class);
            Example.Criteria dc = example.createCriteria();
            dc.andEqualTo("depCode",department.getDepCode());
            if(StrUtil.isNotBlank(department.getId())){
                dc.andNotEqualTo("id",department.getId());
            }
            int count = departmentMapper.selectCountByExample(example);
            if(count > 0){
                throw new ServiceException("部门代码已经存在！");
            }
        }else {//部门编码为空，生成一个可用编码
        	String deptCode = treeCodeService.genDepartmentCode(department.getPid());
        	department.setDepCode(deptCode);
        }

        //保存
        if(StringUtils.isNotEmpty(department.getId())) {//编辑部门
            Department entity = departmentMapper.selectByPrimaryKey(department.getId());//编辑初始化，校验是否存在该可编辑部门
            if(entity == null) {
                throw new ServiceException("部门不存在！");
            }
            if(!department.getPid().equals(entity.getPid()) || !department.getDepCode().equals(entity.getDepCode())) {//修改了上级部门或者部门编号
     	    	String oldCode = entity.getDepCodeSplit();//修改前的当前节点的code
     	    	
     	    	String newPid = department.getPid();//修改后的新的父节点id
     	    	String newCode = "";
     	    	if("0".equals(newPid)) {
     	    		newCode = department.getDepCode()+codeConfig.getSplitCodeSuffix();
     	    	}else {
     	    		newCode = treeCodeService.genSplitCode(newPid, department.getDepCode(), TreeType.DEPARTMENT.getVal());//根据新的父节点的id生成当前节点新的code
     	    	}
     	    	oldCode = oldCode + (oldCode.endsWith(codeConfig.getSplitCodeSuffix()) ? "" : codeConfig.getSplitCodeSuffix());
     	    	department.setDepCodeSplit(newCode);
     	    	
     	    	Map<String,Object> paramMap = new HashMap<String,Object>();
     	    	paramMap.put("oldCode", oldCode);
     	    	paramMap.put("newCode", newCode.endsWith(codeConfig.getSplitCodeSuffix()) ? newCode : (newCode+codeConfig.getSplitCodeSuffix()));
     	    	paramMap.put("type", TreeType.DEPARTMENT.getVal());
     	    	treeNodeMapper.updateChildNodesCode(paramMap);//修改子节点编号
     	    }
            
            Date date = new Date();
            department.setUpdateTime(date);
            department.setUpdateUser(userVo.getAccount());
            departmentMapper.updateByPrimaryKeySelective(department);
        }else{//新增部门
            department.setId(IdGenerator.nextId());
            if(!department.getPid().equals("0")) {
            	 String newCode = treeCodeService.genSplitCode(department.getPid(), department.getDepCode(), TreeType.DEPARTMENT.getVal());//生成部门编号
                 department.setDepCodeSplit(newCode);
            }else {
            	 department.setDepCodeSplit(department.getDepCode());// 根节点
            }
            Date date = new Date();
            department.setCreateTime(date);
            department.setCreateUser(userVo.getAccount());
            department.setUpdateUser(userVo.getAccount());
            department.setUpdateTime(date);
            departmentMapper.insertSelective(department);
        }
    }

	@Override
	public void delDepartmentsByIds(List<String> ids){
		Example deDept = new Example(Department.class);
		deDept.createCriteria().andIn("pid",ids);
		int countDept = departmentMapper.selectCountByExample(deDept);
		if(countDept>0) {
			throw new ServiceException("部门下有子部门，不能删除！");
		}
		
		Example pdeConstructor = new Example(ProjectDepartment.class);
		pdeConstructor.createCriteria().andIn("constructorDepId",ids);
		
		int countConstructor = projectDepartmentMapper.selectCountByExample(pdeConstructor);
		if(countConstructor>0) {
			throw new ServiceException("部门下有关联的项目，不能删除！");
		}
		
		Example pdeBuilder = new Example(ProjectDepartment.class);
		pdeBuilder.createCriteria().andIn("constructorDepId",ids);
		
		int countBuilder = projectDepartmentMapper.selectCountByExample(pdeBuilder);
		if(countBuilder>0) {
			throw new ServiceException("部门下有关联的项目，不能删除！");
		}
		
		Example dpe = new Example(DepartmentMatrix.class);
		dpe.createCriteria().andIn("departmentId",ids);
		
		int countMatrix = departmentMatrixMapper.selectCountByExample(dpe);
		if(countMatrix>0) {
			throw new ServiceException("部门下有关联的矩阵服务器，不能删除！");
		}

		Map<String, Object> param = new HashMap<>();
		param.put("deptIds",ids);

		List<UserDepartmentVo> templist = userDepartmentMapper.selectDepartUser(param);
		if(templist !=null && templist.size() > 0) {
			throw new ServiceException("部门下有关联的用户，不能删除！");
		}
		
		Example de = new Example(Department.class);
		de.createCriteria().andIn("id",ids);
		departmentMapper.deleteByExample(de);
		
		//删除部门矩阵关联
		/*DepartmentMatrixExample dpe = new DepartmentMatrixExample();
		dpe.createCriteria().andDepartmentIdIn(ids);
		departmentMatrixMapper.deleteByExample(dpe);*/
	}

	@Override
	public DepartmentVo getDepartmentById(String curId) {
		/*DepartmentVo deptVo = departmentMapper.selectByPrimaryKey(curId);
		return deptVo;*/
		DepartmentVo deptVo = departmentMapper.selectDepartmentInfoById(curId);
		return deptVo;
	}

    @Override
    public List<DepartmentVo> getDeptList(Map<String,Object> map) {
        List<DepartmentVo> list = departmentMapper.selectChildDepartmentsByPid(map);
        return list;
    }

	@Override
	public void batchInsertDepartments(List<Map<String,String>> list, List<CheckAttributeVo> checkVos, UserVo userVo, Map<String,List<String>> relateIdMap) {
		List<Department> depts = transMapToDepts(list, checkVos, userVo, relateIdMap);
		if(depts != null && depts.size()>0) {
			departmentMapper.insertList(depts);
		}
	}
	private List<Department> transMapToDepts(List<Map<String, String>> list,List<CheckAttributeVo> checkVos, UserVo userVo, Map<String,List<String>> relateIdMap){
		List<Department> depts = new ArrayList<Department>();
		List<String> deptCodes = new ArrayList<String>();
		List<String> parentDeptCodes = new ArrayList<String>();
		
		List<Integer> parent_dept_index = new ArrayList<Integer>();
		
		List<String> xzqhIdList = relateIdMap.get("xzqhIdList");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		
		
		for(int i=0;i<list.size();i++) {
			Map<String,String> map = list.get(i);
			
			Class<Department> clzz = null;
			Department dept = null;
			try {
				clzz = (Class<Department>) Class.forName("com.hdvon.nmp.entity.Department");
				dept = clzz.newInstance();
				dept.setId(IdGenerator.nextId());

				for(int j=0;j<checkVos.size();j++) {
					CheckAttributeVo checkVo = checkVos.get(j);
					String attr = checkVo.getAttr();
					if("deptCode".equals(checkVo.getCode())) {
						deptCodes.add(map.get(attr));
					}else if("parentDeptCode".equals(checkVo.getCode())){
						parentDeptCodes.add(map.get(attr));
					}
					String methodAttr = attr.substring(0, 1).toUpperCase()+checkVo.getAttr().substring(1);
					Method getMethod = clzz.getMethod("get"+methodAttr);
					Type type = getMethod.getGenericReturnType();
					if("class java.lang.Integer".equals(type.toString())) {
						Method setMethod = clzz.getMethod("set"+methodAttr, Integer.class);
						setMethod.invoke(dept, StrUtil.isBlank(map.get(attr)) ? null : Integer.parseInt(map.get(attr)));
					}else if("class java.lang.Double".equals(type.toString())) {
						Method setMethod = clzz.getMethod("set"+methodAttr, Double.class);
						setMethod.invoke(dept, StrUtil.isBlank(map.get(attr)) ? null : Double.parseDouble(map.get(attr)));
					}else if("class java.lang.String".equals(type.toString())) {
						Method setMethod = clzz.getMethod("set"+methodAttr, String.class);
						setMethod.invoke(dept, map.get(attr));
					}else if("class java.util.Date".equals(type.toString())) {
						Method setMethod = clzz.getMethod("set"+methodAttr, Date.class);
						JSONObject valid = checkVo.getValid();
						if(valid != null) {
							String date = valid.getString("DATE");
							if(StrUtil.isNotBlank(date)) {
								JSONArray formatArray = (JSONArray) JSONObject.parse(date);
								if(formatArray != null && formatArray.size()>0) {
									SimpleDateFormat format = new SimpleDateFormat(formatArray.getString(0));
									try {
										setMethod.invoke(dept, format.parse(map.get(attr)));
									} catch (ParseException e) {
										e.printStackTrace();
									}
								}
							}
						}
					}
					
				}
				dept.setOrgId(xzqhIdList.get(i));
				
				dept.setCreateTime(new Date());
				dept.setCreateUser(userVo.getAccount());
				dept.setUpdateTime(new Date());
				dept.setUpdateUser(userVo.getAccount());
				depts.add(dept);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int i=0;i<parentDeptCodes.size();i++) {
			if("0".equals(parentDeptCodes.get(i))) {
				parent_dept_index.add(-1);
			}else {
				if(deptCodes.contains(parentDeptCodes.get(i))) {
					parent_dept_index.add(deptCodes.indexOf(parentDeptCodes.get(i)));
				}else {
					parent_dept_index.add(null);
				}
			}
		}
		List<Department> tempExcelDepts = new ArrayList<Department>();//父节点在excel中的节点
		List<Department> tempDataDepts = new ArrayList<Department>();//父节点在数据库中的节点
		List<String> tempCodes = new ArrayList<String>();
		List<String> tempExcelCodes = new ArrayList<String>();
		List<String> tempExcelParentCodes = new ArrayList<String>();
		for(int i=0;i<depts.size();i++) {
			if(parent_dept_index.get(i) == null){//excel编号中没有对应的父编号，要根据父编号到数据库中找父节点
				tempCodes.add(depts.get(i).getParentDepCode());//缓存excel中父节点在数据库中的所有节点父编号
				tempDataDepts.add(depts.get(i));
			}else if(parent_dept_index.get(i) == -1) {//父节点
				depts.get(i).setPid("0");
				tempExcelCodes.add(depts.get(i).getDepCode());
				tempExcelParentCodes.add(depts.get(i).getParentDepCode());
				tempExcelDepts.add(depts.get(i));
			}else {
				depts.get(i).setPid(depts.get(parent_dept_index.get(i)).getId());
				tempExcelCodes.add(depts.get(i).getDepCode());
				tempExcelParentCodes.add(depts.get(i).getParentDepCode());
				tempExcelDepts.add(depts.get(i));
			}
		}
		Map<String,Object> pcodeMap = new HashMap<String,Object>();
		pcodeMap.put("codeList", tempCodes);
		List<DepartmentVo> tempParentDepts = departmentMapper.selectByParam(pcodeMap);
		
		for(Department department : tempDataDepts) {
			for(DepartmentVo DepartmentVo : tempParentDepts) {
				if(department.getParentDepCode().equals(DepartmentVo.getDepCode())) {//父子关系
					department.setPid(DepartmentVo.getId());
					department.setDepCodeSplit(DepartmentVo.getDepCodeSplit()+codeConfig.getSplitCodeSuffix()+"-"+department.getDepCode());
				}
			}
		}
		
		List<Department> result = new ArrayList<Department>();//返回结果
		if(parentDeptCodes.contains("0")) {//excel中有父编号为0的根节点
			List<Integer> tempParentIndex = new ArrayList<Integer>();
			for(int i=0;i<tempExcelParentCodes.size();i++) {
				if("0".equals(tempExcelParentCodes.get(i))) {
					tempParentIndex.add(-1);
				}else {
					if(tempExcelCodes.contains(tempExcelParentCodes.get(i))) {
						tempParentIndex.add(tempExcelCodes.indexOf(tempExcelParentCodes.get(i)));
					}else {
						tempParentIndex.add(null);
					}
				}
			}
			Department rootDept = null;
			for(int i=0;i<tempExcelDepts.size();i++) {
				Department dept = tempExcelDepts.get(i);
				if(tempParentIndex.get(i)==null)continue;
				if(tempParentIndex.get(i) == -1) {
					dept.setPid("0");
					dept.setDepCodeSplit(dept.getDepCode());
					rootDept = dept;
				}else {
					dept.setPid(depts.get(tempParentIndex.get(i)).getId());
				}
			}
			
			result.add(rootDept);
			setCodeSplit(rootDept, tempExcelDepts, result);
			for(Department department : tempDataDepts) {
				setCodeSplit(department, tempExcelDepts, result);
			}
			result.addAll(tempDataDepts);//使用数据库数据更新excel中父节点在数据库中的节点数据，并添加到excel数据集合中
		}else {//没有根节点，说明没有父节点在excel中的节点，即excel中节点的父节点都在数据库中
			result.addAll(tempDataDepts);
			
		}
		return result;
	}
	/**
	 * @param rootCode
	 * @param index
	 * @param depts
	 */
	private void setCodeSplit(Department dept,List<Department> depts, List<Department> newDepts) {
		for(int i=0;i<depts.size();i++) {
			if(depts.get(i) == null) {
				continue;
			}
			if(dept.getDepCode().equals(depts.get(i).getParentDepCode())) {
				Department department = depts.get(i);
				department.setDepCodeSplit(dept.getDepCodeSplit()+codeConfig.getSplitCodeSuffix()+"-"+department.getDepCode());
				newDepts.add(department);
				depts.set(i, null);
				setCodeSplit(department,depts,newDepts);
			}
		}
	}
	@Override
	public List<DepartmentVo> getUserDepartments(String userId) {
		List<String> ids= new ArrayList<String>();
		List<DepartmentVo> dataList=departmentMapper.selectDepartmentByUserId(userId);
		List<String> pids= new ArrayList<String>();
		for(DepartmentVo list:dataList) {
			for(DepartmentVo data:dataList) {
				pids.add(data.getId());
			}
			if(! pids.contains(list.getPid())) {
				list.setPid("0");
			}
		}
		return dataList;
	}
    /**
     * 根据用户查询可授权部门用户树列表
     * @param userId 用户id
     * @return
     */
	@Override
	public List<DepartmentUserTreeVo> getUserAuthorizeDeptTree(String userId) {
		// TODO Auto-generated method stub
		//根据用户id获取其授权的部门
		return userDepartmentDataMapper.getUserAuthorizeDeptTree(userId);
	}

	@Override
	public List<DepartmentVo> getDepartmentByPid(String pid) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("pid", pid);
		List<DepartmentVo> list= departmentMapper.selectByParam(param);
		return list;
	}

	@Override
	public PageInfo<DepartmentVo> getDepartmentUserCount(Map<String, Object> param, PageParam pageParam) {
		PageHelper.startPage(pageParam.getPageNo(), pageParam.getPageSize());
        List<DepartmentVo> list = departmentMapper.selectDepartmentUserCount(param);
        return new PageInfo<>(list);
	}

	@Override
	public List<DepartmentVo> getDepartmentList(DepartmentVo departmentVo, TreeNodeChildren treeNodeChildren) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("search", departmentVo.getName());
		param.put("deptIds", treeNodeChildren.getDeptNodeIds());
		List<DepartmentVo> list = departmentMapper.selectChildDepartmentsByPid(param);
		return list;
	}
	

}
