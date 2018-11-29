package com.hdvon.client;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.hdvon.client.enums.AggFieldEnum;
import com.hdvon.client.form.CameraForm;
import com.hdvon.client.service.ICameraService;
import com.hdvon.client.vo.CameraPermissionVo;
import com.hdvon.client.vo.CameraVo;
import com.hdvon.client.vo.EsCameraVo;
import com.hdvon.client.vo.EsPage;
import com.hdvon.client.vo.PointVo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CameraServiceTests {
    @Autowired
    ICameraService service;
    
	@Test
	public void getAddressCameraPermissionTreeTest() {
		String userId ="3884980183662592";//超管
		String pid = "0";
		int isAdmin = 0;
		List<CameraPermissionVo> list = null;

//		System.out.println("=========================【ADDRESS】获取当前地址节点的拥有的摄像机资源");
//		list = service.getAddressCameraPermissionTree(userId, pid, isAdmin);
//		System.out.println("=========================【ADDRESS】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		pid = "3955501540999168";// 广州市
//		list = service.getAddressCameraPermissionTree(userId, pid, isAdmin);
//		System.out.println("=========================【ADDRESS】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		pid = "4048887326261248";//地铁分局
//		list = service.getAddressCameraPermissionTree(userId, pid, isAdmin);
//		System.out.println("=========================【ADDRESS】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		pid="4048887327670272";//一号线扩建改造
//		list = service.getAddressCameraPermissionTree(userId, pid, isAdmin);
//		System.out.println("=========================【ADDRESS】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		pid="4048887327735808";//一号线扩建改造
//		list = service.getAddressCameraPermissionTree(userId, pid, isAdmin);
//		System.out.println("=========================【ADDRESS】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
		
		
    	userId ="3905951719325696";
//		isAdmin = 0;
//		System.out.println("=========================【ADDRESS】获取当前地址节点的拥有的摄像机资源");
//		list = service.getAddressCameraPermissionTree(userId, pid, isAdmin);
//		System.out.println("=========================【ADDRESS】【用户："+userId+"】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		pid = "3882143967445969";// 广州市
//		list = service.getAddressCameraPermissionTree(userId, pid, isAdmin);
//		System.out.println("=========================【ADDRESS】【用户："+userId+"】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		pid = "3882143967379540";//交警支队
//		list = service.getAddressCameraPermissionTree(userId, pid, isAdmin);
//		System.out.println("=========================【ADDRESS】【用户："+userId+"】【pid:"+pid+"】"+JSON.toJSONString(list));
		
		pid="4054493683712000";//交警一大队
		list = service.getAddressCameraPermissionTree(userId, pid, isAdmin);
		System.out.println("=========================【ADDRESS】【用户："+userId+"】【pid:"+pid+"】"+JSON.toJSONString(list));


	}
//    
//	@Test
//	public void getORGCameraPermissionTreeTest() {
//		String userId ="3884980183662592";//超管
//		String pid = "0";
//		int isAdmin = 1;
//
//		System.out.println("=========================【ORG】获取当前地址节点的拥有的摄像机资源");
//		List<CameraPermissionVo> list = service.getOrganizationCameraPermission(userId, pid, isAdmin);
//		System.out.println("=========================【ORG】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		
//		pid="3893188138041344";//广州市
//		list = service.getOrganizationCameraPermission(userId, pid, isAdmin);
//		System.out.println("=========================【ORG】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		pid = "3893188138041345";// 广州市公安局
//		list = service.getOrganizationCameraPermission(userId, pid, isAdmin);
//		System.out.println("=========================【ORG】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		pid = "3893188138041647";//市公安局政治部
//		list = service.getOrganizationCameraPermission(userId, pid, isAdmin);
//		System.out.println("=========================【ORG】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//    	userId ="3900498199035904";
//		isAdmin = 0;
//		list = service.getOrganizationCameraPermission(userId, pid, isAdmin);
//		System.out.println("=========================【ORG】【用户："+userId+"】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		
//		pid="3893188138041344";//广州市
//		list = service.getOrganizationCameraPermission(userId, pid, isAdmin);
//		System.out.println("=========================【ORG】【用户："+userId+"】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		pid = "3893188138041345";// 广州市公安局
//		list = service.getOrganizationCameraPermission(userId, pid, isAdmin);
//		System.out.println("=========================【ORG】【用户："+userId+"】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		pid = "3893188138041647";//市公安局政治部
//		list = service.getOrganizationCameraPermission(userId, pid, isAdmin);
//		System.out.println("=========================【ORG】【用户："+userId+"】【pid:"+pid+"】"+JSON.toJSONString(list));
//
//
//	}
//	
//	@Test
//	public void getProjectCameraPermissionTreeTest() {
//		String userId ="3884980183662592";//超管
//		String pid = "0";
//		int isAdmin = 1;
//		int isProject = 0;
//		System.out.println("=========================【PROJECT】获取当前地址节点的拥有的摄像机资源");
//		List<CameraPermissionVo> list = service.getProjectCameraPermission(userId, pid, isAdmin, isProject);
//		System.out.println("=========================【PROJECT】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		
//		pid="3888689700732928";//弘度资源云(部门)
//		list = service.getProjectCameraPermission(userId, pid, isAdmin, isProject);
//		System.out.println("=========================【PROJECT】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		pid = "3895752523792384";// 弘度承建单位
//		list = service.getProjectCameraPermission(userId, pid, isAdmin, isProject);
//		System.out.println("=========================【PROJECT】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		pid = "3895753082060800";//弘度承建单位001
//		list = service.getProjectCameraPermission(userId, pid, isAdmin, isProject);
//		System.out.println("=========================【PROJECT】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		pid ="3895754294312960";//弘度承建单位项目01
//		isProject = 1;
//		list = service.getProjectCameraPermission(userId, pid, isAdmin, isProject);
//		System.out.println("=========================【PROJECT】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
//
//		
//		userId ="3900498199035904";
//		pid = "0";
//		isAdmin = 0;
//		isProject = 0;
//		list = service.getProjectCameraPermission(userId, pid, isAdmin, isProject);
//		System.out.println("=========================【PROJECT】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		pid = "3895752523792384";// 弘度承建单位
//		list = service.getProjectCameraPermission(userId, pid, isAdmin, isProject);
//		System.out.println("=========================【PROJECT】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		pid = "3895753082060800";//弘度承建单位001
//		list = service.getProjectCameraPermission(userId, pid, isAdmin, isProject);
//		System.out.println("=========================【PROJECT】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		pid ="3895754294312960";//弘度承建单位项目01
//		isProject = 1;
//		list = service.getProjectCameraPermission(userId, pid, isAdmin, isProject);
//		System.out.println("=========================【PROJECT】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
//
//
//	}
//	
//	
//	@Test
//	public void getCameraGroupPermissionTreeTest() {
//		String userId ="3884980183662592";//超管
//		String pid = "0";
//		int isAdmin = 1;
//		String account = null;
//		System.out.println("=========================【GROUP】获取当前地址节点的拥有的摄像机资源");
//		List<CameraPermissionVo> list = service.getCameraGroupPermission(userId, account, pid, isAdmin);
//		System.out.println("=========================【GROUP】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		
//		pid = "3884777997254656";//涉水点位
//		list = service.getCameraGroupPermission(userId, account, pid, isAdmin);
//		System.out.println("=========================【GROUP】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
//
//		pid = "3884778726998016";//珠江
//		list = service.getCameraGroupPermission(userId, account, pid, isAdmin);
//		System.out.println("=========================【GROUP】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		
//		pid = "3886204848422912";//珠江下游
//		list = service.getCameraGroupPermission(userId, account, pid, isAdmin);
//		System.out.println("=========================【GROUP】【用户：超管】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		
//		
//		userId ="3900498199035904";//超管
//		pid = "0";
//		isAdmin = 0;
//		account = "michael";
//		System.out.println("=========================【GROUP】获取当前地址节点的拥有的摄像机资源");
//		list = service.getCameraGroupPermission(userId, account, pid, isAdmin);
//		System.out.println("=========================【GROUP】【用户："+userId+"】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		
//		pid = "3884777997254656";//涉水点位
//		list = service.getCameraGroupPermission(userId, account, pid, isAdmin);
//		System.out.println("=========================【GROUP】【用户："+userId+"】【pid:"+pid+"】"+JSON.toJSONString(list));
//
//		pid = "3884778726998016";//珠江
//		list = service.getCameraGroupPermission(userId, account, pid, isAdmin);
//		System.out.println("=========================【GROUP】【用户："+userId+"】【pid:"+pid+"】"+JSON.toJSONString(list));
//		
//		
//		pid = "3886204848422912";//珠江下游
//		list = service.getCameraGroupPermission(userId, account, pid, isAdmin);
//		System.out.println("=========================【GROUP】【用户："+userId+"】【pid:"+pid+"】"+JSON.toJSONString(list));
//	}
//	@Test
//	public void getDistanceRangeCameraListTest() {
//		String userId ="3884980183662592";//超管
//		
//		String orgCode ="44010001,44010002,44010003";
//		CameraForm form = new CameraForm();
//		form.setUserId(userId);
//		form.setQueryStr("小学");
////		form.setOrgCode(orgCode);
//		System.out.println("=====================【DistanceRange】====地图附近用户搜索");
//		EsPage<EsCameraVo> list = service.getDistanceRangeCameraList(form, 22.768000,113.549020,5000,1,200);
//		System.out.println("=====================【DistanceRange】===="+JSON.toJSONString(list));
//		
//    	userId ="3900498199035904";
//		form.setUserId(userId);
//		list = service.getDistanceRangeCameraList(form, 22.768000,113.549020,5000,1,200);
//		System.out.println("=====================【DistanceRange】【userId:"+userId+"】===="+JSON.toJSONString(list));
//	}
//    
//    
//    /**
//     * 使用 BoundingBoxQuery进行查询
//     */
//    @Test
//    public void getBoundingBoxQueryTest( ){
//    	String userId ="3884980183662592";//超管
//		CameraForm form = new CameraForm();
//		form.setUserId(userId);
//		System.out.println("=======================【BoundingBoxQuery】==地图矩形搜索");
//		EsPage<EsCameraVo> list = service.getBoundingBoxCameraList(form,23.1659368677921,113.308599365233,23.14806253595638, 113.26740063476318,1,200);
//		System.out.println("=======================【BoundingBoxQuery】=="+JSON.toJSONString(list));
//		
//		
//    	userId ="3900498199035904";
//		form.setUserId(userId);
////		maxLon: 113.308599365233, maxLat: 23.1659368677921, minLon: 113.26740063476318, minLat: 23.14806253595638
//		list = service.getBoundingBoxCameraList(form,23.1659368677921,113.308599365233,23.14806253595638, 113.26740063476318,1,200);
//		System.out.println("=======================【BoundingBoxQuery】【userId:"+userId+"】=="+JSON.toJSONString(list));
//    }
//    /**
//     * 多边形查询
//     */
//    @Test
//    public void testPolygonQuery() {
//    	String userId ="3884980183662592";//超管
//    	
//		CameraForm form = new CameraForm();
//		form.setUserId(userId);
//		
//    	List<PointVo> points = new ArrayList<PointVo>();
//    	points.add(new PointVo(21.70,113.50));
//    	points.add(new PointVo(22.80,112.45));
//    	points.add(new PointVo(22.76,114.52));
//    	points.add(new PointVo(22.00,110.44));
//    	System.out.println("======================【PolygonQuery】===地图多边形搜索");
//    	EsPage<EsCameraVo> list = service.getPolygonCameraList(form, points,1,200);
//		System.out.println("======================【PolygonQuery】==="+JSON.toJSONString(list));
//		
//    	userId ="3900498199035904";
//		form.setUserId(userId);
//    	list = service.getPolygonCameraList(form, points,1,200);
//		System.out.println("======================【PolygonQuery】【userId:"+userId+"】==="+JSON.toJSONString(list));
//    }
//    

//    @Test
//    public void searchCameraListTest() {
//    	CameraForm form = new CameraForm();
//    	String userId ="3884980183662592";//超管
////    	Long deviceId = 4072646483017729L;
//    	
////    	String userId ="4108309066186752";
//    	form.setUserId(userId);
////    	form.setOrgCode("4401");
////    	form.setQueryStr("70004公园前站（一号线)一");
//    	form.setQueryStr("测试");
////    	form.setDeviceId(deviceId);
//    	System.out.println("=====================【searchCamera】====地图模糊搜索");
//    	String fields="deviceId,deviceCode,deviceName,deviceType,location,installAddress,permissionValue";
//    	EsPage<EsCameraVo> list = null;
//    	list = service.searchCameraList(form, 1, 200, fields,null);
//    	System.out.println("=====================【searchCamera】  size:"+list.getPageSize()+"===="+JSON.toJSONString(list));
//    	
////    	form.setQueryStr("东山 路");
////    	list = service.searchCameraList(form, 1, 200, fields,null);
//    	System.out.println("=====================【searchCamera】===="+JSON.toJSONString(list));
//    	
//    	
//    	
//    	userId ="3900498199035904";
//    	
//    	form.setUserId(userId);
////    	form.setOrgCode("4401");
////    	form.setQueryStr("东山");
//    	System.out.println("=====================【searchCamera】====地图模糊搜索");
//    	list = service.searchCameraList(form, 1, 200, fields,null);
//    	System.out.println("=====================【searchCamera】===="+JSON.toJSONString(list));
//    	
//    }
    
//    
//  @Test
//  public void findCameraRecordTest() {
//  	String deviceId ="3907118216626176";//超管
//  	
//  	System.out.println("=====================【findCameraRecord】====");
//  	CameraVo list = service.findCameraRecord(deviceId);
//  	System.out.println("=====================【findCameraRecord】===="+JSON.toJSONString(list));
//  	
//  }
   
    
//  @Test
//  public void searchHighCameraListTest() {
//  	
//  	System.out.println("=====================【searchHighCameraList】====");
//  	
//	String userId ="3884980183662592";//超管
//  	
//  	CameraForm form = new CameraForm();
//  	form.setUserId(userId);
////  	form.setDeviceCode("44011500111320010050");
//  	form.setDeviceName("公园");
////  	form.setAddressCode("101149111");
////  	form.setDeviceType(2);
//  	
//  	String fields = null;
//  	
//  	List<CameraPermissionVo> list = service.searchHighCameraList(form,  fields, AggFieldEnum.ADDRESS_CODE);
//  	System.out.println("=====================【searchHighCameraList】===="+JSON.toJSONString(list));
//  	
//  }
//  
//  
//  @Test
//  public void searchNomnalPlanCameraListTest() {
//  	
//  	System.out.println("=====================【searchNomnalPlanCameraListTest】====");
//  	
//	String userId ="3884980183662592";//超管
////  	String userId ="3905951719325696";//hgx
//  	String queryStr = "南洲";
//  	String fields = null;
//  	String deviceCode = null;
//  	List<CameraPermissionVo> list = new ArrayList<>();
////    list = service.searchNomalCameraList(queryStr,deviceCode, userId, fields, AggFieldEnum.ADDRESS_CODE);
////  	
////  	System.out.println("=====================【searchNomnalPlanCameraListTest】 【address_code】===="+JSON.toJSONString(list));
////  	System.out.println("=====================【searchNomnalPlanCameraListTest】 数据===="+list.size());  	
//  	
//  	
////  	list = service.searchNomalCameraList(queryStr,deviceCode, userId, fields, AggFieldEnum.ORG_CODE);
////  	
////  	System.out.println("=====================【searchNomnalPlanCameraListTest】 【ORG_CODE】===="+JSON.toJSONString(list));
////  	System.out.println("=====================【searchNomnalPlanCameraListTest】 数据===="+list.size());  
//  	
//  	
////  	list = service.searchNomalCameraList(queryStr,deviceCode, userId, fields, AggFieldEnum.PROJECT_NAME);
////  	
////  	System.out.println("=====================【searchNomnalPlanCameraListTest】 【ORG_CODE】===="+JSON.toJSONString(list));
////  	System.out.println("=====================【searchNomnalPlanCameraListTest】 数据===="+list.size());  
//  	
//  	
//  	list = service.searchNomalCameraList(queryStr,deviceCode, userId, fields, AggFieldEnum.GROUP_NAME);
//  	
//  	System.out.println("=====================【searchNomnalPlanCameraListTest】 【ORG_CODE】===="+JSON.toJSONString(list));
//  	System.out.println("=====================【searchNomnalPlanCameraListTest】 数据===="+list.size());  
//  }
}
