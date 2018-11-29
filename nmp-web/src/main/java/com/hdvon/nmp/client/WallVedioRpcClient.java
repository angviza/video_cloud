package com.hdvon.nmp.client;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryException;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.remoting.ExecutionException;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.MatrixChannelVo;
import com.hdvon.nmp.vo.MatrixScreenVo;
import com.hdvon.nmp.vo.MatrixVo;
import com.hdvon.nmp.vo.WallPlanVo;
import com.hdvon.nmp.vo.sip.InviteOptionVo;
/**
 * -32000	"ERROR INVITE"    错误的invite
 *	-32001	"ERROR NOT POST"  请求不是POST<IO异常请求未到服务端>
 *	-32002	"ERROR URL"       url不是/api/decode<已验证>
 *	-32003	"ERROR NO BODY"   没有消息体
 *	-32004	"ERROR METHOD NOT PLAY OR STOP"    method不是play或stop<已验证>
 *	-32005	"ERROR METHOD"    method为空<IO异常请求未到服务端>
 *	-32006	"ERROR PARAM"     param为空
 */

public class WallVedioRpcClient {
	
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss,SSS");
	
	private String matrixIp;
	private Integer matrixPort;
	
	public WallVedioRpcClient(String matrixIp, Integer matrixPort) {
		this.setMatrixIp(matrixIp);
		this.setMatrixPort(matrixPort);
	}
	
	public static void main(String[] args) throws ParseException, IOException {
		//可用摄像机编号
		//37020000001320000200----37020000001320000210
		
		InviteOptionVo vo = new InviteOptionVo();
		vo.setMatrixCode("37020000001140000001");
		
		//摄像机上墙、轮巡
		List<String> deviceCodeList = new ArrayList<String>();
		deviceCodeList.add("34020000001320000005");
		//deviceCodeList.add("34020000001320000006");
		//deviceCodeList.add("34020000001320000001");

		List<String> channelIdList = new ArrayList<String>();
		channelIdList.add("37020000001330000001");
		//channelIdList.add("37020000001330000002");
		
		vo.setDeviceCodeList(deviceCodeList);
		vo.setChannelIdList(channelIdList);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long startTime = sdf.parse("2018-09-18 09:00:00").getTime();
		long endTime = sdf.parse("2018-09-21 18:00:00").getTime();
		vo.setStartTime(startTime);
		vo.setEndTime(endTime);
		
		WallPlanVo wallVo = new WallPlanVo();
		List<MatrixChannelVo> channelVos = new ArrayList<MatrixChannelVo>();
		MatrixChannelVo channelVo = new MatrixChannelVo();
		channelVo.setDevicesNo("37020000001330000001");
		List<CameraVo> cameraVos = new ArrayList<CameraVo>();
		CameraVo cameraVo = new CameraVo();
		cameraVo.setSbbm("34020000001320000005");
		cameraVos.add(cameraVo);
		CameraVo cameraVo1 = new CameraVo();
		cameraVo1.setSbbm("34020000001320000001");
		cameraVos.add(cameraVo1);
		channelVo.setCameras(cameraVos);
		
		channelVos.add(channelVo);
		wallVo.setMatrixChannels(channelVos);
		
		//new WallVedioRpcClient("192.168.2.65", 8765).startWallplan(wallVo);
		
		//上墙或者轮巡
	    //new WallVedioRpcClient("192.168.2.65", 8765).upOrPollingWallVedio(vo);
		
		//状态查询
		new WallVedioRpcClient("192.168.2.65", 8765).getScreenStatus("37020000001140000001");
		 
		//下墙
		//new WallVedioRpcClient("192.168.2.65", 8765).downWallVedio(vo,"channel");
		
		//暂停轮巡
		//new WallVedioRpcClient("192.168.2.65", 8765).pausePolling("37020000001140000001");
		
		//继续轮巡
		//new WallVedioRpcClient("192.168.2.65", 8765).continuePolling("37020000001140000001",30);
		
		//停止轮巡
		//new WallVedioRpcClient("192.168.2.65", 8765).stopPolling("37020000001140000001");
		
		//全屏
		
		//目录查询
		//new WallVedioRpcClient("192.168.2.65", 8765).getMatrixScreens("37020000001140000001");
		
		//录像上墙
		//new WallVedioRpcClient("192.168.2.65", 8765).vedioToWall(vo);
	}
	
