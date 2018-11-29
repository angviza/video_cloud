package com.hdvon.sip.video.utils;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import com.hdvon.nmp.util.DateHandlerUtils;
import com.hdvon.sip.video.vo.CameraRegisterSipVo;
import com.hdvon.sip.video.vo.ControlOptionInputVo;
import com.hdvon.sip.video.vo.ControlOptionSipVo;
import com.hdvon.sip.video.vo.CruiseOptionInputVo;
import com.hdvon.sip.video.vo.CruiseOptionSipVo;
import com.hdvon.sip.video.vo.InviteOptionInputVo;
import com.hdvon.sip.video.vo.InviteOptionSipVo;
import com.hdvon.sip.video.vo.PlayDownloadInputVo;
import com.hdvon.sip.video.vo.PresetOptionInputVo;
import com.hdvon.sip.video.vo.PresetOptionSipVo;
import com.hdvon.sip.video.vo.QueryPreOptionInputVo;
import com.hdvon.sip.video.vo.QueryPreOptionSipVo;
import com.hdvon.sip.video.vo.QueryRecOptionInputVo;
import com.hdvon.sip.video.vo.QueryRecOptionSipVo;
import com.hdvon.sip.video.vo.RecordOptionSipVo;
import com.hdvon.sip.video.vo.RegisterCredSipVo;
import com.hdvon.sip.video.vo.RegisterOptionSipVo;
import com.hdvon.sip.video.vo.SipMap;
import com.hdvon.sip.video.vo.WiperOptionInputVo;
import com.hdvon.sip.video.vo.WiperOptionSipVo;
import cn.hutool.core.util.StrUtil;

