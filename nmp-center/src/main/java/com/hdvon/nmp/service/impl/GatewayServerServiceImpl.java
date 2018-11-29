package com.hdvon.nmp.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hdvon.nmp.entity.GatewayServer;
import com.hdvon.nmp.entity.GatewayserverMapping;
import com.hdvon.nmp.entity.Project;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.GatewayServerMapper;
import com.hdvon.nmp.mapper.GatewayserverMappingMapper;
import com.hdvon.nmp.mapper.ProjectMapper;
import com.hdvon.nmp.service.IGatewayServerService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * <br>
 * <b>功能：</b>网管服务器管理Service<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class GatewayServerServiceImpl implements IGatewayServerService {

	@Autowired
	private GatewayServerMapper gatewayServerMapper;
	
	@Autowired
	private ProjectMapper projectMapper;
	
	@Autowired
	private GatewayserverMappingMapper gatewayserverMappingMapper;

	@Override
	public void saveGatewayServer(UserVo userVo, GatewayServerParamVo gatewayServerParamVo, List<String> projectIds) {
		GatewayServer gatewayServer = Convert.convert(GatewayServer.class, gatewayServerParamVo);
		Example pojExample = new Example(Project.class);
		pojExample.createCriteria().andIn("id", projectIds);
		List<Project> projects = projectMapper.selectByExample(pojExample);
		if(projects == null || projects.size()==0) {
			throw new ServiceException("所属项目不存在！");
		}
		Example nameExa = new Example(GatewayServer.class);
		if(StrUtil.isBlank(gatewayServerParamVo.getId())) {

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("name", gatewayServerParamVo.getName()).andNotEqualTo("enabled",-1);
			int countName = gatewayServerMapper.selectCountByExample(nameExa);
			if(countName > 0) {
				throw new ServiceException("网关服务器名称已存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("code", gatewayServerParamVo.getCode()).andNotEqualTo("enabled",-1);
			int countCode = gatewayServerMapper.selectCountByExample(nameExa);
			if(countCode > 0) {
				throw new ServiceException("网关服务器编号已经存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("ip", gatewayServerParamVo.getIp()).andNotEqualTo("enabled",-1);
			int countIp = gatewayServerMapper.selectCountByExample(nameExa);
			if(countIp > 0) {
				throw new ServiceException("网关服务器ip已经存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("domainName", gatewayServerParamVo.getDomainName()).andNotEqualTo("enabled",-1);
			int countDomain = gatewayServerMapper.selectCountByExample(nameExa);
			if(countDomain > 0) {
				throw new ServiceException("网关服务器域名已经存在！");
			}

			gatewayServer.setId(IdGenerator.nextId());
			//gatewayServer.setEnabled(0);//默认禁用
			gatewayServer.setCreateTime(new Date());
			gatewayServer.setCreateUser(userVo.getAccount());
			gatewayServer.setUpdateTime(new Date());
			gatewayServer.setUpdateUser(userVo.getAccount());
			gatewayServerMapper.insertSelective(gatewayServer);
		}else {
			nameExa.clear();
			nameExa.createCriteria().andEqualTo("name", gatewayServerParamVo.getName()).andNotEqualTo("id", gatewayServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countName = gatewayServerMapper.selectCountByExample(nameExa);
			if(countName > 0) {
				throw new ServiceException("网关服务器名称已存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("code", gatewayServerParamVo.getCode()).andNotEqualTo("id", gatewayServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countCode = gatewayServerMapper.selectCountByExample(nameExa);
			if(countCode > 0) {
				throw new ServiceException("网关服务器编号已经存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("ip", gatewayServerParamVo.getIp()).andNotEqualTo("id", gatewayServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countIp = gatewayServerMapper.selectCountByExample(nameExa);
			if(countIp > 0) {
				throw new ServiceException("网关服务器ip已经存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("domainName", gatewayServerParamVo.getDomainName()).andNotEqualTo("id", gatewayServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countDomain = gatewayServerMapper.selectCountByExample(nameExa);
			if(countDomain > 0) {
				throw new ServiceException("网关服务器域名已经存在！");
			}
			gatewayServer.setUpdateTime(new Date());
			gatewayServer.setUpdateUser(userVo.getAccount());
			gatewayServerMapper.updateByPrimaryKeySelective(gatewayServer);
			
			Example delPojExample = new Example(GatewayserverMapping.class);
			delPojExample.createCriteria().andEqualTo("gatewayserverId",gatewayServer.getId());
			gatewayserverMappingMapper.deleteByExample(delPojExample);//删除未修改前关联的所有项目
		}
		List<GatewayserverMapping> gatewayserverMappings = new ArrayList<GatewayserverMapping>();
		for(Project project : projects) {
			GatewayserverMapping gatewayserverMapping = new GatewayserverMapping();
			gatewayserverMapping.setId(IdGenerator.nextId());
			gatewayserverMapping.setGatewayserverId(gatewayServer.getId());
			gatewayserverMapping.setProjectId(project.getId());
			gatewayserverMapping.setProjectName(project.getName());
			gatewayserverMapping.setProjectCode(project.getCode());
			gatewayserverMappings.add(gatewayserverMapping);
		}
		gatewayserverMappingMapper.insertList(gatewayserverMappings);
	}

	@Override
	public void delGatewayServersByIds(List<String> ids) {
//		Example projectExa = new Example(GatewayserverMapping.class);
//		projectExa.createCriteria().andIn("gatewayserverId", ids);
//		int countProject = gatewayserverMappingMapper.selectCountByExample(projectExa);
//		if(countProject > 0) {
//			throw new ServiceException("网关服务器存在所属项目，不能删除！");
//		}
		// 删除网关
//		Example sigExa = new Example(GatewayServer.class);
//		sigExa.createCriteria().andIn("id", ids);
//		gatewayServerMapper.deleteByExample(sigExa);
//		// 删除映射表
//		Example mapper = new Example(GatewayserverMapping.class);
//		mapper.createCriteria().andIn("gatewayserverId", ids);
//		gatewayserverMappingMapper.deleteByExample(sigExa);

		// 启用不能删除
		Example example = new Example(GatewayServer.class);
		example.createCriteria().andIn("id",ids).andEqualTo("enabled",1);
		int count = gatewayServerMapper.selectCountByExample(example);
		if (count > 0){
			throw new ServiceException("网关服务器正在启用，不能删除！");
		}

		for(String id : ids){
			GatewayServer gatewayServer = new GatewayServer();
			gatewayServer.setId(id);
			gatewayServer.setEnabled(-1);
			gatewayServerMapper.updateByPrimaryKeySelective(gatewayServer);
		}
	}

	@Override
	public GatewayServerVo getGatewayServerById(String id) {
        Map<String,Object> map = new HashMap<>();
        map.put("id", id);
		GatewayServerVo gatewayServerVo = gatewayServerMapper.selectGatewayServerByParam(map);
        if(gatewayServerVo == null){
            throw new ServiceException("网关服务器不存在");
        }
		map.clear();
		map.put("gatewayserverId", id);
		List<GatewayserverMappingVo> projectMappingVos = gatewayserverMappingMapper.selectByParam(map);
		gatewayServerVo.setGatewayserverMappingVos(projectMappingVos); 
		return gatewayServerVo;
	}

	@Override
	public PageInfo<GatewayServerVo> getGatewayServerPages(PageParam pp, TreeNodeChildren treeNodeChildren, GatewayServerParamVo gatewayServerParamVo) {
		
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", gatewayServerParamVo.getName());
        map.put("code", gatewayServerParamVo.getCode());
        map.put("ip", gatewayServerParamVo.getIp());
        map.put("addrIds", treeNodeChildren.getAddressNodeIds());
        map.put("enabled", gatewayServerParamVo.getEnabled());
        
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
		List<GatewayServerVo> gatewayServerVos = gatewayServerMapper.selectGatewayServersList(map);
		
		if(gatewayServerVos!=null && gatewayServerVos.size()>0) {
			List<String> gatewayServerIds = new ArrayList<String>();
			for(int i=0;i<gatewayServerVos.size();i++) {
				if(gatewayServerVos.get(i) == null) {
					continue;
				}
				gatewayServerIds.add(gatewayServerVos.get(i).getId());
			}
			if(gatewayServerIds.size()>0) {
				Multimap<String,GatewayserverMappingVo> projectMutimap=ArrayListMultimap.create(); 
				List<GatewayserverMappingVo> gatewayserverMappingVos = gatewayserverMappingMapper.selectProjectByGatewayserverIds(gatewayServerIds);
				for(GatewayserverMappingVo gatewayserverMappingVo : gatewayserverMappingVos) {
					projectMutimap.put(gatewayserverMappingVo.getGatewayserverId(), gatewayserverMappingVo);
				}
				for(GatewayServerVo gatewayServerVo : gatewayServerVos) {
					gatewayServerVo.setGatewayserverMappingVos((List<GatewayserverMappingVo>) projectMutimap.get(gatewayServerVo.getId()));
				}
			}
		}
		
		return new PageInfo<GatewayServerVo>(gatewayServerVos);
	}

	@Override
	public List<GatewayServerVo> getGatewayServerList(TreeNodeChildren treeNodeChildren,GatewayServerParamVo gatewayServerParamVo) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", gatewayServerParamVo.getName());
        map.put("code", gatewayServerParamVo.getCode());
        map.put("ip", gatewayServerParamVo.getIp());
        map.put("addrIds", treeNodeChildren.getAddressNodeIds());
        map.put("enabled", gatewayServerParamVo.getEnabled());
        
		List<GatewayServerVo> list = gatewayServerMapper.selectGatewayServersList(map);
		return list;
	}

	@Override
	public String getMaxCodeBycode(Map<String, Object> map) {
		return gatewayServerMapper.selectMaxCodeBycode(map);
	}

}
