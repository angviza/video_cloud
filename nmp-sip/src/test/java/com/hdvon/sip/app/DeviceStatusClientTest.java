package com.hdvon.sip.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.service.SipService;
import com.hdvon.sip.vo.DeviceStatusVo;

/**
 * sip服务测试类
 * 
 * @author wanshaojian
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DeviceStatusClientTest {
	@Autowired
	SipConfig sipConfig;
	
	@Autowired
	SipService SipService;
	

	
	
	@Test
	public void searchDeviceStatusTest() {
		try{
			String deviceCode = sipConfig.getDefaultDeviceCode();
			DeviceStatusVo data = SipService.searchDeviceStatus(deviceCode);

			System.out.println("打印设备状态查询结果："+JSON.toJSONString(data));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
