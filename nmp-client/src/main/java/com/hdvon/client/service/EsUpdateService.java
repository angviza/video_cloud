package com.hdvon.client.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import com.hdvon.client.entity.ResourceUserCameraTemp;
import com.hdvon.client.es.IndexField;
import com.hdvon.client.es.search.CameraGroupSearchService;
import com.hdvon.client.es.search.CameraSearchService;
import com.hdvon.client.es.search.UserCameraGroupSearchService;
import com.hdvon.client.es.search.UserCameraSearchService;
import com.hdvon.client.mapper.CameraPermissionMapper;
import com.hdvon.client.mapper.PermissionPlanMapper;
import com.hdvon.client.mapper.UserMapper;
import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.client.vo.CameraMsg;
import com.hdvon.client.vo.EsCameraPermissionVo;
import com.hdvon.client.vo.EsUserCameraGroupVo;
import com.hdvon.nmp.common.SystemConstant;
import com.hdvon.nmp.enums.MsgTypeEnum;

import cn.hutool.core.util.ArrayUtil;
/**
 * <br>
 * <b>功能：</b>ES数据更新服务接口实现<br>
 * <b>作者：</b>wanshaojian<br>
 * <b>日期：</b>2018-5-31<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Component
public class EsUpdateService {
	@Resource
	CameraSearchService cameraSearchService;
	@Resource
	CameraGroupSearchService cameraGroupSearchService;
	@Resource
	UserCameraSearchService userCameraSearchService;
	@Resource
	UserCameraGroupSearchService userCameraGroupSearchService;
	
	@Resource
	private CameraPermissionMapper cameraPermissionMapper;
	
	@Resource
	PermissionPlanMapper permissionPlanMapper;
	
	@Resource
	ResourceUserCameraTempService resUserCameraService;
	
	@Resource
	private UserMapper userMapper;

	/**
	 * 根据资源角色关联用户更新ES用户摄像机资源数据
	 * @param model 更新消息对象
	 *  同时把关联的摄像机也添加到临时表
	 *  updateIds 为空标识移除这个资源角色的所有摄像机
	 *  {"relationId":"资源角色id","type":1,"updateIds":"更新用户id",}
	 */
	public void batchUpdateIndexByUser(CameraMappingMsg model) {
		if(model == null) {
			return;
		}
		
		//根据资源角色关联用户同步 hdvon_camera_permissio索引库
		List<EsCameraPermissionVo>  list = userCameraSearchService.updateByUserId(model);
		resUserCameraService.add(model, list);
		
		//根据资源角色关联用户同步 hdvon_user_camera_group索引库 
		model.setType(MsgTypeEnum.ES_GROUP_MSG.getKey());
		List<EsUserCameraGroupVo> groupList=userCameraGroupSearchService.updateByUserId(model);
		resUserCameraService.addGroup(model, groupList);
		
		//修改资源角色在新增授权预案里，应把资源角色下的所有摄像机同步到新增授权预案摄像机
		List<String> planIds=permissionPlanMapper.selectPlanIdByResId(model.getRelationId());
		if(! planIds.isEmpty()) {
			model.setType(MsgTypeEnum.ES_PLLAN_MSG.getKey());
			for(String planId : planIds) {
				List<String> planUserId=permissionPlanMapper.selectUserIdByPlan(planId);
				if(planUserId.size() >0 ) {
					model.setRelationId(planId);
					model.setUpdateIds(ArrayUtil.join(planUserId.toArray(), ","));
					this.batchUpdateIndexByPlan(model);
				}
			}
		}
	}
	
	
	/**
	 * 根据权限预案对用户授权更新ES用户摄像机资源数据
	 * @param model 更新消息对象、
	 * updateIds 为 10000000000 则标识删除该预案的所有用户拥有的摄像机权限
	 * {"relationId":"新增授权id","type":2,"updateIds":"更新用户id"}
	 */
	public void batchUpdateIndexByPlan(CameraMappingMsg model) {
		if(model == null) {
			return;
		}
		//根据权限预案对用户授权同步hdvon_camera_permissio索引库
		List<EsCameraPermissionVo>  list = userCameraSearchService.updateByPlanId(model);
		resUserCameraService.add(model, list);
		
		//根据权限预案对用户授权同步hdvon_user_camera_group索引库
		model.setType(MsgTypeEnum.ES_GROUP_MSG.getKey());
		List<EsUserCameraGroupVo> groupList=userCameraGroupSearchService.updateByPlanId(model);
		resUserCameraService.addGroup(model, groupList);
		
	}	

	/**
	 * 根据摄像机自定义分组更新ES用户摄像机资源数据
	 * @param model 更新消息对象
	 * {"relationId":"自定义分组id","type":3,"updateIds":"需要更新设备deviceIds","deleteIds":"需要删除的设备deviceIds"}
	 */
	public void batchUpdateIndexByGroup(CameraMappingMsg model) {
		if(model == null) {
			return;
		}
		//根据摄像机自定义分组同步 hdvon_camera_group索引库 超管用户
		//超级管理员分组
		cameraGroupSearchService.updateByGroupId(model);
		
		//根据摄像机自定义分组同步hdvon_user_camera_group索引库  普通用户
		List<EsUserCameraGroupVo> groupList=userCameraGroupSearchService.updateByGroupId(model);
		resUserCameraService.addGroup(model, groupList);
		
		//更新 摄像机存在预案权限预案的摄像机
		//删除预案已存在的设备 
		userCameraGroupSearchService.detePlanByGroup(model);
		// 更改的摄像机重新加到新的分组
		List<EsUserCameraGroupVo> planList= userCameraGroupSearchService.updatePlanByGroup(model);
		resUserCameraService.addGroup(model, planList);
	}

	
	/**
	 * 根据用户分配资源角色更新ES用户摄像机资源数据
	 * @param model
	 * {"relationId":"用户id","type":4,"updateIds":"资源角色id","deleteIds":"需要删除的资源角色id"}
	 */
	public void updateByUserRole(CameraMappingMsg msg) {
		/**
		 *  根据用户分配资源角色同步hdvon_camera_permissio索引库
		 *  获取用户关联资源角色需要删除索引数据
		 *  relationId 用户id ,updateIds 跟新角色id集合【多个已‘,’分割】,type=1 查找 
		 */
		List<String> delList=new ArrayList<String>();
		List<String> deviceList=new ArrayList<String>();// device集合
		List<ResourceUserCameraTemp> cameraTemp = resUserCameraService.getResourceUserByUserRole(msg);
		for(ResourceUserCameraTemp temp :cameraTemp) {
			delList.add(temp.getId());
			deviceList.add(temp.getDeviceId());
		}
		if(delList.size() >0) {
			//根据用户分配资源角色同步hdvon_user_camera_group索引库
			BoolQueryBuilder delQuery = new BoolQueryBuilder();
			delQuery.must(QueryBuilders.termsQuery(IndexField.ID, delList));
			delQuery.must(QueryBuilders.matchQuery(IndexField.PERMANENT, SystemConstant.ES_PERMANENT));
			
			userCameraSearchService.delete(SystemConstant.es_cameraPermission_index, SystemConstant.es_cameraPermission_mapping, delQuery);
			resUserCameraService.delete(delList);
			
			/** 普通用户分组索引库
			 *  获取用户关联资源角色需要删除索引数据
			 *  relationId 用户id ,updateIds 跟新角色id集合【多个已‘,’分割】,type=3 查找 
			 */
			msg.setType(MsgTypeEnum.ES_GROUP_MSG.getKey());
			msg.setDeviceId(deviceList);
			List<String> delGroupList = resUserCameraService.findDeviceListByUserRole(msg);
			if(delGroupList.size() >0) {
				//根据用户分配资源角色同步hdvon_user_camera_group索引库
				BoolQueryBuilder groupQuery = new BoolQueryBuilder();
				groupQuery.must(QueryBuilders.termsQuery(IndexField.ID, delGroupList));
				groupQuery.must(QueryBuilders.matchQuery(IndexField.PERMANENT, SystemConstant.ES_PERMANENT));
				//根据用户分配资源角色同步hdvon_user_camera_group索引库
				
				userCameraGroupSearchService.delete(SystemConstant.es_user_cameragroup_index, SystemConstant.es_user_cameragroup_mapping, groupQuery);
				resUserCameraService.delete(delGroupList);
			}
		}
		//新增索引
		if(StringUtils.isEmpty(msg.getUpdateIds())) {
			return;
		}
		String[] arr = msg.getUpdateIds().split(",");
		for(String roleId:arr) {
			//获取当前用户角色所有用户
			List<String> userIds=userMapper.getUserIdsByRoleId(roleId);
			
			CameraMappingMsg model = new CameraMappingMsg();
			model.setRelationId(roleId);
			model.setUpdateIds(userIds.size()>0 ?ArrayUtil.join(userIds.toArray(), ","):null);
			model.setType(MsgTypeEnum.ES_USER_MSG.getKey());
			
			batchUpdateIndexByUser(model);
		}
	}
	
	
	/**
	 * 根据设备id更新索引数据
	 * @param msg
	 */
	public void updateIndexBydevId(CameraMsg msg) {
		//根据设备id更新hvdon_camera索引库
		cameraSearchService.updateByDevId(msg);
		
		//根据设备id更新hvdon_camera_permission索引库
		cameraGroupSearchService.updateByDevId(msg);
		
		//根据设备id更新hvdon_camera_group索引库
		userCameraSearchService.updateByDevId(msg);
		
		//根据设备id更新hvdon_user_camera_group索引库
		userCameraGroupSearchService.updateByDevId(msg);
	}
	

	/**
	 * 同步所有的用户摄像机信息
	 */
	public void syncAllCameraPermission() {
		//获取所有已授权的用户列表
		List<CameraMappingMsg> roleList = cameraPermissionMapper.findAllPermisllionUserList();
		
		if(!roleList.isEmpty() && roleList.size()>0) {
			
			Map<String, List<CameraMappingMsg>> roleMap = roleList.stream().collect(Collectors.groupingBy(CameraMappingMsg::getRelationId));
			
			for(Iterator<String> it=roleMap.keySet().iterator();it.hasNext();) {
				String key = it.next();
				List<String> userIds = roleMap.get(key).stream().map(t->t.getUpdateIds()).collect(Collectors.toList());
				CameraMappingMsg msg = new CameraMappingMsg();
				msg.setRelationId(key);
				msg.setUpdateIds(StringUtils.join(userIds.toArray(),","));
				msg.setType(1);
				
				batchUpdateIndexByUser(msg);
			}
		}
		
		//获取所有临时授权的planId集合
		List<CameraMappingMsg> planList = permissionPlanMapper.getAllPermissionPlanList();
		if(!planList.isEmpty() && planList.size()>0) {
			Map<String, List<CameraMappingMsg>> planMap = planList.stream().collect(Collectors.groupingBy(CameraMappingMsg::getRelationId));
			
			for(Iterator<String> it=planMap.keySet().iterator();it.hasNext();) {
				String key = it.next();
				List<String> userIds = planMap.get(key).stream().map(t->t.getUpdateIds()).collect(Collectors.toList());
				CameraMappingMsg msg = new CameraMappingMsg();
				msg.setRelationId(key);
				msg.setUpdateIds(StringUtils.join(userIds.toArray(),","));
				msg.setType(2);
				
				batchUpdateIndexByPlan(msg);
			}
		}
	}

}
