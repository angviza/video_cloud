package com.sip;

import com.hdvon.sip.video.vo.CallbackResponseVo;
import com.hdvon.sip.video.vo.FileResponseVo;
import com.hdvon.sip.video.vo.RegisterCallback;
import com.hdvon.sip.video.vo.SipMap;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Callback {
	
	/*
	 * 第一次注册的时候的结果
	 * callID:注册请求的id
	 * iStatusCode 状态
	 */
	public static void OnRegisterCallback(String CallID, int iStatusCode, String UserName){
		System.out.format("JAVA OnRegisterCallback,CallID %s,iStatusCode %d , UserName: %s\r\n"
		,CallID,iStatusCode,UserName);
		
		CallbackResponseVo resp = SipMap.respMap.get(CallID);
		log.info("================= OnRegisterCallback CallbackResponseVo in map is =============== "+resp);

		if (null == resp || null == resp.getCallId() ||
			!CallID.equals(resp.getCallId()) ||
			resp.getStatusCode() != iStatusCode) {

			CallbackResponseVo vo = new CallbackResponseVo();
			vo.setStatusCode(iStatusCode);
			vo.setCallId(CallID);
			SipMap.respMap.put(CallID, vo);

			log.info("================= OnRegisterCallback Update CallbackResponseVo in map is =============== "+SipMap.respMap.get(CallID));
			//ThreadLocalMap.registerThreadLocal.set(vo);
		}
		
		RegisterCallback register = new RegisterCallback();
		register.setCallId(CallID);
		register.setStatusCode(iStatusCode);
		register.setRegister_time(System.currentTimeMillis());
		register.setUsername(UserName);
		if(iStatusCode !=200 ) {
			register.setRegister_errorCallId(CallID);
		}else {
			register.setRegister_errorCallId(null);
		}
		SipMap.registerMap.put(UserName, register);
		//log.info("SipMap.registerMap time="+System.currentTimeMillis());

		/********************************************************** redis处理注册的异步回调  **************************************/
		/**
		String key = callID + "_callback";
		
		if (iStatusCode == 200) {
			
			if (!SipMap.map.isEmpty()) {//同步接口已被调用且未在调用端处理异步回调
				if (SipMap.map.containsKey(callID)) {
					vo = new CallbackResponseVo();
					vo.setStatusCode(iStatusCode);
					log.info("================= OnRegisterCallback callID in map is matched =============== "+callID);
				} else {
					vo = new CallbackResponseVo();
					vo.setStatusCode(1001);
					log.info("================= OnRegisterCallback callID in map is not matched =============== "+callID);
				}
			}
		} 
		
		vo.setCallId(callID);
		CallbackResponseVo res = (CallbackResponseVo) RedisTemplateApp.redisTemplate.opsForValue().get(key);
		
		//设置异步回调中的redis缓存
		if (null == res || !RedisTemplateApp.redisTemplate.hasKey(key) || res.getStatusCode() != vo.getStatusCode()) {
			RedisTemplateApp.redisTemplate.opsForValue().set(key, vo);
			RedisTemplateApp.redisTemplate.expire(key, WebConstant.REGISTER_CALLBACK_MSECS, TimeUnit.SECONDS);
			log.info("================= Refresh Redis in OnRegisterCallback ============== ");
		}
		**/
		/**********************************************************************************************************************/
	}	

	/*
	 * 定时注册的时候的结果返回
	 * status:succ,error
	 * code:信令回应编码401 402 
	 */
	public static void OnRegisterStatus(String status, int code) {
		System.out.format("JAVA OnRegisterStatus status is %s,code is %d \r\n",
				status, code);
		
	}
	
	/*
	 * 当invite结束时收到的回调
	 * callID:invite 的ID
	 * iStatusCode 状态 
	 * iStatusCode=6:Session is terminated
	 */
	public static void OnInviteTerminate(String callID, int iStatusCode) {
		System.out.format("JAVA OnInviteTerminate CallID is %s,iStatusCode is %d \r\n",
				callID, iStatusCode);
		
		String result= callbackResult(callID,iStatusCode);
		if(StrUtil.isNotBlank(result)) {
			log.info("====OnInviteTerminate Update CallbackResponseVo in map is ======"+SipMap.respMap.get(callID));
		}
		
		/********************************************************** redis处理invite的异步回调  **************************************/
		/**
		String key = callID + "_callback";
		
		if (iStatusCode == 200) {
			
			if (!SipMap.map.isEmpty()) {//同步接口已被调用且未在调用端处理异步回调
				if (SipMap.map.containsKey(callID)) {
					vo = new CallbackResponseVo();
					vo.setStatusCode(iStatusCode);
					log.info("================= OnInviteTerminate callID in map is matched =============== "+callID);
				} else {
					vo = new CallbackResponseVo();
					vo.setStatusCode(1001);
					log.info("================= OnInviteTerminate callID in map is not matched =============== "+callID);
				}
			}
		} else if (iStatusCode == 6) {
			log.info("================= OnInviteTerminate session is terminated =============== ");
		}
		
		vo.setCallId(callID);
		
		//设置异步回调中的redis缓存
		RedisTemplateApp.redisTemplate.opsForValue().set(key, vo);
		RedisTemplateApp.redisTemplate.expire(key, WebConstant.INVITE_CALLBACK_MSECS, TimeUnit.SECONDS);
		log.info("================= Refresh Redis in OnInviteTerminate ============== ");
		**/
		/**********************************************************************************************************************/
	}

	/*
	 * 当invite,协商完成SDP之后的回调
	 * callID:invite 的ID
	 * iErrorCode:错误码,0代码正常 
	 */
	public static void OnInviteUpdate(String callID, int iErrorCode,
			String localIp, int localPort, String localTransport,
			String remoteIp, int remotePort, String remoteTransport) {

		System.out.format("JAVA OnInviteUpdate CallID is %s,iErrorCode is %d "+
				"localIp is %s,localPort is %d,localTransport is %s"+
				"remote ip is %s,remotePort is %d,remoteTransport is %s\r\n",
				callID, iErrorCode, localIp, localPort, localTransport,
				remoteIp, remotePort, remoteTransport);
		
		CallbackResponseVo vo = new CallbackResponseVo();
		
		if (iErrorCode == 0) {
			vo.setStatusCode(200);
		} else {
			vo.setStatusCode(iErrorCode);
		}
		
		vo.setErrorCode(iErrorCode);
		vo.setCallId(callID);
		vo.setRemoteIp(remoteIp);
		vo.setRemotePort(remotePort);
		vo.setRemoteTransport(remoteTransport);
		vo.setLocalIp(localIp);
		vo.setLocalPort(localPort);
		vo.setLocalTransport(localTransport);
		SipMap.respMap.put(callID, vo);
		
		log.info("================= OnInviteUpdate Update CallbackResponseVo in map is =============== "+SipMap.respMap.get(callID));
		
		
		/********************************************************** redis处理invite的异步回调  **************************************/
		/**
		String key = callID + "_callback";
		
		if (iErrorCode == 0) {
			
			if (!SipMap.map.isEmpty()) {//同步接口已被调用且未在调用端处理异步回调
				if (SipMap.map.containsKey(callID)) {
					vo = new CallbackResponseVo();
					vo.setStatusCode(200);
					log.info("================= OnInviteUpdate callID in map is matched =============== "+callID);
				} else {
					vo = new CallbackResponseVo();
					vo.setStatusCode(1001);
					log.info("================= OnInviteUpdate callID in map is not matched =============== "+callID);
				}
			}
		}
		
		vo.setCallId(callID);
		vo.setErrorCode(iErrorCode);
		vo.setRemoteIp(remoteIp);
		vo.setRemotePort(remotePort);
		vo.setRemoteTransport(remoteTransport);
		vo.setLocalIp(localIp);
		vo.setLocalPort(localPort);
		vo.setLocalTransport(localTransport);
		
		//设置异步回调中的redis缓存
		RedisTemplateApp.redisTemplate.opsForValue().set(key, vo);
		RedisTemplateApp.redisTemplate.expire(key, WebConstant.INVITE_CALLBACK_MSECS, TimeUnit.SECONDS);
		log.info("================= Refresh Redis in OnInviteUpdate ============== ");
		**/
		/**********************************************************************************************************************/
	}

	/*
	 * 当invite发送之后的回调
	 * callID:invite 的ID
	 * iStatusCode 状态  
	 * iStatusCode=4:After 2xx is received.
	 * iStatusCode=5:After ACK is sent
	 */
	public static void OnInviteResponse(String callID, int iStatusCode) {
		System.out.format("JAVA OnInviteResponse CallID is %s,iStatusCode is %d \r\n",
			callID, iStatusCode);
		
		String result= callbackResult(callID,iStatusCode);
		if(StrUtil.isNotBlank(result)) {
			log.info("==== OnInviteResponse Update CallbackResponseVo in map is ======"+SipMap.respMap.get(callID));
		}

		/********************************************************** redis处理invite的异步回调  **************************************/
		/**
		String key = callID + "_callback";
		
		if (iStatusCode == 200) {
			
			if (!SipMap.map.isEmpty()) {//同步接口已被调用且未在调用端处理异步回调
				if (SipMap.map.containsKey(callID)) {
					vo = new CallbackResponseVo();
					vo.setStatusCode(iStatusCode);
					log.info("================= OnInviteResponse callID in map is matched =============== "+callID);
				} else {
					vo = new CallbackResponseVo();
					vo.setStatusCode(1001);
					log.info("================= OnInviteResponse callID in map is not matched =============== "+callID);
				}
			}
		} else if (iStatusCode == 4) {
			log.info("================= After 2xx is received in OnInviteResponse ===============");
		} else if (iStatusCode == 5) {
			log.info("================= After ACK is sent in OnInviteResponse ===============");
		}
		
		vo.setCallId(callID);
		
		//设置异步回调中的redis缓存
		RedisTemplateApp.redisTemplate.opsForValue().set(key, vo);
		RedisTemplateApp.redisTemplate.expire(key, WebConstant.INVITE_CALLBACK_MSECS, TimeUnit.SECONDS);
		log.info("================= Refresh Redis in OnInviteResponse ============== ");
		**/
		/**********************************************************************************************************************/
	}

	/*
	 * 云台控制回调
	 * callID:云台控制的ID
	 * iStatusCode 状态 
	 */
	public static void OnControl(String callID, int iStatusCode) {
		System.out.format("JAVA OnControl CallID is %s,iStatusCode is %d \r\n",
			callID, iStatusCode);
		
		String result= callbackResult(callID,iStatusCode);
		if(StrUtil.isNotBlank(result)) {
			log.info("==== OnControl Update CallbackResponseVo in map is ======"+SipMap.respMap.get(callID));
		}

		/**
		CallbackResponseVo vo = new CallbackResponseVo();
		vo.setStatusCode(iStatusCode);
		String key = callID + "_callback";
		
		if (iStatusCode == 200) {
			
			if (!SipMap.map.isEmpty()) {//同步接口已被调用且未在调用端处理异步回调
				if (SipMap.map.containsKey(callID)) {
					vo = new CallbackResponseVo();
					vo.setStatusCode(iStatusCode);
					log.info("================= OnControl callID in map is matched =============== "+callID);
				} else {
					vo = new CallbackResponseVo();
					vo.setStatusCode(1001);
					log.info("================= OnControl callID in map is not matched =============== "+callID);
				}
			}
		}
		
		vo.setCallId(callID);
		CallbackResponseVo res = (CallbackResponseVo) RedisTemplateApp.redisTemplate.opsForValue().get(key);
		
		//设置异步回调中的redis缓存
		if (null == res || !RedisTemplateApp.redisTemplate.hasKey(key) || res.getStatusCode() != vo.getStatusCode()) {
			RedisTemplateApp.redisTemplate.opsForValue().set(key, vo);
			RedisTemplateApp.redisTemplate.expire(key, WebConstant.CNTRL_CALLBACK_MSECS, TimeUnit.SECONDS);
			log.info("================= Refresh Redis in OnControl ============== ");
		}
		**/
	}
	
	
	/*
	 *录像回放控制
	 */
	public static void OnPlaybackCtlCallback(String callID, int iStatusCode) {
		System.out.format("JAVA OnPlaybackCtlCallback CallID %s,iStatusCode %d \r\n",
				callID, iStatusCode);
		String result= callbackResult(callID,iStatusCode);
		if(StrUtil.isNotBlank(result)) {
			log.info("==== OnPlaybackCtlCallback Update CallbackResponseVo in map is ======"+SipMap.respMap.get(callID));
		}

	}
	
	/*
	* 录像回放、录像下载，当文件结束时的回调
	*/
	@SuppressWarnings("unchecked")
	public static void OnActionDone(String callID, int iStatusCode) {
		System.out.format("JAVA OnActionDone CallID %s,iStatusCode %d \r\n",
				callID, iStatusCode);
		
//		String result= callbackResult(callID,iStatusCode);
//		if(StrUtil.isNotBlank(result)) {
//			log.info("====OnActionDone Update CallbackResponseVo in map is ======"+SipMap.respMap.get(callID));
//		}
//		
		FileResponseVo fileResponseVo= new FileResponseVo();
		fileResponseVo.setCallId(callID);
		fileResponseVo.setStatusCode(iStatusCode);
		SipMap.fileResponseVo.put(callID, fileResponseVo);

	}
	
	 /*
	  * 巡航控制回调
	  */
	public static void OnCruiseCallback(String callID, int iStatusCode ){
		System.out.format("JAVA OnCruiseCallback CallID %s,IErrorCodd %d \r\n",
				callID, iStatusCode);

		String result = callbackResult(callID,iStatusCode);
		if(StrUtil.isNotBlank(result)) {
			log.info("==== OnCruiseCallback Update CallbackResponseVo in map is ======"+SipMap.respMap.get(callID));
		}
	}
	
	/*
	 *预置位控制
	 */
	 public static void OnPresetCallback(String callID, int iStatusCode){
		 System.out.format("JAVA OnPresetCallback CallID %s,IErrorCodd %d \r\n",
				 callID, iStatusCode);
		 
		String result= callbackResult(callID,iStatusCode);
		if(StrUtil.isNotBlank(result)) {
			log.info("======== OnPresetCallback Update CallbackResponseVo in map is ====== "+SipMap.respMap.get(callID));
		}

	 }

	     /*
		 * 预置位查询回调
		 *
		 */
		public static void OnQueryPresetCallback(String CallID, String presetList){
			System.out.format("JAVA OnQueryPresetCallback CallID: %s,presetlist: %s \r\n",
				CallID,presetList);

			 CallbackResponseVo resp = SipMap.respMap.get(CallID);
			 
			 if (null == resp || null == resp.getCallId() ||
				!CallID.equals(resp.getCallId())) {

				CallbackResponseVo vo = new CallbackResponseVo();
				vo.setCallId(CallID);
				vo.setStatusCode(200);
				vo.setPresetList(presetList);
				SipMap.respMap.put(CallID, vo);
			 }
		}
		
		
		/*
		*	录像文件查询回调
		*/
		public static void OnQueryRecordCallback(String CallID, String recordList){
			System.out.format("JAVA OnQueryRecordCallback CallID: %s, recordList: %s \r\n",
				CallID,recordList);
			
             CallbackResponseVo resp = SipMap.respMap.get(CallID);
			 if (null == resp || null == resp.getCallId() ||
				!CallID.equals(resp.getCallId())) {

				CallbackResponseVo vo = new CallbackResponseVo();
				vo.setCallId(CallID);
				vo.setStatusCode(200);
				vo.setRecordList(recordList);
				SipMap.respMap.put(CallID, vo);
			 }
		}

		/**
		 * 回调返回结果
		 * @param callID
		 * @param resultCode
		 */
		private static String callbackResult(String callID,int resultCode) {
			 CallbackResponseVo resp = SipMap.respMap.get(callID);

			 if (null == resp || null == resp.getCallId() ||
				!callID.equals(resp.getCallId()) ||
				resp.getStatusCode() != resultCode) {

				CallbackResponseVo vo = new CallbackResponseVo();
				vo.setStatusCode(resultCode);
				vo.setCallId(callID);
				SipMap.respMap.put(callID, vo);
			 }else {
				 return null;
			 }
			return "succ";
		}
	
}
