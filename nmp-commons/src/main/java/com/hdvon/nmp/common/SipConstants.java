package com.hdvon.nmp.common;

import com.hdvon.nmp.enums.MediaOperationType;

/**
 * sip常量池
 * @author wanshaojian
 * @since 2018-10-15
 */
public class SipConstants {
	//sip协议交换方式
	public static final String TRANSPORT_UDP = "udp";
	//sip协议交换方式	
	public static final String TRANSPORT_TCP = "tcp";
	//sip默认域名
	public static final String SIP_DOMAIN ="gov.nist";
	
	public static final String STACK_NAME = "hdvon-stack";
	
	//redis前缀
	public static final String REDIS_HEARBEAT_PREFIX = "hearbeat@";
	
	
	//redis注册用户列表名称
	public static final String REDIS_PORT_KYE = "port@";
	
	//redis注册用户列表名称
	public static final String REDIS_USER_KYE = "sipUserList@";
	
	
	//redis视频点播前缀
	public static final String REDIS_MEDIA_PLAY_PREFIX = "mediaplay@";
	
	//redis播放回看视频前缀
	public static final String REDIS_MEDIA_PLAYBACK_PREFIX = "mediaplayback@";
	
	
	//redis视频下载前缀
	public static final String REDIS_MEDIA_DOWNLOAD_PREFIX = "mediaplayback@";
	
	//分隔符
	public static final String SPLIT_CHAR = "@";
	
	//连续发送心跳失败次数
	public static final Integer HEARBEAT_FAIL_SUM = 20;
	
	
	public static final int TIMEOUT_FAIL = 503;
	
	public static final int SUCCESS = 200;
	
	public static final int FAIL = 400;
	
	public static final int USER_NOT_REGISTER = 403;
	
	public static final int NOT_ACCOUNT = 500;
	
	
	//kafka
	public static final String KAFKA_SIP_TOPIC  = "sip_topic_log";

    //webSocket相关配置
    //链接地址
    public static final String WEBSOCKET_PATH = "/sipWebSocket";
    //服务端接收消息代理地址前缀
    public static final String WEBSOCKET_PATH_PERFIX = "/sipApp";
    public static final String WEBSOCKET_PATH_URL = "/operate";

    //消息代理路径
    public static String WEBSOCKET_BROADCAST_PATH = "/topic";
    //服务端生产地址,客户端订阅此地址以接收服务端生产的消息
    public static final String WEBSOCKET_BROADCAST_PATH_MSG = "/topic/message";

    //p2p代理地址前缀
    public static final String WEBSOCKET_P2P_PATH_PERFIX = "/user";
    //p2p消息推送地址前缀
    public static final String WEBSOCKET_P2P_PATH = "/single";
    public static final String WEBSOCKET_P2P_PATH_MSG = "/single/message";


    //成功响应
    public static final String STATE_OK = "OK";
    //临时响应
    public static final String STATE_CONTINUE = "continue";
    //结束
    public static final String 	STATE_TERMINATED = "terminated";
    
    //录像播放结束返回状态值
    public static final String 	NOTIFY_TYPE = "121";
    //是否websocket方式
    public static final boolean IS_WEBSOCKET = true; 
    /**
     * redis用户前缀，格式：OnlineUser_${userId}_${tokenId}
     * **/
    public static final String REDIS_ONLINE_USER_PERFIX = "OnlineUser";
    
    /**
     * 每次产生端口数
     */
	public static final int PROTS_SIZE = 3000;
	/**
	 * 生成视频的rediskey
	 * @param mediaType 请求媒体流的操作类型
	 * @param callId 会话id
	 * @return
	 */
	public static String getMediaRedisKey(String mediaType,String callId) {
		String prefix = null;
		if(MediaOperationType.PLAYBACK.getValue().equals(mediaType)) {
			prefix = REDIS_MEDIA_PLAYBACK_PREFIX;
		}else if(MediaOperationType.DOWNLAOD.getValue().equals(mediaType)) {
			prefix = REDIS_MEDIA_DOWNLOAD_PREFIX;
		}else {
			prefix = REDIS_MEDIA_PLAY_PREFIX;
		}
		return prefix + SPLIT_CHAR + callId;
	}

	/**
	 * 协议枚举
	 * @author wanshaojian
	 *
	 */
	public enum ProtocolEnum {
		/**
		 * 协议，比如gb28181，rtsp，rtmp等
		 */
		GB("gb28181"),RTSP("rtsp"),RTMP("rtmp");
		private ProtocolEnum(String value) {
			this.value = value;
		}
		private String value;

		public static ProtocolEnum getObjectByKey(String value) {
			for(ProtocolEnum em:ProtocolEnum.values()) {
				if(em.getValue().equals(value)) {
					return em;
				}
			};
			return null;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * 协议类型
	 * @author wanshaojian
	 *
	 */
	public enum TransportEnum {
		/**
		 * gb28181协议，UDP, TCP-Active或TCP-Passive
		 * rtsp协议  TCP, UDP
		 */
		GB_UDP("gb28181-UDP","UDP"),GB_TCP_ACTIVE("gb28181-TCP-Active","TCP-Active"),GB_TCP_PASSIVE("gb28181-TCP-Passive","TCP-Passive"),
		RTSP_TCP("rtsp-TCP","TCP"),RTSP_UDP("rtsp-UDP","UDP");
		private TransportEnum(String key,String value) {
			this.key = key;
			this.value = value;
		}
		private String key;
		private String value;

		public static TransportEnum getObjectByKey(String key) {
			for(TransportEnum em:TransportEnum.values()) {
				if(em.getKey().equals(key)) {
					return em;
				}
			};
			return null;
		}
		public String getKey() {
			return key;
		}
		public String getValue() {
			return value;
		}
	}
}
