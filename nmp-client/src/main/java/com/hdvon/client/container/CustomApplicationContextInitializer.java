package com.hdvon.client.container;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.hdvon.client.config.ClientConfig;
import com.hdvon.client.es.ElasticsearchUtils;
import com.hdvon.client.es.search.CameraGroupSearchService;
import com.hdvon.client.es.search.CameraSearchService;
import com.hdvon.client.service.EsUpdateService;
import com.hdvon.client.service.TreeNodeService;
import com.hdvon.nmp.common.SystemConstant;

import lombok.extern.slf4j.Slf4j;


/**
 * 自定义的初始化es数据默认同步
 * @author wanshaojian
 *
 */
@Slf4j
@Component
public class CustomApplicationContextInitializer implements ApplicationRunner {
	@Resource
	private CameraSearchService cameraSearchService;
	
	@Resource
	private CameraGroupSearchService cameraGroupSearchService;
	
	@Autowired
	ClientConfig clientConfig;
	
	@Resource
	EsUpdateService esUpdateService;
	@Resource
	TreeNodeService treeNodeService;

	
	@Override
	public void run(ApplicationArguments arguments) throws Exception {
		
		//将地址与所属上级对应关系写入redis
		treeNodeService.getAddressTree();
		//将行政区域与所属上级对应关系写入redis
		treeNodeService.getOrgTree();
		//将项目区域与所属上级对应关系写入redis
		treeNodeService.getPojectTree();
		//将自定义项目与所属上级对应关系写入redis
		treeNodeService.getGrroupTree();
		
		if(clientConfig.getEsRegenerateIndexFlag()) {
			log.info("=========================== 执行ES中索引库的创建  =======================");
			//创建camera索引
			ElasticsearchUtils.createIndex(SystemConstant.es_camera_index, SystemConstant.es_camera_mapping);
			//创建camera_group索引		
			ElasticsearchUtils.createIndex(SystemConstant.es_cameragroup_index, SystemConstant.es_cameragroup_mapping);
			//创建camera索引
			ElasticsearchUtils.createIndex(SystemConstant.es_cameraPermission_index, SystemConstant.es_cameraPermission_mapping);
			//创建camera索引
			ElasticsearchUtils.createIndex(SystemConstant.es_user_cameragroup_index, SystemConstant.es_user_cameragroup_mapping);
		}
		if(clientConfig.getEsSyncDataEnabled()) {
			log.info("=========================== 服务启动执行,执行数据同步到ES系统中等操作  =======================");
			
			//同步camera索引数据
			cameraSearchService.syncAllCamera();
			//同步camera_group索引数据		
			cameraGroupSearchService.syncAllCamera();
			
			//同步所有普通的用户摄像机信息 
			esUpdateService.syncAllCameraPermission();
		}

		
	}

}
