package com.hdvon.nmp.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hdvon.nmp.entity.*;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.*;
import com.hdvon.nmp.service.IStatusServerService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * <br>
 * <b>功能：</b>状态服务器Service<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class StatusServerServiceImpl implements IStatusServerService {

	@Autowired
	private StatusServerMapper statusServerMapper;
	@Autowired
	private ProjectMapper projectMapper;
	
	@Autowired
	private StatusserverMappingMapper statusserverMappingMapper;

	@Autowired
	private StatusserverCameraMapper statusserverCameraMapper;
	
	@Autowired
	private StoreserverCameraMapper storeserverCameraMapper;
	
	@Autowired
	private CameraMapper cameraMapper;

	@Override
	public void saveStatusServer(UserVo userVo, StatusServerParamVo statusServerParamVo, List<String> projectIds) {
		StatusServer statusServer = Convert.convert(StatusServer.class, statusServerParamVo);
		Example pojExample = new Example(Project.class);
		pojExample.createCriteria().andIn("id", projectIds);
		List<Project> projects = projectMapper.selectByExample(pojExample);
		if(projects == null || projects.size()==0) {
			throw new ServiceException("状态服务器不存在所属项目！");
		}

		Example nameExa = new Example(StoreServer.class);
		if(StrUtil.isBlank(statusServerParamVo.getId())) {
			nameExa.clear();
			nameExa.createCriteria().andEqualTo("name", statusServerParamVo.getName()).andNotEqualTo("enabled",-1);
			int countName = statusServerMapper.selectCountByExample(nameExa);
			if(countName > 0) {
				throw new ServiceException("状态服务器名称已存在！");
			}
			
			nameExa.clear();
			nameExa.createCriteria().andEqualTo("code", statusServerParamVo.getCode()).andNotEqualTo("enabled",-1);
			int countCode = statusServerMapper.selectCountByExample(nameExa);
			if(countCode > 0) {
				throw new ServiceException("状态服务器编号已经存在！");
			}
			
			nameExa.clear();
			nameExa.createCriteria().andEqualTo("ip", statusServerParamVo.getIp()).andNotEqualTo("enabled",-1);
			int countIp = statusServerMapper.selectCountByExample(nameExa);
			if(countIp > 0) {
				throw new ServiceException("状态服务器ip已经存在！");
			}
			
			/*Example domainExa = new Example(TranspondServer.class);
			domainExa.createCriteria().andEqualTo("domainName", transpondServerParamVo.getDomainName()).andNotEqualTo("enabled",-1);
			int countDomain = transpondServerMapper.selectCountByExample(domainExa);
			if(countDomain > 0) {
				throw new ServiceException("状态服务器域名已经存在！");
			}*/
			statusServer.setId(IdGenerator.nextId());
			statusServer.setServerStatus(0);//默认离线0
			statusServer.setCreateTime(new Date());
			statusServer.setCreateUser(userVo.getAccount());
			statusServer.setUpdateTime(new Date());
			statusServer.setUpdateUser(userVo.getAccount());
			statusServerMapper.insertSelective(statusServer);
		}else {
			nameExa.clear();
			nameExa.createCriteria().andEqualTo("name", statusServerParamVo.getName()).andNotEqualTo("id", statusServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countName = statusServerMapper.selectCountByExample(nameExa);
			if(countName > 0) {
				throw new ServiceException("状态服务器名称已存在！");
			}
			
			nameExa.clear();
			nameExa.createCriteria().andEqualTo("code", statusServerParamVo.getCode()).andNotEqualTo("id", statusServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countCode = statusServerMapper.selectCountByExample(nameExa);
			if(countCode > 0) {
				throw new ServiceException("状态服务器编号已经存在！");
			}
			
			nameExa.clear();
			nameExa.createCriteria().andEqualTo("ip", statusServerParamVo.getIp()).andNotEqualTo("id", statusServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countIp = statusServerMapper.selectCountByExample(nameExa);
			if(countIp > 0) {
				throw new ServiceException("状态服务器ip已经存在！");
			}
			
			/*Example domainExa = new Example(TranspondServer.class);
			domainExa.createCriteria().andEqualTo("domainName", transpondServerParamVo.getDomainName()).andNotEqualTo("id", transpondServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countDomain = transpondServerMapper.selectCountByExample(domainExa);
			if(countDomain > 0) {
				throw new ServiceException("中心信令服务器域名已经存在！");
			}*/
			statusServer.setUpdateTime(new Date());
			statusServer.setUpdateUser(userVo.getAccount());
			statusServerMapper.updateByPrimaryKeySelective(statusServer);
			
			Example delPojExample = new Example(StatusserverMapping.class);
			delPojExample.createCriteria().andEqualTo("statusserverId",statusServer.getId());
			statusserverMappingMapper.deleteByExample(delPojExample);//删除未修改前关联的所有项目
		}
		List<StatusserverMapping> statusserverMappings = new ArrayList<StatusserverMapping>();
		for(Project project : projects) {
			StatusserverMapping statusserverMapping = new StatusserverMapping();
			statusserverMapping.setId(IdGenerator.nextId());
			statusserverMapping.setStatusserverId(statusServer.getId());
			statusserverMapping.setProjectId(project.getId());
			statusserverMapping.setProjectName(project.getName());
			statusserverMapping.setProjectCode(project.getCode());
			statusserverMappings.add(statusserverMapping);
		}
		statusserverMappingMapper.insertList(statusserverMappings);
		
	}

	@Override
	public void delStatusServersByIds(List<String> ids) {
//		Example projectExa = new Example(StatusserverMapping.class);
//		projectExa.createCriteria().andIn("statusserverId", ids);
//		int countProject = statusserverMappingMapper.selectCountByExample(projectExa);
//		if(countProject > 0) {
//			throw new ServiceException("状态服务器存在所属项目，不能删除！");
//		}
//		Example sigExa = new Example(StatusServer.class);
//		sigExa.createCriteria().andIn("id", ids);
//		statusServerMapper.deleteByExample(sigExa);
//		Example projectExa = new Example(StatusserverMapping.class);
//		projectExa.createCriteria().andIn("statusserverId", ids);
//		statusserverMappingMapper.deleteByExample(projectExa);

		// 启用不能删除
		Example example = new Example(StatusServer.class);
		example.createCriteria().andIn("id",ids).andEqualTo("enabled",1);
		int count = statusServerMapper.selectCountByExample(example);
		if (count > 0){
			throw new ServiceException("状态服务器正在启用，不能删除！");
		}

		for(String id : ids){
			StatusServer statusServer = new StatusServer();
			statusServer.setId(id);
			statusServer.setEnabled(-1);
			statusServerMapper.updateByPrimaryKeySelective(statusServer);
		}
	}

	@Override
	public StatusServerVo getStatusServerById(String statusserverId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id", statusserverId);
		StatusServerVo statusServerVo = statusServerMapper.selectStatusServerByParam(map);

		if(statusServerVo != null){
            map.clear();
            map.put("statusserverId", statusserverId);
            List<StatusserverMappingVo> storeserverMappingVos=statusserverMappingMapper.selectByParam(map);
            statusServerVo.setStatusserverMappingVos(storeserverMappingVos);
        }

		return statusServerVo;
	}

	@Override
	public PageInfo<StatusServerVo> getStatusServerPages(PageParam pp, TreeNodeChildren treeNodeChildren, StatusServerParamVo statusServerParamVo) {
		
		Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", statusServerParamVo.getName());
        map.put("code", statusServerParamVo.getCode());
        map.put("ip", statusServerParamVo.getIp());
        map.put("serverStatus", statusServerParamVo.getServerStatus());
        map.put("enabled", statusServerParamVo.getEnabled());
        map.put("addrIds", treeNodeChildren.getAddressNodeIds());
        
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
		List<StatusServerVo> statusServerVos = statusServerMapper.selectStatusServersList(map);
		
		if(statusServerVos!=null && statusServerVos.size()>0) {
			List<String> statusServerIds = new ArrayList<String>();
			for(int i=0;i<statusServerVos.size();i++) {
				if(statusServerVos.get(i) == null) {
					continue;
				}
				statusServerIds.add(statusServerVos.get(i).getId());
			}
			if(statusServerIds.size()>0) {
				Multimap<String,StatusserverMappingVo> projectMutimap=ArrayListMultimap.create(); 
				List<StatusserverMappingVo> statusserverMappingVos = statusserverMappingMapper.selectProjectByStatusserverIds(statusServerIds);
				for(StatusserverMappingVo statusserverMappingVo : statusserverMappingVos) {
					projectMutimap.put(statusserverMappingVo.getStatusserverId(), statusserverMappingVo);
				}
				for(StatusServerVo statusServerVo : statusServerVos) {
					statusServerVo.setStatusserverMappingVos((List<StatusserverMappingVo>) projectMutimap.get(statusServerVo.getId()));
				}
			}
		}
		
		return new PageInfo<StatusServerVo>(statusServerVos);
	}

	@Override
	public List<StatusServerVo> getStatusServerList(TreeNodeChildren treeNodeChildren, StatusServerParamVo statusServerParamVo) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", statusServerParamVo.getName());
        map.put("code", statusServerParamVo.getCode());
        map.put("ip", statusServerParamVo.getIp());
        map.put("serverStatus", statusServerParamVo.getServerStatus());
        map.put("enabled", statusServerParamVo.getEnabled());
        map.put("addrIds", treeNodeChildren.getAddressNodeIds());
        
		List<StatusServerVo> list = statusServerMapper.selectStatusServersList(map);
		return list;
	}

	@Override
	public List<CameraNode> getStatusServerCamera(UserVo userVo, String statusServerId) {
	    Map<String,Object> map = new HashMap<>();
	    map.put("isAdmin",userVo.isAdmin());
        map.put("userId",userVo.getId());
        if(statusServerId == null){
            statusServerId = "";
        }
        map.put("statusServerId",statusServerId);
        return cameraMapper.selectCameraNode(map);
	}

	@Override
	public void relateCamerasToStatusserver(UserVo userVo, String statusserverId, List<String> cameraIds) {
		Example delExa = new Example(StatusserverCamera.class);
        Example.Criteria criteria = delExa.createCriteria().andEqualTo("statusserverId", statusserverId);

        //非管理员摄像机权限过滤
        if(!userVo.isAdmin()){
            //查询有权限的摄像机id
            List<String> authCameraIds = cameraMapper.selectAuthCameraIds(userVo.isAdmin(),userVo.getId());
            //增加删除条件，避免删除无权限数据
            criteria.andIn("cameraId",authCameraIds);
            //cameraIds取权限交集!
            cameraIds.retainAll(authCameraIds);
        }
		statusserverCameraMapper.deleteByExample(delExa);

		List<StatusserverCamera> list = new ArrayList<>();
        for (String cameraId : cameraIds) {
            StatusserverCamera statusserverCamera = new StatusserverCamera();
            statusserverCamera.setId(IdGenerator.nextId());
            statusserverCamera.setStatusserverId(statusserverId);
            statusserverCamera.setCameraId(cameraId);
            list.add(statusserverCamera);
        }

        //添加状态服务器下的摄像机关联
        if(list.size() > 0){
            statusserverCameraMapper.insertList(list);
        }
	}

	@Override
	public PageInfo<StatusserverCameraVo> getUserCamerasByStatusserverId(UserVo userVo, PageParam pp, StatusserverCameraVo statusserverCameraVo) {
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId", userVo.getId());
        map.put("statusserverId", statusserverCameraVo.getStatusserverId());
        map.put("cameraName", statusserverCameraVo.getCameraName());
        map.put("cameraNo", statusserverCameraVo.getCameraNo());
        map.put("cameraType", statusserverCameraVo.getCameraType());
        map.put("status", statusserverCameraVo.getStatus());
		map.put("isAdmin", userVo.isAdmin());

		List<StatusserverCameraVo> list = statusserverCameraMapper.selectUserStatusserverCamera(map);
		return new PageInfo<StatusserverCameraVo>(list);
	}

	@Override
	public void delRelatedCamerasByIds(UserVo userVo, String statusserverId, List<String> cameraIds) {
        Example delExa = new Example(StatusserverCamera.class);
        Example.Criteria criteria = delExa.createCriteria().andEqualTo("statusserverId", statusserverId);

        //非管理员摄像机权限过滤
        if(!userVo.isAdmin()){
            //查询有权限的摄像机id
            List<String> authCameraIds = cameraMapper.selectAuthCameraIds(userVo.isAdmin(),userVo.getId());
            //cameraIds取权限交集!
            cameraIds.retainAll(authCameraIds);
        }
        criteria.andIn("cameraId",cameraIds);

        statusserverCameraMapper.deleteByExample(delExa);
	}

	@Override
	public String getMaxCodeBycode(Map<String, Object> map) {
		return statusServerMapper.selectMaxCodeBycode(map);
	}

}
