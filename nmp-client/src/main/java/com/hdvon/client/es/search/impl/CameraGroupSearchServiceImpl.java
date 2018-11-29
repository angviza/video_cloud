package com.hdvon.client.es.search.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hdvon.client.es.ElasticsearchUtils;
import com.hdvon.client.es.IndexField;
import com.hdvon.client.es.search.CameraGroupSearchService;
import com.hdvon.client.mapper.CameraGroupMapper;
import com.hdvon.client.service.CameraHelper;
import com.hdvon.client.service.ResourceUserCameraTempService;
import com.hdvon.client.vo.CameraMapVo;
import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.client.vo.CameraMsg;
import com.hdvon.client.vo.EsCameraGroupVo;
import com.hdvon.nmp.common.SystemConstant;

import cn.hutool.core.util.StrUtil;
/**
 * 摄像机索引库操作类
 * @author wanshaojian
 *
 */
@Service
public class CameraGroupSearchServiceImpl extends BaseSearchServiceImpl<EsCameraGroupVo> implements CameraGroupSearchService{
	
    
	@Resource
    private CameraGroupMapper cameraGroupMapper;
    
	@Resource
	ResourceUserCameraTempService<EsCameraGroupVo> resUserCameraService;
	
	public static final String ES_INDEX = "hdvon_camera_group";
	public static final String ES_MAPPING = "cameraGroup";
    
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
		
		List<EsCameraGroupVo> list = cameraGroupMapper.findCameraGroupListByMap(form);
		
		if(list == null || list.isEmpty()) {
			return;
		}
		setTreeName(list);
		//更新索引
		updateIndex(ES_INDEX, ES_MAPPING,  list);
	}

	
	/**
	 * 根据设备分组更新索引库
	 * @param model 消息对象
	 */
	@Override
	public List<EsCameraGroupVo> updateByGroupId(CameraMappingMsg model) {
		if(model == null) {
			return Collections.emptyList();
		}
		
		BoolQueryBuilder delQuery = new BoolQueryBuilder();
		delQuery.must(QueryBuilders.matchQuery(IndexField.GROUP_ID, model.getRelationId()));
		//delQuery.must(QueryBuilders.matchQuery(IndexField.PERMANENT, SystemConstant.ES_PERMANENT));
		if(StrUtil.isNotBlank(model.getDeleteIds())) {// 
			delQuery.must(QueryBuilders.termsQuery(IndexField.DEVICE_ID, Arrays.asList(model.getDeleteIds().split(","))));
		}
		if(StrUtil.isNotBlank(model.getUpdateIds())) {// 
			delQuery.mustNot(QueryBuilders.termsQuery(IndexField.DEVICE_ID, Arrays.asList(model.getUpdateIds().split(","))));
		}
		//删除索引
		delete(ES_INDEX, ES_MAPPING, delQuery);
		/**
		 * 获取需要删除的索引列表
		 */
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
		List<EsCameraGroupVo> list = cameraGroupMapper.findCameraGroupListByMap(form);
		
		//更新es索引数据
		updateEsIndex(model, list);
		
		return list;
	}
	
	/**
	 * 更新es索引数据 用户分组
	 * @param model
	 * @param list
	 */
	private void updateEsIndex(CameraMappingMsg model,List<EsCameraGroupVo> list) {
		if(list == null || list.isEmpty()) {
			return;
		}
		setTreeName(list);
		
    	//更新索引
        list.stream().forEach(t->{
        	addOrUpdate(t.getId(), t, ES_INDEX, ES_MAPPING);
        });
	}
	
	@Override
	public void syncAllCamera() {
		CameraMapVo form = new CameraMapVo();
		
		List<EsCameraGroupVo> list = cameraGroupMapper.findCameraGroupListByMap(form);
		setTreeName(list);
		
		ElasticsearchUtils.addBatchData(list, IndexField.ID, ES_INDEX,ES_MAPPING);
	}
	
	/**
	 * 树节点名称赋值
	 * @param list
	 */
	@Override
	public void setTreeName(List<EsCameraGroupVo> list) {
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

}
