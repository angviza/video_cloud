package com.hdvon.sip.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.exception.SipSystemException;
import com.hdvon.sip.service.SipService;
import com.hdvon.sip.vo.CloudControlQuery;
import com.hdvon.sip.vo.CloudControlQuery.DirectionEnum;
import com.hdvon.sip.vo.CloudControlQuery.FocusEnum;
import com.hdvon.sip.vo.CloudControlQuery.IrisEnum;
import com.hdvon.sip.vo.CloudControlQuery.ZoomEnum;
/**
 * sip服务测试类
 * 
 * @author wanshaojian
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CloudControlClientTest {
	
	@Autowired
	SipConfig sipConfig;

	@Autowired
	SipService sipService;

	@Test
	public void getUpCloudControlTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		CloudControlQuery model = new CloudControlQuery();
		model.setDeviceCode(deviceCode);
		model.setDirection(DirectionEnum.UP);
		model.setStepSize(1);
		getCloudControlProcess(model);
	}
	@Test
	public void getDownCloudControlTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		CloudControlQuery model = new CloudControlQuery();
		model.setDeviceCode(deviceCode);
		model.setDirection(DirectionEnum.DOWN);
		model.setStepSize(1);
		getCloudControlProcess(model);
	}

	@Test
	public void getLeftCloudControlTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		CloudControlQuery model = new CloudControlQuery();
		model.setDeviceCode(deviceCode);
		model.setDirection(DirectionEnum.LEFT);
		model.setStepSize(5);
		getCloudControlProcess(model);
	}
	@Test
	public void getRightCloudControlTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		CloudControlQuery model = new CloudControlQuery();
		model.setDeviceCode(deviceCode);
		model.setDirection(DirectionEnum.RIGHT);
		model.setStepSize(1);
		getCloudControlProcess(model);
	}
	@Test
	public void getUpperLeftCloudControlTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		CloudControlQuery model = new CloudControlQuery();
		model.setDeviceCode(deviceCode);
		model.setDirection(DirectionEnum.UPPER_LEFT);
		model.setStepSize(1);
		getCloudControlProcess(model);
	}
	
	@Test
	public void getUpperRightCloudControlTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		CloudControlQuery model = new CloudControlQuery();
		model.setDeviceCode(deviceCode);
		model.setDirection(DirectionEnum.UPPER_RIGHT);
		model.setStepSize(1);
		getCloudControlProcess(model);
	}
	@Test
	public void getLowerRightCloudControlTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		CloudControlQuery model = new CloudControlQuery();
		model.setDeviceCode(deviceCode);
		model.setDirection(DirectionEnum.LOWER_RIGHT);
		model.setStepSize(1);
		getCloudControlProcess(model);
	}
	
	
	@Test
	public void getZoomAmptifyCloudControlTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		CloudControlQuery model = new CloudControlQuery();
		model.setDeviceCode(deviceCode);
		model.setZoom(ZoomEnum.AMPLIFY);
		model.setStepSize(1);
		getCloudControlProcess(model);
	}
	@Test
	public void getZoomNarrowCloudControlTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		CloudControlQuery model = new CloudControlQuery();
		model.setDeviceCode(deviceCode);
		model.setZoom(ZoomEnum.NARROW);
		model.setStepSize(1);
		getCloudControlProcess(model);
	}
	
	@Test
	public void getIrisAmplifyCloudControlTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		CloudControlQuery model = new CloudControlQuery();
		model.setDeviceCode(deviceCode);
		model.setIris(IrisEnum.AMPLIFY);
		model.setStepSize(1);
		getCloudControlProcess(model);
	}
	@Test
	public void getIrisNarrowCloudControlTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		CloudControlQuery model = new CloudControlQuery();
		model.setDeviceCode(deviceCode);
		model.setIris(IrisEnum.NARROW);
		model.setStepSize(1);
		getCloudControlProcess(model);
	}
	
	@Test
	public void getFocusFarCloudControlTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		CloudControlQuery model = new CloudControlQuery();
		model.setDeviceCode(deviceCode);
		model.setFocus(FocusEnum.FAR);
		model.setStepSize(1);
		getCloudControlProcess(model);
	}
	
	@Test
	public void getFocusNearCloudControlTest() {
		String deviceCode = sipConfig.getDefaultDeviceCode();
		CloudControlQuery model = new CloudControlQuery();
		model.setDeviceCode(deviceCode);
		model.setFocus(FocusEnum.NEAR);
		model.setStepSize(1);
		getCloudControlProcess(model);
	}
	private void getCloudControlProcess(CloudControlQuery model) {
		try {
			//云台控制
			sipService.cloudControl(model);
		} catch (SipSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
