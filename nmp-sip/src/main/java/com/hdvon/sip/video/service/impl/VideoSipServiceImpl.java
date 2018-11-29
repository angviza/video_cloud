package com.hdvon.sip.video.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hdvon.nmp.common.WebConstant;
import com.hdvon.nmp.enums.InvokeTypeEnums;
import com.hdvon.nmp.exception.SipServiceException;
import com.hdvon.sip.video.service.IVideoSipService;
import com.hdvon.sip.video.service.base.BaseHandler;
import com.hdvon.sip.video.utils.CompositeSipParamsUtils;
import com.hdvon.sip.video.vo.CallbackResponseVo;
import com.hdvon.sip.video.vo.CameraRegisterSipVo;
import com.hdvon.sip.video.vo.ControlOptionInputVo;
import com.hdvon.sip.video.vo.ControlOptionSipVo;
import com.hdvon.sip.video.vo.CruiseOptionInputVo;
import com.hdvon.sip.video.vo.CruiseOptionSipVo;
import com.hdvon.sip.video.vo.FileResponseVo;
import com.hdvon.sip.video.vo.InviteOptionInputVo;
import com.hdvon.sip.video.vo.InviteOptionSipVo;
import com.hdvon.sip.video.vo.PlayDownloadInputVo;
import com.hdvon.sip.video.vo.PlaybackResponseVo;
import com.hdvon.sip.video.vo.PresetOptionInputVo;
import com.hdvon.sip.video.vo.PresetOptionSipVo;
import com.hdvon.sip.video.vo.QueryPreOptionInputVo;
import com.hdvon.sip.video.vo.QueryPreOptionSipVo;
import com.hdvon.sip.video.vo.QueryRecOptionInputVo;
import com.hdvon.sip.video.vo.QueryRecOptionSipVo;
import com.hdvon.sip.video.vo.RecordCtrlOptionSipVo;
import com.hdvon.sip.video.vo.RecordOptionSipVo;
import com.hdvon.sip.video.vo.RegisterCallback;
import com.hdvon.sip.video.vo.RegisterCredSipVo;
import com.hdvon.sip.video.vo.RegisterOptionSipVo;
import com.hdvon.sip.video.vo.ResponseVideoVo;
import com.hdvon.sip.video.vo.SipMap;
import com.hdvon.sip.video.vo.WiperOptionInputVo;
import com.hdvon.sip.video.vo.WiperOptionSipVo;
import com.sip.ControlOption;
import com.sip.CruiseOption;
import com.sip.InviteOption;
import com.sip.PresetOption;
import com.sip.QueryPreOption;
import com.sip.QueryRecOption;
import com.sip.RecordCtrlOption;
import com.sip.RecordOption;
import com.sip.RegisterCred;
import com.sip.RegisterOption;
import com.sip.SipInterface;
import com.sip.WiperOption;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <br>
 * <b>功能：</b>摄像机监控服务实现类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018/5/21<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Slf4j
@Service
public class VideoSipServiceImpl extends BaseHandler implements IVideoSipService {
	
	@Autowired
	private CompositeSipParamsUtils compositeParamsUtils;
	
	private static boolean started = false;
	
	@Override
	public boolean startUp(String ip, int port) {
		boolean isStart = false;
		try {
			log.info("========================== SipInterface.startUp started ==================== "+started);
			if (started) {
				return true;
			}
			
			log.info("========================== Before SipInterface.startUp ==================== ");
			//调用C++的启动接口
			isStart = SipInterface.startUp(ip, port);
			log.info("========================== After SipInterface.startUp ==================== ");
			
			if (!isStart) {
				//启动失败后修改标识开关并修改到内存中
				started = false;
				log.error("=================== 服务启动失败,请检查本地IP地址和端口号设置! =================");
				throw new SipServiceException("1000");//服务启动失败,请联系系统管理员!
			}
			
			//启动成功后修改标识开关并修改到内存中
			started = true;
			
		} catch (Throwable e) {
			started = false;
			e.printStackTrace();
			
			//调用通用的异常处理逻辑
			this.handleSipException(e);
		}
		
		return isStart;
	}

	@Override
	public RegisterCallback callRegister(RegisterOptionSipVo optionVo, RegisterCredSipVo credVo) {
		RegisterCallback register=null;
		try {
			
			log.info("========================== Before SipInterface.callRegister ==================== ");
			
			RegisterOption option = Convert.convert(RegisterOption.class, optionVo);
			RegisterCred cred = Convert.convert(RegisterCred.class, credVo);
			
			//调用C++的注册接口
			String callId = SipInterface.callRegister(option, cred);
			log.info("========================== After SipInterface.callRegister ==================== ");
			
			//同步调用时设置key的过期时间
			this.storeRedis(callId, InvokeTypeEnums.信令注册.getValue());
			//获取注册异步回调结果
			CallbackResponseVo responseVo = this.acquireAsncCallbackResult(callId, InvokeTypeEnums.信令注册.getValue());
		    register=new RegisterCallback();
			register.setCallId(callId);
			if(responseVo !=null) {
				register.setStatusCode(responseVo.getStatusCode());
			}
		} catch (Throwable e) {
			e.printStackTrace();
			//调用通用的异常处理逻辑
			this.handleSipException(e);
		}
			
		return register;
	}

