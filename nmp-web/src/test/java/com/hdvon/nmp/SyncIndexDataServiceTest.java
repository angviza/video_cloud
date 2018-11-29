package com.hdvon.nmp;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.client.vo.CameraMsg;
import com.hdvon.nmp.config.kafka.KafkaMsgProducer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SyncIndexDataServiceTest {
	
	@Autowired
	private KafkaMsgProducer kafkaMsgProducer;

    @Test
    public void sendSyncDeviceMappingPermissionTest1() throws JsonProcessingException {
		//同步ES搜索数据
		CameraMappingMsg msg = new CameraMappingMsg();
		msg.setId("123456");
		msg.setType(1);
		msg.setContent("用户授权更新索引数据");
//		msg.setIds("3905951719325696");
		kafkaMsgProducer.sendCameraMapping(msg);
	}
    
    
    @Test
    public void sendSyncDeviceMappingPermissionTest2() throws JsonProcessingException {
		//同步ES搜索数据
		CameraMappingMsg msg = new CameraMappingMsg();
		msg.setId("123456");
		msg.setType(3);
		msg.setContent("摄像机分组更新索引数据");
//		msg.setIds("4054551670161408");
		kafkaMsgProducer.sendCameraMapping(msg);
	}
    
    
    @Test
    public void sendSyncDeviceMappingPermissionTest3() throws JsonProcessingException {
    	
    	//同步ES搜索数据
		CameraMappingMsg msg = new CameraMappingMsg();
		msg.setId("4143374539407360");
		msg.setType(2);
		msg.setContent("摄像机权限预案更新索引数据");
		msg.setUpdateIds("3905951719325696,3900498199035904");
		msg.setDeleteIds("3884981553152000");
		msg.setRelationId("4051879512965120");
		kafkaMsgProducer.sendCameraMapping(msg);
		
	}

    
    @Test
    public void sendSyncDevicePermissionTest() throws JsonProcessingException {
		
		//同步ES搜索数据
		String deviceId ="3907127126884352";
		CameraMsg msg = new CameraMsg();
		msg.setId("4143374539407360");
		msg.setDeviceIds(deviceId);
		msg.setType(3);
		msg.setContent("删除设备索引数据");
		kafkaMsgProducer.sendCamera(msg);
		
	}

}
