package com.hdvon.sip.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.hdvon.nmp.common.SipConstants;
import com.hdvon.nmp.enums.MethodEum;
import com.hdvon.sip.entity.ByeBean;
import com.hdvon.sip.service.SipService;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能：websocket心跳
 * 作者：chenjiefeng
 * 日期：2018/1/31
 * 版权所有：广州弘度信息科技有限公司 版权所有(C) 2017
 */
@Slf4j
@Component
public class WebSocketHeartBeatTask {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    @Autowired
	SipService sipService;
    
    //可以试试ping - pong心跳
    @Scheduled(cron = "0/5 * * * * ? ")
    public void heartBeat() {
        log.debug("heartbeat");
        simpMessagingTemplate.convertAndSend(SipConstants.WEBSOCKET_BROADCAST_PATH_MSG, new Date().toString());
        //simpMessagingTemplate.convertAndSendToUser("abc", SipConstants.P2P_PUSH_PATH_MSG, "你妹的");
    }

    //定时扫描无用链接
    @Scheduled(cron = "0/10 * * * * ? ")
    public void clearNoActive() {
    	
    	log.debug(">>>>>>>>>>>>>>>>>>>>> 定时扫描无用链接并处理意外关闭视频的事件处理 <<<<<<<<<<<<<<<<<<<<<");
    	this.handleDisconnectEvent();
    	
        WebSocketManager.clear();
    }
    
    //处理浏览器意外关闭视频时的事件处理
    private void handleDisconnectEvent() {
    	
    	//获取存储NoActive的缓存
    	ConcurrentHashMap<String,WsCacheBean> map = WebSocketManager.getNoActiveMap();
    	
    	if(!map.isEmpty()) {
    		
    		//遍历存储在Map缓存实体，包含键值对
    		for(Iterator<Entry<String, WsCacheBean>> it = map.entrySet().iterator(); it.hasNext();) {
    			
    			//获取缓存在map中的WsCacheBean
    			WsCacheBean cacheBean = it.next().getValue();
    			if (null != cacheBean) {
    				
    				if(System.currentTimeMillis() - cacheBean.getConnectTime() > 60){
    					
    					/**
        		         * 发送信令断开callId
        		         */
    					Map<String,Object> sipMap = cacheBean.getSipMap();
        		        
        		        if(!sipMap.isEmpty()) {
        		        	
        		        	log.debug(">>>>>>>>>>>>>>>>>>>> 存储callId的map:{} <<<<<<<<<<<<<<<<<<<< "+JSON.toJSONString(sipMap));
        		        	
        		        	for(Iterator<String> ite = sipMap.keySet().iterator(); ite.hasNext();) {
        		        		
        		        		String callId = ite.next();
        		            	this.callTerminate(callId, cacheBean.getReqIp(), cacheBean.getUserId());
        		            }	
        		        }
    				}
    				
    			}
    			
            }
    		
    	}
    	
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
