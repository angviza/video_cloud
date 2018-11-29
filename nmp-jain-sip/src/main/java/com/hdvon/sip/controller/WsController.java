package com.hdvon.sip.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hdvon.nmp.common.SipConstants;
import com.hdvon.sip.config.ws.MyPrincipal;
import com.hdvon.sip.entity.ErrorBean;
import com.hdvon.sip.entity.ErrorMsg.ResponseCodeEnum;
import com.hdvon.sip.entity.RequestBean;
import com.hdvon.sip.entity.ResponseBean;
import com.hdvon.sip.entity.param.CloudParam;
import com.hdvon.sip.service.SipService;
import com.hdvon.sip.utils.ClientUtil;
import com.hdvon.sip.websocket.WebSocketManager;
import com.hdvon.sip.websocket.WsCacheBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Controller
public class WsController{
	
	@Autowired
    SipService sipService;
	
	private static final int DEFAULT_PORT = 30000;

    @MessageMapping(SipConstants.WEBSOCKET_PATH_URL)//@MessageMapping和@RequestMapping功能类似，用于设置URL映射地址，浏览器向服务器发起请求，需要通过该地址。
    @SendToUser(SipConstants.WEBSOCKET_P2P_PATH_MSG)//如果服务器接受到了消息，就会对订阅了@SendTo括号中的地址传送消息。
    //在springmvc 中可以直接获得principal,principal 中包含当前用户的信息(使用了wsId，作为principal)
    public ResponseBean operate(MyPrincipal principal, String message) throws Exception {
		//获取客户端请求ip
		String host = principal.getReqIp();
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>host:"+host);
		//获取请求的ip地址
        ResponseBean respData = sipService.sendMsg(message,host);
        
        return respData;

    }
	@PostMapping(value = "/cloudControl")
	public ErrorBean cloudControl(CloudParam paramVo){
		return sipService.cloudControl(paramVo);
	}
	
	
	@GetMapping(value = "/getInvtiePort")
	public JSONObject getInvtiePort(HttpServletRequest request){
		//获取客户端请求ip
		String host = ClientUtil.getClientIp(request);
		
		String wsId = request.getParameter("wsId");
		WsCacheBean wsBean = WebSocketManager.get(wsId);
		if(wsBean==null) {
			log.error(">>>>>>>>>>>>>>>>>>>>wsId申请端口失败，原因没有建立websocket连接");
			return getErrorMsg(ResponseCodeEnum.UNAUTH_RROR);
		}
		//根据wsId获取端口	
		int port = 0;
		try {
			//流媒体服务器会返回rtsp rtmp返回2个端口，防止端口出现串流问题
			port = wsBean.getPortCounter().incrementAndGet()-1;
			
			wsBean.getPortCounter().incrementAndGet();
		} catch (NullPointerException e) {
			// TODO: handle exception
			log.error(">>>>>>>>>>>>>>>>>>>>wsId申请端口失败，端口已被使用完");
			return getErrorMsg(ResponseCodeEnum.REQ_PORT_ERROR);
		}
		JSONObject json = new JSONObject();
		json.put("code", String.valueOf(SipConstants.SUCCESS));
		json.put("host", host);
		json.put("port", port);
		
		return json;
	}
    @MessageMapping("/test")//@MessageMapping和@RequestMapping功能类似，用于设置URL映射地址，浏览器向服务器发起请求，需要通过该地址。
    @SendToUser(SipConstants.WEBSOCKET_P2P_PATH_MSG)//如果服务器接受到了消息，就会对订阅了@SendTo括号中的地址传送消息。
    //在springmvc 中可以直接获得principal,principal 中包含当前用户的信息(使用了wsId，作为principal)
    public ResponseBean test(MyPrincipal principal, RequestBean message) throws Exception {

        ResponseBean<String> data = new ResponseBean();
        data.setResult("testtest------principal="+principal.getName());

        return data;
    }
    
    
    private JSONObject getErrorMsg(ResponseCodeEnum em) {
    	JSONObject json = new JSONObject();
		json.put("code", em.getCode());
		json.put("msg", em.getValue());
		return json;
    }
}