/**
 * <br>
 * <b>功能：</b>调用视频监控服务接口参数拼接的组件<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018/5/21<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Component
@PropertySource(value = {"classpath:jni.properties"})
public class CompositeSipParamsUtils {
	
	@Value("${jni.schema}")  
	private String schema;
	
	@Value("${jni.expire}")  
	private String expire;
	
//	@Value("${jni.username}")
//	private String userName;
//	
//	@Value("${jni.password}")
//	private String password;
//	
//	@Value("${jni.username2}")
//	private String userName2;
//	
//	@Value("${jni.password2}")
//	private String password2;
	
	@Value("${jni.serverCode}")
	private String serverCode;
	
	@Value("${jni.serverIp}")
	private String serverIp;
	
	@Value("${jni.serverPort}")
	private String serverPort;
	
	//接受者 web服务器IP
	@Value("${jni.receiverIp}")  
	private String receiverIp;
	
	//接受者 web服务器端口 默认5060
	@Value("${jni.receiverPort}")  
	private String receiverPort;

	public static String registeredUserName;//本次注册账号
	private static String registeredPassword;//账号密码
	private static String userRealm;//账号所在域
	//当前信令账号+"_"+web应用服务器的IP+"_REG"作为注册的key
	public static String register_redisKey="_REG";//
	
	
	public Map<String, Object> compositeRegisterParams(CameraRegisterSipVo registeredParam) {
		RegisterCredSipVo credParam = new RegisterCredSipVo();
		RegisterOptionSipVo optionParam = new RegisterOptionSipVo();

		String serverId = serverCode;
		String serverRealm = serverCode.substring(0, 10);
		String gatewayPort = serverPort;
		String gatewayIp = serverIp;

		credParam.setRealm("*");
		credParam.setUsername(registeredUserName);
		credParam.setPassword(registeredPassword);
		credParam.setSchema(schema);
		//信令中心ID@信令中心域
		optionParam.setServer("sip:"+serverId+"@"+serverRealm);
		//信令中心的路由IP地址和端口
		optionParam.setRoute("sip:"+serverId+"@"+gatewayIp+":"+gatewayPort);
		//本地信令账号@域
		optionParam.setFrom("sip:"+registeredUserName+"@"+userRealm);
		//本地信令账号@下一跳信令的IP地址，（填本地IP地址）:下一跳信令的端口（填本地端口）
		optionParam.setContact("sip:"+registeredUserName+"@"+receiverIp+":"+receiverPort);
		//本地信令账号@中心域（提供第三方注册使用，本客户端填写本地信令账号）
		optionParam.setTo("sip:"+registeredUserName+"@"+serverRealm);
		//optionParam.setExpire(Integer.parseInt(expire));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("optionParam", optionParam);
		params.put("credParam", credParam);
		return params;
	}
	
	
	public Map<String, Object> compositeParams() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("receiverIp", receiverIp);
		params.put("sipUserName", registeredUserName);
		params.put("receiverPort", Integer.parseInt(receiverPort));
		return params;
	}
	
	
	//控制参数封装
	public ControlOptionSipVo creatControlOptionParam(ControlOptionInputVo vo) {
		ControlOptionSipVo paramVo =new ControlOptionSipVo();
		String senderCode=vo.getDeviceCode();//发送者编码，也就是device表的设备编码
		String codeRealm=vo.getDeviceCode().substring(0, 10);//设备所在域
		//SIP: 本地信令账号@信令中心IP:端口
		paramVo.setTarget("sip:"+registeredUserName+"@"+serverIp+":"+serverPort);
		//SIP: 目标信令通道@设备所在域
		paramVo.setTo("sip:"+senderCode+"@"+codeRealm);
		//本地信令账号@域
		paramVo.setFrom("sip:"+registeredUserName+"@"+userRealm);
		//通道ID(设备编码)
		paramVo.setCameraId(senderCode);
		//优先级
		paramVo.setIPriority(1);
		//是否取消摄像头控制
		if (null != vo.getIsCancel()) {
			paramVo.setIStop(vo.getIsCancel());
		}
		
		//摄像头方向控制
		if(null != vo.getDirection()) {
			paramVo.setDirection(vo.getDirection());
			
			//方向速度
			if (null != vo.getStep()) {
				paramVo.setIDSpeed(vo.getStep()); 
			}
		}
		
		//摄像头镜头变倍控制，用于云台控制微调变焦
		if(null != vo.getZoom()) {
			paramVo.setZoom(vo.getZoom());
			
			//ZOOM速度
			if (null != vo.getStep()) {
				paramVo.setIZSpeed(vo.getStep()); 
			}
		}
		
		//摄像头镜头光圈控制
		if(null != vo.getIris()) {
			paramVo.setIris(vo.getIris());
			
			//IRIS速度
			if (null != vo.getStep()) {
				paramVo.setIISpeed(vo.getStep()); 
			}
		}
		
		//摄像头镜头焦距控制，用于云台控制微调调焦
		if(null != vo.getFocus()) {
			paramVo.setFocus(vo.getFocus());
			
			//focus速度
			if (null != vo.getStep()) {
				paramVo.setIFSpeed(vo.getStep()); 
			}
		}
		
		return paramVo;
	}
	
	
	//预置位控制参数封装
	public PresetOptionSipVo creatPresetOptionParam(PresetOptionInputVo vo) {
		PresetOptionSipVo paramVo = new PresetOptionSipVo();
		String senderCode = vo.getDeviceCode();//发送者编码，也就是device表的设备编码
		String codeRealm = vo.getDeviceCode().substring(0, 10);//设备所在域
		
		//SIP: 本地信令账号@信令中心IP:端口
		paramVo.setTarget("sip:"+registeredUserName+"@"+serverIp+":"+serverPort);
		//SIP: 目标信令通道@设备所在域
		paramVo.setTo("sip:"+senderCode+"@"+codeRealm);
		//本地信令账号@域
		paramVo.setFrom("sip:"+registeredUserName+"@"+userRealm);
		//通道ID(设备编码)
		paramVo.setCameraId(senderCode);
		//优先级
		paramVo.setIPriority(1);
		//预置位控制类型
		paramVo.setPresetType(vo.getPresetType());
		//预置位号
		paramVo.setPresetNum(vo.getPresetNum());
		
		return paramVo;
	}
		

	/**
	 * invite 请求参数组装
	 * @param vo
	 * @param paramVo
	 * @param port
	 * @return
	 */
	public InviteOptionSipVo creatInviteParam(InviteOptionInputVo vo, CameraRegisterSipVo paramVo) {
		InviteOptionSipVo param =new InviteOptionSipVo();
		long unixstart=0;
		long unixTime=0;
		if(vo.getEndTime() !=null) {
			unixTime=vo.getEndTime();
		}
		if(vo.getStartTime() !=null) {
			unixstart=vo.getStartTime();
		}
		
		String clientIp = paramVo.getClientIp();//发送者ip 也就是点播客户端ip
		String clientPort = paramVo.getClientPort();//发送者端口 也就是点播客户端ip
		String protocol="UDP".equals(vo.getProtocol())?"RTP/AVP":"TCP/RTP/AVP";//默认使用tcp协议
		//TODO 现在不支持tcp协议播放
		protocol="RTP/AVP";
		String senderCode=vo.getDeviceCode();//发送者编码，也就是device表的设备编码
		String codeRealm=vo.getDeviceCode().substring(0, 10);//设备所在域
		String serviceId=serverCode;
		String gatewayIp=serverIp;
		String gatewayPort=serverPort;

	    String palyType=null;
		if(vo.getPlayType().equals("1")) {//实时播放
			param.setSubject(senderCode+":0"+vo.getDeviceCode().substring(2, 20)+","+serviceId+":0"+codeRealm);//目标信令通道：标识(同一路标识唯一),信令中心ID：标识(无要求)
			palyType="Play";
		}else if(vo.getPlayType().equals("2")) {//录像回放
			param.setSubject(senderCode+":1"+(long)((Math.random()*9+1)*1000000000000000L)+","+serviceId+":0"+codeRealm);//目标信令通道：标识(不可重复),信令中心ID：标识
			palyType="Playback";
		}else if(vo.getPlayType().equals("3")) {//录像下载
			param.setSubject(senderCode+":1"+(long)((Math.random()*9+1)*1000000000000000L)+","+serviceId+":0"+codeRealm);//目标信令通道：标识(不可重复),信令中心ID：标识
			palyType="Download";
		}else {//语音对讲
			param.setSubject(senderCode+":0"+(long)((Math.random()*9+1)*1000000000000000L)+","+serviceId+":1"+codeRealm);//目标信令通道：标识,信令中心ID：标识
			palyType="Talk";
		}

		param.setFrom("sip:"+registeredUserName+"@"+userRealm);//本地信令账号@域
		param.setTo("sip:"+senderCode+"@"+codeRealm);//目标信令通道@设备所在域
		param.setContact("sip:"+registeredUserName+"@"+receiverIp+":"+receiverPort);//本地信令账号@本地信令IP地址：端口
		param.setRoute("sip:"+serviceId+"@"+gatewayIp+":"+gatewayPort);//信令中心ID @信令中心IP：端口

		StringBuffer sb =new StringBuffer();
		sb.append("v=0\r\n");
		sb.append("o="+registeredUserName+" 0 0 IN IP4 "+clientIp+"\r\n");
		sb.append("s="+palyType+"\r\n");//播放方式
		sb.append("c=IN IP4 "+clientIp+"\r\n");
		
		if(vo.getPlayType().equals("2") || vo.getPlayType().equals("3")) {//录像回放或下载
			sb.append("u="+senderCode+":255\r\n");
		} 
		sb.append("t="+unixstart+" "+unixTime+"\r\n");
		sb.append("m=video "+clientPort +" "+protocol+" 96 97 98 99\r\n");
		sb.append("a=recvonly\r\n"); 
		sb.append("a=rtpmap:96 PS/90000\r\n");
		sb.append("a=rtpmap:97 MPEG4/90000\r\n");
		sb.append("a=rtpmap:98 H264/90000\r\n");
		sb.append("a=rtpmap:99 SVAC/90000\r\n");
		if("TCP/RTP/AVP".equals(protocol)) {
			sb.append("a=setup:active\r\n"); 
			sb.append("a=connection:new\r\n"); 
		}
		if(vo.getPlayType().equals("3")) {
			sb.append("a=downloads peed:4\r\n");
		}
		param.setBody(sb.toString());
		param.setExpire(Integer.parseInt(expire));
		return param;
	}

	/**
	 * 录像控制参数拼接
	 * @param vo
	 * @param clientIp
	 * @return
	 */
	public RecordOptionSipVo creatPalyDownloadParam(PlayDownloadInputVo vo) {
		String senderCode=vo.getDeviceCode();//发送者编码，也就是device表的设备编码
		String codeRealm=vo.getDeviceCode().substring(0, 10);//设备所在域
		
		RecordOptionSipVo param = new RecordOptionSipVo();
		param.setTarget("sip:"+registeredUserName+"@"+serverIp+":"+serverPort);//SIP: 本地信令账号@IP:端口
		param.setTo("sip:"+senderCode+"@"+codeRealm);//SIP: 目标信令通道@设备所在域
		param.setFrom("sip:"+registeredUserName+"@"+userRealm);//SIP：本地信令账号@域
		param.setCameraId(senderCode);//通道ID,也就是设备编码
		param.setCmdType(vo.getCmdType());
		return param;
	}
	
	/*
	public List<PlugntNeedInfoVo> creatPlugntNeedInfo(String clientIp,int number) {
		List<PlugntNeedInfoVo> listvo= new ArrayList<PlugntNeedInfoVo>();
		
		List<String> list=ClientUtil.getClientIpAndProt(clientIp, number);
		for(String prot :list) {
			PlugntNeedInfoVo vo = new PlugntNeedInfoVo ();
			vo.setClientIp(clientIp);
			vo.setClientProt(prot);
			listvo.add(vo);
		}
		return listvo;
	}
	*/


	/**
	 * 雨刷控制参数组装
	 * @param vo
	 * @return
	 */
	public WiperOptionSipVo creatWiperOptionParam(WiperOptionInputVo vo) {
		String senderCode=vo.getDeviceCode();//发送者编码，也就是device表的设备编码
		String codeRealm=vo.getDeviceCode().substring(0, 10);//设备所在域

		WiperOptionSipVo param = new WiperOptionSipVo();
		param.setTarget("sip:"+registeredUserName+"@"+serverIp+":"+serverPort);//SIP: 本地信令账号@IP:端口
		param.setTo("sip:"+senderCode+"@"+codeRealm);//SIP: 目标信令通道@设备所在域
		param.setFrom("sip:"+registeredUserName+"@"+userRealm);//SIP：本地信令账号@域
		param.setCameraId(senderCode);//通道ID,也就是设备编码
		param.setCmdType(vo.getCmdType());
		param.setIPriority(1);//优先级
		return param;
	}


	/***
	 * 预置位查询参数组装
	 * @param vo
	 * @return
	 */
	public QueryPreOptionSipVo creatQueryPreOptionParam(QueryPreOptionInputVo vo) {
		String senderCode=vo.getDeviceCode();//发送者编码，也就是device表的设备编码
		String codeRealm=vo.getDeviceCode().substring(0, 10);//设备所在域

		QueryPreOptionSipVo param = new QueryPreOptionSipVo();
		param.setTarget("sip:"+registeredUserName+"@"+serverIp+":"+serverPort);//SIP: 本地信令账号@IP:端口
		param.setTo("sip:"+senderCode+"@"+codeRealm);//SIP: 目标信令通道@设备所在域
		param.setFrom("sip:"+registeredUserName+"@"+userRealm);//SIP：本地信令账号@域
		param.setCameraId(senderCode);//通道ID,也就是设备编码
		return param;
	}


	//巡航预案控制参数封装
	public CruiseOptionSipVo creatCruiseOptionParam(CruiseOptionInputVo vo) {
		CruiseOptionSipVo paramVo = new CruiseOptionSipVo();
		String senderCode = vo.getDeviceCode();//发送者编码，也就是device表的设备编码
		String codeRealm = vo.getDeviceCode().substring(0, 10);//设备所在域
		//SIP: 本地信令账号@信令中心IP:端口
		paramVo.setTarget("sip:"+registeredUserName+"@"+serverIp+":"+serverPort);
		//SIP: 目标信令通道@设备所在域
		paramVo.setTo("sip:"+senderCode+"@"+codeRealm);
		//本地信令账号@域
		paramVo.setFrom("sip:"+registeredUserName+"@"+userRealm);
		//通道ID(设备编码)
		paramVo.setCameraId(senderCode);
		//优先级
		paramVo.setIPriority(1);
		//巡航控制类型
		paramVo.setCmdType(vo.getCmdType());
		//巡航组号
		paramVo.setGroupNum(vo.getGroupNum());
		//预置位号
		paramVo.setPresetNum(vo.getPresetNum());
		//巡航停留时间
		paramVo.setStayTime(vo.getStayTime());
		//巡航速度
		paramVo.setSpeed(vo.getSpeed());

		return paramVo;
	}
	
	/**
	 * 录像查询参数
	 * @param vo
	 * @param paramVo
	 * @return
	 */
	public QueryRecOptionSipVo creatVideoFileParam(QueryRecOptionInputVo vo, CameraRegisterSipVo paramVo) {
		QueryRecOptionSipVo option =new QueryRecOptionSipVo();
		String senderCode = vo.getDeviceCode();//发送者编码，也就是device表的设备编码
		String codeRealm = vo.getDeviceCode().substring(0, 10);//设备所在域
		//SIP: 本地信令账号@信令中心IP:端口
		option.setTarget("sip:"+registeredUserName.trim()+"@"+serverIp.trim()+":"+serverPort.trim());
		//SIP: 目标信令通道@设备所在域
		option.setTo("sip:"+senderCode+"@"+codeRealm);
		//本地信令账号@域
		option.setFrom("sip:"+registeredUserName+"@"+userRealm);
		//通道ID(设备编码)
		option.setCameraId(senderCode);
		// 开始时间
		option.setStartTime(DateHandlerUtils.transForDate(vo.getStartTime()));
		// 结束时间
		option.setEndTime(DateHandlerUtils.transForDate(vo.getEndTime()));
		//默认填all （all 、alarm、manual）
		option.setType("all");
		return option;
	}
	
	
	/**
	 * 改变注册用户信息
	 * @param param
	 */
	public void changeUserName(CameraRegisterSipVo param) {
		//根据注册使用方式那个账号注册
		if(param.getType().equals("one")) {
			String username=PropertiesUtils.getProperty("jni.username");
			if(StrUtil.isNotBlank(username)) {
				registeredUserName=username;
				registeredPassword=PropertiesUtils.getProperty("jni.password");
			}else {
				return ;
			}
		}else {
			String username2=PropertiesUtils.getProperty("jni.username2");
			if(StrUtil.isNotBlank(username2)) {
				registeredUserName=username2;
				registeredPassword=PropertiesUtils.getProperty("jni.password2");
			}else {
				return ;
			}
		}

		userRealm=registeredUserName.substring(0, 10);//账号所在域
		register_redisKey=registeredUserName+"_"+receiverIp+register_redisKey;//注册放到redis KEY
		//动态改变注册用户
		SipMap.register_type=SipMap.register_type.equals("one")?"two":"one";
	}
	
	public static void main(String[] args) {
		for(int j = 0; j< 100; j++){
		    System.out.println((long)((Math.random()*9+1)*1000000000000000L));
		}
	}

}