	/**
	 * 视屏上墙或者轮询
	 * @param vo
	 */
	public boolean upOrPollingWallVedio(InviteOptionVo vo) throws IOException{
		try {
			
			System.out.println("请求上墙接口:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			String url ="http://"+this.matrixIp+":"+this.matrixPort+"/api/decode";
			
			URL restServiceURL = new URL(url);
			HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setUseCaches(false);
			
			//发送json格式数据
			httpConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
			//接收json格式数据
			httpConnection.setRequestProperty("Accept","application/json");
			
			/*httpConnection.setRequestProperty("Content-type", "text/html");  
			httpConnection.setRequestProperty("Accept-Charset", "utf-8");  
			httpConnection.setRequestProperty("contentType", "utf-8"); */
			
			JSONObject json = new JSONObject();
			json.put("method", "playall");
			JSONObject paramJson = new JSONObject();
			
			List<String> deviceCodeList = vo.getDeviceCodeList();
			List<String> channelIdList = vo.getChannelIdList();
			JSONArray deviceCodeArr = new JSONArray();
			deviceCodeArr.addAll(deviceCodeList);
			JSONArray channelIdArr = new JSONArray();
			channelIdArr.addAll(channelIdList);
			
			paramJson.put("cameraID", deviceCodeArr);
			paramJson.put("screenID", channelIdArr);
			json.put("params", paramJson);
			json.put("playtime", vo.getTimeInterval());
			json.put("outtime", 5);
			json.put("id", 1);
			
			
			byte[] writeBytes = json.toString().getBytes();
			 
			
			OutputStream out = httpConnection.getOutputStream();
			
			
			 
			out.write(writeBytes);
			out.flush();
			out.close();
				/* DataOutputStream dos = new DataOutputStream(httpConnection.getOutputStream());
             dos.writeBytes(URLEncoder.encode(json.toString(), "utf-8"));
             dos.flush();
             dos.close();*/
			int code = httpConnection.getResponseCode();
	        if(code != 200) {
	            throw new RuntimeException("HTTP GET Request Failed with Error code : "
	                          +httpConnection.getResponseCode());
	        }
	        System.out.println(httpConnection.getContentEncoding()+"-------------");
	        String msg = httpConnection.getResponseMessage();
	        System.out.println(msg+"-------------");
	        

	        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(),"utf-8"));
	       
	        String output;
	        StringBuffer sb = new StringBuffer();
	        System.out.println("Output from Server:  \n");
	        while ((output =bufferedReader.readLine()) != null) {
	        	sb.append(output);
	        }
	        System.out.println(sb.toString());
	        JSONObject jsonObj = new JSONObject();
	        JSONObject jsonResult = jsonObj.parseObject(sb.toString());
	        Integer id = (Integer) jsonResult.get("id");
	        String result = (String) jsonResult.get("result");
	        
	        System.out.println("{id="+id+"},{result="+result+"},{code="+code+"}");

	        //解析json
	        httpConnection.disconnect();
	        
	       
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 视屏、录像下墙
	 * @param vo
	 * @param downType (channel:通道下墙；matrix:一键矩阵下墙)
	 */
	public void downWallVedio(InviteOptionVo vo, String downType) throws IOException{
		try {
			System.out.println("请求下墙接口:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			String url ="http://"+this.matrixIp+":"+this.matrixPort+"/api/decode";
			
			URL restServiceURL = new URL(url);
			HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setUseCaches(false);
			
			//返回xml
			httpConnection.setRequestProperty("Content-Type","application/json; charset=utf-8");
			//返回json
			httpConnection.setRequestProperty("Accept","application/json");
			
			JSONObject json = new JSONObject();
			json.put("method", "downwall");
			JSONObject paramJson = new JSONObject();
			
			List<String> channelList = vo.getChannelIdList();
			JSONArray screenIdArr = new JSONArray();
			screenIdArr.addAll(channelList);
			if("channel".equals(downType)) {
				paramJson.put("screenID", screenIdArr);
			}else if("matrix".equals(downType)) {
				paramJson.put("decoderid", vo.getMatrixCode());
			}
			json.put("params", paramJson);
			json.put("id", 1);
			//byte[] writeBytes = json.toString().getBytes();
			
//			OutputStream out = httpConnection.getOutputStream();
//			 int code = httpConnection.getResponseCode();
//			 System.out.println("{code="+code+"}");
//			out.write(writeBytes);
//			out.flush();
//			out.close();
			
			OutputStreamWriter writer = new OutputStreamWriter(httpConnection.getOutputStream());
		    //发送参数
		    writer.write(json.toString());
		    //清理当前编辑器的左右缓冲区，并使缓冲区数据写入基础流
		    writer.flush();
		    //链接地址
		    httpConnection.connect();
		    
			 int code = httpConnection.getResponseCode();
	        if(httpConnection.getResponseCode() != 200) {
	            throw new RuntimeException("HTTP GET Request Failed with Error code : "
	                          +httpConnection.getResponseCode());
	        }
	        BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
	        String output;
	        StringBuffer sb = new StringBuffer();
	        System.out.println("Output from Server:  \n");
	        while ((output =responseBuffer.readLine()) != null) {
	        	sb.append(output);
	        	System.out.println(output);
	        }
	        JSONObject jsonObj = new JSONObject();
	        JSONObject jsonResult = jsonObj.parseObject(sb.toString());
	        Integer id = (Integer) jsonResult.get("id");
	        String result = (String) jsonResult.get("result");
	       
	        System.out.println("{id="+id+"},{result="+result+"},{code="+code+"}");
	        
	        //解析json
	        httpConnection.disconnect();
	        
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 矩阵屏号目录查询
	 * @param vo
	 */
	public JSONArray getMatrixScreens(String matrixCode) throws IOException{
		List<MatrixScreenVo> ret = new ArrayList<MatrixScreenVo>();
		JSONArray jsonData = null;
		try {
			System.out.println("请求目录查询接口:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			String url ="http://"+this.matrixIp+":"+this.matrixPort+"/api/decode";
			
			URL restServiceURL = new URL(url);
			HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setUseCaches(false);
			
			//发送json格式数据
			httpConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
			//接收json格式数据
			httpConnection.setRequestProperty("Accept","application/json");
			
			/*httpConnection.setRequestProperty("Content-type", "text/html");  
			httpConnection.setRequestProperty("Accept-Charset", "utf-8");  
			httpConnection.setRequestProperty("contentType", "utf-8"); */
			
			JSONObject json = new JSONObject();
			json.put("method", "selectdir");
			JSONObject paramJson = new JSONObject();
			paramJson.put("decoderid", matrixCode);
			json.put("params", paramJson);
			json.put("id", 2);
			
			
			byte[] writeBytes = json.toString().getBytes();
			 
			
			OutputStream out = httpConnection.getOutputStream();
			
			
			 
			out.write(writeBytes);
			out.flush();
			out.close();
				/* DataOutputStream dos = new DataOutputStream(httpConnection.getOutputStream());
             dos.writeBytes(URLEncoder.encode(json.toString(), "utf-8"));
             dos.flush();
             dos.close();*/
			int code = httpConnection.getResponseCode();
	        if(code != 200) {
	            throw new RuntimeException("HTTP GET Request Failed with Error code : "
	                          +httpConnection.getResponseCode());
	        }
	        System.out.println(httpConnection.getContentEncoding()+"-------------");
	        String msg = httpConnection.getResponseMessage();
	        System.out.println(msg+"-------------");
	        

	        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(),"utf-8"));
	        String output;
	        StringBuffer sb = new StringBuffer();
	        System.out.println("Output from Server:  \n");
	        while ((output =bufferedReader.readLine()) != null) {
	        	sb.append(output);
	        }
	        System.out.println(sb.toString());
	        JSONObject jsonObj = new JSONObject();
	        JSONObject jsonResult = jsonObj.parseObject(sb.toString());
	        Integer id = (Integer) jsonResult.get("id");
	        String result = (String) jsonResult.get("result");
	        
	        if("successed".equals(result)) {
	        	 jsonData = jsonResult.getJSONArray("screens");
	        }
	        //System.out.println("{id="+id+"},{result="+result+"},{code="+code+"}");

	        //解析json
	        httpConnection.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return jsonData;
	}
	
	/**
	 * 查询屏号状态
	 * @param vo
	 * @throws IOException 
	 */
	public JSONArray getScreenStatus(String matrixCode) throws IOException {
		List<MatrixScreenVo> ret = new ArrayList<MatrixScreenVo>();
		JSONArray screenStatus = null;
		HttpURLConnection httpConnection = null;
		try {
			
			System.out.println("请求状态查询接口:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			
			String url ="http://"+this.matrixIp+":"+this.matrixPort+"/api/decode";
			
			URL restServiceURL = new URL(url);
			httpConnection = (HttpURLConnection) restServiceURL.openConnection();
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setUseCaches(false);
			
			//发送json格式数据
			httpConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
			//接收json格式数据
			httpConnection.setRequestProperty("Accept","application/json");
			
			/*httpConnection.setRequestProperty("Content-type", "text/html");  
			httpConnection.setRequestProperty("Accept-Charset", "utf-8");  
			httpConnection.setRequestProperty("contentType", "utf-8"); */
			
			JSONObject json = new JSONObject();
			json.put("method", "statusall");
			JSONObject paramJson = new JSONObject();
			paramJson.put("decoderid", matrixCode);
			json.put("params", paramJson);
			json.put("id", 2);
			
			
			byte[] writeBytes = json.toString().getBytes();
			 
			
			OutputStream out = httpConnection.getOutputStream();
			
			
			 
			out.write(writeBytes);
			out.flush();
			out.close();
				/* DataOutputStream dos = new DataOutputStream(httpConnection.getOutputStream());
             dos.writeBytes(URLEncoder.encode(json.toString(), "utf-8"));
             dos.flush();
             dos.close();*/
			int code = httpConnection.getResponseCode();
	        if(code != 200) {
	            throw new RuntimeException("HTTP GET Request Failed with Error code : "
	                          +httpConnection.getResponseCode());
	        }
	        System.out.println(httpConnection.getContentEncoding()+"-------------");
	        String msg = httpConnection.getResponseMessage();
	        System.out.println(msg+"-------------");
	        

	        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(),"utf-8"));
	       
	       String output;
	        StringBuffer sb = new StringBuffer();
	        System.out.println("Output from Server:  \n");
	        while ((output =bufferedReader.readLine()) != null) {
	        	sb.append(output);
	        }
	        System.out.println(sb.toString());
	        JSONObject jsonObj = new JSONObject();
	        JSONObject jsonResult = jsonObj.parseObject(sb.toString());
	        Integer id = (Integer) jsonResult.get("id");
	        String result = (String) jsonResult.get("result");

	        if("successed".equals(result)) {
	        	screenStatus = jsonResult.getJSONArray("screens");
	        }
	        System.out.println("{id="+id+"},{result="+result+"},{code="+code+"}");

	        //解析json
	        httpConnection.disconnect();
		} catch (MalformedURLException e) {
			if(httpConnection != null) {
				httpConnection.disconnect();
			}
			throw new MalformedURLException();
		}
		return screenStatus;
	}
	/**
	 * 暂停轮巡
	 * @param matrixCode
	 * @return
	 */
	public boolean pausePolling(String matrixCode) throws IOException{
		try {
			
			System.out.println("请求暂停轮巡接口:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			String url ="http://"+this.matrixIp+":"+this.matrixPort+"/api/decode";
			
			URL restServiceURL = new URL(url);
			HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setUseCaches(false);
			
			//发送json格式数据
			httpConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
			//接收json格式数据
			httpConnection.setRequestProperty("Accept","application/json");
			
			/*httpConnection.setRequestProperty("Content-type", "text/html");  
			httpConnection.setRequestProperty("Accept-Charset", "utf-8");  
			httpConnection.setRequestProperty("contentType", "utf-8"); */
			
			JSONObject json = new JSONObject();
			json.put("method", "pauseall");
			JSONObject paramJson = new JSONObject();
			paramJson.put("decoderid", matrixCode);
			json.put("params", paramJson);
			json.put("id", 1);
			
			byte[] writeBytes = json.toString().getBytes();	 
			
			OutputStream out = httpConnection.getOutputStream();
			
			out.write(writeBytes);
			out.flush();
			out.close();
				/* DataOutputStream dos = new DataOutputStream(httpConnection.getOutputStream());
             dos.writeBytes(URLEncoder.encode(json.toString(), "utf-8"));
             dos.flush();
             dos.close();*/
			int code = httpConnection.getResponseCode();
	        if(code != 200) {
	            throw new RuntimeException("HTTP GET Request Failed with Error code : "
	                          +httpConnection.getResponseCode());
	        }
	        System.out.println(httpConnection.getContentEncoding()+"-------------");
	        String msg = httpConnection.getResponseMessage();
	        System.out.println(msg+"-------------");
	        
	        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(),"utf-8"));
	       
	        String output;
	        StringBuffer sb = new StringBuffer();
	        System.out.println("Output from Server:  \n");
	        while ((output =bufferedReader.readLine()) != null) {
	        	sb.append(output);
	        }
	        System.out.println(sb.toString());
	        JSONObject jsonObj = new JSONObject();
	        JSONObject jsonResult = jsonObj.parseObject(sb.toString());
	        Integer id = (Integer) jsonResult.get("id");
	        String result = (String) jsonResult.get("result");
	        
	        System.out.println("{id="+id+"},{result="+result+"},{code="+code+"}");
	        
	        //解析json
	        httpConnection.disconnect();
	        if("successed".equals(result)) {
	        	return true;
	        }else {
	        	return false;
	        }
	       
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		} 
	}

	/**
	 * 继续轮巡
	 * @param matrixCode
	 */
	public boolean continuePolling(String matrixCode, Integer timeInterval) throws IOException{
		try {
			
			System.out.println("请求继续轮巡接口:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			String url ="http://"+this.matrixIp+":"+this.matrixPort+"/api/decode";
			
			URL restServiceURL = new URL(url);
			HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setUseCaches(false);
			
			//发送json格式数据
			httpConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
			//接收json格式数据
			httpConnection.setRequestProperty("Accept","application/json");
			
			/*httpConnection.setRequestProperty("Content-type", "text/html");  
			httpConnection.setRequestProperty("Accept-Charset", "utf-8");  
			httpConnection.setRequestProperty("contentType", "utf-8"); */
			
			JSONObject json = new JSONObject();
			json.put("method", "continueall");
			JSONObject paramJson = new JSONObject();
			paramJson.put("decoderid", matrixCode);
			paramJson.put("playtime", timeInterval);
			json.put("params", paramJson);
			json.put("id", 1);
			
			byte[] writeBytes = json.toString().getBytes();	 
			
			OutputStream out = httpConnection.getOutputStream();
			
			out.write(writeBytes);
			out.flush();
			out.close();
				/* DataOutputStream dos = new DataOutputStream(httpConnection.getOutputStream());
             dos.writeBytes(URLEncoder.encode(json.toString(), "utf-8"));
             dos.flush();
             dos.close();*/
			int code = httpConnection.getResponseCode();
	        if(code != 200) {
	            throw new RuntimeException("HTTP GET Request Failed with Error code : "
	                          +httpConnection.getResponseCode());
	        }
	        System.out.println(httpConnection.getContentEncoding()+"-------------");
	        String msg = httpConnection.getResponseMessage();
	        System.out.println(msg+"-------------");
	        
	        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(),"utf-8"));
	       
	        String output;
	        StringBuffer sb = new StringBuffer();
	        System.out.println("Output from Server:  \n");
	        while ((output =bufferedReader.readLine()) != null) {
	        	sb.append(output);
	        }
	        System.out.println(sb.toString());
	        JSONObject jsonObj = new JSONObject();
	        JSONObject jsonResult = jsonObj.parseObject(sb.toString());
	        Integer id = (Integer) jsonResult.get("id");
	        String result = (String) jsonResult.get("result");
	        
	        System.out.println("{id="+id+"},{result="+result+"},{code="+code+"}");
	        
	        //解析json
	        httpConnection.disconnect();
	        if("successed".equals(result)) {
	        	return true;
	        }else {
	        	return false;
	        }
	       
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 停止轮巡
	 * @param matrixCode
	 */
	public boolean stopPolling(String matrixCode) throws IOException{
		try {
			
			System.out.println("请求停止轮巡接口:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			String url ="http://"+this.matrixIp+":"+this.matrixPort+"/api/decode";
			
			URL restServiceURL = new URL(url);
			HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setUseCaches(false);
			
			//发送json格式数据
			httpConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
			//接收json格式数据
			httpConnection.setRequestProperty("Accept","application/json");
			
			/*httpConnection.setRequestProperty("Content-type", "text/html");  
			httpConnection.setRequestProperty("Accept-Charset", "utf-8");  
			httpConnection.setRequestProperty("contentType", "utf-8"); */
			
			JSONObject json = new JSONObject();
			json.put("method", "stopall");
			JSONObject paramJson = new JSONObject();
			paramJson.put("decoderid", matrixCode);
			json.put("params", paramJson);
			json.put("id", 1);
			
			byte[] writeBytes = json.toString().getBytes();	 
			
			OutputStream out = httpConnection.getOutputStream();
			
			out.write(writeBytes);
			out.flush();
			out.close();
				/* DataOutputStream dos = new DataOutputStream(httpConnection.getOutputStream());
             dos.writeBytes(URLEncoder.encode(json.toString(), "utf-8"));
             dos.flush();
             dos.close();*/
			int code = httpConnection.getResponseCode();
	        if(code != 200) {
	            throw new RuntimeException("HTTP GET Request Failed with Error code : "
	                          +httpConnection.getResponseCode());
	        }
	        System.out.println(httpConnection.getContentEncoding()+"-------------");
	        String msg = httpConnection.getResponseMessage();
	        System.out.println(msg+"-------------");
	        
	        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(),"utf-8"));
	       
	        String output;
	        StringBuffer sb = new StringBuffer();
	        System.out.println("Output from Server:  \n");
	        while ((output =bufferedReader.readLine()) != null) {
	        	sb.append(output);
	        }
	        System.out.println(sb.toString());
	        JSONObject jsonObj = new JSONObject();
	        JSONObject jsonResult = jsonObj.parseObject(sb.toString());
	        Integer id = (Integer) jsonResult.get("id");
	        String result = (String) jsonResult.get("result");
	        
	        System.out.println("{id="+id+"},{result="+result+"},{code="+code+"}");
	        
	        //解析json
	        httpConnection.disconnect();
	        if("successed".equals(result)) {
	        	return true;
	        }else {
	        	return false;
	        }
	       
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 启动上墙预案
	 * @param 
	 */
	public boolean startWallplan(WallPlanVo wallVo) throws IOException{
		try {
			System.out.println("请求启动上墙预案接口:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			String url ="http://"+this.matrixIp+":"+this.matrixPort+"/api/decode";
			
			URL restServiceURL = new URL(url);
			HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setUseCaches(false);
			
			//发送json格式数据
			httpConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
			//接收json格式数据
			httpConnection.setRequestProperty("Accept","application/json");
			
			/*httpConnection.setRequestProperty("Content-type", "text/html");  
			httpConnection.setRequestProperty("Accept-Charset", "utf-8");  
			httpConnection.setRequestProperty("contentType", "utf-8"); */
			
			List<MatrixChannelVo> channelVos = wallVo.getMatrixChannels();
			
			JSONObject json = new JSONObject();
			json.put("method", "onetomore");
			JSONObject paramJson = new JSONObject();
			
			JSONArray jsonarray = new JSONArray();
			for(MatrixChannelVo channelVo : channelVos) {
				
				JSONObject jsonObj = new JSONObject();
				
				jsonObj.put("screenID", channelVo.getDevicesNo());
				
				JSONArray array = new JSONArray();
				List<CameraVo> cameraVos = channelVo.getCameras();
				for(CameraVo cameraVo : cameraVos) {
					array.add(cameraVo.getSbbm());
				}
				jsonObj.put("cameraID", array);
				
				jsonarray.add(jsonObj);
				
			}
			paramJson.put("screens", jsonarray);
			paramJson.put("playtime", 30);
			
			json.put("params", paramJson);
			json.put("id", 1);
			
			byte[] writeBytes = json.toString().getBytes();	 
			
			OutputStream out = httpConnection.getOutputStream();
			
			out.write(writeBytes);
			out.flush();
			out.close();
				/* DataOutputStream dos = new DataOutputStream(httpConnection.getOutputStream());
             dos.writeBytes(URLEncoder.encode(json.toString(), "utf-8"));
             dos.flush();
             dos.close();*/
			int code = httpConnection.getResponseCode();
	        if(code != 200) {
	            throw new RuntimeException("HTTP GET Request Failed with Error code : "
	                          +httpConnection.getResponseCode());
	        }
	        System.out.println(httpConnection.getContentEncoding()+"-------------");
	        String msg = httpConnection.getResponseMessage();
	        System.out.println(msg+"-------------");
	        
	        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(),"utf-8"));
	       
	        String output;
	        StringBuffer sb = new StringBuffer();
	        System.out.println("Output from Server:  \n");
	        while ((output =bufferedReader.readLine()) != null) {
	        	sb.append(output);
	        }
	        System.out.println(sb.toString());
	        JSONObject jsonObj = new JSONObject();
	        JSONObject jsonResult = jsonObj.parseObject(sb.toString());
	        Integer id = (Integer) jsonResult.get("id");
	        String result = (String) jsonResult.get("result");
	        
	        System.out.println("{id="+id+"},{result="+result+"},{code="+code+"}");
	        
	        //解析json
	        httpConnection.disconnect();
	        if("successed".equals(result)) {
	        	return true;
	        }else {
	        	return false;
	        }
	       
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 录像上墙
	 * @param vo
	 * @return
	 */
	public boolean vedioToWall(InviteOptionVo vo) throws IOException{
		try {
			
			System.out.println("请求录像上墙接口:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			String url ="http://"+this.matrixIp+":"+this.matrixPort+"/api/decode";
			
			URL restServiceURL = new URL(url);
			HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setUseCaches(false);
			
			//发送json格式数据
			httpConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
			//接收json格式数据
			httpConnection.setRequestProperty("Accept","application/json");
			
			/*httpConnection.setRequestProperty("Content-type", "text/html");  
			httpConnection.setRequestProperty("Accept-Charset", "utf-8");  
			httpConnection.setRequestProperty("contentType", "utf-8"); */
			
			JSONObject json = new JSONObject();
			json.put("method", "playback");
			JSONObject paramJson = new JSONObject();
			
			List<String> deviceCodeList = vo.getDeviceCodeList();
			List<String> channelIdList = vo.getChannelIdList();
			JSONArray deviceCodeArr = new JSONArray();
			deviceCodeArr.addAll(deviceCodeList);
			JSONArray channelIdArr = new JSONArray();
			channelIdArr.addAll(channelIdList);
			
			paramJson.put("cameraID", deviceCodeArr);
			paramJson.put("screenID", channelIdArr);
			paramJson.put("starttime", vo.getStartTime().intValue());
			paramJson.put("endtime", vo.getEndTime().intValue());
			json.put("params", paramJson);
			json.put("id", 1);
			
			
			byte[] writeBytes = json.toString().getBytes();
			 
			
			OutputStream out = httpConnection.getOutputStream();
			
			
			 
			out.write(writeBytes);
			out.flush();
			out.close();
				/* DataOutputStream dos = new DataOutputStream(httpConnection.getOutputStream());
             dos.writeBytes(URLEncoder.encode(json.toString(), "utf-8"));
             dos.flush();
             dos.close();*/
			int code = httpConnection.getResponseCode();
	        if(code != 200) {
	            throw new RuntimeException("HTTP GET Request Failed with Error code : "
	                          +httpConnection.getResponseCode());
	        }
	        System.out.println(httpConnection.getContentEncoding()+"-------------");
	        String msg = httpConnection.getResponseMessage();
	        System.out.println(msg+"-------------");
	        

	        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(),"utf-8"));
	       
	        String output;
	        StringBuffer sb = new StringBuffer();
	        System.out.println("Output from Server:  \n");
	        while ((output =bufferedReader.readLine()) != null) {
	        	sb.append(output);
	        }
	        System.out.println(sb.toString());
	        JSONObject jsonObj = new JSONObject();
	        JSONObject jsonResult = jsonObj.parseObject(sb.toString());
	        Integer id = (Integer) jsonResult.get("id");
	        String result = (String) jsonResult.get("result");
	        
	        System.out.println("{id="+id+"},{result="+result+"},{code="+code+"}");

	        //解析json
	        httpConnection.disconnect();
	        
	       
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String getMatrixIp() {
		return matrixIp;
	}
	public void setMatrixIp(String matrixIp) {
		this.matrixIp = matrixIp;
	}
	public Integer getMatrixPort() {
		return matrixPort;
	}
	public void setMatrixPort(Integer matrixPort) {
		this.matrixPort = matrixPort;
	}
}
