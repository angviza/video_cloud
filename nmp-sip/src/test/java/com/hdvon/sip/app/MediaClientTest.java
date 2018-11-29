package com.hdvon.sip.app;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.exception.SipSystemException;
import com.hdvon.sip.service.SipServiceImpl;
import com.hdvon.sip.utils.SipConstants;
import com.hdvon.sip.vo.MediaPlayQuery;
import com.hdvon.sip.vo.MediaPlayVo;
/**
 * sip服务测试类
 * 
 * @author wanshaojian
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MediaClientTest {
	
	@Autowired
	SipConfig sipConfig;

	@Autowired
	SipServiceImpl sipService;
	
	private static final Integer NUM_THREADS = 1;

	@Test
	public void getMediaPlayTest() {
		//发送实时媒体点播信息
		MediaPlayVo  record = getMediaPlay();
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>实时点播信息"+JSON.toJSONString(record));
		
		//视频播放成功才关闭
		if(record.getStatus() == SipConstants.SUCCESS) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			getStopMediaPlay(record);
		}
	}
	private MediaPlayVo  getMediaPlay() {
		
		try {
			MediaClient client = MediaClient.getInstance(sipConfig);
			
			String clientIp = "192.168.2.56";

			MediaPlayQuery model = new MediaPlayQuery();
			model.setReceiveIp(clientIp);
			
			String deviceCode = sipConfig.getDefaultDeviceCode();
			model.setDeviceCode(deviceCode);
			
			String transportType = SipConstants.TRANSPORT_UDP;
			model.setTransportType(transportType);
			
			
			String registerCode = "38020000003000000201";
			
			//发送实时媒体点播信息
			return client.videoPlay(model,registerCode);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void getStopMediaPlay(MediaPlayVo  record ) {

		try{
			MediaClient client = MediaClient.getInstance(sipConfig);
			client.sendBye(record);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	@Test
//	public void getBatchMediaPlayTest() throws InterruptedException, java.util.concurrent.ExecutionException {
//		
//		try {
//	        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
//	        
//			for(int i=0;i<NUM_THREADS;i++) {
//				String clientIp = "192.168.2.56";
//				Future<MediaPlayVo> future = pool.submit(new MediaPlayThread(clientIp,sipService));
//				MediaPlayVo mediaBean = future.get();
//				
//				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+JSON.toJSONString(mediaBean));
//				try {
//					Thread.sleep(10000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				sipService.stopMedia(mediaBean.getCallId(), MediaOperationType.PLAY);
//			}
//			pool.shutdown();
//
//		} catch (SipSystemException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//	}
	
	
	class MediaPlayThread implements Callable<MediaPlayVo>{
		String clientIp;
		SipServiceImpl sipService;
		
		public MediaPlayThread(String clientIp,SipServiceImpl sipService) {
			// TODO Auto-generated constructor stub
			this.clientIp = clientIp;
			this.sipService = sipService;
		}

		@Override
		public MediaPlayVo call() throws Exception {
			// TODO Auto-generated method stub
			MediaPlayVo record = null;
			MediaPlayQuery model = new MediaPlayQuery();
			model.setReceiveIp(clientIp);
			
			String deviceCode = sipConfig.getDefaultDeviceCode();
			model.setDeviceCode(deviceCode);
			
			String transportType = SipConstants.TRANSPORT_UDP;
			model.setTransportType(transportType);
			try {
				//发送实时媒体点播信息
				record = sipService.mediaPlay(model);
				System.out.println("视频播放结果："+JSON.toJSONString(record));
			} catch (SipSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return record;
		}
		
	}

}
