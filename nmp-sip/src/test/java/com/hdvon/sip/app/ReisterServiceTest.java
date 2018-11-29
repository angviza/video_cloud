package com.hdvon.sip.app;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.entity.RegisterBean;
import com.hdvon.sip.service.SipService;
/**
 * sip服务测试类
 * 
 * @author wanshaojian
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ReisterServiceTest {

	private static final Integer NUM_THREADS = 4;
	
	@Autowired
	SipConfig sipConfig;
	
	@Autowired
	SipService sipService;
	
	@Test
	public void registerTest() {

		try(RegisterClient client = RegisterClient.getInstance(sipConfig)) {
			String registerCode = "38020000003000000201";
			RegisterBean model = new RegisterBean(registerCode, sipConfig.getExpire());
			client.registerProcess(model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void logoutTest() throws IOException {
		
		try(RegisterClient client = RegisterClient.getInstance(sipConfig)) {
			String registerCode = "38020000003000000201";
			int expires = 0;
			RegisterBean model = new RegisterBean(registerCode, expires);
			client.registerProcess(model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void batchRegisterTest() {
		
		try {
	        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
	        
	        String[] userList = {"38020000003000000200","38020000003000000201","38020000003000000202","38020000003000000206"};
//	        String[] userList = {"38020000003000000200","38020000003000000201"};
			for(int i=0;i<userList.length;i++) {
				String registerCode = userList[i];
				RegisterBean model = new RegisterBean(registerCode, sipConfig.getExpire());
				pool.submit(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						sipService.register();
						System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>"+JSON.toJSONString(model));
					}
				});
			}
			pool.shutdown();
			
			
			Thread.sleep(1000);
			
			sipService.logout("38020000003000000201");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
