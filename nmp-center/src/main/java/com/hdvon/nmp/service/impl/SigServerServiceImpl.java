package com.hdvon.nmp.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hdvon.nmp.entity.Project;
import com.hdvon.nmp.entity.ProjectMapping;
import com.hdvon.nmp.entity.SigServer;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.ProjectMapper;
import com.hdvon.nmp.mapper.ProjectMappingMapper;
import com.hdvon.nmp.mapper.SigServerMapper;
import com.hdvon.nmp.service.ISigServerService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * <br>
 * <b>功能：</b>信令中心服务器表Service<br>
 * <b>作者：</b>guoweijun<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class SigServerServiceImpl implements ISigServerService {

	@Autowired
	private SigServerMapper sigServerMapper;
	
	@Autowired
	private ProjectMappingMapper projectMappingMapper;
	
	@Autowired
	private ProjectMapper projectMapper;

	@Override
	public void saveSigServer(UserVo userVo, SigServerParamVo sigServerParamVo, List<String> projectIds) {
		SigServer sigServer = Convert.convert(SigServer.class, sigServerParamVo);
		Example pojExample = new Example(Project.class);
		pojExample.createCriteria().andIn("id", projectIds);
		List<Project> projects = projectMapper.selectByExample(pojExample);
		if(projects == null || projects.size()==0) {
			throw new ServiceException("中心信令服务器所属项目不存在！");
		}

		Example nameExa = new Example(SigServer.class);
		if(StrUtil.isBlank(sigServerParamVo.getId())) {
			nameExa.clear();
			nameExa.createCriteria().andEqualTo("name", sigServerParamVo.getName()).andNotEqualTo("enabled",-1);
			int countName = sigServerMapper.selectCountByExample(nameExa);
			if(countName > 0) {
				throw new ServiceException("中心信令服务器名称已存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("code", sigServerParamVo.getCode()).andNotEqualTo("enabled",-1);
			int countCode = sigServerMapper.selectCountByExample(nameExa);
			if(countCode > 0) {
				throw new ServiceException("中心信令服务器编号已经存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("ip", sigServerParamVo.getIp()).andNotEqualTo("enabled",-1);
			int countIp = sigServerMapper.selectCountByExample(nameExa);
			if(countIp > 0) {
				throw new ServiceException("中心信令服务器ip已经存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("domainName", sigServerParamVo.getDomainName()).andNotEqualTo("enabled",-1);
			int countDomain = sigServerMapper.selectCountByExample(nameExa);
			if(countDomain > 0) {
				throw new ServiceException("中心信令服务器域名已经存在！");
			}

			sigServer.setId(IdGenerator.nextId());
			sigServer.setCpuStatus("0%");//默认0%
			sigServer.setMemoryStatus("0%");//默认0%
			sigServer.setNetworkStatus("0%");//默认0%
			sigServer.setServerStatus(0);//默认离线0
			sigServer.setCreateTime(new Date());
			sigServer.setCreateUser(userVo.getAccount());
			sigServer.setUpdateTime(new Date());
			sigServer.setUpdateUser(userVo.getAccount());
			sigServerMapper.insertSelective(sigServer);
		}else {
			nameExa.clear();
			nameExa.createCriteria().andEqualTo("name", sigServerParamVo.getName()).andNotEqualTo("id", sigServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countName = sigServerMapper.selectCountByExample(nameExa);
			if(countName > 0) {
				throw new ServiceException("中心信令服务器名称已存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("code", sigServerParamVo.getCode()).andNotEqualTo("id", sigServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countCode = sigServerMapper.selectCountByExample(nameExa);
			if(countCode > 0) {
				throw new ServiceException("中心信令服务器编号已经存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("ip", sigServerParamVo.getIp()).andNotEqualTo("id", sigServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countIp = sigServerMapper.selectCountByExample(nameExa);
			if(countIp > 0) {
				throw new ServiceException("中心信令服务器ip已经存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("domainName", sigServerParamVo.getDomainName()).andNotEqualTo("id", sigServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countDomain = sigServerMapper.selectCountByExample(nameExa);
			if(countDomain > 0) {
				throw new ServiceException("中心信令服务器域名已经存在！");
			}

			sigServer.setUpdateTime(new Date());
			sigServer.setUpdateUser(userVo.getAccount());
			sigServerMapper.updateByPrimaryKeySelective(sigServer);
			
			Example delPojExample = new Example(ProjectMapping.class);
			delPojExample.createCriteria().andEqualTo("sigserverId",sigServer.getId());
			projectMappingMapper.deleteByExample(delPojExample);//删除未修改前关联的所有项目
		}
		List<ProjectMapping> projectMappings = new ArrayList<ProjectMapping>();
		for(Project project : projects) {
			ProjectMapping projectMapping = new ProjectMapping();
			projectMapping.setId(IdGenerator.nextId());
			projectMapping.setSigserverId(sigServer.getId());
			projectMapping.setProjectId(project.getId());
			projectMapping.setProjectName(project.getName());
			projectMapping.setProjectCode(project.getCode());
			projectMappings.add(projectMapping);
		}
		projectMappingMapper.insertList(projectMappings);
	}

	@Override
	public void delSigServersByIds(List<String> ids) {
//		Example projectExa = new Example(ProjectMapping.class);
//		projectExa.createCriteria().andIn("sigserverId", ids);
//		int countProject = projectMappingMapper.selectCountByExample(projectExa);
//		if(countProject > 0) {
//			throw new ServiceException("信令服务器存在所属项目，不能删除！");
//		}

		// 启用不能删除
		Example example = new Example(SigServer.class);
		example.createCriteria().andIn("id",ids).andEqualTo("enabled",1);
		int count = sigServerMapper.selectCountByExample(example);
		if (count > 0){
			throw new ServiceException("中心信令服务器正在启用，不能删除！");
		}

		for(String id : ids){
			SigServer sigServer = new SigServer();
			sigServer.setId(id);
			sigServer.setEnabled(-1);
			sigServerMapper.updateByPrimaryKeySelective(sigServer);
		}

//		Example sigExa = new Example(SigServer.class);
//		sigExa.createCriteria().andIn("id", ids);
//		sigServerMapper.deleteByExample(sigExa);
		
//		Example projectExa = new Example(ProjectMapping.class);
//		projectExa.createCriteria().andIn("sigserverId", ids);
//		projectMappingMapper.deleteByExample(projectExa);
	}

	@Override
	public SigServerVo getSigServerById(SigServerParamVo sigServerParamVo) {
		SigServerVo sigServerVo = sigServerMapper.selectSigServerByParam(sigServerParamVo);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("sigserverId", sigServerParamVo.getId());

//		Example project_exa = new Example(ProjectMapping.class);
//		project_exa.createCriteria().andEqualTo("sigserverId",sigServerParamVo.getId());
//		List<ProjectMapping> list = projectMappingMapper.selectByExample(project_exa);
//		List<ProjectMappingVo> projectMappingVos = BeanHelper.convertList(ProjectMappingVo.class, list);

		List<ProjectMappingVo> projectMappingVos =projectMappingMapper.selectByParam(map);
		sigServerVo.setProjectMappingVos(projectMappingVos); 
		return sigServerVo;
	}

	@Override
	public PageInfo<SigServerVo> getSigServerPages(PageParam pp, TreeNodeChildren treeNodeChildren, SigServerParamVo sigServerParamVo) {
		Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", sigServerParamVo.getName());
        map.put("code", sigServerParamVo.getCode());
        map.put("ip", sigServerParamVo.getIp());
        map.put("serverStatus", sigServerParamVo.getServerStatus());
        map.put("enabled", sigServerParamVo.getEnabled());
        map.put("addrIds", treeNodeChildren.getAddressNodeIds());
	        
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
		List<SigServerVo> sigServerVos = sigServerMapper.selectSigServersList(map);
		
		if(sigServerVos!=null && sigServerVos.size()>0) {
			List<String> sigServerIds = new ArrayList<String>();
			for(int i=0;i<sigServerVos.size();i++) {
				if(sigServerVos.get(i) == null) {
					continue;
				}
				sigServerIds.add(sigServerVos.get(i).getId());
			}
			if(sigServerIds.size()>0) {
				Multimap<String,ProjectMappingVo> projectMutimap=ArrayListMultimap.create(); 
				List<ProjectMappingVo> projectMappingVos = projectMappingMapper.selectProjectBySigserverIds(sigServerIds);
				for(ProjectMappingVo projectMappingVo : projectMappingVos) {
					projectMutimap.put(projectMappingVo.getSigserverId(), projectMappingVo);
				}
				for(SigServerVo sigServerVo : sigServerVos) {
					sigServerVo.setProjectMappingVos((List<ProjectMappingVo>) projectMutimap.get(sigServerVo.getId()));
				}
			}
		}
		return new PageInfo<SigServerVo>(sigServerVos);
	}

	@Override
	public List<SigServerVo> getSigServerList(TreeNodeChildren treeNodeChildren, SigServerParamVo sigServerParamVo) {
		Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", sigServerParamVo.getName());
        map.put("code", sigServerParamVo.getCode());
        map.put("ip", sigServerParamVo.getIp());
        map.put("serverStatus", sigServerParamVo.getServerStatus());
        map.put("enabled", sigServerParamVo.getEnabled());
        map.put("addrIds", treeNodeChildren.getAddressNodeIds());
        
		List<SigServerVo> sigServerVos = sigServerMapper.selectSigServersList(map);
		
		if(sigServerVos!=null && sigServerVos.size()>0) {
			List<String> sigServerIds = new ArrayList<String>();
			for(int i=0;i<sigServerVos.size();i++) {
				if(sigServerVos.get(i) == null) {
					continue;
				}
				sigServerIds.add(sigServerVos.get(i).getId());
			}
			if(sigServerIds.size()>0) {
				Multimap<String,ProjectMappingVo> projectMutimap=ArrayListMultimap.create(); 
				List<ProjectMappingVo> projectMappingVos = projectMappingMapper.selectProjectBySigserverIds(sigServerIds);
				for(ProjectMappingVo projectMappingVo : projectMappingVos) {
					projectMutimap.put(projectMappingVo.getSigserverId(), projectMappingVo);
				}
				for(SigServerVo sigServerVo : sigServerVos) {
					sigServerVo.setProjectMappingVos((List<ProjectMappingVo>) projectMutimap.get(sigServerVo.getId()));
				}
			}
		}
		
		return sigServerVos;
	}

	@Override
	public String getMaxCodeBycode(Map<String, Object> map) {
		return sigServerMapper.selectMaxCodeBycode(map);
	}

}
