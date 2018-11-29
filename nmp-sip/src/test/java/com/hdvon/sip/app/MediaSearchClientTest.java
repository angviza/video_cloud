package com.hdvon.sip.app;

import java.util.concurrent.Callable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.exception.SipSystemException;
import com.hdvon.sip.service.SipService;
import com.hdvon.sip.utils.CommonUtil;
import com.hdvon.sip.vo.MediaDirectoryVo;
import com.hdvon.sip.vo.MediaQuery;
/**
 * sip服务测试类
 * 
 * @author wanshaojian
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MediaSearchClientTest {

	@Autowired
	SipConfig sipConfig;

	@Autowired
	SipService sipService;
	
	private static final Integer NUM_THREADS = 4;

	@Test
	public void getMediaSearchTest() {
		try {
			String startDate ="2018-09-19T00:00:00";
			String endDate ="2018-09-19T12:00:00";
			
			MediaQuery model = new MediaQuery();
			String deviceCode = sipConfig.getDefaultDeviceCode();
			model.setDeviceCode(deviceCode);
			model.setStartTime(CommonUtil.convertDate(startDate));
			model.setEndTime(CommonUtil.convertDate(endDate));
			
			
			MediaDirectoryVo data = sipService.searchMedia(model);
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>返回结果"+JSON.toJSONString(data));
			
			
		} catch (SipSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	@Test
//	public void batchMediaSearchTest() {
//		try {
//
//	        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
//	        
//			String strText = "[{\"startTime\":\"2018-08-26T13:46:17\",\"endTime\":\"2018-08-26T14:20:17\"},{\"startTime\":\"2018-08-27T13:46:17\",\"endTime\":\"2018-08-27T14:20:17\"},{\"startTime\":\"2018-08-28T13:46:17\",\"endTime\":\"2018-08-28T14:20:17\"},{\"startTime\":\"2018-08-29T13:46:17\",\"endTime\":\"2018-08-29T14:20:17\"}]";
//			List<MediaQuery>  dataList = JSON.parseArray(strText, MediaQuery.class);
//			for(int i=0;i<NUM_THREADS;i++) {
//				MediaQuery model = dataList.get(i);
//				Future<MediaDirectoryVo> future = pool.submit(new MediaSearchThread(model, sipService));
//				MediaDirectoryVo bean = future.get();
//				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>"+JSON.toJSONString(bean));
//			}
//
//		} catch (SipSystemException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//	}

	
	class MediaSearchThread implements Callable<MediaDirectoryVo>{
		MediaQuery model;
		SipService sipService;
		
		public MediaSearchThread(MediaQuery model,SipService sipService) {
			// TODO Auto-generated constructor stub
			this.model = model;
			this.sipService = sipService;
		}

		@Override
		public MediaDirectoryVo call() throws Exception {
			// TODO Auto-generated method stub
			String deviceCode = sipConfig.getDefaultDeviceCode();
			model.setDeviceCode(deviceCode);
			//发送实时媒体点播信息
			return sipService.searchMedia(model);
			
		}
		
	}

}