	@Override
	public void callUnRegister(String registerId) {
		try {
			
			log.info("========================== Before SipInterface.callUnRegister ==================== ");
			//调用C++的注销接口
			SipInterface.callUnRegister(registerId);
			log.info("========================== After SipInterface.callUnRegister ==================== ");
			
		} catch (Throwable e) {
			e.printStackTrace();
			
			//调用通用的异常处理逻辑
			this.handleSipException(e);
		}
	}

	@Override
	public CallbackResponseVo callInvite(InviteOptionSipVo optionVo) {
		//获取invite异步回调结果
		CallbackResponseVo responseVo = null;
		try {
			
			log.info("========================== Before SipInterface.callInvite ==================== ");
			
			InviteOption option = Convert.convert(InviteOption.class, optionVo);
			
			//调用C++的invite接口
			String inviteId = SipInterface.callInvite(option);
			log.info("========================== After SipInterface.callInvite ==================== ");
			
			//存入内存，用于在invite的异步回调函数中比较callID的值
			//SipMap.map.put(inviteId, vo);
			//同步调用时设置key的过期时间
			this.storeRedis(inviteId, InvokeTypeEnums.实时播放.getValue());
			//获取invite异步回调结果
			responseVo = this.acquireAsncCallbackResult(inviteId, InvokeTypeEnums.实时播放.getValue());
			
		} catch (Throwable e) {
			e.printStackTrace();
			
			//调用通用的异常处理逻辑
			this.handleSipException(e);
		}
		
		return responseVo;
	}

	@Override
	public CallbackResponseVo callTerminate(String inviteId) {
		CallbackResponseVo responseVo = new CallbackResponseVo();
		try {
			
			log.info("========================== Before SipInterface.callTerminate ==================== ");
			//调用C++的invite请求结束接口
			SipInterface.callTerminate(inviteId);
			log.info("========================== After SipInterface.callTerminate ==================== ");
			
			responseVo.setCallId("success");
			responseVo.setStatusCode(200);
		} catch (Throwable e) {
			e.printStackTrace();
			//调用通用的异常处理逻辑
			this.handleSipException(e);
		}
		return responseVo;
	}

	@Override
	public void callACK(String inviteId) {
		try {
			
			log.info("========================== Before SipInterface.callACK ==================== ");
			//调用C++的callACK接口
			SipInterface.callACK(inviteId);
			log.info("========================== After SipInterface.callACK ==================== ");
			
		} catch (Throwable e) {
			e.printStackTrace();
			
			//调用通用的异常处理逻辑
			this.handleSipException(e);
		}
	}

	@Override
	public CallbackResponseVo callCloudCmd(ControlOptionSipVo ctrOptionVo) {
		CallbackResponseVo responseVo=null;
		try {
			
			log.info("========================== Before SipInterface.callCloudCmd ==================== ");
			
			ControlOption ctrOption = Convert.convert(ControlOption.class, ctrOptionVo);
			
			//调用C++的云台控制接口
			String ctrId = SipInterface.callCloudCmd(ctrOption);
			log.info("========================== After SipInterface.callCloudCmd ==================== ");
			
			//存入内存，用于在云台控制异步回调函数中比较callID的值
			//SipMap.map.put(ctrId, vo);
			//同步调用时设置key的过期时间
			this.storeRedis(ctrId, InvokeTypeEnums.云台控制.getValue());
			//获取云台控制异步回调结果
			responseVo = this.acquireAsncCallbackResult(ctrId, InvokeTypeEnums.云台控制.getValue());
		} catch (Throwable e) {
			e.printStackTrace();
			//调用通用的异常处理逻辑
			this.handleSipException(e);
		}
		
		return responseVo;
	}

	//回看控制
	@Override
	public CallbackResponseVo playBackCtrl(String callId, RecordCtrlOptionSipVo vo) {
		CallbackResponseVo responseVo = null;
		try {
			
			log.info("========================== Before SipInterface.playBackCtrl ==================== ");
			
			RecordCtrlOption ctrOption = Convert.convert(RecordCtrlOption.class, vo);
			
			//调用C++的云台控制接口
			String ctrId = SipInterface.playBackCtrl(callId, ctrOption);
			log.info("========================== After SipInterface.playBackCtrl ==================== ");
			
			//同步调用时设置key的过期时间
			this.storeRedis(ctrId, InvokeTypeEnums.回放控制.getValue());
			//获取回放控制异步回调结果
			 responseVo = this.acquireAsncCallbackResult(ctrId, InvokeTypeEnums.回放控制.getValue());
		} catch (Throwable e) {
			e.printStackTrace();
			//调用通用的异常处理逻辑
			this.handleSipException(e);
		}
		
		return responseVo;
	}
	
