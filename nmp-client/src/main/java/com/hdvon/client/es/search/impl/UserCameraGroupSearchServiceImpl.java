package com.hdvon.client.es.search.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hdvon.client.entity.ResourceUserCameraTemp;
import com.hdvon.client.es.IndexField;
import com.hdvon.client.es.search.UserCameraGroupSearchService;
import com.hdvon.client.mapper.CameraGroupMapper;
import com.hdvon.client.service.CameraHelper;
import com.hdvon.client.service.ResourceUserCameraTempService;
import com.hdvon.client.vo.CameraMapVo;
import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.client.vo.CameraMsg;
import com.hdvon.client.vo.EsUserCameraGroupVo;
import com.hdvon.nmp.common.SystemConstant;

import cn.hutool.core.util.StrUtil;
/**
 * 摄像机索引库操作类
 * @author wanshaojian
 *
 */
@Service
public class UserCameraGroupSearchServiceImpl extends BaseSearchServiceImpl<EsUserCameraGroupVo> implements UserCameraGroupSearchService{
	
    
	@Resource
    private CameraGroupMapper cameraGroupMapper;
    
	@Resource
	ResourceUserCameraTempService<EsUserCameraGroupVo> resUserCameraService;
	
    
	public static final String ES_INDEX = "hdvon_user_camera_group";
	public static final String ES_MAPPING = "userCameraGroup";
	
    
	/**
	 * 根据用户变更摄像机信息更新索引库
	 * @param deviceIds
	 */
	@Override
	public void updateByDevId(CameraMsg msg) {
		if(StringUtils.isEmpty(msg.getDeviceIds())) {
			return;
		}
		
		List<Long> deviceList = CameraHelper.convertIdList(msg.getDeviceIds());
		if(msg.getType()==3) {// 删除设备
			BoolQueryBuilder groupQuery = new BoolQueryBuilder();
			groupQuery.must(QueryBuilders.termsQuery(IndexField.DEVICE_ID, deviceList));
			delete(ES_INDEX, ES_MAPPING, groupQuery);
			return ;
		}

		CameraMapVo form = new CameraMapVo();
		form.setDeviceList(deviceList);
		
		List<EsUserCameraGroupVo> list = cameraGroupMapper.findUserCameraGroupListByMap(form);
		if(list == null || list.isEmpty()) {
			return;
		}
		
		setTreeName(list);
		//更新索引
		updateIndex(ES_INDEX, ES_MAPPING, list);
	}

	/**
	 * 根据资源角色关联用户更新索引库（hdvon_user_camera_group）
	 * @param model 消息对象
	 * @param updateList 需要更新的数据源
	 */
	@Override
	public List<EsUserCameraGroupVo> updateByUserId(CameraMappingMsg model) {
		if(model == null) {
			return Collections.emptyList();
		}
		/**
		 * 获取需要删除的索引列表
		 */
		List<ResourceUserCameraTemp> deleteList = resUserCameraService.findDeviceList(model);
		List<String> delList=new ArrayList<String>();
		List<String> deviceList=new ArrayList<String>();
		for(ResourceUserCameraTemp temp:deleteList) {
			delList.add(temp.getId());
			deviceList.add(temp.getDeviceId());
		}
		
		BoolQueryBuilder delQuery = new BoolQueryBuilder();
		delQuery.must(QueryBuilders.termsQuery(IndexField.ID, delList));
		delQuery.must(QueryBuilders.matchQuery(IndexField.PERMANENT, SystemConstant.ES_PERMANENT));
		delete(ES_INDEX, ES_MAPPING, delQuery);
		resUserCameraService.delete(delList);

		if(StringUtils.isEmpty(model.getUpdateIds())) {
			return Collections.emptyList();
		}
		//获取需要添加的设备信息
		List<Long> userList = CameraHelper.convertIdList(model.getUpdateIds());
		CameraMapVo form = new CameraMapVo();
		form.setUserList(userList);
		form.setResRoleId(model.getRelationId());
		List<EsUserCameraGroupVo> list = cameraGroupMapper.findUserCameraGroupListByMap(form);
		//更新es索引数据
		updateEsIndex(model, list);
		
		// 预案相关的
		if(deviceList.size()> 0) {
			form.setUserList(null);
			form.setResRoleId(null);
			form.setNoEqRoleId(model.getRelationId());
			form.setDevices(deviceList);
			List<EsUserCameraGroupVo> planList = cameraGroupMapper.findUserCameraGroupListByPlan(form);
			model.setType(3);
			updateEsIndex(model, planList);
			resUserCameraService.add(model, list);
		}
		
        return list;
	}


