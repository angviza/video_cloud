package com.hdvon.sip.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.service.SipService;
import com.hdvon.sip.vo.PresetQuery;
import com.hdvon.sip.vo.PresetQuery.PresetTypeEnum;
import com.hdvon.sip.vo.PresetVo;
/**
 * sip服务测试类
 * 
 * @author wanshaojian
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PresetClientTest {
	
	@Autowired
	SipConfig sipConfig;

	@Autowired
	SipService sipService;

	@Test
	public void getDeletePresetTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		PresetQuery model = new PresetQuery();
		model.setDeviceCode(deviceCode);
		model.setTypeEnum(PresetTypeEnum.DELETE);
		model.setPresetNum(5);
		
		PresetVo data = getPresetProcess(model);
		System.out.println(">>>>>>>>>>>>>>>>>>Delete:"+JSON.toJSONString(data));
	}
	@Test
	public void getSetPresetTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		PresetQuery model = new PresetQuery();
		model.setDeviceCode(deviceCode);
		model.setTypeEnum(PresetTypeEnum.SET);
		model.setPresetNum(5);
		
		PresetVo data = getPresetProcess(model);
		System.out.println(">>>>>>>>>>>>>>>>>>Delete:"+JSON.toJSONString(data));
	}
	
	@Test
	public void getTranPresetTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		PresetQuery model = new PresetQuery();
		model.setDeviceCode(deviceCode);
		model.setTypeEnum(PresetTypeEnum.TRANSFER);
		model.setPresetNum(1);
		
		PresetVo data = getPresetProcess(model);
		System.out.println(">>>>>>>>>>>>>>>>>>Delete:"+JSON.toJSONString(data));
	}
	
	
	private PresetVo getPresetProcess(PresetQuery model) {
		try {
			//云台控制
			 PresetVo data = sipService.presetCmd(model);
			 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>data:"+JSON.toJSONString(data));
			 return data;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
