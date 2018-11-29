package com.hdvon.nmp.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hdvon.nmp.entity.AlarmServer;
import com.hdvon.nmp.entity.AlarmserverMapping;
import com.hdvon.nmp.entity.Project;
import com.hdvon.nmp.exception.ServiceException;
import com.hdvon.nmp.mapper.*;
import com.hdvon.nmp.service.IAlarmServerService;
import com.hdvon.nmp.util.PageParam;
import com.hdvon.nmp.util.snowflake.IdGenerator;
import com.hdvon.nmp.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * <br>
 * <b>功能：</b>报警设备表Service<br>
 * <b>作者：</b>liyong<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Service
public class AlarmServerServiceImpl implements IAlarmServerService {

	@Autowired
	private AlarmServerMapper alarmServerMapper;
	
	@Autowired
	private AddressMapper addressMapper;
	
	@Autowired
	private CameraMapper cameraMapper;

    @Autowired
    private DeviceMapper deviceMapper;
    
	@Autowired
	private ProjectMapper projectMapper;
	
	@Autowired
	private AlarmserverMappingMapper alarmserverMappingMapper;

	@Override
	public void saveAlarmServer(UserVo userVo, AlarmServerParamVo alarmServerParamVo, List<String> projectIds) {
		AlarmServer alarmServer = Convert.convert(AlarmServer.class, alarmServerParamVo);

		Example pojExample = new Example(Project.class);
		pojExample.createCriteria().andIn("id", projectIds);
		List<Project> projects = projectMapper.selectByExample(pojExample);
		if(projects == null || projects.size()==0) {
			throw new ServiceException("报警设备所属项目不存在！");
		}

		DeviceVo device = deviceMapper.selectDeviceByCameraId(alarmServerParamVo.getCameraId());
		if(device == null){
            throw new ServiceException("报警设备关联摄像机不存在!");
        }
		Example nameExa = new Example(AlarmServer.class);
		if(StrUtil.isBlank(alarmServerParamVo.getId())) {
			nameExa.createCriteria().andEqualTo("name", alarmServerParamVo.getName()).andNotEqualTo("enabled",-1);
			int countName = alarmServerMapper.selectCountByExample(nameExa);
			if(countName > 0) {
				throw new ServiceException("报警设备名称已存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("code", alarmServerParamVo.getCode()).andNotEqualTo("enabled",-1);
			int countCode = alarmServerMapper.selectCountByExample(nameExa);
			if(countCode > 0) {
				throw new ServiceException("报警设备编号已经存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("ip", alarmServerParamVo.getIp()).andNotEqualTo("enabled",-1);
			int countIp = alarmServerMapper.selectCountByExample(nameExa);
			if(countIp > 0) {
				throw new ServiceException("报警设备ip已经存在！");
			}

			alarmServer.setId(IdGenerator.nextId());
			alarmServer.setCreateTime(new Date());
			alarmServer.setCreateUser(userVo.getAccount());
			alarmServer.setUpdateTime(new Date());
			alarmServer.setUpdateUser(userVo.getAccount());
			alarmServerMapper.insertSelective(alarmServer);
		}else {
			nameExa.clear();
			nameExa.createCriteria().andEqualTo("name", alarmServerParamVo.getName()).andNotEqualTo("id", alarmServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countName = alarmServerMapper.selectCountByExample(nameExa);
			if(countName > 0) {
				throw new ServiceException("报警设备名称已存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("code", alarmServerParamVo.getCode()).andNotEqualTo("id", alarmServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countCode = alarmServerMapper.selectCountByExample(nameExa);
			if(countCode > 0) {
				throw new ServiceException("报警设备编号已经存在！");
			}

			nameExa.clear();
			nameExa.createCriteria().andEqualTo("ip", alarmServerParamVo.getIp()).andNotEqualTo("id", alarmServerParamVo.getId()).andNotEqualTo("enabled",-1);
			int countIp = alarmServerMapper.selectCountByExample(nameExa);
			if(countIp > 0) {
				throw new ServiceException("报警设备ip已经存在！");
			}

			alarmServer.setUpdateTime(new Date());
			alarmServer.setUpdateUser(userVo.getAccount());
			alarmServerMapper.updateByPrimaryKeySelective(alarmServer);
			// 删除以前关联的项目
			Example alarMapping = new Example(AlarmserverMapping.class);
			alarMapping.createCriteria().andEqualTo("alarmserverId", alarmServer.getId());
			alarmserverMappingMapper.deleteByExample(alarMapping);
		}

		// 去重
		List<String> tempProjectIds = new ArrayList<>();
		for (String str : projectIds) {
			if(!tempProjectIds.contains(str)) {
				tempProjectIds.add(str);
			}
		}

		List<AlarmserverMapping> alarmserverMappings = new ArrayList<AlarmserverMapping>();
		for(String projectId : tempProjectIds) {
			AlarmserverMapping alarmserverMapping = new AlarmserverMapping();
			alarmserverMapping.setId(IdGenerator.nextId());
			alarmserverMapping.setAlarmserverId(alarmServer.getId());
			alarmserverMapping.setProjectId(projectId);
			alarmserverMappings.add(alarmserverMapping);
		}
		alarmserverMappingMapper.insertList(alarmserverMappings);
	}

	@Override
	public void delAlarmServersByIds(List<String> ids) {
//		Example sigExa = new Example(SigServer.class);
//		sigExa.createCriteria().andIn("id", ids);
//		alarmServerMapper.deleteByExample(sigExa);
//
//		Example projectExa = new Example(AlarmserverMapping.class);
//		projectExa.createCriteria().andIn("alarmserverId", ids);
//		alarmserverMappingMapper.deleteByExample(projectExa);

		// 启用不能删除
		Example example = new Example(AlarmServer.class);
		example.createCriteria().andIn("id",ids).andEqualTo("enabled",1);
		int count = alarmServerMapper.selectCountByExample(example);
		if (count > 0){
			throw new ServiceException("报警设备正在启用，不能删除！");
		}

		for(String id : ids){
			AlarmServer alarmServer = new AlarmServer();
			alarmServer.setId(id);
			alarmServer.setEnabled(-1);
			alarmServerMapper.updateByPrimaryKeySelective(alarmServer);
		}

	}

	@Override
	public AlarmServerVo getAlarmServerById(AlarmServerParamVo alarmServerParamVo) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("id", alarmServerParamVo.getId());
		paramMap.put("name", alarmServerParamVo.getName());
		paramMap.put("code", alarmServerParamVo.getCode());
		paramMap.put("enabled", alarmServerParamVo.getEnabled());
		paramMap.put("addressId", alarmServerParamVo.getAddressId());
		AlarmServerVo alarmServerVo = alarmServerMapper.selectAlarmServerByParam(paramMap);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("alarmserverId", alarmServerParamVo.getId());
		List<AlarmserverMappingVo> alarmserverMappingVos =alarmserverMappingMapper.selectByParam(map);
		alarmServerVo.setAlarmserverMappingVos(alarmserverMappingVos);
		return alarmServerVo;
	}

	@Override
	public PageInfo<AlarmServerVo> getAlarmServerPages(PageParam pp, TreeNodeChildren treeNodeChildren, AlarmServerParamVo alarmServerParamVo) {
		
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", alarmServerParamVo.getName());
        map.put("code", alarmServerParamVo.getCode());
        map.put("ip", alarmServerParamVo.getIp());
        map.put("addrIds", treeNodeChildren.getAddressNodeIds());
        
		PageHelper.startPage(pp.getPageNo(), pp.getPageSize());
		List<AlarmServerVo> alarmServerVos = alarmServerMapper.selectAlarmServersList(map);

		if(alarmServerVos != null && alarmServerVos.size() > 0) {
			for(AlarmServerVo obj : alarmServerVos){
				Map<String, Object> tempmap = new HashMap<>();
				tempmap.put("alarmserverId", obj.getId());
				List<AlarmserverMappingVo> templist = alarmserverMappingMapper.selectByParam(tempmap);
				obj.setAlarmserverMappingVos(templist);
			}
		}
		return new PageInfo<AlarmServerVo>(alarmServerVos);
	}

	@Override
	public List<AlarmServerVo> getAlarmServerList(TreeNodeChildren treeNodeChildren, AlarmServerParamVo alarmServerParamVo) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", alarmServerParamVo.getName());
        map.put("code", alarmServerParamVo.getCode());
        map.put("ip", alarmServerParamVo.getIp());
        map.put("addrIds", treeNodeChildren.getAddressNodeIds());
        
		List<AlarmServerVo> alarmServerVos = alarmServerMapper.selectAlarmServersList(map);
		
		if(alarmServerVos!=null && alarmServerVos.size()>0) {
			List<String> alarmServerIds = new ArrayList<String>();
			for(int i=0;i<alarmServerVos.size();i++) {
				if(alarmServerVos.get(i) == null) {
					continue;
				}
				alarmServerIds.add(alarmServerVos.get(i).getId());
			}
			if(alarmServerIds.size()>0) {
				Multimap<String,AlarmserverMappingVo> projectMutimap=ArrayListMultimap.create(); 
				List<AlarmserverMappingVo> alarmserverMappingVos = alarmserverMappingMapper.selectProjectByAlarmserverIds(alarmServerIds);
				for(AlarmserverMappingVo alarmserverMappingVo : alarmserverMappingVos) {
					projectMutimap.put(alarmserverMappingVo.getAlarmserverId(), alarmserverMappingVo);
				}
				for(AlarmServerVo alarmServerVo : alarmServerVos) {
					alarmServerVo.setAlarmserverMappingVos((List<AlarmserverMappingVo>) projectMutimap.get(alarmServerVo.getId()));
				}
			}
		}
		
		return alarmServerVos;
	}

	@Override
	public String getMaxCodeBycode(Map<String, Object> map) {
		return alarmServerMapper.selectMaxCodeBycode(map);
	}

}
