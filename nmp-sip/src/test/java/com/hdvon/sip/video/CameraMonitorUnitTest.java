/*package com.hdvon.sip.video;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import com.hdvon.nmp.biz.CameraBiz;
import com.hdvon.nmp.util.ClientUtil;
import com.hdvon.nmp.util.CompositeParamsUtils;
import com.hdvon.nmp.vo.sip.CameraRegisteredParamVo;
import com.hdvon.nmp.vo.sip.ControlOptionVo;
import com.hdvon.nmp.vo.sip.InviteOptionVo;
import com.hdvon.nmp.vo.sip.PlayDownload;
import com.hdvon.nmp.vo.sip.QueryPreOptionVo;
import com.hdvon.nmp.vo.sip.WiperOptionVo;
import com.sip.CallbackResponseVo;
import com.sip.InviteOption;
import com.sip.RegisterCallback;
import com.sip.device.jni.service.IDeviceMonitorService;
import lombok.extern.slf4j.Slf4j;

*//**
 * <br>
 * <b>功能：</b>视频监控单元测试类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018/7/16<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 *//*
@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackages = {"com.sip","com.hdvon.nmp"})
@Slf4j
public class CameraMonitorUnitTest {
	
	@Autowired
	private IDeviceMonitorService deviceMonitorService;
	@Autowired
	private CompositeParamsUtils compositeParamsUtils;
	@Autowired
	private CameraBiz cameraBiz;
	
	private static String deviceCode="38020000001320000010";
	
	@Before
	public void testRegister() {
		
		RegisterCallback callback=this.cameraBiz.register(null);
		log.info("请求注册----"+callback);
		
	}
	
	//实时播放
	@Test
	public void testPaly() {
		InviteOptionVo invite = new InviteOptionVo();
		CameraRegisteredParamVo param = new CameraRegisteredParamVo();
		invite.setDeviceCode(deviceCode);
		invite.setPlayType("1");
		invite.setProtocol("RTP/AVP");
		String clientIp = "192.168.2.60";
		param.setClientIp(clientIp);//获取点播的ip地址
		param.setClientPort(ClientUtil.getClientIpAndProt(clientIp, 1).get(0));
		
		InviteOption inviteParam = compositeParamsUtils.creatInviteParam(invite, param);
		log.info("请求参数点播参数----"+ param);
		
		CallbackResponseVo	responseVo = deviceMonitorService.callInvite(inviteParam);
		log.info("请求点播返回结果 -----"+responseVo);
		
		if(responseVo !=null) {
			deviceMonitorService.callTerminate(responseVo.getCallId());
			log.info("请求点播成功后关闭流-----");
		}
	}
	
	
	//录像回放
	@Test
	public void testPalyBack() {
		InviteOptionVo invite = new InviteOptionVo();
		CameraRegisteredParamVo param = new CameraRegisteredParamVo();
		invite.setDeviceCode(deviceCode);
		invite.setPlayType("2");
		invite.setProtocol("RTP/AVP");
		invite.setStartTime(1532052000L);
		invite.setEndTime(1532053200L);
		String clientIp = "192.168.2.60";
		param.setClientIp(clientIp);//获取点播的ip地址
		param.setClientPort(ClientUtil.getClientIpAndProt(clientIp, 1).get(0));
		
		InviteOption inviteParam = compositeParamsUtils.creatInviteParam(invite, param);
		log.info("请求参数点播参数----"+ param);
		
		CallbackResponseVo	responseVo = deviceMonitorService.callInvite(inviteParam);
		log.info("请求点播返回结果 -----"+responseVo);
		
		if(responseVo !=null) {
			deviceMonitorService.callTerminate(responseVo.getCallId());
			log.info("请求点播成功后关闭流-----");
		}
	}
	
	//云台控制
	@Test
	public void testDirectionControl() {
		ControlOptionVo parame =new ControlOptionVo();
		parame.setDeviceCode(deviceCode);
		parame.setStep(2);
		parame.setIPriority(2);
		parame.setIsCancel(1);
		this.cameraBiz.cloudControl(parame);
		log.info("云台控制成功---");
	}
	
	
	//取消云台控制
	@Test
	public void testIsCancel() {
		ControlOptionVo parame =new ControlOptionVo();
		parame.setDeviceCode(deviceCode);
		parame.setIsCancel(0);//0 取消云台控制
		this.cameraBiz.cloudControl(parame);
		log.info("云台控制取消成功---");
	}
	
	//预置位控制
	@Test
	public void testPresetControl() {
		PresetOptionVo param = new PresetOptionVo();
		param.setDeviceCode(deviceCode);
		param.setPresetNum(10);
		param.setPresetType(1);//1 设置预置位, 2 调用预置位,3  删除预置位
		this.cameraBiz.presetControl(param);
		log.info("预置位控制成功---参数--"+param);
		
	}
	
	
	//预置位查询
	@Test
	public void testQueryPreset() {
		QueryPreOptionVo param = new QueryPreOptionVo();
		param.setDeviceCode(deviceCode);
		this.cameraBiz.queryPreset(param);
		log.info("预置位查询成功---参数--"+param);
		
	}
	
	//录像控制
	@Test
	public void testPalyDownload() {
		PlayDownload param = new PlayDownload();
		param.setDeviceCode(deviceCode);
		param.setCmdType(1);//1 发起录像；2 发起取消录像
		this.cameraBiz.palyDownload(param);
		log.info("录像控制成功---参数--"+param);
		
	}
	
	//雨刷控制
	@Test
	public void testWiperControl() {
		WiperOptionVo param = new WiperOptionVo();
		param.setDeviceCode(deviceCode);
		param.setCmdType(1);//控制类型：1：开2：关
		this.cameraBiz.wiperControl(param);
		log.info("录像控制成功---参数--"+param);
		
	}
	
	
	
	

//	@Autowired
//	private IDeviceMonitorService deviceMonitorService;
//
//	@Test
//	public void testCallRegister() {
//		//String deviceCode = "44011500541320000008";
//		//String key = "3884981553152000_"+deviceCode+"_CALLID";
//		
//		String receiverIp = "192.168.2.60";
//		Integer receiverPort = 5060;
//		deviceMonitorService.startUp(receiverIp, receiverPort);
//			
//		RegisterOption option = new RegisterOption();
//		RegisterCred cred = new RegisterCred();
//		
//		cred.setRealm("*");
//		cred.setUsername("38020000003000000009");
//		cred.setPassword("12345678");
//		cred.setSchema("Digest");
//		
//		//信令中心ID@信令中心域
//		option.setServer("sip:38020000002000000001@3802000000");
//		//信令中心的路由IP地址和端口
//		option.setRoute("sip:38020000002000000001@192.168.2.3:5060");
//		//本地信令账号@域
//		option.setFrom("sip:38020000003000000009@3802000000");
//		//本地信令账号@下一跳信令的IP地址，（填本地IP地址）:下一跳信令的端口（填本地端口）
//		option.setContact("sip:38020000003000000009@"+receiverIp+":"+receiverPort);
//		//本地信令账号@中心域（提供第三方注册使用，本客户端填写本地信令账号）
//		option.setTo("sip:38020000003000000009@3802000000");
//		//option.setExpire(7200);
//		
//		//调用dubbo微服务接口
//		RegisterCallback responseVo = deviceMonitorService.callRegister(option, cred);
//		log.info("=================== testCallRegister CallbackResponseVo ============== "+responseVo);
//			
//		//InviteOptionParamVo param = new InviteOptionParamVo();
//		//CallbackResponseVo responseVo = cameraMonitorService.callInvite(param);
//		
//	}
	
	
	

}
*/