	/**
	 * 用户分组库
	 * 根据设备分组更新索引库 
	 * @param model 消息对象
	 */
	@Override
	public List<EsUserCameraGroupVo> updateByGroupId(CameraMappingMsg model) {
		if(model == null) {
			return Collections.emptyList();
		}
		/**
		 * 获取需要删除的索引列表
		 * 摄像机移除分组 改该分组的所有摄像机都移除 包括预案关联的摄像机
		 */
		List<String> delList = resUserCameraService.findDeviceListByGroup(model);
		BoolQueryBuilder delQuery = new BoolQueryBuilder();
		delQuery.must(QueryBuilders.termsQuery(IndexField.ID, delList));
		//delQuery.must(QueryBuilders.matchQuery(IndexField.PERMANENT, SystemConstant.ES_PERMANENT)); 
		delete(ES_INDEX, ES_MAPPING, delQuery);
		resUserCameraService.delete(delList);
		
		if(StringUtils.isEmpty(model.getUpdateIds())) {
			return Collections.emptyList();
		}
		//获取需要添加的设备信息
		List<Long> deviceList = CameraHelper.convertIdList(model.getUpdateIds());
		CameraMapVo form = new CameraMapVo();
		form.setDeviceList(deviceList);
		form.setGroupId(model.getRelationId());
		/**
		 * 获取需要新增的用户分组索引数据
		 */
		List<EsUserCameraGroupVo> list = cameraGroupMapper.findUserCameraGroupListByMap(form);
		
		//更新es索引数据
		updateEsIndex(model, list);
		return list;
	}

	/**
	 * 根据权限预案对用户授权更新索引库
	 * @param model 消息对象
	 */
	@Override
	public List<EsUserCameraGroupVo> updateByPlanId(CameraMappingMsg model) {
		if(model == null) {
			return Collections.emptyList();
		}
		/**
		 * 获取需要删除的索引列表
		 */
		List<String> delList = resUserCameraService.findDeviceListByUser(model);
		BoolQueryBuilder delQuery = new BoolQueryBuilder();
		delQuery.must(QueryBuilders.termsQuery(IndexField.ID, delList));
		delQuery.must(QueryBuilders.matchQuery(IndexField.PERMANENT, SystemConstant.ES_NOT_PERMANENT));
		delete(ES_INDEX, ES_MAPPING, delQuery);
		resUserCameraService.delete(delList);
		
		if(StringUtils.isEmpty(model.getUpdateIds())) {
			return Collections.emptyList();
		}
		//获取需要添加的设备信息
		List<Long> userList = CameraHelper.convertIdList(model.getUpdateIds());
		CameraMapVo form = new CameraMapVo();
		form.setUserList(userList);
		form.setPlanId(model.getRelationId());
		
		List<EsUserCameraGroupVo> list = cameraGroupMapper.findUserCameraGroupListByPlan(form);
		
		//更新es索引数据
		updateEsPlanIndex(model, list);
		return list;
	}

	
	/**
	 * 更新es索引数据
	 * @param model
	 * @param list
	 */
	private void updateEsIndex(CameraMappingMsg model,List<EsUserCameraGroupVo> list) {
		if(list == null || list.isEmpty()) {
			return;
		}
		setTreeName(list);
		
    	//更新索引
        list.stream().forEach(t->{
        	addOrUpdate(t.getId(), t, ES_INDEX, ES_MAPPING);
        });
	}
	