	/**
	 * 录像控制
	 */
	@Override
	public CallbackResponseVo palyDownload(RecordOptionSipVo recordOptionVo) {
		CallbackResponseVo responseVo = null;
		try {
			
			log.info("========================== Before SipInterface.callRecordCmd ==================== ");
			
			RecordOption recordOption = Convert.convert(RecordOption.class, recordOptionVo);
			
			String callId = SipInterface.callRecordCmd(recordOption);
			log.info("========================== After SipInterface.callRecordCmd ==================== ");
			
			log.info("palyDownload is callId" +callId);
			this.storeRedis(callId, InvokeTypeEnums.云台控制.getValue());
			responseVo = this.acquireAsncCallbackResult(callId, InvokeTypeEnums.云台控制.getValue());
		} catch (Throwable e) {
			e.printStackTrace();
			//调用通用的异常处理逻辑
			this.handleSipException(e);
		}
		
		return responseVo;
	}

	//预置位设置控制
	@Override
	public CallbackResponseVo callPresetCmd(PresetOptionSipVo presetOptionVo) {
		CallbackResponseVo responseVo=null;
		try {
			
			log.info("========================== Before SipInterface.callPresetCmd ==================== ");
			
			PresetOption presetOption = Convert.convert(PresetOption.class, presetOptionVo);
			
			//调用C++的预置位控制接口
			String ctrId = SipInterface.callPresetCmd(presetOption);
			log.info("========================== After SipInterface.callPresetCmd ==================== ");
			
			//同步调用时设置key的过期时间
			this.storeRedis(ctrId, InvokeTypeEnums.预置位控制.getValue());
			//获取预置位控制异步回调结果
			responseVo = this.acquireAsncCallbackResult(ctrId, InvokeTypeEnums.预置位控制.getValue());
		} catch (Throwable e) {
			e.printStackTrace();
			//调用通用的异常处理逻辑
			this.handleSipException(e);
		}
		
		return responseVo;
	}

	/**
	 * 雨刷控制
	 */
	@Override
	public CallbackResponseVo wiperControl(WiperOptionSipVo vo) {
		CallbackResponseVo responseVo=null;
		try {
			
			log.info("========================== Before SipInterface.callWiperCmd ==================== ");
			
			WiperOption param = Convert.convert(WiperOption.class, vo);
			
			//调用C++的预置位控制接口
			String ctrId = SipInterface.callWiperCmd(param);
			log.info("========================== After SipInterface.callWiperCmd ==================== ");
			
			
			//同步调用时设置key的过期时间
			this.storeRedis(ctrId, InvokeTypeEnums.云台控制.getValue());
			//获取预置位控制异步回调结果
			responseVo = this.acquireAsncCallbackResult(ctrId, InvokeTypeEnums.云台控制.getValue());
		} catch (Throwable e) {
			e.printStackTrace();
			//调用通用的异常处理逻辑
			this.handleSipException(e);
		}
		return responseVo;
	}

	/**
	 * 预置位查询
	 */
	@Override
	public CallbackResponseVo queryPreset(QueryPreOptionSipVo vo) {
		CallbackResponseVo responseVo = null;
		try {
			
			log.info("========================== Before SipInterface.callQueryPresetCmd ==================== ");
			
			QueryPreOption param = Convert.convert(QueryPreOption.class, vo);
			
			//调用C++的预置位控制接口
			String ctrId = SipInterface.callQueryPresetCmd(param);
			log.info("========================== After SipInterface.callQueryPresetCmd ==================== ");
			
			//同步调用时设置key的过期时间
			this.storeRedis(ctrId, InvokeTypeEnums.云台控制.getValue());
			//获取异步回调结果
			responseVo = this.acquireAsncCallbackResult(ctrId, InvokeTypeEnums.云台控制.getValue());
		} catch (Throwable e) {
			e.printStackTrace();
			//调用通用的异常处理逻辑
			this.handleSipException(e);
		}
		
		return responseVo;
	}
		
