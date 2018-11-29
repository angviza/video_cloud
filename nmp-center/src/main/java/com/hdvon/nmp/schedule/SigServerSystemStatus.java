package com.hdvon.nmp.schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class SigServerSystemStatus {
	//@Scheduled(initialDelay = 60000, fixedRate = 60000)
	//@Scheduled(cron = "0/2 * * * * *")
	public void updateSystemStatus() {
		try {
			System.out.println("更新信令服务器系统状态定时器:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			String url ="";
			URL restServiceURL = new URL(url);
			HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
			httpConnection.setRequestMethod("GET");
			//返回xml
			//httpConnection.setRequestProperty("Content-Type","text/plain; charset=utf-8");
			//返回json
			httpConnection.setRequestProperty("Accept","application/json");
	        if(httpConnection.getResponseCode() != 200) {
	            throw new RuntimeException("HTTP GET Request Failed with Error code : "
	                          +httpConnection.getResponseCode());
	        }
	        BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
	        String output;
	        System.out.println("Output from Server:  \n");
	        while ((output =responseBuffer.readLine()) != null) {
	        	System.out.println(output);
	        }
	        //解析json
	        httpConnection.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
