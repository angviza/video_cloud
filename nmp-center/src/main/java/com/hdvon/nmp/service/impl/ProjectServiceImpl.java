package com.hdvon.nmp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hdvon.nmp.entity.*;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdvon.nmp.service.IProjectService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.DepartmentProject;
import com.hdvon.nmp.vo.OrganizationVo;
import com.hdvon.nmp.vo.ProjectDepartmentVo;
import com.hdvon.nmp.vo.ProjectParamVo;
import com.hdvon.nmp.vo.ProjectVo;
import com.hdvon.nmp.vo.TreeNodeChildren;
import com.hdvon.nmp.vo.UserVo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * <br>
 * <b>功能：</b>项目信息Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class ProjectServiceImpl implements IProjectService {

	@Autowired
	private ProjectMapper projectMapper;
	
	@Autowired
	private ProjectDepartmentMapper projectDepartmentMapper;
	
	@Autowired
	private DepartmentMapper departmentMapper;
	
	@Autowired
	private EncodeserverMappingMapper encodeserverMappingMapper;
	
	@Autowired
	private CameraMappingMapper cameraMappingMapper;

	@Autowired
	private AlarmserverMappingMapper alarmserverMappingMapper;

	@Autowired
	private ProjectMappingMapper projectMappingMapper;

	@Autowired
	private TranspondserverMappingMapper transpondserverMappingMapper;

	@Autowired
	private StoreserverMappingMapper storeserverMappingMapper;

	@Autowired
	private StatusserverMappingMapper statusserverMappingMapper;

	@Autowired
	private GatewayserverMappingMapper gatewayserverMappingMapper;

	@Override
	public void addProject(UserVo userVo, ProjectVo projectVo){
		Project project = Convert.convert(Project.class,projectVo);
		ProjectDepartment projectDepartment = Convert.convert(ProjectDepartment.class,projectVo);
		
	        if(StrUtil.isNotBlank(projectVo.getId())) {//编辑项目
	        	Example peName = new Example(Project.class);
	        	peName.createCriteria().andEqualTo("name", projectVo.getName()).andNotEqualTo("id", projectVo.getId());
	        	int countName = projectMapper.selectCountByExample(peName);
	    		if(countName>0) {
	    			throw new ServiceException("项目名称已存在！");
	    		}
	    		Example peCode = new Example(Project.class);
	    		peCode.createCriteria().andEqualTo("code", projectVo.getCode()).andNotEqualTo("id", projectVo.getId());
	    		int countCode = projectMapper.selectCountByExample(peCode);
	        	if(countCode>0) {
	        		throw new ServiceException("项目编号已存在！");
	        	}
	        	String constructorDepId = projectVo.getConstructorDepId();
	        	Example deConstructor = new Example(Department.class);
	        	deConstructor.createCriteria().andEqualTo("id", constructorDepId).andEqualTo("isConstructor", "1");
	        	int countConstructor = departmentMapper.selectCountByExample(deConstructor);
	        	if(countConstructor == 0) {
	        		throw new ServiceException("没有选择正确的建设单位！");
	        	}
	        	
	        	String builderDepId = projectVo.getBuilderDepId();
	        	Example deBuilder = new Example(Department.class);
	        	deBuilder.createCriteria().andEqualTo("id", builderDepId).andEqualTo("isBuilder", "1");
	        	int countBuilder = departmentMapper.selectCountByExample(deBuilder);
	        	if(countBuilder == 0) {
	        		throw new ServiceException("没有选择正确的承建单位！");
	        	}
	        	project.setUpdateTime(new Date());
	        	project.setUpdateUser(userVo.getAccount());

	            projectMapper.updateByPrimaryKeySelective(project);
	            
	            Example pde = new Example(ProjectDepartment.class);
	            pde.createCriteria().andEqualTo("projectId", project.getId());
	            projectDepartmentMapper.updateByExampleSelective(projectDepartment, pde);
	        }else if(StrUtil.isNotBlank(projectVo.getConstructorDepId()) || StrUtil.isNotBlank(projectVo.getBuilderDepId())){//增加指定部门的项目
	        	Example peName = new Example(Project.class);
	        	peName.createCriteria().andEqualTo("name", projectVo.getName());
	        	int countName = projectMapper.selectCountByExample(peName);
	    		if(countName>0) {
	    			throw new ServiceException("项目名称已存在！");
	    		}
	    		Example peCode = new Example(Project.class);
	    		peCode.createCriteria().andEqualTo("code", projectVo.getCode());
	    		int countCode = projectMapper.selectCountByExample(peCode);
	        	if(countCode>0) {
	        		throw new ServiceException("项目编号已存在！");
	        	}
	        	String constructorDepId = projectVo.getConstructorDepId();
	        	if(StrUtil.isNotBlank(constructorDepId)) {
		        	Example deConstructor = new Example(Department.class);
	        		deConstructor.createCriteria().andEqualTo("id", constructorDepId).andEqualTo("isConstructor", "1");
	        		int countConstructor = departmentMapper.selectCountByExample(deConstructor);
		        	if(countConstructor == 0) {
		        		throw new ServiceException("没有选择正确的建设单位！");
		        	}
	        	}
	        	String builderDepId = projectVo.getBuilderDepId();
	        	if(StrUtil.isNotBlank(builderDepId)) {
	        		Example deBuilder = new Example(Department.class);
		        	deBuilder.createCriteria().andEqualTo("id",builderDepId).andEqualTo("isBuilder", "1");
		        	int countBuilder = departmentMapper.selectCountByExample(deBuilder);
		        	if(countBuilder == 0) {
		        		throw new ServiceException("没有选择正确的承建单位！");
		        	}
	        	}
	    		String projectId = IdGenerator.nextId();
	        	project.setId(projectId);
	        	Date date = new Date();
	        	project.setCreateTime(date);
	        	project.setCreateUser(userVo.getAccount());
	        	project.setUpdateTime(date);
	        	project.setUpdateUser(userVo.getAccount());
	        	projectMapper.insertSelective(project);
	        	
	        	projectDepartment.setId(IdGenerator.nextId());
	        	projectDepartment.setProjectId(projectId);
	        	projectDepartment.setConstructorDepId(projectVo.getConstructorDepId());
	        	projectDepartment.setBuilderDepId(projectVo.getBuilderDepId());
	        	projectDepartmentMapper.insertSelective(projectDepartment);
	        }
	}

	@Override
	public void delProjectsByIds(List<String> ids) {
		// 校验项目是否关联编码器
		Example eme = new Example(EncodeserverMapping.class);
		eme.createCriteria().andIn("projectId", ids);
        int countEncodes = encodeserverMappingMapper.selectCountByExample(eme);
        if(countEncodes>0) {
        	throw new ServiceException("项目下有关联编码器，不能删除！");
        }

		// 校验项目是否关联摄像机
        Example cme = new Example(CameraMapping.class);
        cme.createCriteria().andIn("projectId", ids);
        int countCameras = cameraMappingMapper.selectCountByExample(cme);
        if(countCameras > 0) {
        	throw new ServiceException("项目下有关联的摄像头，不能删除");
        }

		Map<String, Object> param = new HashMap<>();
		param.put("projectIds",ids);
		// 校验项目是否关联报警设备
		int countArm = alarmserverMappingMapper.selectAlarmProject(param);
		if(countArm > 0) {
			throw new ServiceException("项目下有关联报警设备，不能删除");
		}

		// 校验项目是否关联中心信令
		int pcount = projectMappingMapper.selectSigServerProject(param);
		if(pcount > 0) {
			throw new ServiceException("项目下有关联中心信令服务器，不能删除");
		}

		// 校验项目是否关联转发服务器
		int tcount = transpondserverMappingMapper.selectTranServerProject(param);
		if(tcount > 0) {
			throw new ServiceException("项目下有关联转发服务器，不能删除");
		}

		// 校验项目是否关联存储服务器
		int scount = storeserverMappingMapper.selectStoreServerProject(param);
		if(scount > 0) {
			throw new ServiceException("项目下有关联存储服务器，不能删除");
		}

		// 校验项目是否关联状态服务器
		int stacount = statusserverMappingMapper.selectStatusServerProject(param);
		if(stacount > 0) {
			throw new ServiceException("项目下有关联状态服务器，不能删除");
		}

		// 校验项目是否关联网关服务器
		int gatcount = gatewayserverMappingMapper.selectGatewayServerProject(param);
		if(gatcount > 0) {
			throw new ServiceException("项目下有关联网关服务器，不能删除");
		}

		Example pe = new Example(Project.class);
		pe.createCriteria().andIn("id", ids);
		projectMapper.deleteByExample(pe);
		
		Example pde  = new Example(ProjectDepartment.class);
		pde.createCriteria().andIn("projectId", ids);
		projectDepartmentMapper.deleteByExample(pde);
	}

	@Override
	public ProjectVo getProjectById(ProjectDepartmentVo projectDepartmentVo){
		ProjectVo projectVo = projectMapper.selectProjectInfoById(projectDepartmentVo);
		if(projectVo == null) {
			throw new ServiceException("查询不到该项目！");
		}
		return projectVo;
	}

	@Override
	public PageInfo<ProjectVo> getProjectPages(PageParam pp, TreeNodeChildren treeNodeChildren, ProjectParamVo projectParamVo) {
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());

		Map<String,Object> map = new HashMap<String, Object>();
		map.put("deptIds", treeNodeChildren.getDeptNodeIds());
		map.put("search", projectParamVo.getProjectName());
    	List<ProjectVo> list = projectMapper.selectProjectList(map);
    	return new PageInfo<ProjectVo>(list);
	}


	@Override
	public void batchInsertProjects(List<Map<String, Object>> list, String[] titles) {
		List<Project> projects = new ArrayList<Project>();
		List<ProjectDepartment> projectDepts = new ArrayList<ProjectDepartment>();
		
		/*List<String> builderCodes = new ArrayList<String>();
		List<String> constructorCodes = new ArrayList<String>();*/
		
		//"name","code","classType","contactor","phone","mobile","mail","builderCode","constructorCode"
		for(Map<String,Object> map : list) {
			Project proj = new Project();
			
			proj.setId(IdGenerator.nextId());
			proj.setName(map.get(titles[0]) == null ?"" : map.get(titles[0]).toString());
			proj.setCode(map.get(titles[1]) == null ?"" : map.get(titles[1]).toString());
			proj.setClassType(map.get(titles[2]) == null?null:(Integer) map.get(titles[2]));
			proj.setContactor(map.get(titles[3]) == null ? "" : map.get(titles[3]).toString());
			proj.setTeleno(map.get(titles[4]) == null ? "" : map.get(titles[4]).toString());
			proj.setMobile(map.get(titles[5]) == null?"":map.get(titles[5]).toString());
			proj.setEmail(map.get(titles[6]) == null ? "" : map.get(titles[6]).toString());
			/*
			builderCodes.add(map.get(titles[7]) == null ? "" : map.get(titles[7]).toString());
			constructorCodes.add(map.get(titles[8]) == null ? "" : map.get(titles[8]).toString());*/
			
			ProjectDepartment projectDepartment = null;
			String builderCode = map.get(titles[7]) == null ? "" : map.get(titles[7]).toString();
			if(StrUtil.isNotBlank(builderCode)) {
				Example builderDeptExa = new Example(Department.class);
				builderDeptExa.createCriteria().andEqualTo("depCode", builderCode).andEqualTo("isBuilder",1);
				List<Department> builderDepts = departmentMapper.selectByExample(builderDeptExa);
				if(builderDepts != null && builderDepts.size()>0) {//是承建
					projectDepartment = new ProjectDepartment();
					projectDepartment.setId(IdGenerator.nextId());
					projectDepartment.setProjectId(proj.getId());
					projectDepartment.setBuilderDepId(builderDepts.get(0).getId());
				}
			}
			String constructorCode = map.get(titles[8]) == null ? "" : map.get(titles[8]).toString();
			if(StrUtil.isNotBlank(constructorCode)) {
				Example constructorDeptExa = new Example(Department.class);
				constructorDeptExa.createCriteria().andEqualTo("depCode", constructorCode).andEqualTo("isBuilder",1);
				List<Department> constructorDepts = departmentMapper.selectByExample(constructorDeptExa);
				if(constructorDepts != null && constructorDepts.size()>0) {
					if(projectDepartment == null) {//只是建设
						projectDepartment = new ProjectDepartment();
						projectDepartment.setId(IdGenerator.nextId());
						projectDepartment.setProjectId(proj.getId());
						projectDepartment.setConstructorDepId(constructorDepts.get(0).getId());
					}else {//既是承建又是建设
						projectDepartment.setConstructorDepId(constructorDepts.get(0).getId());
					}
				}
			}
			/*if(projectDepartment == null) {
				throw new ServiceException("项目【"+map.get(titles[0].toString())+"】没有关联的承建或者建设单位！");
			}*/
			if(projectDepartment != null) {
				projectDepts.add(projectDepartment);
			}
			
			projects.add(proj);
			
			
		}
		projectMapper.insertList(projects);
		projectDepartmentMapper.insertList(projectDepts);
		/*Example builderDeptExa = new Example(Department.class);
		builderDeptExa.createCriteria().andIn("depCode", builderCodes).andEqualTo("isBuilder",1);
		List<Department> builderDepts = departmentMapper.selectByExample(builderDeptExa);

		Example constructorDeptExa = new Example(Department.class);
		constructorDeptExa.createCriteria().andIn("depCode", constructorCodes).andEqualTo("isConstructor",1);
		List<Department> constructorDepts = departmentMapper.selectByExample(constructorDeptExa);*/
	}
}