	//巡航预案控制
	@Override
	public CallbackResponseVo callCruiseCmd(CruiseOptionSipVo cruiseOptionVo) {
		CallbackResponseVo responseVo = null;
		try {
			
			log.info("========================== Before SipInterface.callCruiseCmd ==================== ");
			
			CruiseOption cruiseOption = Convert.convert(CruiseOption.class, cruiseOptionVo);
			
			//调用C++的巡航预案控制接口
			String ctrId = SipInterface.callCruiseCmd(cruiseOption);
			log.info("========================== After SipInterface.callCruiseCmd ==================== ");
			
			//存入内存，用于在巡航预案控制异步回调函数中比较callID的值
			//SipMap.map.put(ctrId, vo);
			//同步调用时设置key的过期时间
			this.storeRedis(ctrId, InvokeTypeEnums.巡航预案控制.getValue());
			//获取巡航预案控制异步回调结果
			responseVo = this.acquireAsncCallbackResult(ctrId, InvokeTypeEnums.巡航预案控制.getValue());
		} catch (Throwable e) {
			e.printStackTrace();
			//调用通用的异常处理逻辑
			this.handleSipException(e);
		}
		
		return responseVo;
	}

	@Override
	public CallbackResponseVo getVideoFile(QueryRecOptionSipVo vo) {
		CallbackResponseVo responseVo = null;
		try {
			
			log.info("========================== Before SipInterface.callQueryRecordCmd ==================== ");
			
			QueryRecOption param = Convert.convert(QueryRecOption.class, vo);
			
			//调用C++的巡航预案控制接口
			String ctrId = SipInterface.callQueryRecordCmd(param);
			log.info("========================== After SipInterface.callQueryRecordCmd ==================== ");
			
			log.info("----------------"+ctrId);
			//存入内存，用于在巡航预案控制异步回调函数中比较callID的值
			//SipMap.map.put(ctrId, vo);
			//同步调用时设置key的过期时间
			this.storeRedis(ctrId, InvokeTypeEnums.录像查询.getValue());
			//获取巡航预案控制异步回调结果
			responseVo = this.acquireAsncCallbackResult(ctrId, InvokeTypeEnums.录像查询.getValue());
		} catch (Throwable e) {
			e.printStackTrace();
			this.handleSipException(e);
		}
		
		return responseVo;
	}
	
	
	/**
	 * 所有的关于请求信令都经过这里
	 * 用户注册请求注册参数
	 * 
	 * @return
	 */
	@Override
	public RegisterCallback register(String type) {
		RegisterCallback responseVo=null;
		String username=CompositeSipParamsUtils.registeredUserName;//注册账号
		Map<String, Object> params = compositeParamsUtils.compositeParams();
		String receiverIp = (String) params.get("receiverIp");
		Integer receiverPort = (Integer) params.get("receiverPort");
		this.startUp(receiverIp, receiverPort);

		CameraRegisterSipVo paramVo = new CameraRegisterSipVo();
		paramVo.setType(SipMap.register_type);
		compositeParamsUtils.changeUserName(paramVo);//切换用户

		boolean check=StrUtil.isNotBlank(checkRegistered())?true:false;
		//第一次注册
		if (check && StrUtil.isBlank(type)) {
			Map<String, Object> regParam_one=compositeParamsUtils.compositeRegisterParams(paramVo);
			RegisterOptionSipVo	option = (RegisterOptionSipVo) regParam_one.get("optionParam");
			RegisterCredSipVo cred = (RegisterCredSipVo) regParam_one.get("credParam");

			//是否满足注册条件
			if(StrUtil.isNotBlank(cred.getUsername())) {
				log.info(">>>>>>>>>>>>>>>>信令账号在请求注册"+cred.getUsername()+">>>>>>>>>>>>>>>");
				responseVo=sendRegister(option,cred);
			    log.info("this call register1 param "+regParam_one);
			}
		}

		//请求信令错误时，重置另一账号注册，更换参数请求
		if(StrUtil.isNotBlank(type)) {
//			paramVo.setType(SipMap.register_type);
//			compositeParamsUtils.changeUserName(paramVo);//切换用户
			Map<String, Object> regParam_one=compositeParamsUtils.compositeRegisterParams(paramVo);
			RegisterOptionSipVo	option = (RegisterOptionSipVo) regParam_one.get("optionParam");
			RegisterCredSipVo cred = (RegisterCredSipVo) regParam_one.get("credParam");

			//第一次注册和第二次注册不是同一个账号
			if(! username.equals(cred.getUsername())) {
				log.info(">>>>>>>>>>>>>>>>信令账号在请求注册"+cred.getUsername()+">>>>>>>>>>>>>>>");
				responseVo=sendRegister(option,cred);
			    log.info("this call RE_register param "+responseVo);
			}
		}
		return responseVo;
	}

	/**
	 * 根据条件发送注册请求
	 * @param option
	 * @param cred
	 * @return
	 */
	private RegisterCallback sendRegister(RegisterOptionSipVo option, RegisterCredSipVo cred) {
		RegisterCallback registerCall = null;
		String key = CompositeSipParamsUtils.register_redisKey.replace(".", "");
		//key 不存在注册
		if(!redisTemplate.hasKey(key)) {
			registerCall= this.callRegister(option, cred);
			registerResults(registerCall,key,cred.getUsername());
		}
		return registerCall;

	}

