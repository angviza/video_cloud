package com.hdvon.sip.config;

import com.hdvon.nmp.common.SipConstants;
import com.hdvon.sip.config.ws.AuthHandshakeInterceptor;
import com.hdvon.sip.config.ws.MyPrincipalHandshakeHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Slf4j
@Configuration
//@EnableWebSocketMessageBroker注解用于开启使用STOMP协议来传输基于代理（MessageBroker）的消息，这时候控制器（controller）
//开始支持@MessageMapping,就像是使用@requestMapping一样。
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer{
    @Autowired
    private MyPrincipalHandshakeHandler myDefaultHandshakeHandler;
    @Autowired
    private AuthHandshakeInterceptor sessionAuthHandshakeInterceptor;
	@Override
	public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        //注册一个Stomp的节点（endpoint）,并指定使用SockJS协议。
        stompEndpointRegistry.addEndpoint(SipConstants.WEBSOCKET_PATH)
                .addInterceptors(sessionAuthHandshakeInterceptor)
                .setHandshakeHandler(myDefaultHandshakeHandler).setAllowedOrigins("*")
                .withSockJS();
    }

	@Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //路径前缀 + (用户名) + destination(前缀+后缀)
        //配置destination前缀
        registry.enableSimpleBroker(SipConstants.WEBSOCKET_BROADCAST_PATH, SipConstants.WEBSOCKET_P2P_PATH);
        //定义一对一时用户订阅的路径前缀（只能一个）
        registry.setUserDestinationPrefix(SipConstants.WEBSOCKET_P2P_PATH_PERFIX);
        //定义服务端订阅消息的路径前缀，客户端将消息发到此代理
        registry.setApplicationDestinationPrefixes(SipConstants.WEBSOCKET_PATH_PERFIX);
    }
/**
    //消息传输参数配置
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(8192) //设置消息字节数大小
                .setSendBufferSizeLimit(8192)//设置消息缓存大小
                .setSendTimeLimit(10000); //设置消息发送时间限制毫秒
    }
    */
    //输入通道参数设置--客户端ws连接、发送ws业务请求都生效
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        /*
        registration.taskExecutor().corePoolSize(4) //设置消息输入通道的线程池线程数
                .maxPoolSize(8)//最大线程数
                .keepAliveSeconds(60);//线程活动时间
                */
        //registration.interceptors(presenceChannelInterceptor());
        registration.interceptors(new ChannelInterceptorAdapter() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand()) || StompCommand.SEND.equals(accessor.getCommand())) {
                    String token = accessor.getFirstNativeHeader("token");
                    try {
                        log.info("configureClientInboundChannel --------token={}",token);
                    } catch (Exception e) {
                        log.error(e.toString());
                        return null;
                    }
                }
                return message;
            }
        });
    }

/**
    //输出通道参数设置
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(4).maxPoolSize(8);
        registration.setInterceptors(presenceChannelInterceptor());
    }
*/

}
