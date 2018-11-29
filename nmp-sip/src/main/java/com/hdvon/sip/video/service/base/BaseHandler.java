package com.hdvon.sip.video.service.base;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.hdvon.nmp.common.WebConstant;
import com.hdvon.nmp.exception.SipServiceException;
import com.hdvon.sip.config.redis.BaseRedisDao;
import com.hdvon.sip.video.utils.PropertiesUtils;
import com.hdvon.sip.video.vo.CallbackResponseVo;
import com.hdvon.sip.video.vo.ControlOptionInputVo;
import com.hdvon.sip.video.vo.CruiseOptionInputVo;
import com.hdvon.sip.video.vo.InviteOptionInputVo;
import com.hdvon.sip.video.vo.PlayDownloadInputVo;
import com.hdvon.sip.video.vo.PresetOptionInputVo;
import com.hdvon.sip.video.vo.QueryPreOptionInputVo;
import com.hdvon.sip.video.vo.QueryRecOptionInputVo;
import com.hdvon.sip.video.vo.SipMap;
import com.hdvon.sip.video.vo.WiperOptionInputVo;

public class BaseHandler {
	
	
	@SuppressWarnings("rawtypes")
	@Autowired
    public RedisTemplate redisTemplate;
	
	@SuppressWarnings("unchecked")
	public void storeRedis(String callId, int invokeType) {
		redisTemplate.opsForValue().set(callId, callId);
		
		if (invokeType == 0) {//注册
			redisTemplate.expire(callId, WebConstant.SYNC_REGISTER_MSECS, TimeUnit.SECONDS);
		} else if (invokeType == 1) {//播放
			redisTemplate.expire(callId, WebConstant.SYNC_INVITE_MSECS, TimeUnit.SECONDS);
		} else if (invokeType == 2) {//云台控制
			redisTemplate.expire(callId, WebConstant.SYNC_CNTRL_MSECS, TimeUnit.SECONDS);
		} else if (invokeType == 3) {//回放控制
			redisTemplate.expire(callId, WebConstant.SYNC_PLAYBACK_MSECS, TimeUnit.SECONDS);
		} else if (invokeType == 4) {//预置位控制
			redisTemplate.expire(callId, WebConstant.SYNC_PRESET_MSECS, TimeUnit.SECONDS);
		} else if (invokeType == 5) {//巡航预案控制
			redisTemplate.expire(callId, WebConstant.SYNC_CRUISE_MSECS, TimeUnit.SECONDS);
		} else if (invokeType == 6) {//录像查询
			redisTemplate.expire(callId, WebConstant.SYNC_REVE_MSECS, TimeUnit.SECONDS);
		}
	}
	
	public void handleSipException(Throwable e) {
		String message = e.getMessage();
		if (message.toLowerCase().indexOf("connection")!=-1 && 
			message.toLowerCase().indexOf("exception")!=-1) {
			throw new SipServiceException("1001");//请求连接超时,请保持网络畅通后重试
		}
		
		if (message.toLowerCase().indexOf("exception")!=-1) {
			throw new SipServiceException("1011");//服务异常，请稍后重试
		}
		
		if (message.toLowerCase().indexOf("com.sip") != -1) {
			throw new SipServiceException("1012");//服务器内部错误，请稍后重试
    	}
		
		throw new SipServiceException(message);
	}
	
	/**
	 * 针对注册、invite、云台控制、回放控制异步回调结果通用的业务处理逻辑
	 */
	@SuppressWarnings({ "unchecked"})
	public CallbackResponseVo acquireAsncCallbackResult(String callId, int invokeType) {
		boolean flag = true;
		CallbackResponseVo vo = null;
		while(flag) {//需要重复执行的逻辑
			try {
				//当前线程睡眠0.8秒，以便可以获取到异步回调中map缓存的数据
				Thread.sleep(WebConstant.WAIT_ASNC_MSECS);
			} catch (InterruptedException e) {
				e.printStackTrace();
				//flag=false;
				throw new SipServiceException("1003");//请求超时,请稍后重试!
			}
			//同步调redis缓存key的处理
			if (!redisTemplate.hasKey(callId) || 
				null == redisTemplate.opsForValue().get(callId)) {//redis缓存中的key已过期
				if (SipMap.respMap.containsKey(callId)) {
					SipMap.respMap.remove(callId);
				}
				flag=false;
				//throw new SipServiceException("1003");//请求超时,请稍后重试!
			}
			//从异步回调中的map中返回对应的数据
			vo = SipMap.respMap.get(callId);
			if(vo !=null) {
				if (callId.equals(vo.getCallId())) {//同步返回的key与异步回调返回的key相匹配则从对应的map中移除掉
					flag=false;
					//返回之后删除对应的key
					SipMap.respMap.remove(callId);
				}
			}
		}
		return vo;
	}
	
	public void setInviteDeviceCode(InviteOptionInputVo vo) {
		if(PropertiesUtils.getBooleanProperty("isDEV")) {	//开发环境或测试环境
			vo.setDeviceCode(PropertiesUtils.getProperty("default.deviceCode"));
		} //生产环境
	}
	
	public void setControlDeviceCode(ControlOptionInputVo vo) {
		if(PropertiesUtils.getBooleanProperty("isDEV")) {	//开发环境或测试环境
			vo.setDeviceCode(PropertiesUtils.getProperty("default.deviceCode"));
		} //生产环境
	}
	
	
	public void setPresetDeviceCode(PresetOptionInputVo vo) {
		if(PropertiesUtils.getBooleanProperty("isDEV")) {	//开发环境或测试环境
			vo.setDeviceCode(PropertiesUtils.getProperty("default.deviceCode"));
		} //生产环境
	}
	
	public void setQueryPresetDeviceCode(QueryPreOptionInputVo vo) {
		if(PropertiesUtils.getBooleanProperty("isDEV")) {	//开发环境或测试环境
			vo.setDeviceCode(PropertiesUtils.getProperty("default.deviceCode"));
		} //生产环境
	}
	
	public void setCloudControlDeviceCode(ControlOptionInputVo vo) {
		if(PropertiesUtils.getBooleanProperty("isDEV")) {	//开发环境或测试环境
			vo.setDeviceCode(PropertiesUtils.getProperty("default.deviceCode"));
		} //生产环境
	}
	
	public void setDownloadDeviceCode(PlayDownloadInputVo vo) {
		if(PropertiesUtils.getBooleanProperty("isDEV")) {	//开发环境或测试环境
			vo.setDeviceCode(PropertiesUtils.getProperty("default.deviceCode"));
		} //生产环境
	}
	
	public void setWiperControlDeviceCode(WiperOptionInputVo vo) {
		if(PropertiesUtils.getBooleanProperty("isDEV")) {	//开发环境或测试环境
			vo.setDeviceCode(PropertiesUtils.getProperty("default.deviceCode"));
		} //生产环境
	}
	
	public void setCruiseControlDeviceCode(CruiseOptionInputVo vo) {
		if(PropertiesUtils.getBooleanProperty("isDEV")) {	//开发环境或测试环境
			vo.setDeviceCode(PropertiesUtils.getProperty("default.deviceCode"));
		} //生产环境
	}
	
	public void setDownloadVideoDeviceCode(QueryRecOptionInputVo vo) {
		if(PropertiesUtils.getBooleanProperty("isDEV")) {	//开发环境或测试环境
			vo.setDeviceCode(PropertiesUtils.getProperty("default.deviceCode"));
		} //生产环境
	}
	
}
