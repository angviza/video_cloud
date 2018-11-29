package com.hdvon.sip.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.service.SipService;
import com.hdvon.sip.utils.SipConstants;
import com.hdvon.sip.vo.VideotapeQuery;
import com.hdvon.sip.vo.VideotapeVo;

/**
 * sip服务测试类
 * 
 * @author wanshaojian
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class VideotapeClientTest {
	@Autowired
	SipConfig sipConfig;
	
	@Autowired
	SipService sipService;
	
	@Test
	public void videotapeTest() {
		
		String deviceCode = sipConfig.getDefaultDeviceCode();
		String storageServerCode = sipConfig.getDefaultStorageServerCode();
		
		VideotapeQuery model = new VideotapeQuery();
		model.setStorageServerCode(storageServerCode);
		model.setDeviceCode(deviceCode);
		model.setCmdType(SipConstants.TAPE_TYPE_START);
		//开始录像
		VideotapeVo tapeVo1 = sipService.mediaTapeControl(model);
		System.out.println("打印录像结果："+JSON.toJSONString(tapeVo1));
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.setCmdType(SipConstants.TAPE_TYPE_STOP);
		//停止录像
		VideotapeVo tapeVo2 = sipService.mediaTapeControl(model);
		System.out.println("打印停止录像结果："+JSON.toJSONString(tapeVo2));
		
	}
	
	
}
