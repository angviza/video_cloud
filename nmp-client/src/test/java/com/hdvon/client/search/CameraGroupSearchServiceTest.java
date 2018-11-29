package com.hdvon.client.search;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hdvon.client.es.search.CameraGroupSearchService;
import com.hdvon.client.mapper.CameraMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CameraGroupSearchServiceTest {

	@Resource
	CameraGroupSearchService searchService;
	
	@Autowired
	CameraMapper cameraMapper;
	
//	@Test
//	public void updateByDevIdTest() {
//		String deviceIds ="3962295686217764";
//		searchService.updateByDevId(deviceIds);
//		
//	}
	
	
	@Test
	public void updateByGroupIdTest() {
		String groupIds ="3961031186366464";
//		searchService.updateByGroupId(groupIds);
		
	}
	
}
