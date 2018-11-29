package com.hdvon.client;


import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hdvon.client.service.TreeNodeService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TreeNodeServiceTest {
	
	@Resource
	TreeNodeService treeNodeService;
	
//	@Test
//	public void addressTreeTest() throws JsonProcessingException {
//		 treeNodeService.getAddressTree();
//	}
	
	@Test
	public void projectTreeTest() throws JsonProcessingException {
		 treeNodeService.getPojectTree();
	}

}
