/**
 * @Project: rcp-java
 * @Copyright: ©2017  广州弘度信息科技有限公司. 版权所有
 */
package com.hdvon.nmp.config;

import com.hdvon.nmp.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.*;

/**
 * websocket配置类
 * @author zhuxiaojin
 * @Date 2018-10-15
 * 版权所有：广州弘度信息科技有限公司 版权所有(C) 2018
 */
@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {


    //STOMP监听类
    @Bean
    public STOMPConnectEventListener applicationStartListener(){
        return new STOMPConnectEventListener();
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        //建立连接端点，注册一个STOMP的协议节点,并指定使用SockJS协议
        stompEndpointRegistry.addEndpoint("/nmpSocketWeb")
                .setAllowedOrigins("*")
                .withSockJS();
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry messageBrokerRegistry) {
        //配置消息代理（MessageBroker）。
        messageBrokerRegistry.enableSimpleBroker("/topic");// 推送消息前缀
        messageBrokerRegistry.setApplicationDestinationPrefixes("/app");// 应用请求前缀，前端发过来的消息将会带有“/app”前缀。
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        //token认证
        registration.setInterceptors(new ChannelInterceptorAdapter() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand()) || StompCommand.SEND.equals(accessor.getCommand())) {
                    String token = accessor.getFirstNativeHeader("token");
                    try {
                        tokenValidate(token);
                    } catch (Exception e) {
                        log.error(e.toString());
                        return null;
                    }
                }
                return message;
            }
        });
    }


    public boolean tokenValidate(String token) throws Exception {
        if (token == null || token.isEmpty()) {
            throw new Exception("webSocket：token为空！");
        }
        if (JwtUtil.validateToken(token)==null) {
            throw new Exception("webSoc：token无效！");
        }
        return true;
    }

}
