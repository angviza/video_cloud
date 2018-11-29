package com.hdvon.sip.entity;

import org.springframework.beans.BeanUtils;

public class ErrorMsg {
	/**
	 * 响应状态枚举
	 * @author wanshaojian
	 *
	 */
	public enum ResponseCodeEnum{
		//说明caller正在呼叫，但还没联系上callee
		TRYING("100","Trying"),
		
		//说明callee已经被联系上,callee的铃正在响.收到这个信息后，等待200 OK
		RINGING("180","Ringing"),
		//说明call被重新路由到另外一个目的地
		CALL_BEING_FORWARD("181","Call is being forwarded"),
		//说明callee当前是不可获得的，但是对方不想直接拒绝呼叫，而是选择放在呼叫队列中
		QUEUED("182","Queued"),
		//用来警告caller频段(inband)错误。当从PSTN收到一个ISDN消息，SIP gateway 产生183 Session progress 。
		SESSION_PROGRESS("183","Session progress"),
		
		//请求成功
		SUCCESS("200","ok"),
		
		
		//3xx Redirection Responses
		//说明呼叫的地址被解析成多个地址，所有的地址都被提供出来，用户或用户代理可以从中选择联系哪个。
		MULTIPLE_CHOICES("300","Multiple choices"),
		//说明指定地址的用户已经永远不可用，在头中已经用另外一个地址替换了.
		MOVED_PERMANENTLY("301","Moved permanently"),
		//说明指定地址的用户临时不可用，在头中已经用另外一个地址代替了.
		MOVED_TEMPORARILY("302","Moved temporarily"),
		//说明caller必须用一个proxy来联系callee.
		USE_PROXY("305","Use proxy"),
		//说明call不成功，但是可选择其他的服务
		ALTERNATIVE_SERVICE("380","Alternative service"),
		
		
		//4xx Request Failure Responses
		//参数有误
		PARAM_ERROR("400","Bad Request"),
		//鉴权失败
		UNAUTH_RROR("401","Unauthorized"),
		//说明完成会话需要付费
		PAYMENT_RROR("402","Payment required"),
		//用户未注册
		NOT_REGISTER("403","Not Register yet"),
		//说明server有明确的信息在指定的域中用户不存在
		NOT_FOUND("404","Not Found"),
		//说明请求中指定的方法是不被允许的。将返回一个允许的方法列表
		METHOD_ERROR("405","Method Not Allowed"),
		//说明被请求的资源只对某些特殊的请求作出响应，对目前头(header)中指定的请求不接受
		NOT_ACCPTABLE("406","Not Acceptable"),
		//和401 Unauthorized response相似.但是，它说明client必须首先在proxy上认证自己。
		PROXY_UNAUTH_RROR("407","Proxy authentication required"),
		//说明在timeout时间过期前，server不能产生响应.
	    REQUEST_TIMEOUT("408","request timeout"),
	    //说明由于和当前资源状态产生冲突，请求不能被处理。
	    CONFLICT("409","Conflict"),
	    //说明请求资源在server上永远不可用，也没有转发的地址
	    GONE("410","Gone"),
	    //说明用户拒绝接受没有定义content长度的请求。
	    LENGTH_REQUIRED("411","Length required"),
	    //说明server拒绝处理请求，因为它太大，超过了server能处理的大小。
	    REQUEST_LARGE_ERROR("413","Request entity too large"),
	    //说明server拒绝处理请求，因为请求的URI太长，server不能解释它。
	    REQUEST_URI_LONG("414","Request-URI too long"),
	    //说明server拒绝处理请求，因为body格式不被目的终端支持
	    UNSUPPORTED_MEDIA("415","Unsupported media"),
	    //说明server不能理解在header中指出的扩展协议。
	    BAD_EXTENSION("420","Bad extension"),
	    //说明callee已经被联系上，但是暂时不可用。
	    TEMPORARILY_UNAVAILABLE("480","Temporarily unavailable"),
	    //说明server正在忽略请求，由于它是一个没有匹配legID的BYE或者是一个没有匹配事务的CANCEL。
	    NOT_CALL("481","Call leg/transaction does not exist"),
	    //说明server收到了一个包含它自己路径的请求.
	    LOOP_DETECTED("482","Loop detected"),
	    //说明server收到了一个请求，它需要的hop数超过了在header中允许的最大hop数.
	    TOO_MANY_HOPS("483","Too many hops"),
	    //说明server收到一个地址不完整的请求.
	    ADDRESS_INCOMLETE("484","Address incomplete"),
	    //说明server收到一个请求，其中callee的地址是不明确的，也没有可能备用的地址供选择。
	    AMBIGUOUS("485","Ambiguous"),
	    //说明callee已经被联系上，但是它们的系统不能承受额外的call.
	    BUSY_HERE("486","Busy here"),
	   
