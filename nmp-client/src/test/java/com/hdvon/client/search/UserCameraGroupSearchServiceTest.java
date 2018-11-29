package com.hdvon.client.search;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hdvon.client.es.search.UserCameraGroupSearchService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserCameraGroupSearchServiceTest {

	@Resource
	UserCameraGroupSearchService cameraSearchService;
	

	
	
//	@Test
//	public void updateByDevIdTest() {
//		String deviceIds ="3962295685480448";
//		cameraSearchService.updateByDevId(deviceIds);
//		
//	}
	
	@Test
	public void updateByPlanIdTest() {
		String planId ="3962295685480448";
//		cameraSearchService.updateByPlanId(planId);
		
	}
	
	@Test
	public void updateByUserIdTest() {
		String userId ="3962295685480448";
//		cameraSearchService.updateByUserId(userId);
		
	}


}