	/**
	 * 更新es索引数据
	 * @param model
	 * @param list
	 */
	private void updateEsPlanIndex(CameraMappingMsg model,List<EsUserCameraGroupVo> list) {
		if(list == null || list.isEmpty()) {
			return;
		}
		setTreeName(list);
		//获取当前已存在的用户摄像机信息
		List<String> idList = list.stream().map(t->t.getId()).collect(Collectors.toList());
		BoolQueryBuilder boolQuery = new BoolQueryBuilder();
		boolQuery.must(QueryBuilders.termsQuery(IndexField.ID, idList));
		boolQuery.must(QueryBuilders.matchQuery(IndexField.PERMANENT, SystemConstant.ES_PERMANENT));
		
		List<String> existsIds = search(ES_INDEX, ES_MAPPING,boolQuery);
		List<EsUserCameraGroupVo> addList = null;
		if(!existsIds.isEmpty()) {
			addList = list.stream().filter(t->!existsIds.contains(t.getId())).collect(Collectors.toList());
		}else {
			addList = list;
		}
		
    	//更新索引
		addList.stream().forEach(t->{
        	addOrUpdate(t.getId(), t, ES_INDEX, ES_MAPPING);
        });
	}
	

	
	@Override
	public void setTreeName(List<EsUserCameraGroupVo> list) {
		treeKeyExists();
		Map<String,String> addresMap= redisDao.getMap(SystemConstant.TREENODE_ADDRESSNODE_KEY);
		Map<String,String> orgMap= redisDao.getMap(SystemConstant.TREENODE_ORGNODE_KEY);
		Map<String,String> projectMap=redisDao.getMap(SystemConstant.TREENODE_PROJECTNODE_KEY);
		Map<String,String> gruopMap=redisDao.getMap(SystemConstant.TREENODE_GROUPNODE_KEY);
		
		if(list==null || list.isEmpty()) {
			return;
		}
		list.stream().forEach(t->{
			if(t.getAddressId()!=null) {
				String addressName=addresMap.get(t.getAddressId().toString());
				t.setAddressName(StrUtil.isNotBlank(addressName)? addressName:t.getAddressName());
			}
			if(t.getOrgId()!=null) {
				String orgName=orgMap.get(t.getOrgId().toString());
				t.setOrgName(StrUtil.isNotBlank(orgName)? orgName:t.getOrgName());
			}
			if(t.getProjectId()!=null) {
				String projectName=projectMap.get(t.getProjectId().toString());
				t.setProjectName(StrUtil.isNotBlank(projectName)? projectName:t.getProjectName());
			}
			if(t.getGroupId() !=null) {
				String groupName=gruopMap.get(t.getGroupId().toString());
				t.setGroupName(StrUtil.isNotBlank(groupName)? groupName:t.getGroupName());
			}
			
		});
	}

	
	/**
	 * 设备移除分组 删除预案分组索引
	 */
	@Override
	public void detePlanByGroup(CameraMappingMsg model) {
		if(model == null) {
			return ;
		}
		if(StrUtil.isBlank(model.getDeleteIds())) {
			return ;
		}
		List<String> delList = resUserCameraService.findDeviceListByGroup(model);
		BoolQueryBuilder delQuery = new BoolQueryBuilder();
		delQuery.must(QueryBuilders.termsQuery(IndexField.ID, delList));
		delQuery.must(QueryBuilders.matchQuery(IndexField.PERMANENT, SystemConstant.ES_NOT_PERMANENT));
		delete(ES_INDEX, ES_MAPPING, delQuery);
		resUserCameraService.delete(delList);
		
	}

	/**
	 * 设备添加分组 添加关联的在有效预案
	 * updateIds 更改分组的deviceIds 集合
	 * relationId 分组id
	 */
	@Override
	public List<EsUserCameraGroupVo> updatePlanByGroup(CameraMappingMsg model) {
		//获取需要添加的设备信息
	    List<Long> deviceList = CameraHelper.convertIdList(model.getUpdateIds());
		CameraMapVo form = new CameraMapVo();
		form.setDeviceList(deviceList);
		form.setGroupId(model.getRelationId());
		
		List<EsUserCameraGroupVo> list = cameraGroupMapper.findUserCameraGroupListByPlan(form);
	
		//更新es索引数据
		updateEsPlanIndex(model, list);
		
		return list;
	}


	

}
