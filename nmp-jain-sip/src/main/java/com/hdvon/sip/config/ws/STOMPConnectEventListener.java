package com.hdvon.sip.config.ws;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.alibaba.fastjson.JSON;
import com.hdvon.nmp.enums.MethodEum;
import com.hdvon.sip.entity.ByeBean;
import com.hdvon.sip.service.SipService;
import com.hdvon.sip.websocket.WebSocketManager;
import com.hdvon.sip.websocket.WsCacheBean;

import lombok.extern.slf4j.Slf4j;


/**
 * STOMP监听类，用于缓存websocket_session
 * @author chenjiefeng
 * @Date 2018-10-19
 * 版权所有：广州弘度信息科技有限公司 版权所有(C) 2018
 */
@Slf4j
@Component
public class STOMPConnectEventListener{ //implements ApplicationListener<SessionConnectEvent> {

	@Autowired
	SipService sipService;

	
    //连接成功
    @EventListener
    public void onConnectEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        if(sha.getUser() == null) {
        	return;
        }
        String myPrincipal = sha.getUser().getName();
        log.info("onConnectEvent ===="+myPrincipal);
        //删除activemap
        WebSocketManager.removeNoActive(myPrincipal);
    }

    //连接断开
    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event)  {
    	CloseStatus status =event.getCloseStatus();
        log.info(">>>>>>>>>>>>>>>>>>>>CloseStatus:{}",status.getCode());
        
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        if(sha.getUser() == null) {
        	return;
        }
        String myPrincipal = sha.getUser().getName();
        log.info("onDisconnectEvent  ===={}",myPrincipal);
        //根据wsID 获取callId
        WsCacheBean wsCacheBean = WebSocketManager.get(myPrincipal);
        if(wsCacheBean == null) {
        	return;
        }
        /**
         * 发送信令断开callId
         */
        Map<String,Object> sipMap = wsCacheBean.getSipMap();
        log.info("onDisconnectEvent  ====自动关闭Invite流 的callId集合为：{}",JSON.toJSONString(sipMap));
        if(!sipMap.isEmpty()) {
        	for(Iterator<String> it=sipMap.keySet().iterator();it.hasNext();) {
            	String callID = it.next();
            	callTerminate(callID, wsCacheBean.getReqIp(), wsCacheBean.getUserId());
            }	
        }
        
        WebSocketManager.removePrincipal(myPrincipal);
        WebSocketManager.removeNoActive(myPrincipal);

    }
    
    
    /**
     * 自动关流
     * @param callID
     */
    private void callTerminate(String callId, String reqIp, String userId) {
    	
    	ByeBean model = new ByeBean();
		model.setVersion("1.0");
		model.setToken("系统操作");
		model.setWsId("12345");
		model.setTransactionID("eyJsaWNlbnNlSWQiOiJLNzFV");
		model.setMethod(MethodEum.TERMINATE.getValue());
		model.setCallId(callId);
		model.setReqIp(reqIp);
		model.setUserId(userId);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("callId", callId);
		String param = JSON.toJSONString(map);
		model.setParam(param);
		
		sipService.terminate(model);
    }
    
}

