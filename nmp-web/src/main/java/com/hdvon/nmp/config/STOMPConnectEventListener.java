package com.hdvon.nmp.config;

import com.hdvon.nmp.util.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;


/**
 * STOMP监听类，用于缓存websocket_session
 * @author zhuxiaojin
 * @Date 2018-10-16
 * 版权所有：广州弘度信息科技有限公司 版权所有(C) 2018
 */
public class STOMPConnectEventListener implements ApplicationListener<SessionConnectEvent> {

    @Autowired
    private RedisHelper redisHelper;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        //login get from browser
        if(sha.getNativeHeader("userid")==null){
            return;
        }
        String userid = sha.getNativeHeader("userid").get(0);
        String sessionId = sha.getSessionId();
        redisHelper.redisTemplate.opsForValue().set("websocket:"+userid,sessionId);
    }
}

