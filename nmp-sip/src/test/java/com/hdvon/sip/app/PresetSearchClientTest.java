package com.hdvon.sip.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.service.SipService;
import com.hdvon.sip.vo.DevicePresetVo;
/**
 * sip服务测试类
 * 
 * @author wanshaojian
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PresetSearchClientTest {
	
	@Autowired
	SipConfig sipConfig;

	@Autowired
	SipService sipService;
	
	@Test
	public void getDeletePresetTest() {
		String registerCode = "38020000003000000201";
		String deviceCode = sipConfig.getDefaultDeviceCode();
		PresetSearchClient client = PresetSearchClient.getInstance(sipConfig);
		
		
		DevicePresetVo data = client.searchPreset(deviceCode, registerCode);
		System.out.println(">>>>>>>>>>>>>>>>>>Delete:"+JSON.toJSONString(data));
	}

}
