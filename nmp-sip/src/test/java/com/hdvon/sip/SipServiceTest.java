package com.hdvon.sip;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.enums.MediaOperationType;
import com.hdvon.sip.exception.SipSystemException;
import com.hdvon.sip.service.SipServiceImpl;
import com.hdvon.sip.utils.CommonUtil;
import com.hdvon.sip.utils.SipConstants;
import com.hdvon.sip.vo.MediaDirectoryVo;
import com.hdvon.sip.vo.MediaDownloadQuery;
import com.hdvon.sip.vo.MediaDownloadVo;
import com.hdvon.sip.vo.MediaItemVo;
import com.hdvon.sip.vo.MediaPlayQuery;
import com.hdvon.sip.vo.MediaPlayVo;
import com.hdvon.sip.vo.MediaQuery;
import com.hdvon.sip.vo.VideoControlType.ScaleEnum;
import com.hdvon.sip.vo.VideoControlType.VideoMethodEnum;
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
	SipServiceImpl sipService;
	
	
	@Autowired
	SipConfig sipConfig;


//	@Test
//	public void registerTest() {
//		try {
//			sipService.register();
//		} catch (SipSystemException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void logoutTest() {
//		try {
//			sipService.logout("38020000003000000202");
//		} catch (SipSystemException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void checkHeartbeatTest() {
//		try {
//			sipService.checkHeartbeat();
//		} catch (SipSystemException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	@Test
	public void getMediaPlayTest() {
		try {
			String clientIp= "192.168.2.56";
			MediaPlayQuery model = new MediaPlayQuery();
			model.setReceiveIp(clientIp);
			
			String deviceCode = sipConfig.getDefaultDeviceCode();
			model.setDeviceCode(deviceCode);
			
			String transportType = SipConstants.TRANSPORT_UDP;
			model.setTransportType(transportType);
			try {
				//发送实时媒体点播信息
				MediaPlayVo record = sipService.mediaPlay(model);
				System.out.println("视频播放结果："+JSON.toJSONString(record));
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(record.getStatus()!=null) {
					boolean status = sipService.stopMedia(record.getCallId(),MediaOperationType.PLAY);
					System.out.println("视频停止结果："+(status?"成功":"失败"));
				}
			} catch (SipSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SipSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	
//	@Test
//	public void queryVideoTest() {
//		try {
//			String deviceCode = sipConfig.getDefaultDeviceCode();
//			MediaQuery model = new MediaQuery();
//			model.setDeviceCode(deviceCode);
//			model.setStartTime(CommonUtil.convertDate("2018-09-03T00:00:00"));
//			model.setEndTime(CommonUtil.convertDate("2018-09-03T14:00:00"));
//			
//			MediaDirectoryVo data = sipService.searchMedia(model);
//			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>翻查结果"+JSON.toJSONString(data));
//			
//		} catch (SipSystemException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//
//	@Test
//	public void getMediaDownloadTest() {
//		
//		String deviceCode = sipConfig.getDefaultDeviceCode();
//		MediaQuery model1 = new MediaQuery();
//		model1.setDeviceCode(deviceCode);
//		model1.setStartTime(CommonUtil.convertDate("2018-09-03T00:00:00"));
//		model1.setEndTime(CommonUtil.convertDate("2018-09-03T02:00:00"));
//		
//		MediaDirectoryVo data = sipService.searchMedia(model1);
//
//		if(data.getDataList()== null || data.getDataList().isEmpty()) {
//			return;
//		}
//		MediaItemVo itemVo = data.getDataList().get(0);
//		String uri = itemVo.getUri();
//		String clientIp = "192.168.2.56";
//		
//		Date startTime = itemVo.getStartDate();
//		Date endTime = itemVo.getEndDate();
//		
//		
//		MediaDownloadQuery model = new MediaDownloadQuery();
//		model.setReceiveIp(clientIp);
//		
//		model.setDeviceCode(deviceCode);
//		
//		String transportType = SipConstants.TRANSPORT_UDP;
//		model.setTransportType(transportType);
//		model.setUri(uri);
//		model.setStartDate(startTime);
//		model.setEndDate(endTime);
//		try {
//			//发送实时媒体点播信息
//			MediaDownloadVo record = sipService.mediaDownload(model);
//			System.out.println("视频下载结果："+JSON.toJSONString(record));
//			
//			try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			if(record.getStatus()!=null) {
//				boolean status = sipService.stopMedia(record.getCallId(),MediaOperationType.DOWNLAOD);
//				System.out.println("视频停止结果："+(status?"成功":"失败"));
//			}
//
//		} catch (SipSystemException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	
	
//	@Test
//	public void getMediaPauseTest() {
//		String clientIp = "192.168.2.56";
//		
//		String startDate ="2018-09-27T00:00:00";
//		String endDate ="2018-09-27T03:00:00";
//		String deviceCode = sipConfig.getDefaultDeviceCode();
//		
//		MediaQuery model = new MediaQuery();
//		model.setReceiveIp(clientIp);
//		model.setDeviceCode(deviceCode);
//		model.setStartTime(CommonUtil.convertDate(startDate));
//		model.setEndTime(CommonUtil.convertDate(endDate));
//		
//		//发送实时媒体点播信息
//		MediaDirectoryVo record = sipService.searchMediaAndPlay(model);
//		System.out.println("视频回看结果："+JSON.toJSONString(record));
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//录像暂停
//		MediaDirectoryVo controlRecord = sipService.playbackControl(record.getPkId(),VideoMethodEnum.PAUSE, null);
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>录像回看监控状态："+JSON.toJSONString(controlRecord));
//		
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		//录像播放
//		sipService.playbackControl(record.getPkId(), VideoMethodEnum.PLAY, null);
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		//录像快进
//		sipService.playbackFastForward(record.getPkId(), ScaleEnum.TWO_TIMES);
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		//录像随机播放
//		Long randomTime = 3680L;
//		sipService.playbackControl(record.getPkId(), VideoMethodEnum.RANDOM_PLAY, randomTime);
//		try {
//			Thread.sleep(30000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		boolean status = sipService.stopMedia(record.getPkId(),MediaOperationType.PLAYBACK);
//		System.out.println("视频停止结果："+(status?"成功":"失败"));
//	}
	



}
