/*package com.hdvon.nmp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import com.sip.device.jni.service.IDeviceMonitorService;

*//**
 * <br>
 * <b>功能：</b>线索翻查单元测试类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018/7/16<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 *//*
@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackages = {"com.sip"})
public class CameraReplayUnitTest {

	@Autowired
	private IDeviceMonitorService deviceMonitorService;

	@Test
	public void test() {
		//String deviceCode = "44011500541320000008";
		//String key = "3884981553152000_"+deviceCode+"_CALLID";
		
		String receiverIp = "192.168.2.101";
		Integer receiverPort = 5060;
		deviceMonitorService.startUp(receiverIp, receiverPort);
		
	}
	
	
	

}
*/