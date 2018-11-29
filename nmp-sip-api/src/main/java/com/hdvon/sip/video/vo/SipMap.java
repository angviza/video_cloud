package com.hdvon.sip.video.vo;

import java.util.concurrent.ConcurrentHashMap;

import com.hdvon.sip.video.vo.CallbackResponseVo;
import com.hdvon.sip.video.vo.FileResponseVo;
import com.hdvon.sip.video.vo.RegisterCallback;

public class SipMap {
	
	public static ConcurrentHashMap<String, CallbackResponseVo> respMap = new ConcurrentHashMap<String, CallbackResponseVo>();
	
	public static String register_type="one"; //注册账号
	
	public static ConcurrentHashMap<String, RegisterCallback> registerMap = new ConcurrentHashMap<String, RegisterCallback>();
	
	//录像回放、下载结束是回调
	public static ConcurrentHashMap<String, FileResponseVo> fileResponseVo = new ConcurrentHashMap<String, FileResponseVo>();
	
	
}
