package com.hdvon.client.es.search.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hdvon.client.entity.ResourceUserCameraTemp;
import com.hdvon.client.es.ElasticsearchUtils;
import com.hdvon.client.es.IndexField;
import com.hdvon.client.es.search.UserCameraSearchService;
import com.hdvon.client.mapper.CameraPermissionMapper;
import com.hdvon.client.mapper.PermissionPlanMapper;
import com.hdvon.client.mapper.UserMapper;
import com.hdvon.client.service.CameraHelper;
import com.hdvon.client.service.ResourceUserCameraTempService;
import com.hdvon.client.vo.CameraMapVo;
import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.client.vo.CameraMsg;
import com.hdvon.client.vo.EsCameraPermissionVo;
import com.hdvon.client.vo.EsUserCameraGroupVo;
import com.hdvon.nmp.common.SystemConstant;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
/**
 * 摄像机索引库操作类
 * @author wanshaojian
 *
 */
@Service
public class UserCameraSearchServiceImpl extends BaseSearchServiceImpl<EsCameraPermissionVo> implements UserCameraSearchService{
	
    
	@Autowired
	private CameraPermissionMapper cameraMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private PermissionPlanMapper permissionPlanMapper;
    
	@Resource
	ResourceUserCameraTempService<EsCameraPermissionVo> resUserCameraService;
	
	public static final String ES_INDEX = "hdvon_camera_permission";
	public static final String ES_MAPPING = "cameraPermission";
	
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
		List<EsCameraPermissionVo> list = cameraMapper.findCameraPermissionByMap(form);
		if(list == null || list.isEmpty()) {
			return;
		}
		
		setTreeName(list);
		//更新索引
		updateIndex(ES_INDEX, ES_MAPPING, list);

	}	
	/**
	 * 根据资源角色关联用户更新索引库
	 * @param model 消息对象
	 */
	@Override
	public List<EsCameraPermissionVo> updateByUserId(CameraMappingMsg model) {
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
		try {
			//获取需要添加的设备信息
			List<Long> addUserList = CameraHelper.convertIdList(model.getUpdateIds());
			CameraMapVo form = new CameraMapVo();
			form.setUserList(addUserList);
			form.setResRoleId(model.getRelationId());
			List<EsCameraPermissionVo> list = cameraMapper.findCameraPermissionByMap(form);
			
			//更新索引
			updateEsIndex(model, list);
			
			// 预案相关 
			if(deviceList.size()> 0) {
				form.setUserList(null);
				form.setResRoleId(null);
				form.setNoEqRoleId(model.getRelationId());
				form.setDevices(deviceList);
				List<EsCameraPermissionVo> planList = cameraMapper.findCameraPermissionByPlan(form);
				model.setType(2);
				updateEsIndex(model, planList);
				resUserCameraService.add(model, list);
			}
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 删除的摄像机是否在有效预案里
		return Collections.emptyList();

	}
	/**
	 * 根据权限预案对用户授权更新索引库
	 * @param model 消息对象
	 */
	@Override
	public List<EsCameraPermissionVo> updateByPlanId(CameraMappingMsg model) {
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
		
		//根据授权id获取相关的用户列表
		List<Long> newUserList = CameraHelper.convertIdList(model.getUpdateIds());
		
		//获取需要添加的设备信息
		CameraMapVo form = new CameraMapVo();
		form.setPlanId(model.getRelationId());
		form.setUserList(newUserList);
		List<EsCameraPermissionVo> list = cameraMapper.findCameraPermissionByPlan(form);
		
		//更新索引
		updateEsPlanIndex(model, list);
//		updateEsIndex(model,list);
		return list;
    }
	

	
	/**
	 * 更新es索引数据
	 * @param model
	 * @param list
	 */
	private void updateEsIndex(CameraMappingMsg model,List<EsCameraPermissionVo> list) {
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
	private void updateEsPlanIndex(CameraMappingMsg model,List<EsCameraPermissionVo> list) {
		if(list == null || list.isEmpty()) {
			return;
		}
		setTreeName(list);
		//获取当前已存在的用户摄像机信息
		List<String> idList = list.stream().map(t->t.getId()).collect(Collectors.toList());
		BoolQueryBuilder boolQuery = new BoolQueryBuilder();
		boolQuery.must(QueryBuilders.termsQuery(IndexField.ID, idList));
		boolQuery.must(QueryBuilders.matchQuery(IndexField.PERMANENT, SystemConstant.ES_PERMANENT));
		
		List<String> existsIds = search(ES_INDEX,ES_MAPPING,boolQuery);
		
		List<EsCameraPermissionVo> addList = null;
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
	public void setTreeName(List<EsCameraPermissionVo> list) {
		treeKeyExists();
		Map<String,String> addresMap= redisDao.getMap(SystemConstant.TREENODE_ADDRESSNODE_KEY);
		Map<String,String> orgMap= redisDao.getMap(SystemConstant.TREENODE_ORGNODE_KEY);
		Map<String,String> projectMap=redisDao.getMap(SystemConstant.TREENODE_PROJECTNODE_KEY);
		
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
			
		});
		
	}
	
	/**
	 * 根据资源角色id查找出关联的预案的所有摄像机
	 */
	@Override
	public List<EsCameraPermissionVo> updatePlanByResId(CameraMappingMsg model) {
		if(model == null) {
			return Collections.emptyList();
		}
		// 资源角色关联的新增授权预案
		List<String> planIds=permissionPlanMapper.selectPlanIdByResId(model.getRelationId());
		if( planIds.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<EsCameraPermissionVo> tempList=new ArrayList<EsCameraPermissionVo>();
		// 新增授权关联的用户
		for(String planId : planIds) {
			List<String> planUserId=permissionPlanMapper.selectUserIdByPlan(planId);
			if(planUserId.size() >0 ) {
				
				model.setRelationId(planId);
				// 获取需要删除的索引列表
				List<String> delList = resUserCameraService.findDeviceListByUser(model);
				BoolQueryBuilder delQuery = new BoolQueryBuilder();
				delQuery.must(QueryBuilders.termsQuery(IndexField.ID, delList));
				delQuery.must(QueryBuilders.matchQuery(IndexField.PERMANENT, SystemConstant.ES_NOT_PERMANENT));
				delete(ES_INDEX, ES_MAPPING, delQuery);
				resUserCameraService.delete(delList);
				
				//根据授权id获取相关的用户列表
				List<Long> newUserList = CameraHelper.convertIdList(ArrayUtil.join(planUserId.toArray(), ","));
				
				//获取需要添加的设备信息
				CameraMapVo form = new CameraMapVo();
				form.setPlanId(model.getRelationId());
				form.setUserList(newUserList);
				List<EsCameraPermissionVo> list = cameraMapper.findCameraPermissionByPlan(form);
				
				//更新索引
				updateEsPlanIndex(model, list);
				tempList.addAll(list);
				
			}
		}
		return tempList;
	}


	

}
