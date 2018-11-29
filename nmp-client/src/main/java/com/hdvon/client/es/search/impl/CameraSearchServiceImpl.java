package com.hdvon.client.es.search.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdvon.client.es.ElasticsearchUtils;
import com.hdvon.client.es.IndexField;
import com.hdvon.client.es.search.CameraSearchService;
import com.hdvon.client.es.search.UserCameraGroupSearchService;
import com.hdvon.client.mapper.CameraMapper;
import com.hdvon.client.service.CameraHelper;
import com.hdvon.client.vo.CameraMapVo;
import com.hdvon.client.vo.CameraMsg;
import com.hdvon.client.vo.EsCameraVo;
import com.hdvon.nmp.common.SystemConstant;

import cn.hutool.core.util.StrUtil;
/**
 * 摄像机索引库操作类
 * @author wanshaojian
 *
 */
@Service
public class CameraSearchServiceImpl extends BaseSearchServiceImpl<EsCameraVo> implements CameraSearchService{
    
	@Autowired
	private CameraMapper cameraMapper;
	
	public static final String ES_INDEX = "hdvon_camera";
	public static final String ES_MAPPING = "camera";
    
    
	/**
	 * 根据用户变更摄像机信息更新索引库
	 * @param deviceIds
	 */
	@Override
	public void updateByDevId(CameraMsg msg) {
		List<Long> deviceList = CameraHelper.convertIdList(msg.getDeviceIds());
		
		if(msg.getType()==3) {// 删除设备
			BoolQueryBuilder groupQuery = new BoolQueryBuilder();
			groupQuery.must(QueryBuilders.termsQuery(IndexField.DEVICE_ID, deviceList));
			delete(ES_INDEX, ES_MAPPING, groupQuery);
			return ;
		}
		
		CameraMapVo form = new CameraMapVo();
		form.setDeviceList(deviceList);
		List<EsCameraVo> list = cameraMapper.findCameraByDevId(form);
		if(list == null || list.isEmpty()) {
			return;
		}
		
		setTreeName(list);
		//更新索引
		updateIndex(ES_INDEX, ES_MAPPING,  list);
	}
	
	@Override
	public void syncAllCamera() {
		List<EsCameraVo> list = cameraMapper.findAllCamera();
		if(list == null || list.isEmpty()) {
			return;
		}
		setTreeName(list);
	
		ElasticsearchUtils.addBatchData(list, "id", SystemConstant.es_camera_index,SystemConstant.es_camera_mapping);
	}
	

	/**
	 * 树节点名称赋值
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setTreeName(List<EsCameraVo> list) {
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
				t.setAddressName(StrUtil.isNotBlank(addressName) ? addressName:t.getAddressName());
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




}
