package com.hdvon.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hdvon.client.es.ElasticsearchUtils;
import com.hdvon.client.mapper.CameraMapper;
import com.hdvon.client.mapper.CameraPermissionMapper;
import com.hdvon.client.service.CommonService;
import com.hdvon.client.service.EsUpdateService;
import com.hdvon.client.vo.CameraMappingMsg;
import com.hdvon.client.vo.CameraMsg;
import com.hdvon.nmp.common.SystemConstant;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsUpdateServiceTest {
    @Autowired
    CameraMapper cameraMapper;
    @Autowired
    EsUpdateService esUpdateService;
    
    @Autowired
    CameraPermissionMapper premissionMapper;
    
    @Autowired
    CommonService commonService;
    
//	@Test
//	public void updateDeviceTest() throws JsonProcessingException {
//		//同步ES搜索数据
//		//"id":"4143699541131264","relationId":"4051879512965120","type":1,"updateIds":"3905951719325696,3885889107836928,3954149503188992,3979256754438144"
//		CameraMsg msg = new CameraMsg();
//		msg.setId("4143374539407360");
//		msg.setType(1);
//		msg.setContent("资源角色分配更新索引数据");
//		msg.setDeviceIds("4080092855353345,4054505723133958,4054505723133959");
//		esUpdateService.updateIndexBydevId(msg);
//	}
//    
	@Test
	public void updateUserDevicePermissionTest() throws JsonProcessingException {
		//同步ES搜索数据
		//"relationId":"4140750739357696","type":1,"updateIds":"3885889107836928,3884981553152000"
		//"id":"4143699541131264","relationId":"4051879512965120","type":1,"updateIds":"3905951719325696,3885889107836928,3954149503188992,3979256754438144"
		CameraMappingMsg msg = new CameraMappingMsg();
		msg.setId("4140750739357696");
		msg.setType(1);
		msg.setContent("资源角色分配更新索引数据");
		msg.setUpdateIds("3885889107836928,3884981553152000");
		msg.setRelationId("4051879512965120");
		esUpdateService.batchUpdateIndexByUser(msg);
		

	}
//	
//	@Test
//	public void updatePlanDevicePermissionTest() throws JsonProcessingException {
//		//同步ES搜索数据
//		//{"id":"4143758258880512","relationId":"4139152059613184","type":2,"updateIds":"3885889107836928,3884981553152000,3884980183662592"}
//		CameraMappingMsg msg = new CameraMappingMsg();
//		msg.setId("4143374539407360");
//		msg.setType(2);
//		msg.setContent("权限预案分配更新索引数据");
//		msg.setUpdateIds("3885889107836928,3884981553152000,3884980183662592");
//		msg.setRelationId("4139152059613184");
//		esUpdateService.batchUpdateIndexByPlan(msg);
//		
//	
//	}
//     
//	@Test
//	public void updateGroupDevicePermissionTest() throws JsonProcessingException {
//		//同步ES搜索数据
//		//{"id":"4143758258880512","relationId":"4139152059613184","type":2,"updateIds":"3885889107836928,3884981553152000,3884980183662592"}
//		CameraMappingMsg msg = new CameraMappingMsg();
//		msg.setId("4143374539407360");
//		msg.setType(3);
//		msg.setContent("自定义分组分配更新索引数据");
//		msg.setUpdateIds("4080092855353345,4054505723133958,4054505723133959");
//		msg.setRelationId("4142460262694912");
//		esUpdateService.batchUpdateIndexByGroup(msg);
//		
//	
//	}
//  @Test
//  public void updateIndexByUserRoleTest() throws JsonProcessingException {
////	  {"deleteIds":"3886064884809728","id":"4145229711654912","msgId":"4145229711654913","relationId":"3884981553152000","type":4,"updateIds":"3886064884809728"}
//		//同步ES搜索数据
//		CameraMappingMsg msg = new CameraMappingMsg();
//		msg.setId("4143374539407360");
//		msg.setType(4);
//		msg.setContent("用户分配角色更新索引数据");
//		msg.setUpdateIds("3886064884809728");
//		msg.setRelationId("3884981553152000");
////		msg.setDeleteIds("3886064884809728");
//		esUpdateService.updateByUserRole(msg);
//		
//	}
//	
//	
//    @Test
//    public void updateIndexByDevIdTest() throws JsonProcessingException {
//		//同步ES搜索数据
//		String deviceId ="3907127126884352";
//		CameraMsg msg = new CameraMsg();
//		msg.setId("4143374539407360");
//		msg.setDeviceIds(deviceId);
//		msg.setType(3);
//		msg.setContent("删除索引数据");
//		esUpdateService.updateIndexBydevId(msg);
//		
//	}
//    
//    @Test
//    public void createIndexTest() {
//    	try {
//			File file = ResourceUtils.getFile("classpath:es/hdvon_user_camera_group.json");
//			FileReader fr = new FileReader(file);
//			BufferedReader bfr = new BufferedReader(fr);
//			StringBuffer mapping = new StringBuffer();
//	 
//			String index = SystemConstant.es_user_cameragroup_index;
//			String indexType = SystemConstant.es_user_cameragroup_mapping;
//			boolean isExist= ElasticsearchUtils.isIndexExist(index);
//			//存在则删除该索引库
//			if(isExist) {
//				ElasticsearchUtils.deleteIndex(index);
//			}
//			//重新创建索引库
//			String str = null;
//			while((str = bfr.readLine()) != null) {
////				System.out.println(str.trim());//此时str就保存了一行字符串
//				mapping.append(str.trim());
//			}
////			System.out.println(mapping_json.toString());
//			//创建索引
//			ElasticsearchUtils.createIndex(index);
//			//设置索引元素
//			ElasticsearchUtils.putMapping(index, indexType, mapping.toString());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
//    
//  /**
//  * 生成地址编码
//  * @throws JsonProcessingException
//  */
//	@Test
//	public void genAddressCodeTest() throws JsonProcessingException {
//		//同步ES搜索数据 
//		String code = commonService.genAddressCode("0");
//		System.out.println("========================下级编码："+code);
//		
//		code = commonService.genAddressCode("3882143967445876");//天河区 101142
//		System.out.println("========================下级编码："+code);
////		code = service.genAddressCode("0");
//	}
//
// /**
//  * 生成自定义分组编码
//  * @throws JsonProcessingException
//  */
//	@Test
//	public void genCameraGroupCodeTest() throws JsonProcessingException {
//		//同步ES搜索数据 
//		String code = commonService.genCameraGroupCode("0");
//		System.out.println("========================下级编码："+code);
//		
//		code = commonService.genCameraGroupCode("3884778726998016");//珠江 102101
//		System.out.println("========================下级编码："+code);
////		code = service.genAddressCode("0");
//	}
}
