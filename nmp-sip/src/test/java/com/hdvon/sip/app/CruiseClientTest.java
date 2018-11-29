package com.hdvon.sip.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.service.SipService;
import com.hdvon.sip.vo.CruiseQuery;
import com.hdvon.sip.vo.CruiseQuery.CruiseTypeEnum;
import com.hdvon.sip.vo.CruiseVo;
/**
 * sip服务测试类
 * 
 * @author wanshaojian
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CruiseClientTest {
	
	@Autowired
	SipConfig sipConfig;

	@Autowired
	SipService sipService;
	
	@Test
	public void getAddCruiseTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		CruiseQuery model = new CruiseQuery();
		model.setDeviceCode(deviceCode);
		model.setTypeEnum(CruiseTypeEnum.ADD);
		model.setGroupNum(1);
		model.setPresetNum(1);
		
		getCruiseProcess(model);
	}
	
	@Test
	public void getDelCruiseTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		CruiseQuery model = new CruiseQuery();
		model.setDeviceCode(deviceCode);
		model.setTypeEnum(CruiseTypeEnum.DEL);
		model.setGroupNum(1);
		model.setPresetNum(1);
		
		getCruiseProcess(model);
	}
	
	@Test
	public void getSpeedCruiseTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		CruiseQuery model = new CruiseQuery();
		model.setDeviceCode(deviceCode);
		model.setTypeEnum(CruiseTypeEnum.SET_SPEED);
		model.setGroupNum(1);
		model.setPresetNum(1);
		model.setSpeed(2);
		
		getCruiseProcess(model);
	}
	@Test
	public void getStoppoverCruiseTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		CruiseQuery model = new CruiseQuery();
		model.setDeviceCode(deviceCode);
		model.setTypeEnum(CruiseTypeEnum.SET_STOPOVER);
		model.setGroupNum(1);
		model.setPresetNum(1);
		
		getCruiseProcess(model);
	}
	@Test
	public void getStartCruiseTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		CruiseQuery model = new CruiseQuery();
		model.setDeviceCode(deviceCode);
		model.setTypeEnum(CruiseTypeEnum.START);
		model.setGroupNum(1);
		model.setPresetNum(1);
		
		getCruiseProcess(model);
	}
	@Test
	public void getStopCruiseTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		CruiseQuery model = new CruiseQuery();
		model.setDeviceCode(deviceCode);
		model.setTypeEnum(CruiseTypeEnum.STOP);
		model.setGroupNum(1);
		model.setPresetNum(1);
		
		getCruiseProcess(model);
	}
	private void getCruiseProcess(CruiseQuery model) {
		try {
			//云台控制
			CruiseVo data = sipService.CruiseControl(model);
			System.out.println("打印巡航预案结果："+JSON.toJSONString(data));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
