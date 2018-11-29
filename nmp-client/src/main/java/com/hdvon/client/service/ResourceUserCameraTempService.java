package com.hdvon.client.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hdvon.client.entity.ResourceUserCameraTemp;
import com.hdvon.client.mapper.ResourceUserCameraTempMapper;
import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.nmp.enums.MsgTypeEnum;

import tk.mybatis.mapper.entity.Example;

@Service
public class ResourceUserCameraTempService<T> {
	@Resource
	ResourceUserCameraTempMapper mapper;
	
	
	public void add(CameraMappingMsg msg,List<T> list) {
		//根据类型删除所有数据
//		Example example = new Example(ResourceUserCameraTemp.class);
//		example.createCriteria().andEqualTo("type", msg.getType()).andEqualTo("relationId",msg.getRelationId());
//		mapper.deleteByExample(example);
		
		if(list == null || list.isEmpty()) {
			return;
		}
		list.stream().forEach(t->{
			ResourceUserCameraTemp model = new ResourceUserCameraTemp();
			Long userId = CameraHelper.getFieldLongValueByFieldName("userId", t);
			if(userId != null ) {
				//获取当前ES数据的id
				String id = CameraHelper.getFieldValueByFieldName("id", t);
				long deviceId = CameraHelper.getFieldLongValueByFieldName("deviceId", t);
				model.setId(id);
				model.setType(msg.getType());
				model.setUserId(userId.toString());
				model.setDeviceId(String.valueOf(deviceId));
				model.setRelationId(msg.getRelationId());
				mapper.insert(model);
			}
		});
	}
	
	/**
	 * 自定义分组
	 * @param msg
	 * @param list
	 */
	public void addGroup(CameraMappingMsg msg,List<T> list) {
		if(list == null || list.isEmpty()) {
			return;
		}
		list.stream().forEach(t->{
			ResourceUserCameraTemp model = new ResourceUserCameraTemp();
			Long userId = CameraHelper.getFieldLongValueByFieldName("userId", t);
			if(userId != null ) {
				//获取当前ES数据的id
				String id = CameraHelper.getFieldValueByFieldName("id", t);
				long deviceId = CameraHelper.getFieldLongValueByFieldName("deviceId", t);
				long groupId = CameraHelper.getFieldLongValueByFieldName("groupId", t);// 
				model.setId(id);
				model.setType(msg.getType());
				model.setUserId(userId.toString());
				model.setDeviceId(String.valueOf(deviceId));
				model.setGroupId(String.valueOf(groupId));
				model.setRelationId(msg.getRelationId());
				mapper.insert(model);
			}
		});
	}
	
	
	
	/**
	 * 新增授权预案
	 * @param msg
	 * @param list
	 */
	public void addPlan(CameraMappingMsg msg,List<T> list) {
		if(list == null || list.isEmpty()) {
			return;
		}
		list.stream().forEach(t->{
			ResourceUserCameraTemp model = new ResourceUserCameraTemp();
			Long userId = CameraHelper.getFieldLongValueByFieldName("userId", t);
			if(userId != null ) {
				//获取当前ES数据的id
				String id = CameraHelper.getFieldValueByFieldName("id", t);
				long deviceId = CameraHelper.getFieldLongValueByFieldName("deviceId", t);
				long groupId = CameraHelper.getFieldLongValueByFieldName("groupId", t);// 
				model.setId(id);
				model.setType(msg.getType());
				model.setUserId(userId.toString());
				model.setDeviceId(String.valueOf(deviceId));
				model.setGroupId(String.valueOf(groupId));
				model.setRelationId(msg.getRelationId());
				mapper.insert(model);
			}
		});
	}
	
	/**
	 * 获取当前用户需要的删除的摄像机列表
	 * @param msg
	 * @return
	 */
	public List<String> findDeviceListByUser(CameraMappingMsg msg) {
		//根据类型删除所有数据
		Example example = new Example(ResourceUserCameraTemp.class);
		Example.Criteria criteria = example.createCriteria().andEqualTo("type", msg.getType()).andEqualTo("relationId",msg.getRelationId());
		List<ResourceUserCameraTemp> list = mapper.selectByExample(example);
		if(list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		return list.stream().map(t->t.getId()).collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	public List<ResourceUserCameraTemp> findDeviceList(CameraMappingMsg msg) {
		//根据类型删除所有数据
		Example example = new Example(ResourceUserCameraTemp.class);
		Example.Criteria criteria = example.createCriteria().andEqualTo("type", msg.getType()).andEqualTo("relationId",msg.getRelationId());
		List<ResourceUserCameraTemp> list = mapper.selectByExample(example);
		if(list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		return list;
	}
	
	/**
	 * 获取当前用户需要的删除的摄像机列表
	 * @param msg
	 * @return
	 */
	public List<String> findDeviceListByGroup(CameraMappingMsg msg) {
		//根据类型删除所有数据
		Example example = new Example(ResourceUserCameraTemp.class);
		Example.Criteria criteria = example.createCriteria().andEqualTo("type", msg.getType()).andEqualTo("groupId",msg.getRelationId());
		if(!StringUtils.isEmpty(msg.getDeleteIds())) {
			criteria.andIn("deviceId", Arrays.asList(msg.getDeleteIds().split(",")));
		}
		List<ResourceUserCameraTemp> list = mapper.selectByExample(example);
		if(list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		return list.stream().map(t->t.getId()).collect(Collectors.toList());
	}
	
	
	/**
	 * 查找自定义分组索引库临时数据
	 * @return
	 */
	public List<String> findDeviceListByUserRole(CameraMappingMsg msg) {
		//根据类型删除所有数据
		Example example = new Example(ResourceUserCameraTemp.class);

		Example.Criteria criteria = example.createCriteria().andEqualTo("type", msg.getType());
		if(msg.getDeviceId().size() >0) {
			criteria.andIn("deviceId", msg.getDeviceId());
		}
		List<ResourceUserCameraTemp> list = mapper.selectByExample(example);
		if(list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		return list.stream().map(t->t.getId()).collect(Collectors.toList());
	}
	
	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	public List<ResourceUserCameraTemp> getResourceUserByUserRole(CameraMappingMsg msg) {
		//根据类型删除所有数据
		Example example = new Example(ResourceUserCameraTemp.class);

		Example.Criteria criteria = example.createCriteria().andEqualTo("type", MsgTypeEnum.ES_USER_MSG.getKey())
				.andEqualTo("userId", msg.getRelationId());
		// updateid为空，则删除该用户所有的摄像机
		if(! StringUtils.isEmpty(msg.getUpdateIds())) {
			criteria.andNotIn("relationId", Arrays.asList(msg.getUpdateIds().split(",")));
		}
		List<ResourceUserCameraTemp> list = mapper.selectByExample(example);
		if(list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		return list;
	}
	
	
	/**
	 * 删除临时表
	 * @param delList
	 */
	public void delete(List<String> delList) {
		if(delList.size() >0) {
			Example example = new Example(ResourceUserCameraTemp.class);
			example.createCriteria().andIn("id", delList);
			mapper.deleteByExample(example);
		}
	}
}
