package com.hdvon.quartz.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hdvon.quartz.vo.ServicesInfoVo;

/**
 * <br>
 * <b>功能：</b>统计报表静态数据通用构造类，模拟实时数据场景<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public class ReportDataBuilder {
	
	
	public static List<ServicesInfoVo> createServicesInfo(int type) {
		
		List<ServicesInfoVo> list = new ArrayList<ServicesInfoVo>();
		ServicesInfoVo vo = new ServicesInfoVo();
		
		for (int index = 0; index < 8; index++) {
			
			vo = new ServicesInfoVo();
			vo.setIpAddress("192.168.2."+(index+1));
			vo.setPort(5060);
			vo.setName("中心信令服务器"+(index+1));
			vo.setCode("sipServer");
			vo.setEnabled(1);
			vo.setServerStatus(1);
			vo.setUserName("admin");
			vo.setPassword("123456");
			
			DecimalFormat df = new DecimalFormat("0.00");
			
			vo.setCpuUseRate(df.format((Math.random()*99+1)));
			vo.setMemoryUseRate(df.format((Math.random()*99+1)));
			vo.setNetworkUseRate(df.format((Math.random()*99+1)));
			vo.setDiskUseRate(df.format((Math.random()*99+1)));
			
			if (index%4 == 1) {
				vo.setConnectivity("否");
			} else {
				vo.setConnectivity("是");
			}
			
			if (index%4 == 1) {
				vo.setOnlineStatus(0);
			} else {
				vo.setOnlineStatus(1);
			}
			
			if (index%4 == 1) {
				vo.setPacketLostRate(df.format(Math.random()*10));
			} else {
				vo.setPacketLostRate(String.valueOf(index*0.01));
			}
			
			vo.setAverageDelay((int)(Math.random()*100+1)*100L);
			vo.setConcurrentRequest((int)(Math.random()*100+1)*10L);
			vo.setConnections((int)(Math.random()*100+1)*10L);
			vo.setRequestFailure((int)(Math.random()*100+1)*10L);
			
			if (type == 1) {
				
				vo.setCreateTime(new Date());
				vo.setCreateUser("admin");
			} else {
				
				if (index%4 != 1) {
					
					vo.setUpdateTime(new Date());
					vo.setUpdateUser("admin");
				}
			}
			
			list.add(vo);
		}
		
		for (int index = 8; index < 16; index++) {
			
			vo = new ServicesInfoVo();
			vo.setIpAddress("192.168.2."+(index+1));
			vo.setPort(5060);
			vo.setName("流媒体服务器"+(index+1));
			vo.setCode("streamMediaServer");
			vo.setEnabled(1);
			vo.setServerStatus(1);
			vo.setUserName("admin");
			vo.setPassword("123456");
			
			DecimalFormat df = new DecimalFormat("0.00");
			
			vo.setCpuUseRate(df.format((Math.random()*99+1)));
			vo.setMemoryUseRate(df.format((Math.random()*99+1)));
			vo.setNetworkUseRate(df.format((Math.random()*99+1)));
			vo.setDiskUseRate(df.format((Math.random()*99+1)));
			
			if (index%4 == 1) {
				vo.setConnectivity("否");
			} else {
				vo.setConnectivity("是");
			}
			
			if (index%4 == 1) {
				vo.setOnlineStatus(0);
			} else {
				vo.setOnlineStatus(1);
			}
			
			if (index%4 == 1) {
				vo.setPacketLostRate(df.format(Math.random()*10));
			} else {
				vo.setPacketLostRate(String.valueOf(index%5*0.01));
			}
			
			vo.setAverageDelay((int)(Math.random()*100+1)*100L);
			vo.setConcurrentRequest((int)(Math.random()*100+1)*10L);
			vo.setConnections((int)(Math.random()*100+1)*10L);
			vo.setRequestFailure((int)(Math.random()*100+1)*10L);
			
			if (type == 1) {
				
				vo.setCreateTime(new Date());
				vo.setCreateUser("admin");
			} else {
				
				if (index%4 != 1) {
					
					vo.setUpdateTime(new Date());
					vo.setUpdateUser("admin");
				}
			}
			
			list.add(vo);
		}
		
		for (int index = 16; index < 24; index++) {
			
			vo = new ServicesInfoVo();
			vo.setIpAddress("192.168.2."+(index+1));
			vo.setPort(5060);
			vo.setName("转发服务器"+(index+1));
			vo.setCode("transmitServer");
			vo.setEnabled(1);
			vo.setServerStatus(1);
			vo.setUserName("admin");
			vo.setPassword("123456");
			
			DecimalFormat df = new DecimalFormat("0.00");
			
			vo.setCpuUseRate(df.format((Math.random()*99+1)));
			vo.setMemoryUseRate(df.format((Math.random()*99+1)));
			vo.setNetworkUseRate(df.format((Math.random()*99+1)));
			vo.setDiskUseRate(df.format((Math.random()*99+1)));
			
			if (index%4 == 1) {
				vo.setConnectivity("否");
			} else {
				vo.setConnectivity("是");
			}
			
			if (index%4 == 1) {
				vo.setOnlineStatus(0);
			} else {
				vo.setOnlineStatus(1);
			}
			
			if (index%4 == 1) {
				vo.setPacketLostRate(df.format(Math.random()*10));
			} else {
				vo.setPacketLostRate(String.valueOf(index%10*0.01));
			}
			
			vo.setAverageDelay((int)(Math.random()*100+1)*100L);
			vo.setConcurrentRequest((int)(Math.random()*100+1)*10L);
			vo.setConnections((int)(Math.random()*100+1)*10L);
			vo.setRequestFailure((int)(Math.random()*100+1)*10L);
			
			if (type == 1) {
				
				vo.setCreateTime(new Date());
				vo.setCreateUser("admin");
			} else {
				
				if (index%4 != 1) {
					
					vo.setUpdateTime(new Date());
					vo.setUpdateUser("admin");
				}
			}
			
			list.add(vo);
		}
		
		for (int index = 24; index < 32; index++) {
			
			vo = new ServicesInfoVo();
			vo.setIpAddress("192.168.2."+(index+1));
			vo.setPort(5060);
			vo.setName("网关服务器"+(index+1));
			vo.setCode("gatewayServer");
			vo.setEnabled(1);
			vo.setServerStatus(1);
			vo.setUserName("admin");
			vo.setPassword("123456");
			
			DecimalFormat df = new DecimalFormat("0.00");
			
			vo.setCpuUseRate(df.format((Math.random()*99+1)));
			vo.setMemoryUseRate(df.format((Math.random()*99+1)));
			vo.setNetworkUseRate(df.format((Math.random()*99+1)));
			vo.setDiskUseRate(df.format((Math.random()*99+1)));
			
			if (index%4 == 1) {
				vo.setConnectivity("否");
			} else {
				vo.setConnectivity("是");
			}
			
			if (index%4 == 1) {
				vo.setOnlineStatus(0);
			} else {
				vo.setOnlineStatus(1);
			}
			
			if (index%4 == 1) {
				vo.setPacketLostRate(df.format(Math.random()*10));
			} else {
				vo.setPacketLostRate(String.valueOf(index%15*0.01));
			}
			
			vo.setAverageDelay((int)(Math.random()*100+1)*100L);
			vo.setConcurrentRequest((int)(Math.random()*100+1)*10L);
			vo.setConnections((int)(Math.random()*100+1)*10L);
			vo.setRequestFailure((int)(Math.random()*100+1)*10L);
			
			if (type == 1) {
				
				vo.setCreateTime(new Date());
				vo.setCreateUser("admin");
			} else {
				
				if (index%4 != 1) {
					
					vo.setUpdateTime(new Date());
					vo.setUpdateUser("admin");
				}
			}
			
			list.add(vo);
		}
		
		for (int index = 32; index < 40; index++) {
			
			vo = new ServicesInfoVo();
			vo.setIpAddress("192.168.2."+(index+1));
			vo.setPort(5060);
			vo.setName("存储服务器"+(index+1));
			vo.setCode("storeServer");
			vo.setEnabled(1);
			vo.setServerStatus(1);
			vo.setUserName("admin");
			vo.setPassword("123456");
			
			DecimalFormat df = new DecimalFormat("0.00");
			
			vo.setCpuUseRate(df.format((Math.random()*99+1)));
			vo.setMemoryUseRate(df.format((Math.random()*99+1)));
			vo.setNetworkUseRate(df.format((Math.random()*99+1)));
			vo.setDiskUseRate(df.format((Math.random()*99+1)));
			
			if (index%4 == 1) {
				vo.setConnectivity("否");
			} else {
				vo.setConnectivity("是");
			}
			
			if (index%4 == 1) {
				vo.setOnlineStatus(0);
			} else {
				vo.setOnlineStatus(1);
			}
			
			if (index%4 == 1) {
				vo.setPacketLostRate(df.format(Math.random()*10));
			} else {
				vo.setPacketLostRate(String.valueOf(index%20*0.01));
			}
			
			vo.setAverageDelay((int)(Math.random()*100+1)*100L);
			vo.setConcurrentRequest((int)(Math.random()*100+1)*10L);
			vo.setConnections((int)(Math.random()*100+1)*10L);
			vo.setRequestFailure((int)(Math.random()*100+1)*10L);
			
			if (type == 1) {
				
				vo.setCreateTime(new Date());
				vo.setCreateUser("admin");
			} else {
				
				if (index%4 != 1) {
					
					vo.setUpdateTime(new Date());
					vo.setUpdateUser("admin");
				}
			}
			
			list.add(vo);
		}
		
		for (int index = 117; index < 121; index++) {
			
			vo = new ServicesInfoVo();
			vo.setIpAddress("192.168.2."+(index+1));
			vo.setPort(5060);
			vo.setName("转发服务器"+(index+1));
			vo.setCode("transmitServer");
			vo.setEnabled(1);
			vo.setServerStatus(1);
			vo.setUserName("admin");
			vo.setPassword("123456");
			
			DecimalFormat df = new DecimalFormat("0.00");
			
			vo.setCpuUseRate(df.format((Math.random()*99+1)));
			vo.setMemoryUseRate(df.format((Math.random()*99+1)));
			vo.setNetworkUseRate(df.format((Math.random()*99+1)));
			vo.setDiskUseRate(df.format((Math.random()*99+1)));
			
			if (index%4 == 1) {
				vo.setConnectivity("否");
			} else {
				vo.setConnectivity("是");
			}
			
			if (index%4 == 1) {
				vo.setOnlineStatus(0);
			} else {
				vo.setOnlineStatus(1);
			}
			
			if (index%4 == 1) {
				vo.setPacketLostRate(df.format(Math.random()*10));
			} else {
				vo.setPacketLostRate(String.valueOf(index%110*0.01));
			}
			
			vo.setAverageDelay((int)(Math.random()*100+1)*100L);
			vo.setConcurrentRequest((int)(Math.random()*100+1)*10L);
			vo.setConnections((int)(Math.random()*100+1)*10L);
			vo.setRequestFailure((int)(Math.random()*100+1)*10L);
			
			if (type == 1) {
				
				vo.setCreateTime(new Date());
				vo.setCreateUser("admin");
			} else {
				
				if (index%4 != 1) {
					
					vo.setUpdateTime(new Date());
					vo.setUpdateUser("admin");
				}
			}
			
			list.add(vo);
		}
		
		for (int index = 149; index < 154; index++) {
			
			vo = new ServicesInfoVo();
			vo.setIpAddress("192.168.2."+(index+1));
			vo.setPort(5060);
			vo.setName("网关服务器"+(index+1));
			vo.setCode("gatewayServer");
			vo.setEnabled(1);
			vo.setServerStatus(1);
			vo.setUserName("admin");
			vo.setPassword("123456");
			
			DecimalFormat df = new DecimalFormat("0.00");
			
			vo.setCpuUseRate(df.format((Math.random()*99+1)));
			vo.setMemoryUseRate(df.format((Math.random()*99+1)));
			vo.setNetworkUseRate(df.format((Math.random()*99+1)));
			vo.setDiskUseRate(df.format((Math.random()*99+1)));
			
			if (index%4 == 1) {
				vo.setConnectivity("否");
			} else {
				vo.setConnectivity("是");
			}
			
			if (index%4 == 1) {
				vo.setOnlineStatus(0);
			} else {
				vo.setOnlineStatus(1);
			}
			
			if (index%4 == 1) {
				vo.setPacketLostRate(df.format(Math.random()*10));
			} else {
				vo.setPacketLostRate(String.valueOf(index%140*0.01));
			}
			
			vo.setAverageDelay((int)(Math.random()*100+1)*100L);
			vo.setConcurrentRequest((int)(Math.random()*100+1)*10L);
			vo.setConnections((int)(Math.random()*100+1)*10L);
			vo.setRequestFailure((int)(Math.random()*100+1)*10L);
			
			if (type == 1) {
				
				vo.setCreateTime(new Date());
				vo.setCreateUser("admin");
			} else {
				
				if (index%4 != 1) {
					
					vo.setUpdateTime(new Date());
					vo.setUpdateUser("admin");
				}
			}
			
			list.add(vo);
		}
		
		for (int index = 170; index < 173; index++) {
			
			vo = new ServicesInfoVo();
			vo.setIpAddress("192.168.2."+(index+1));
			vo.setPort(5060);
			vo.setName("存储服务器"+(index+1));
			vo.setCode("storeServer");
			vo.setEnabled(1);
			vo.setServerStatus(1);
			vo.setUserName("admin");
			vo.setPassword("123456");
			
			DecimalFormat df = new DecimalFormat("0.00");
			
			vo.setCpuUseRate(df.format((Math.random()*99+1)));
			vo.setMemoryUseRate(df.format((Math.random()*99+1)));
			vo.setNetworkUseRate(df.format((Math.random()*99+1)));
			vo.setDiskUseRate(df.format((Math.random()*99+1)));
			
			if (index%4 == 1) {
				vo.setConnectivity("否");
			} else {
				vo.setConnectivity("是");
			}
			
			if (index%4 == 1) {
				vo.setOnlineStatus(0);
			} else {
				vo.setOnlineStatus(1);
			}
			
			if (index%4 == 1) {
				vo.setPacketLostRate(df.format(Math.random()*10));
			} else {
				vo.setPacketLostRate(String.valueOf(index%170*0.01));
			}
			
			vo.setAverageDelay((int)(Math.random()*100+1)*100L);
			vo.setConcurrentRequest((int)(Math.random()*100+1)*10L);
			vo.setConnections((int)(Math.random()*100+1)*10L);
			vo.setRequestFailure((int)(Math.random()*100+1)*10L);
			
			if (type == 1) {
				
				vo.setCreateTime(new Date());
				vo.setCreateUser("admin");
			} else {
				
				if (index%4 != 1) {
					
					vo.setUpdateTime(new Date());
					vo.setUpdateUser("admin");
				}
			}
			
			list.add(vo);
		}
		
		for (int index = 200; index < 204; index++) {
			
			vo = new ServicesInfoVo();
			vo.setIpAddress("192.168.2."+(index+1));
			vo.setPort(5060);
			vo.setName("流媒体服务器"+(index+1));
			vo.setCode("streamMediaServer");
			vo.setEnabled(1);
			vo.setServerStatus(1);
			vo.setUserName("admin");
			vo.setPassword("123456");
			
			DecimalFormat df = new DecimalFormat("0.00");
			
			vo.setCpuUseRate(df.format((Math.random()*99+1)));
			vo.setMemoryUseRate(df.format((Math.random()*99+1)));
			vo.setNetworkUseRate(df.format((Math.random()*99+1)));
			vo.setDiskUseRate(df.format((Math.random()*99+1)));
			
			if (index%4 == 1) {
				vo.setConnectivity("否");
			} else {
				vo.setConnectivity("是");
			}
			
			if (index%4 == 1) {
				vo.setOnlineStatus(0);
			} else {
				vo.setOnlineStatus(1);
			}
			
			if (index%4 == 1) {
				vo.setPacketLostRate(df.format(Math.random()*10));
			} else {
				vo.setPacketLostRate(String.valueOf(index%200*0.01));
			}
			
			vo.setAverageDelay((int)(Math.random()*100+1)*100L);
			vo.setConcurrentRequest((int)(Math.random()*100+1)*10L);
			vo.setConnections((int)(Math.random()*100+1)*10L);
			vo.setRequestFailure((int)(Math.random()*100+1)*10L);
			
			if (type == 1) {
				
				vo.setCreateTime(new Date());
				vo.setCreateUser("admin");
			} else {
				
				if (index%4 != 1) {
					
					vo.setUpdateTime(new Date());
					vo.setUpdateUser("admin");
				}
			}
			
			list.add(vo);
		}
		
		return list;
		
	}
	
}