	/**
	 * 注册code非200 或者 注册回调时间大于70秒 说明注册失败
	 * 将删除redis key
	 * @return
	 */
	private String checkRegistered() {
		//没有任何用户注册成功
		String userName="";
		String error="";
		String key = CompositeSipParamsUtils.register_redisKey.replace(".", "");
		String userKey=key.substring(0, 20);

		//此时没有用户是注册成功的
		if(SipMap.registerMap.entrySet().size() < 1 ) {
			redisTemplate.delete(key);
			return "400";
		}
		//有用户注册
	    for (Entry<String, RegisterCallback> register : SipMap.registerMap.entrySet()) {
	    	long time=System.currentTimeMillis();
		    RegisterCallback vo= register.getValue();
		    //有用户注册失败
			if((vo.getStatusCode() != 200) || (time-vo.getRegister_time() > 70*1000 )) {
				//取消注册
			   this.callUnRegister(vo.getCallId());
			   SipMap.registerMap.remove(vo.getUsername());
			   error="400";
		   }

			//账号已被注册多次,把当前的账号注销，否则会造成registerMap内存溢出
			if(!userName.contains(vo.getUsername())) {
		    	 userName+=vo.getUsername()+",";
		    }else {
		    	this.callUnRegister(vo.getCallId());
				SipMap.registerMap.remove(vo.getUsername());
		    }
			
			//redis key不存在了,但是注册一直都在正常使用
		    if(!redisTemplate.hasKey(key)) {
		    	if(userKey.equals(vo.getUsername())) {
					 redisTemplate.opsForValue().set(key, vo);
					 redisTemplate.expire(key, WebConstant.DEVICE_EXPIRE_SECONDS, TimeUnit.SECONDS);
		    	}
		    }

        }//for

	    //当前请求redis key不存在map中
	    if(!userName.contains(userKey)) {
		    redisTemplate.delete(key);
		    error="400";
	    }

	    return error;

	}


    /**
     * 响应注册结果
     * @param registerCall
     * @param key
     * @param username
     */
    private void registerResults(RegisterCallback registerCall,String key,String username) {
		if(registerCall==null) {
			throw new SipServiceException("1012");//信令服务异常
	   }else if(registerCall.getStatusCode()==200) {
		   registerCall.setUsername(username);
		   redisTemplate.opsForValue().set(key, registerCall);
		   redisTemplate.expire(key, WebConstant.DEVICE_EXPIRE_SECONDS, TimeUnit.SECONDS);
	   }
	}


    /**
     * 视频监控参数组装 返回inviteId
     * @param vo
     * @param request
     * @return
     */
    @Override
    public CallbackResponseVo invite(InviteOptionInputVo vo) {
    	
    	this.setInviteDeviceCode(vo);
		
    	//注册
		this.register(null);
		log.info("===================== 信令账号注册成功，准备调用视频点播接口  ====================");
		
		CallbackResponseVo responseVo = null;
		CameraRegisterSipVo paramVo = new CameraRegisterSipVo();
		paramVo.setClientIp(vo.getClientIp());//获取点播的ip地址
		paramVo.setClientPort(String.valueOf(vo.getPort()));
		
		InviteOptionSipVo param = compositeParamsUtils.creatInviteParam(vo, paramVo);
		log.info("this is invite request param----"+ param);
		//调用C++的播放接口，此接口会返回媒体服务器的IP、端口号、协议等信息并会根据插件协议的设置主动推流会被动推流到播放插件
		responseVo = this.callInvite(param);
		log.info("===================== 调用视频点播接口成功  ====================");
		
		//重新注册请求
//		responseVo =new CallbackResponseVo();
//		responseVo.setCallId("11111");
		repeatRequest(responseVo);
		log.info("===================== 重新调用注册接口成功  ====================");
		
		if(responseVo !=null) {
			if( responseVo.getStatusCode() !=200) {
				responseVo = this.callInvite(param);
				log.info("===================== 请求超时或回调超时重新调用视频点播接口成功  ====================");
			}
		}
		//还是非200 抛出异常
		throwError(responseVo);
		
		return responseVo;
	}
	

