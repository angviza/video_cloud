package com.hdvon.sip;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.hdvon.nmp.enums.MethodEum;
import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.entity.RequestBean;
import com.hdvon.sip.entity.ResponseBean;
import com.hdvon.sip.entity.SipResultBean;
import com.hdvon.sip.entity.param.CallParam;
import com.hdvon.sip.entity.param.CloudParam;
import com.hdvon.sip.entity.param.DeviceParam;
import com.hdvon.sip.entity.param.DownloadParam;
import com.hdvon.sip.entity.param.InviteParam;
import com.hdvon.sip.entity.param.PlaybackControlParam;
import com.hdvon.sip.entity.param.PlaybackParam;
import com.hdvon.sip.entity.param.RecordParam;
import com.hdvon.sip.entity.param.VideotapeParam;
import com.hdvon.sip.enums.CloudControlTypeEnum;
import com.hdvon.sip.enums.DirectionEnum;
import com.hdvon.sip.enums.PlaybackEnum;
import com.hdvon.sip.enums.ScaleEnum;
import com.hdvon.sip.enums.TapeTypeEnum;
import com.hdvon.sip.service.SipService;
/**
 * sip服务测试类
 * 
 * @author wanshaojian
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SipServiceTest {

	@Autowired
	SipService sipService;
	
	
	@Autowired
	SipConfig sipConfig;

	private static final String host ="192.168.2.56";

//	@Test
//	public void registerTest() {
//		String registerCode = "38020000003000000200";
//		SipResultBean data = sipService.register(registerCode);
//		
//	}
//	
//
//	@Test
//	public void sendHeartbeatTest() {
//		String registerCode = "38020000003000000200";
//		sipService.sendHeartbeat(registerCode);
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block

//		}
//	}
//	
//	

	@Test
	public void inviteTest() {
		String registerCode = "38020000003000000200";
		
		RequestBean model = new RequestBean();
		model.setVersion("1.0");
		model.setMethod(MethodEum.PLAY.getValue());
		model.setToken("K71U8DBPNE");
		model.setWsId("abc");
		model.setTransactionID("eyJsaWNlbnNlSWQiOiJLNzFV");
		
		InviteParam invite = new InviteParam();
		invite.setProtocol("28181");
		invite.setTransport("udp");
		invite.setHost("192.168.2.57");
		invite.setPort(5600);
		invite.setDeviceID("38020000001320000010");
		model.setParam(invite);
		
		ResponseBean data = sipService.sendMsg(JSON.toJSONString(model),host);
		
		System.out.println(">>>>>>>>>>>>>>视频点播消息："+JSON.toJSONString(data));
		SipResultBean inviteData = (SipResultBean) data.getResult();
		String callId = inviteData.getCallId();
//		//取消操作
//		sendCannel(callId);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//停止流
		sendTerminate(callId);

	}
	
//	@Test
//	public void terminateTest() {
//		sendTerminate("702739a5497653c63d0cc1f07d470daa@192.168.2.56");
//	}
//	
//	@Test
//	public void queryRecordTest() {
//		
//		String startDate ="2018-11-09 14:44:36";
//		String endDate ="2018-11-09 20:46:36";
////		String endDate ="2018-11-09 14:46:36";
//		
//		RequestBean model = new RequestBean();
//		model.setVersion("1.0");
//		model.setMethod(MethodEum.QUERYRECORD.getValue());
//		model.setTransactionID("123456");
//		model.setWsId("123456");
//		
//		RecordParam param = new RecordParam();
//		String deviceID = sipConfig.getDefaultDeviceCode();
//		param.setDeviceID(deviceID);
//		param.setStartTime(startDate);
//		param.setEndTime(endDate);
//		
//		model.setParam(param);
//		
//		sipService.sendMsg(JSON.toJSONString(model),host);
//		
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block

//		}
//	}
//	@Test
//	public void playbackTest() {
//		RequestBean model = new RequestBean();
//		model.setVersion("1.0");
//		model.setMethod(MethodEum.PLAYBACK.getValue());
//		model.setToken("K71U8DBPNE");
//		model.setTransactionID("eyJsaWNlbnNlSWQiOiJLNzFV");
//		model.setWsId("1234");
//		//"recordList":[{"deviceID":"38020000001320000010","endTime":"2018-10-18T11:02:47","fileSize":3145728,"name":"DH-NVR5408-4KS2","playTime":1000,"secrecy":"0","startTime":"2018-10-18T11:02:46","type":"time","uri":"38020000001320000010:1"},{"deviceID":"38020000001320000010","endTime":"2018-10-18T11:03:21","fileSize":19398656,"name":"DH-NVR5408-4KS2","playTime":33000,"secrecy":"0","startTime":"2018-10-18T11:02:48","type":"time","uri":"38020000001320000010:2"}
//		String uri="38020000001320000010:255";
//		
//		String startTime = "2018-11-04T09:32:35";
//		String endTime = "2018-11-04T09:43:45";
//		PlaybackParam invite = new PlaybackParam();
//		invite.setProtocol("28181");
//		invite.setTransport("udp");
//		invite.setHost("192.168.2.56");
//		invite.setPort(31000);
//		invite.setDeviceID("38020000001320000010");
//		invite.setUri(uri);
//		invite.setStartTime(startTime);
//		invite.setEndTime(endTime);
//		model.setParam(invite);
//		
//		ResponseBean data = sipService.sendMsg(JSON.toJSONString(model),host);
//		
//		System.out.println(">>>>>>>>>>>>>>视频点播消息："+JSON.toJSONString(data));
//		SipResultBean inviteData = (SipResultBean) data.getResult();
//		String callId = inviteData.getCallId();
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block

//		}
//		
//		//回看控制
//		playbackControl(callId,PlaybackEnum.MULTIPLE,ScaleEnum.AHALF_TIMES,0);
//		
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block

//		}
//		playbackControl(callId,PlaybackEnum.RANDOM_PLAY,null,10000);
//		
//		
////		//取消操作
////		sendCannel(callId);
//		
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block

//		}
////		//停止流
//		sendTerminate(callId);
//	}
//	
//	private void playbackControl(String callId,PlaybackEnum em,ScaleEnum scaleEm,int range) {
//		RequestBean model = new RequestBean();
//		model.setVersion("1.0");
//		model.setToken("K71U8DBPNE");
//		model.setWsId("123456");
//		model.setTransactionID("eyJsaWNlbnNlSWQiOiJLNzFV");
//		model.setMethod(MethodEum.PLAYBACK_CONTROL.getValue());
//		
//		//取消操作
//		PlaybackControlParam callParam = new PlaybackControlParam();
//		callParam.setCallId(callId);
//		callParam.setType(em.getKey());
//		if(scaleEm!=null) {
//			callParam.setScale(scaleEm.getKey());
//		}
//		callParam.setRange(range);
//		model.setParam(callParam);
//		
//		ResponseBean data  = (ResponseBean) sipService.sendMsg(JSON.toJSONString(model),host);
//		
//		System.out.println(">>>>>>>>>>>>>>回看控制："+JSON.toJSONString(data));
//	}
//	@Test
//	public void downloadTest() {
//		String registerCode = "38020000003000000200";
//		
//		RequestBean model = new RequestBean();
//		model.setVersion("1.0");
//		model.setMethod(MethodEum.DOWNLOAD.getValue());
//		model.setToken("K71U8DBPNE");
//		model.setTransactionID("eyJsaWNlbnNlSWQiOiJLNzFV");
//		model.setWsId("abc");
//		
//		String uri="38020000001320000010:1";
//		String startTime = "2018-10-18T11:02:46";
//		String endTime = "2018-10-18T11:02:47";
//		DownloadParam param = new DownloadParam();
//		param.setDeviceID("38020000001320000010");
//		param.setProtocol("28181");
//		param.setTransport("udp");
//		param.setHost("192.168.2.56");
//		param.setPort(5600);
//		param.setDeviceID("38020000001320000010");
//		param.setUri(uri);
//		param.setStartTime(startTime);
//		param.setEndTime(endTime);
//		model.setParam(param);
//		
//		ResponseBean data = sipService.sendMsg(JSON.toJSONString(model),host);
//		System.out.println(">>>>>>>>>>>>>>录像下载："+JSON.toJSONString(data));
//		
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block

//		}
//	}
//	
//	
//	@Test
//	public void recordTest() {
//		String registerCode = "38020000003000000200";
//		
//		RequestBean model = new RequestBean();
//		model.setVersion("1.0");
//		model.setMethod(MethodEum.RECORD.getValue());
//		model.setToken("K71U8DBPNE");
//		model.setTransactionID("eyJsaWNlbnNlSWQiOiJLNzFV");
//		model.setWsId("abc");
//		
//		VideotapeParam param = new VideotapeParam();
//		param.setDeviceID("38020000001320000010");
//		param.setType(TapeTypeEnum.START.getValue());
//		model.setParam(param);
//		
//		ResponseBean data = sipService.sendMsg(JSON.toJSONString(model),host);
//		System.out.println(">>>>>>>>>>>>>>录像开始："+JSON.toJSONString(data));
//		
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block

//		}
//		param.setType(TapeTypeEnum.STOP.getValue());
//		model.setParam(param);
//		
//		data = sipService.sendMsg(JSON.toJSONString(model),host);
//		System.out.println(">>>>>>>>>>>>>>录像结束："+JSON.toJSONString(data));
//	}
//	
//	@Test
//	public void cloudControlTest() {
//		String registerCode = "38020000003000000200";
//		
//		RequestBean model = new RequestBean();
//		model.setVersion("1.0");
//		model.setMethod(MethodEum.CLOUD.getValue());
//		model.setToken("K71U8DBPNE");
//		model.setTransactionID("eyJsaWNlbnNlSWQiOiJLNzFV");
//		model.setWsId("abc");
//		
//		CloudParam param = new CloudParam();
//		param.setDeviceID("38020000001320000010");
//		param.setType(CloudControlTypeEnum.DIRECTION.getValue());
//		param.setDirection(DirectionEnum.UP.getKey());
//		param.setSpeed(1);
//		model.setParam(param);
//		
//		ResponseBean data = sipService.sendMsg(JSON.toJSONString(model),host);
//		
//		
//		System.out.println(">>>>>>>>>>>>>>云台控制："+JSON.toJSONString(data));
//
//	}
//	
//	
//
//	@Test
//	public void searchDeviceStatusTest() {
//		String deviceCode = sipConfig.getDefaultDeviceCode();
//		DeviceParam param = new DeviceParam();
//		param.setDeviceID(deviceCode);
//		
//		RequestBean model = new RequestBean();
//		model.setVersion("1.0");
//		model.setMethod(MethodEum.CLOUD.getValue());
//		model.setToken("K71U8DBPNE");
//		model.setTransactionID("eyJsaWNlbnNlSWQiOiJLNzFV");
//		model.setWsId("abc");
//		
//		model.setParam(param);
//		sipService.searchDeviceStatus(model);
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block

//		}
//	}
//	
//	@Test
//	public void queryPresetTest() {
//		String deviceCode = sipConfig.getDefaultDeviceCode();
//		DeviceParam param = new DeviceParam();
//		param.setDeviceID(deviceCode);
//		
//		RequestBean model = new RequestBean();
//		model.setVersion("1.0");
//		model.setMethod(MethodEum.QUERYPRESET.getValue());
//		model.setToken("K71U8DBPNE");
//		model.setTransactionID("eyJsaWNlbnNlSWQiOiJLNzFV");
//		model.setWsId("abc");
//		
//		model.setParam(param);
//		sipService.sendMsg(JSON.toJSONString(model), deviceCode);
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block

//		}
//	}
//	
	private void sendTerminate(String callId) {
		RequestBean model = new RequestBean();
		model.setVersion("1.0");
		model.setToken("K71U8DBPNE");
		model.setWsId("12345");
		model.setTransactionID("eyJsaWNlbnNlSWQiOiJLNzFV");
		model.setMethod(MethodEum.TERMINATE.getValue());
		
		//取消操作
		CallParam callParam = new CallParam();
		callParam.setCallId(callId);
		model.setParam(callParam);
		
		ResponseBean data  = (ResponseBean) sipService.sendMsg(JSON.toJSONString(model),host);
		
		System.out.println(">>>>>>>>>>>>>>停止消息："+JSON.toJSONString(data));
	}
//	
//	
//
//	private void sendCannel(String callId) {
//		//取消操作
//		CallParam callParam = new CallParam();
//		callParam.setCallId(callId);
//		
//		RequestBean model = new RequestBean();
//		model.setVersion("1.0");
//		model.setMethod(MethodEum.PLAY.getValue());
//		model.setToken("K71U8DBPNE");
//		model.setWsId("abc");
//		model.setTransactionID("eyJsaWNlbnNlSWQiOiJLNzFV");
//		model.setMethod(MethodEum.CANCEL.getValue());
//		model.setParam(callParam);
//		ResponseBean<SipResultBean> data = (ResponseBean<SipResultBean>) sipService.sendMsg(JSON.toJSONString(model),host);
//		System.out.println(">>>>>>>>>>>>>>取消消息："+JSON.toJSONString(data));
//	}
}