	    //说明server或gateway发生意外错误从而不能处理请求.
	    SERVER_INTERNAL_ERROR("500","Server internal error"),
	    //说明server或gateway不支持完成请求所需的功能.
	    NOT_IMPLEMENTED("501","Not implemented"),
	    //说明server或gateway从下游server收到一个非法响应.
	    BAD_GETEWAY("502","Bad gateway"),
	    //说明由于超负载或维护问题，server或gateway不能处理请求.
	    SERVICE_UNAVAILABLE("503","Service unavailable"),
	    //说明server或gateway没有从另外一个server(如location server)收到一个及时的响应.
	    GETEWAY_TIMEOUT("504","Gateway timeout"),
	    //说明server或gateway不支持在请求中用到的SIP版本。
	    VERSION_NOT_SUPPORTED("505","Version not supported"),

	    
	    //6xx Global Responses
	    //说明callee已经被联系上，但是处于忙状态中，在这个时间不能接受call。
	    BUSY_EVERYWHERE("600","Busy everywhere"),
	    //说明callee已经被联系上，但是不能或不想加入call。
	    DECLINE("603","Decline"),
	    //说明server有正式的信息说明callee不存在于网络中。
	    DOES_NOT_EXIST("604","Does not exist anywhere"),	    
	    NOT_ACCEPTABLE("606","Not acceptable"),
	    
	    //版本不匹配
	    VERSION_ERROR("1010","version number is incorrect"),
	    //云台控制类型不存在
	    CLOUD_TYPE_ERROR("1011","Cloud Control Type Not Found"),
	    //云台方向控制类型不存在
	    DIRECTION_TYPE_ERROR("1012","Direction Type Not Found"),
	    //云台镜头变倍控制类型不存在
	    ZOOM_TYPE_ERROR("1013","Zoom Type Not Found"),
	    //光圈控制类型不存在
	    IRIS_TYPE_ERROR("1014","Iris Type Not Found"),
	    //焦距控制类型不存在
	    FOCUS_TYPE_ERROR("1015","Focus Type Not Found"),
	    //巡航预案控制类型不存在
	    CRUISE_TYPE_ERROR("1016","Cruise Type Not Found"),
	    //预置位控制不存在
	    PRESET_TYPE_ERROR("1017","Preset Type Not Found"),
	    //回看控制类型不存在
	    PLAYBACK_TYPE_ERROR("1018","Playback Type Not Found"),
	    //用户在信令服务器未注册
	    USER_UNAUTH_ERROR("1020","sip Unauthorized"),
	    //信令异常
	    SIGNALLING_ERROR("1021","signalling exception"),
	    //信令异常
	    REQ_PARAM_ERROR("1022","Request parameter can not be empty"),
	    //信令指令不存在
	    RECORD_TYPE_ERROR("1023","Video command doesn't exist."),
	    //下载速度不存在
	    DOWNLOAD_SPEED_ERROR("1024","download speed Not Found"),
	    //暂时不支持tcp协议
	    TRANSPORT_ERROR("1025","Tcp protocol is not supported for the time being"),
	    //申请端口失败
	    REQ_PORT_ERROR("1026","Application port failed"),
	    //申请端口失败
	    VIDEO_PLAY_ERROR("1027","video play failed"),
	    //sip异常
	    SIP_ERROR("700","sip exception");
	    
		private ResponseCodeEnum(String code,String value) {
			this.code= code;
			this.value = value;
		}
		
		String code;
		String value;
		
		public static ResponseCodeEnum getObjectByKey(String key) {
			for(ResponseCodeEnum em:ResponseCodeEnum.values()) {
				if(em.getCode().equals(String.valueOf(key))) {
					return em;
				}
			};
			return ResponseCodeEnum.SIP_ERROR;
		}
		
		public String getCode(){
			return code;
		}
		public String getValue() {
			return value;
		}	
	}
	public static ResponseBean resultErrorMsg(RequestBean request,ResponseCodeEnum em) {
		
		ResponseBean respData = new ResponseBean();
		BeanUtils.copyProperties(request, respData);
		
		ErrorBean errorBean = new ErrorBean();
		errorBean.setCode(em.getCode());
		errorBean.setMessage(em.getValue());
		
		respData.setError(errorBean);
		return respData;
	}
	
	public static ResponseBean resultErrorMsg(ResponseBean<?> respBean,ResponseCodeEnum em) {
		ErrorBean errorBean = new ErrorBean();
		errorBean.setCode(em.getCode());
		errorBean.setMessage(em.getValue());
		
		respBean.setError(errorBean);
		return respBean;
	}
	
	public static ErrorBean resultMsg(ResponseCodeEnum em) {
		ErrorBean errorBean = new ErrorBean();
		errorBean.setCode(em.getCode());
		errorBean.setMessage(em.getValue());
		
		return errorBean;
	}
}