	/**
	 * 云台控制参数组装 返回callId
	 * @param vo
	 * @return
	 */
    @Override
    public ControlOptionSipVo directionControl(ControlOptionInputVo vo) {
    	
    	this.setControlDeviceCode(vo);
    	
		//注册
		this.register(null);
		ControlOptionSipVo paramVo = compositeParamsUtils.creatControlOptionParam(vo);
		log.info("this is invite cloudControl param----"+ paramVo);
		//调用C++的云台控制接口
		CallbackResponseVo responseVo = this.callCloudCmd(paramVo);
		repeatRequest(responseVo);
		
		if(responseVo !=null) {
			if(responseVo.getStatusCode() !=200) {
				responseVo=this.callCloudCmd(paramVo);
			}
		}
		
		//还是非200 抛出异常
		throwError(responseVo);
		
		return paramVo;
	}
    
	
	/**
	 * 预置位控制参数组装 返回callId
	 * @param vo
	 * @return
	 */
    @Override
	public PresetOptionSipVo presetControl(PresetOptionInputVo vo) {
    	
    	this.setPresetDeviceCode(vo);
    	
		//注册
		this.register(null);
		PresetOptionSipVo paramVo = compositeParamsUtils.creatPresetOptionParam(vo);
		//调用C++的预置位控制接口
		log.info("this call invite presetControl param----"+ paramVo);
		CallbackResponseVo responseVo =this.callPresetCmd(paramVo);
		repeatRequest(responseVo);
		if(responseVo !=null) {
			if(responseVo.getStatusCode() !=200) {
				responseVo =this.callPresetCmd(paramVo);
			}
		}
		throwError(responseVo);
		return paramVo;
	}

    
    /**
     * 预置位查询
     * @param vo
     */
    @Override
    public CallbackResponseVo queryPreset(QueryPreOptionInputVo vo) {
    	
    	this.setQueryPresetDeviceCode(vo);
    	
        this.register(null);
        QueryPreOptionSipVo paramVo = compositeParamsUtils.creatQueryPreOptionParam(vo);
        log.info("this is queryPreset param----"+ paramVo);
        //调用C++的云台控制接口
        CallbackResponseVo responseVo=this.queryPreset(paramVo);
        repeatRequest(responseVo);
        if(responseVo ==null || responseVo.getStatusCode() !=200) {
            responseVo=this.queryPreset(paramVo);
        }
        //还是非200 抛出异常
        throwError(responseVo);
        log.info("----- result "+responseVo.getPresetList());
        return responseVo;
    }
    
    
    /**
     * 云台控制
     * @param vo
     */
    @Override
	public ControlOptionSipVo cloudControl(ControlOptionInputVo vo) {
		
    	this.setCloudControlDeviceCode(vo);
    	
		//注册
		this.register(null);
		
		ControlOptionSipVo paramVo = compositeParamsUtils.creatControlOptionParam(vo);
		log.info("this is invite cloudControl param----"+ paramVo);
		//调用C++的云台控制接口
		CallbackResponseVo responseVo = this.callCloudCmd(paramVo);
		repeatRequest(responseVo);
		if(responseVo != null) {
			if(responseVo.getStatusCode() != 200) {
				responseVo = this.callCloudCmd(paramVo);
			}
		}
		//还是非200 抛出异常
		throwError(responseVo);
		return paramVo;
	}
	

	/**
	 * 回看控制
	 * @param callId
	 * @param vo
	 * @return
	 */
    @Override
	public String playbackCtrlInvite(String callId, RecordCtrlOptionSipVo vo) {
    	
		log.info("this is playbackCtrlInvite request param----"+ callId+vo);
		this.register(null);
		CallbackResponseVo responseVo = this.playBackCtrl(callId, vo);
		
		repeatRequest(responseVo);
		if(responseVo !=null) {
			if(responseVo.getStatusCode() !=200) {
				responseVo =this.playBackCtrl(callId,vo);
			}
		}
		throwError(responseVo);
//		if(responseVo ==null || responseVo.getStatusCode() !=200) {
//			responseVo =this.playBackCtrl(callId,vo);
//		}
//		//还是非200 抛出异常
//		if(responseVo.getStatusCode() !=200) {
//			throw new SipServiceException(responseVo.getStatusCode()+"");
//		}
		return responseVo.getCallId();
	}
	
	/**
	 * 录像控制，不是实时录像
	 * @param vo
	 * @return
	 */
	@Override
	public String playDownload(PlayDownloadInputVo vo) {
		
		this.setDownloadDeviceCode(vo);
		
		//注册
		this.register(null);
		RecordOptionSipVo param = compositeParamsUtils.creatPalyDownloadParam(vo);
		log.info("this is palyDownload request param----"+param);
		CallbackResponseVo responseVo = this.palyDownload(param);
		repeatRequest(responseVo);
		if(responseVo !=null) {
			if(responseVo.getStatusCode() !=200) {
				responseVo =this.palyDownload(param);
			}
		}
		throwError(responseVo);
		return responseVo.getCallId();
	}
	
