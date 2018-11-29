package com.hdvon.client.search;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.hdvon.client.mapper.TreeMapper;
import com.hdvon.client.vo.CameraPermissionVo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TreeServiceTest {
	
	@Autowired
	TreeMapper treeMapper;
	
	@Test
	public void addressTreeTest() {
		List<CameraPermissionVo> treeList = treeMapper.findAddressTree();
		Map<String, CameraPermissionVo> treeMap = treeList.stream().collect(Collectors.toMap(CameraPermissionVo::getCode, Function.identity(),(entity1,entity2) -> entity1));
		System.out.println(JSON.toJSONString(treeMap));
		
	}
	
//	@Test
//	public void organizationTreeTest() {
//		List<CameraPermissionVo> treeList =treeMapper.findOrganizationTree();
//		Map<String, CameraPermissionVo> treeMap = treeMap = treeList.stream().collect(Collectors.toMap(CameraPermissionVo::getCode, cameraPermissionVo->cameraPermissionVo));
//		System.out.println(JSON.toJSONString(treeMap));
//	}
	
}
