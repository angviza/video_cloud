package com.hdvon.nmp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hdvon.nmp.vo.CameraVo;
import com.hdvon.nmp.vo.MatrixChannelVo;
import com.hdvon.nmp.vo.MatrixScreenVo;
import com.hdvon.nmp.vo.WallPlanVo;
import com.hdvon.nmp.vo.sip.InviteOptionVo;
import com.hdvon.nmp.vo.sip.VideoWallVo;

public class WallCameraRpcClient {
private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss,SSS");
	
	private String matrixIp;
	private Integer matrixPort;
	
	public WallCameraRpcClient(String matrixIp, Integer matrixPort) {
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
	 * 视屏上墙
	 * @param vo
	 */
	public boolean upWallVedio(VideoWallVo vo) throws IOException{
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
			json.put("method", "play");
			JSONObject paramJson = new JSONObject();
			
			paramJson.put("cameraID", vo.getDeviceCodes());
			paramJson.put("screenID", vo.getChannelNos());
			json.put("params", paramJson);
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
	 * 视屏轮询
	 * @param vo
	 */
	public boolean pollingWallVedio(VideoWallVo vo) throws IOException{
		try {
			
			System.out.println("请求轮巡接口:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
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
			List<String> channelNoList = vo.getChannelNoList();
			JSONArray deviceCodeArr = new JSONArray();
			deviceCodeArr.addAll(deviceCodeList);
			JSONArray channelIdArr = new JSONArray();
			channelIdArr.addAll(channelNoList);
			
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
	public void downWallVedio(List<String> taskIds, String downType) throws IOException{
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
			JSONArray paramArr = new JSONArray();
			for(String taskId : taskIds) {
				paramArr.add(taskId);
			}
			json.put("taskIds", paramArr);
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
	 * @param taskId
	 * @return
	 */
	public boolean pausePolling(String taskId) throws IOException{
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
			paramJson.put("taskId", taskId);
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
	 * @param taskId
	 * @param timeInterval
	 * @return
	 * @throws IOException
	 */
	public boolean continuePolling(String taskId, Integer timeInterval) throws IOException{
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
			paramJson.put("taskId", taskId);
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
	 * @param taskId
	 */
	public boolean stopPolling(String taskId) throws IOException{
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
			json.put("taskId", taskId);
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
	public boolean vedioToWall(VideoWallVo vo) throws IOException{
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
			List<String> channelNoList = vo.getChannelNoList();
			JSONArray deviceCodeArr = new JSONArray();
			deviceCodeArr.addAll(deviceCodeList);
			JSONArray channelNoArr = new JSONArray();
			channelNoArr.addAll(channelNoList);
			
			paramJson.put("cameraID", deviceCodeArr);
			paramJson.put("screenID", channelNoArr);
			paramJson.put("starttime", vo.getStartTime().intValue());
			paramJson.put("endtime", vo.getEndTime().intValue());
			json.put("params", paramJson);
			json.put("id", 1);
			
			
			byte[] writeBytes = json.toString().getBytes();
			 
			
			OutputStream out = httpConnection.getOutputStream();
			
			
			 
			out.write(writeBytes);
			out.flush();
			out.close();

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
