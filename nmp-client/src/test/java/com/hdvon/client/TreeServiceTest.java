package com.hdvon.client;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.hdvon.client.service.ITreeService;
import com.hdvon.client.vo.CameraPermissionVo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TreeServiceTest {
	@Autowired
	ITreeService treeService;
	
	@Test
	public void addressTreeTest() throws JsonProcessingException {
		List<CameraPermissionVo> dataList = treeService.getAddressTree();
		
    	System.out.println("=====================【address】===="+JSON.toJSONString(dataList));
	}
	
	@Test
	public void organizationTreeTest() throws JsonProcessingException {
		List<CameraPermissionVo> dataList = treeService.getOrganizationTree();
		
		System.out.println("=====================【organization】===="+JSON.toJSONString(dataList));
	}
	
	@Test
	public void projectTreeTest() throws JsonProcessingException {
		List<CameraPermissionVo> dataList = treeService.getProjectTree();
			
		System.out.println("=====================【projectTree】===="+JSON.toJSONString(dataList));
	}
	
	@Test
	public void groupTreeTest() throws JsonProcessingException {
    	String userId ="3884980183662592";//超管
//    	String userId ="3900498199035904";
		List<CameraPermissionVo> dataList = treeService.getCameraGroupTree(userId);
		
		System.out.println("=====================【cameraGroupTree】===="+JSON.toJSONString(dataList));
	}
	

}