	/**
	 * 用户正常注销时调用
	 */
	public void closeInvite(String inviteIds) {
		this.register(null);
		if(StrUtil.isNotBlank(inviteIds)){
			String[] callId = inviteIds.split(",");
			for(String inviteId: callId) {
				if(StrUtil.isNotBlank(inviteId)) {
					CallbackResponseVo responseVo = this.callTerminate(inviteId);
					repeatRequest(responseVo);
					if(responseVo !=null) {
						if(responseVo.getStatusCode() !=200) {
							responseVo =this.callTerminate(inviteId);
						}
					}
					throwError(responseVo);
				}
			}
		}
	}

	/**
	 * 雨刷开关控制
	 * @param vo
	 * @return
	 */
	@Override
    public void wiperControl(WiperOptionInputVo vo) {
		
		this.setWiperControlDeviceCode(vo);
		
 		this.register(null);
 		WiperOptionSipVo param = compositeParamsUtils.creatWiperOptionParam(vo);
 		log.info("this is wiperControl request param----"+ param);
 		CallbackResponseVo responseVo = this.wiperControl(param);
 		repeatRequest(responseVo);
 		if(responseVo ==null || responseVo.getStatusCode() !=200) {
 			responseVo =this.wiperControl(param);
 		}
 		throwError(responseVo);
	}

    
    /**
 	 * 巡航预案控制参数组装 返回callId
 	 * @param vo
 	 * @return
 	 */
	@Override
 	public CruiseOptionSipVo cruiseControl(CruiseOptionInputVo vo) {
		
		this.setCruiseControlDeviceCode(vo);
		
 		//注册
 		this.register(null);
 		
 		CruiseOptionSipVo paramVo = compositeParamsUtils.creatCruiseOptionParam(vo);
 		log.info("this is cruiseControl param----"+ paramVo);
 		//调用C++的巡航预案控制接口
 		CallbackResponseVo responseVo = this.callCruiseCmd(paramVo);
 		repeatRequest(responseVo);
 		if(responseVo == null || responseVo.getStatusCode() != 200) {
 			responseVo = this.callCruiseCmd(paramVo);
 		}
 		//还是非200 抛出异常
 		throwError(responseVo);
 		return paramVo;
 	}

	
	/**
	 * 根据请求结果是否 再次发送请求
	 * @return
	 */
	private void repeatRequest(CallbackResponseVo responseVo) {
		if(responseVo ==null || responseVo.getStatusCode() !=200 ) {
			if (redisTemplate.hasKey(CompositeSipParamsUtils.register_redisKey)) {
				redisTemplate.delete(CompositeSipParamsUtils.register_redisKey);
			}
			String type="2";
			this.register(type);
		}
	}
	
	/**
	 * 如果请求结果还是非200，则抛出异常信息
	 * @param responseVo
	 */
	private void throwError(CallbackResponseVo responseVo) {
		//还是非200 抛出异常
		if(responseVo !=null) {
			if(responseVo.getStatusCode() !=200) {
				throw new SipServiceException(responseVo.getStatusCode()+"");
			}
		}else {
			throw new SipServiceException("1003");//播放失败
		}
	}

