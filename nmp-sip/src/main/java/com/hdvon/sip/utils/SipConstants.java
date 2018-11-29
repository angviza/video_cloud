package com.hdvon.sip.utils;

import com.hdvon.sip.enums.MediaOperationType;

/**
 * sip常量池
 * @author wanshaojian
 *
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
	
	//开始录像标志
	public static final int TAPE_TYPE_START = 1;
	//停止录像标志
	public static final int TAPE_TYPE_STOP  = 2;
	/**
	 * 生成视频的rediskey
	 * @param em 请求媒体流的操作类型
	 * @param callId 会话id
	 * @return
	 */
	public static String getMediaRedisKey(MediaOperationType em,String callId) {
		String prefix = null;
		if(MediaOperationType.PLAYBACK.equals(em)) {
			prefix = REDIS_MEDIA_PLAYBACK_PREFIX;
		}else if(MediaOperationType.DOWNLAOD.equals(em)) {
			prefix = REDIS_MEDIA_DOWNLOAD_PREFIX;
		}else {
			prefix = REDIS_MEDIA_PLAY_PREFIX;
		}
		return prefix + SPLIT_CHAR + callId;
	}
}
