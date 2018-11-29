package com.hdvon.nmp.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hdvon.nmp.entity.Project;
import com.hdvon.nmp.entity.TranspondServer;
import com.hdvon.nmp.entity.TranspondserverMapping;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.ProjectMapper;
import com.hdvon.nmp.mapper.TranspondServerMapper;
import com.hdvon.nmp.mapper.TranspondserverMappingMapper;
import com.hdvon.nmp.service.ITranspondServerService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * <br>
 * <b>功能：</b>转发服务器Service<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class TranspondServerServiceImpl implements ITranspondServerService {

	@Autowired
	private TranspondServerMapper transpondServerMapper;

	@Autowired
	private ProjectMapper projectMapper;
	
	@Autowired
	private TranspondserverMappingMapper transpondserverMappingMapper;
	
	@Override
	public void saveTranspondServer(UserVo userVo, TranspondServerParamVo transpondServerParamVo, List<String> projectIds) {
		TranspondServer transpondServer = Convert.convert(TranspondServer.class, transpondServerParamVo);
		Example pojExample = new Example(Project.class);
		pojExample.createCriteria().andIn("id", projectIds);
		List<Project> projects = projectMapper.selectByExample(pojExample);
		if(projects == null || projects.size()==0) {
			throw new ServiceException("转发服务器不存在所属项目！");
		}

		Example nameExa = new Example(TranspondServer.class);
		if(StrUtil.isBlank(transpondServerParamVo.getId())) {
			nameExa.clear();
			nameExa.createCriteria().andEqualTo("name", transpondServerParamVo.getName()).andNotEqualTo("enabled",-1);
			int countName = transpondServerMapper.selectCountByExample(nameExa);
			if(countName > 0) {
				throw new ServiceException("转发服务器名称已存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("code", transpondServerParamVo.getCode()).andNotEqualTo("enabled",-1);
			int countCode = transpondServerMapper.selectCountByExample(nameExa);
			if(countCode > 0) {
				throw new ServiceException("转发服务器编号已经存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("ip", transpondServerParamVo.getIp()).andNotEqualTo("enabled",-1);
			int countIp = transpondServerMapper.selectCountByExample(nameExa);
			if(countIp > 0) {
				throw new ServiceException("转发服务器ip已经存在！");
			}

			/*Example domainExa = new Example(TranspondServer.class);
			domainExa.createCriteria().andEqualTo("domainName", transpondServerParamVo.getDomainName()).andNotEqualTo("enabled",-1);
			int countDomain = transpondServerMapper.selectCountByExample(domainExa);
			if(countDomain > 0) {
				throw new ServiceException("转发服务器域名已经存在！");
			}*/
			transpondServer.setId(IdGenerator.nextId());
			transpondServer.setServerStatus(0);//默认离线0
			transpondServer.setCreateTime(new Date());
			transpondServer.setCreateUser(userVo.getAccount());
			transpondServer.setUpdateTime(new Date());
			transpondServer.setUpdateUser(userVo.getAccount());
			transpondServerMapper.insertSelective(transpondServer);
		}else {
			nameExa.clear();
			nameExa.createCriteria().andEqualTo("name", transpondServerParamVo.getName()).andNotEqualTo("id", transpondServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countName = transpondServerMapper.selectCountByExample(nameExa);
			if(countName > 0) {
				throw new ServiceException("转发服务器名称已存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("code", transpondServerParamVo.getCode()).andNotEqualTo("id", transpondServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countCode = transpondServerMapper.selectCountByExample(nameExa);
			if(countCode > 0) {
				throw new ServiceException("转发服务器编号已经存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("ip", transpondServerParamVo.getIp()).andNotEqualTo("id", transpondServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countIp = transpondServerMapper.selectCountByExample(nameExa);
			if(countIp > 0) {
				throw new ServiceException("转发服务器ip已经存在！");
			}

			/*Example domainExa = new Example(TranspondServer.class);
			domainExa.createCriteria().andEqualTo("domainName", transpondServerParamVo.getDomainName()).andNotEqualTo("id", transpondServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countDomain = transpondServerMapper.selectCountByExample(domainExa);
			if(countDomain > 0) {
				throw new ServiceException("转发服务器域名已经存在！");
			}*/
			transpondServer.setUpdateTime(new Date());
			transpondServer.setUpdateUser(userVo.getAccount());
			transpondServerMapper.updateByPrimaryKeySelective(transpondServer);
			
			Example delPojExample = new Example(TranspondserverMapping.class);
			delPojExample.createCriteria().andEqualTo("transserverId",transpondServer.getId());
			transpondserverMappingMapper.deleteByExample(delPojExample);//删除未修改前关联的所有项目
		}
		List<TranspondserverMapping> transpondserverMappings = new ArrayList<TranspondserverMapping>();
		for(Project project : projects) {
			TranspondserverMapping transpondserverMapping = new TranspondserverMapping();
			transpondserverMapping.setId(IdGenerator.nextId());
			transpondserverMapping.setTransserverId(transpondServer.getId());
			transpondserverMapping.setProjectId(project.getId());
			transpondserverMapping.setProjectName(project.getName());
			transpondserverMapping.setProjectCode(project.getCode());
			transpondserverMappings.add(transpondserverMapping);
		}
		transpondserverMappingMapper.insertList(transpondserverMappings);
		
	}

	@Override
	public void delTranspondServersByIds(List<String> ids) {
//		Example projectExa = new Example(TranspondserverMapping.class);
//		projectExa.createCriteria().andIn("transserverId", ids);
//		int countProject = transpondserverMappingMapper.selectCountByExample(projectExa);
//		if(countProject > 0) {
//			throw new ServiceException("转发服务器存在所属项目，不能删除！");
//		}
//		Example sigExa = new Example(TranspondServer.class);
//		sigExa.createCriteria().andIn("id", ids);
//		transpondServerMapper.deleteByExample(sigExa);
//
//		Example projectExa = new Example(TranspondserverMapping.class);
//		projectExa.createCriteria().andIn("transserverId", ids);
//		transpondserverMappingMapper.deleteByExample(projectExa);

		// 启用不能删除
		Example example = new Example(TranspondServer.class);
		example.createCriteria().andIn("id",ids).andEqualTo("enabled",1);
		int count = transpondServerMapper.selectCountByExample(example);
		if (count > 0){
			throw new ServiceException("转发服务器正在启用，不能删除！");
		}

		for(String id : ids){
			TranspondServer transpondServer = new TranspondServer();
			transpondServer.setId(id);
			transpondServer.setEnabled(-1);
			transpondServerMapper.updateByPrimaryKeySelective(transpondServer);
		}
	}

	@Override
	public TranspondServerVo getTranspondServerById(TranspondServerParamVo transpondServerParamVo) {
		TranspondServerVo transpondServerVo = transpondServerMapper.selectTranspondServerByParam(transpondServerParamVo);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("transpondserverId", transpondServerParamVo.getId());
		/*Example project_exa = new Example(TranspondserverMapping.class);
		project_exa.createCriteria().andEqualTo("transpondserverId",transpondServerParamVo.getId());
		List<TranspondserverMapping> list = transpondserverMappingMapper.selectByExample(project_exa);
		List<TranspondserverMappingVo> transpondserverMappingVos = BeanHelper.convertList(TranspondserverMappingVo.class, list);
		*/
		List<TranspondserverMappingVo> transpondserverMappingVos= transpondserverMappingMapper.selectByParam(map);
		transpondServerVo.setTranspondserverMappingVos(transpondserverMappingVos);
		return transpondServerVo;
	}

	@Override
	public PageInfo<TranspondServerVo> getTranspondServerPages(PageParam pp, TreeNodeChildren treeNodeChildren, TranspondServerParamVo transpondServerParamVo) {
		
		Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", transpondServerParamVo.getName());
        map.put("code", transpondServerParamVo.getCode());
        map.put("ip", transpondServerParamVo.getIp());
        map.put("serverStatus", transpondServerParamVo.getServerStatus());
        map.put("enabled", transpondServerParamVo.getEnabled());
        map.put("addrIds", treeNodeChildren.getAddressNodeIds());
		
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
		List<TranspondServerVo> transpondServerVos = transpondServerMapper.selectTranspondServersList(map);
		
		if(transpondServerVos!=null && transpondServerVos.size()>0) {
			List<String> transpondServerIds = new ArrayList<String>();
			for(int i=0;i<transpondServerVos.size();i++) {
				if(transpondServerVos.get(i) == null) {
					continue;
				}
				transpondServerIds.add(transpondServerVos.get(i).getId());
			}
			if(transpondServerIds.size()>0) {
				Multimap<String,TranspondserverMappingVo> projectMutimap=ArrayListMultimap.create(); 
				List<TranspondserverMappingVo> transpondserverMappingVos = transpondserverMappingMapper.selectProjectByTransserverIds(transpondServerIds);
				for(TranspondserverMappingVo transpondserverMappingVo : transpondserverMappingVos) {
					projectMutimap.put(transpondserverMappingVo.getTransserverId(), transpondserverMappingVo);
				}
				for(TranspondServerVo transpondServerVo : transpondServerVos) {
					transpondServerVo.setTranspondserverMappingVos((List<TranspondserverMappingVo>) projectMutimap.get(transpondServerVo.getId()));
				}
			}
		}
		
		return new PageInfo<TranspondServerVo>(transpondServerVos);
	}

	@Override
	public List<TranspondServerVo> getTranspondServerList(TreeNodeChildren treeNodeChildren, TranspondServerParamVo transpondServerParamVo) {
		
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", transpondServerParamVo.getName());
        map.put("code", transpondServerParamVo.getCode());
        map.put("ip", transpondServerParamVo.getIp());
        map.put("serverStatus", transpondServerParamVo.getServerStatus());
        map.put("enabled", transpondServerParamVo.getEnabled());
        map.put("addrIds", treeNodeChildren.getAddressNodeIds());
        
		List<TranspondServerVo> transpondServerVos = transpondServerMapper.selectTranspondServersList(map);
		
		if(transpondServerVos!=null && transpondServerVos.size()>0) {
			List<String> transpondServerIds = new ArrayList<String>();
			for(int i=0;i<transpondServerVos.size();i++) {
				if(transpondServerVos.get(i) == null) {
					continue;
				}
				transpondServerIds.add(transpondServerVos.get(i).getId());
			}
			if(transpondServerIds.size()>0) {
				Multimap<String,TranspondserverMappingVo> projectMutimap=ArrayListMultimap.create(); 
				List<TranspondserverMappingVo> transpondserverMappingVos = transpondserverMappingMapper.selectProjectByTransserverIds(transpondServerIds);
				for(TranspondserverMappingVo transpondserverMappingVo : transpondserverMappingVos) {
					projectMutimap.put(transpondserverMappingVo.getTransserverId(), transpondserverMappingVo);
				}
				for(TranspondServerVo transpondServerVo : transpondServerVos) {
					transpondServerVo.setTranspondserverMappingVos((List<TranspondserverMappingVo>) projectMutimap.get(transpondServerVo.getId()));
				}
			}
		}
		
		return transpondServerVos;
	}

	@Override
	public String getMaxCodeBycode(Map<String, Object> map) {
		
		return transpondServerMapper.selectMaxCodeBycode(map);
	}

}
