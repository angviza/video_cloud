package com.hdvon.sip.app;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.entity.HearbeatBean;
import com.hdvon.sip.service.SipService;

import lombok.extern.slf4j.Slf4j;

/**
 * sip服务测试类
 * 
 * @author wanshaojian
 *
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class HearbeatClientTest {

	@Autowired
	SipConfig sipConfig;
	
	@Autowired
	SipService sipService;
	
	private static final Integer NUM_THREADS = 4;
	
	@Test
	public void checkHearbeatTest() throws IOException {
		try(HearbeatClient client = HearbeatClient.getInstance(sipConfig)) {
			String checkUserCode = "38020000003000000201";
			
			
			HearbeatBean model = new HearbeatBean(checkUserCode);
			client.sendHeartbeat(model);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	@Test
//	public void batchHearbeatTest() throws IOException {
//		
//		try(HearbeatClient client = new HearbeatClient(sipConfig)) {
//	        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
//	        
//	        String[] userList = {"38020000003000000200","38020000003000000201","38020000003000000202","38020000003000000206"};
//			for(int i=0;i<NUM_THREADS;i++) {
//				String checkUserCode = userList[i];
//				HearbeatBean model = new HearbeatBean(checkUserCode);
//				pool.submit(new Runnable() {
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						client.sendHeartbeat(model);
//						System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>"+JSON.toJSONString(model));
//					}
//				});
//			}
//			pool.shutdown();
//			
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			
//		} catch (SipSystemException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
