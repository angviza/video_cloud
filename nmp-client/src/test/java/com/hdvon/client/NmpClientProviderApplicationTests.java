package com.hdvon.client;


import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hdvon.client.mapper.CameraMapper;
import com.hdvon.client.mapper.CameraPermissionMapper;
import com.hdvon.client.service.EsUpdateService;
import com.hdvon.client.service.ICameraService;
import com.hdvon.client.service.IUserService;
import com.hdvon.client.vo.EsCameraVo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NmpClientProviderApplicationTests {
    
    @Autowired
    ICameraService service;
    
    

    
    @Autowired
    IUserService userService;
    
    @Autowired
    CameraMapper cameraMapper;
    
//    @Test
//    public void getDepartmentTreeTest() {
//    	List<DepartmentVo> list = userService.getDepartmentTree();
//    	System.out.println(JSON.toJSON(list));
//    }
//	@Test
//	public void isIndexExistTest() {
//		
//		boolean exist = ElasticsearchUtils.isIndexExist("hdvon");
//		System.out.println("========================="+exist);
////		ElasticsearchUtils.createIndex("ymq_inexsssss");
//	}
//	
//	@Test
//	public void searchListDataTest() {
//		
//		List<Map<String, Object>> list = ElasticsearchUtils.searchListData("hdvon", "cameraPermission", "", null);
//		System.out.println("========================="+JSON.toJSONString(list));
////		ElasticsearchUtils.createIndex("ymq_inexsssss");
//	}

	
//	@Test
//	public void getNearbyPeopleTest() {
//		List<Map<String, Object>> list = ElasticsearchUtils.getNearbyPeople("hdvon", "cameraPermission","3900498199035904", 22.768000,113.549020,10);
//		System.out.println("========================="+JSON.toJSONString(list));
//	}
    

    

    
    

}