	/**
	 * 录像回放/下载结束回调
	 * @param callId
	 * @return
	 */
	@Override
	public List<FileResponseVo> responseStatus(String callIds) {
		List<FileResponseVo> list = new ArrayList<FileResponseVo>();
		String[] ids = callIds.split(",");
		for(String id :ids) {
			if(StrUtil.isNotBlank(id)) {
				FileResponseVo responseVo=SipMap.fileResponseVo.get(id);
				if(responseVo !=null) {
					list.add(responseVo);
					SipMap.fileResponseVo.remove(id);
				}
			}
		}
		return list;
	}

	
	/**
	 * 录像查询
	 * @param vo
	 * @return
	 */
	@Override
	public List<ResponseVideoVo> getDownloadVideo(QueryRecOptionInputVo vo) {
		
		this.setDownloadVideoDeviceCode(vo);
		
		//注册
		this.register(null);
		
		List<ResponseVideoVo> list = new ArrayList<ResponseVideoVo>();
		CallbackResponseVo responseVo = null;
		CameraRegisterSipVo paramVo = new CameraRegisterSipVo();
		QueryRecOptionSipVo param = compositeParamsUtils.creatVideoFileParam(vo, paramVo);
		log.info("this is getVideoFile request param----"+ param);
		responseVo = this.getVideoFile(param);
		//重新注册请求
		repeatRequest(responseVo);
		if(responseVo != null) {
			if(responseVo.getStatusCode() != 200) {
				responseVo = this.getVideoFile(param);
			}
		}else {
			throw new SipServiceException("1013");//没有找到到录像
		}
		
		if(responseVo != null ) {
			if(responseVo.getStatusCode() == 200) {
				if(responseVo.getRecordList() != null) {
					   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
					   String json = JSON.toJSONString(responseVo.getRecordList());
					   log.info(">>>>>>>>>>>>>录像查询返回录像文件"+json+">>>>>>>>>>>>>>>>>>>>");
					   JSONArray array = JSONArray.parseArray(json);
					   for(int i=0;i<array.size();i++) {
						   ResponseVideoVo video = new ResponseVideoVo();
						   Map<String ,String> map = new HashMap<>();
						   String obj = (String) array.get(i);
						   String[] arr = obj.split(",");
						   for(String key :arr) {
							  String[] value = key.split("=");
							  if(value.length > 1) {
								  map.put(value[0], value[1]);
							  }
						  }
						 if(!map.isEmpty()) {
							 String  startTime=map.get("startTime").trim();
							 String  endTime=map.get("endTime").trim();
							 if(StrUtil.isNotBlank(startTime) && StrUtil.isNotBlank(endTime)) {
								 try {
									if(sdf.parse(startTime).before(sdf.parse(endTime))) {// 开始时间不能大于结束时间
										video.setStartTime(startTime);
										video.setEndTime(endTime);
										video.setFileSize(map.get("fileSize"));
										video.setType(map.get("type"));
										list.add(video);
									 }
								} catch (ParseException e) {
									e.printStackTrace();
								}
							 }
						 }
					 }
				}
			}
		  
		}
	
		return list;
	}

	
	@Override
	public void videoStop(String inviteIds) {
		
		//查询出点播返回的callid
		this.register(null);
		String[] callId = inviteIds.split(",");
		
		for(String inviteId: callId) {
			if(StrUtil.isNotBlank(inviteId)) {
				
				CallbackResponseVo responseVo = this.callTerminate(inviteId);
				repeatRequest(responseVo);
				
				if(responseVo != null) {
					if(responseVo.getStatusCode() != 200) {
						responseVo = this.callTerminate(inviteId);
					}
				}
				
				throwError(responseVo);
			}
		}
	}

	@Override
	public List<CallbackResponseVo> cameraMutiChannelPlayback(InviteOptionInputVo vo, List<Integer> prots) {
		
		List<CallbackResponseVo> list = new ArrayList<CallbackResponseVo>();
		//CallbackResponseVo responseVo=null;
		long sumTime = vo.getEndTime()-vo.getStartTime();
		long startTime = vo.getStartTime();
		long endTime = vo.getEndTime();
		
		//判断是否整除
		if(sumTime % vo.getNumber()==0) {
			
			int time = (int)sumTime/vo.getNumber();
			for(int i = 0;i < vo.getNumber();i++) {
				
				vo.setStartTime(startTime+time*i);
				vo.setEndTime(startTime+time*(i+1));
				vo.setPort(prots.get(i));
				CallbackResponseVo responseVo = this.invite(vo);
				PlaybackResponseVo back = copyProperties(responseVo, vo);
				list.add(back);
			}
		} else {
			
			int time = (int)sumTime/(vo.getNumber());
			for(int i = 0;i < vo.getNumber()-1;i++) {
				
				vo.setStartTime(startTime+time*i);
				vo.setEndTime(startTime+time*(i+1));
				vo.setPort(prots.get(i));
				
				CallbackResponseVo responseVo = this.invite(vo);
				PlaybackResponseVo back = copyProperties(responseVo, vo);
				list.add(back);
			}
			
			vo.setStartTime(vo.getEndTime());
			vo.setEndTime(endTime);
			vo.setPort(prots.get((vo.getNumber()-1)));
			
			CallbackResponseVo responseVo = this.invite(vo);
			PlaybackResponseVo back = copyProperties(responseVo, vo);
			list.add(back);
		}
		
		return list;
	}
	
	
	/**
	 * 返回回放开始时间和结束时间
	 * @param responseVo
	 * @param vo
	 * @return
	 */
	private PlaybackResponseVo copyProperties(CallbackResponseVo responseVo, InviteOptionInputVo vo) {
		PlaybackResponseVo back = new PlaybackResponseVo();
		BeanUtils.copyProperties(responseVo,back);
		back.setStartTime(vo.getStartTime());
		back.setEndTime(vo.getEndTime());
		return back;
	}

	
	@Override
	public List<String> playbackCtrl(RecordCtrlOptionSipVo vo, String callId) {
		
		//不是暂停的情况下
		if(vo.getCmdType() != 2) {
			double scale = vo.getScale() == 0? 1: vo.getScale();
			vo.setScale(scale);//播放速度
		}
		
		String[] ids = callId.split(",");
		List<String> list = new ArrayList<>();
		for(int i = 0;i < ids.length;i++) {
			
			if(StrUtil.isNotBlank(ids[i])) {
				String ctrID = this.playbackCtrlInvite(ids[i], vo);
				list.add(ctrID);
			}
		}
		
		return list;
	}
	
}
