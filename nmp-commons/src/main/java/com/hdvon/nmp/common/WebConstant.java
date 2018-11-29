package com.hdvon.nmp.common;

import cn.hutool.crypto.digest.DigestUtil;

/**
 * 应用配置
 */
public class WebConstant {
    /**
     * 盐值
     */
    public static final String MD5_SALT = "!@#!@#%%@#R#HD";

    /**
     * TOKEN_HEADER
     */
    public static final String TOKEN_HEADER = "token";

    /**
     * Token超时时间，单位：秒
     */
    public static final long TOKEN_EXPIRE_SECONDS = 60 * 60 * 24;//60 * 60 * 3;

    /**
     * redis用户前缀，格式：OnlineUser_${userId}_${tokenId}
     * **/
    public static final String REDIS_ONLINE_USER_PERFIX = "OnlineUser";

    /**
     * redis在线摄像机(正在播放的摄像机)，格式：OnlineCamera
     * **/
    public static final String REDIS_ONLINE_CAMERA_PERFIX = "OnlineCamera";

    /**
     * redis禁止登录用户的前缀，格式：LimitUser_${userId}
     * **/
    public static final String REDIS_LIMIT_USER_PERFIX = "LimitUser";

    /**
     * redis禁止登录用户的前缀，格式：TimeOutUser_${userId}
     * **/
    public static final String REDIS_TIMEOUT_USER_PERFIX = "TimeOutUser";

    /**
     * Redis全局默认的过期时间，单位：秒
     */
    public static final long REDIS_EXPIRE_SECONDS = 60 * 60 * 24;

    /**
     * redis部门key
     * **/
    //public static final String REDIS_DEPT_KEY = "deptMapkey";

    /**
     * redis非虚拟组织(行政区域)key
     * **/
    //public static final String REDIS_ORG_KEY = "orgMapkey";
    
    /**
     * redis地址key
     * **/
    //public static final String REDIS_ADDRESS_KEY = "addressMapkey";

    /**
     * redis虚拟组织key
     * **/
    //public static final String REDIS_VIRTUAL_ORG_KEY = "virtualOrgMapkey";
    
    /**
     * redis部门项目分组key
     * **/
    //public static final String REDIS_DEPT_PROJECT_KEY = "deptProjectMapkey";

    /**
     * 部门、虚拟组织、行政区域key的过期时间，单位：秒
     */
    //public static final long KEY_EXPIRE_SECONDS = 60 * 60 * 12L;
    
    /**
     * 设备注册key的过期时间，单位：秒；默认为一天
     */
    public static final long DEVICE_EXPIRE_SECONDS = 60 * 60 * 12L;
    //public static final long DEVICE_EXPIRE_SECONDS = 100;
  
    
    /**
     * 注册监听map的过期时间，单位：秒；默认为10分钟
     */
    public static final long REDI_EXPIRE_SECONDS = 60 * 10 ;
    /**
     * invite会话key的过期时间，单位：秒
     */
    public static final long INVITE_EXPIRE_SECONDS = 60 * 60 * 2;
    
    /**
     * REDIS分布式锁的key
     */
    public static final String REDIS_LOCK_KEY = "REDIS_LOCK";
    
    /**
     * 默认的锁超时时间，防止线程在入锁以后，无限的执行等待
     */
    public static final int EXPIRE_MSECS = 60 * 60 * 2;
    
    /**
     * 默认的锁等待时间，防止线程饥饿
     */
    public static final int TIMEOUT_MSECS = 10 * 1000;
    
    public static final int DEFAULT_ACQUIRY_RESOLUTION_MILLIS = 100;
    
    /**
     * 权限级别的key
     */
//    public static final String PERMISSION_LEVEL_KEY = "PERMISSION_LEVEL_KEY";
    
    /**
     * 用户编号的key
     */
//    public static final String USER_NO_KEY = "USER_NO_KEY";
    
    /**
     * 消息提示的key
     */
    public static final String ALERT_MSG_KEY = "ALERT_MSG_KEY";
    
	/**
	 * 上墙轮巡的消息队列名。
	 * @see RotateMessageVo
	 */
    public static final String BATCH_QUEUE_ROTATE = "BATCH_QUEUE_ROTATE";
    
    /**
     * 注册异步回调的过期时间，默认为45秒
     */
    public static final int REGISTER_CALLBACK_MSECS = 45;
    
    /**
     * 注册接口同步调用的过期时间，默认为5秒
     */
    public static final int SYNC_REGISTER_MSECS = 10;
    
    /**
     * 点播异步回调的过期时间，默认为20秒
     */
    public static final int INVITE_CALLBACK_MSECS = 20;
    
    
    /**
     * 录像回放/下载结束过期时间 默认十分钟
     */
    public static final int FILE_CALLBACK_MSECS = 60*10;
    /**
     * 点播接口同步调用的过期时间，默认为5秒
     */
    public static final int SYNC_INVITE_MSECS = 10;
    
    /**
     * 云台控制异步回调的过期时间，默认为20秒
     */
    public static final int CNTRL_CALLBACK_MSECS = 20;
    
    /**
     * 云台控制同步调用的过期时间，默认为5秒
     */
    public static final int SYNC_CNTRL_MSECS = 10;
    
    /**
     *回放控制同步调用的过期时间，默认为5秒
     */
    public static final int SYNC_PLAYBACK_MSECS = 10;
    
    /**
     *预置位控制同步调用的过期时间，默认为5秒
     */
    public static final int SYNC_PRESET_MSECS = 10;
    
    /**
     *巡航预案控制同步调用的过期时间，默认为8秒
     */
    public static final int SYNC_CRUISE_MSECS = 10;
    
    /**
     * 录像查询
     */
    public static final int SYNC_REVE_MSECS = 12;
    
    /**
     * 重试接口的过期时间，默认为5秒
     */
    public static final int RETRY_CALLID_MSECS = 5;
    
    /**
     * 信令服务器默认的端口号5060
     */
    public static final int SIP_PORT = 5060;
    
    /**
     * 等待异步回调结果的等待时间，单位为毫秒
     */
    public static final int WAIT_ASNC_MSECS = 800;
    
    /**
     * 默认生成提供给客户端插件的端口数量为100个
     */
    public static final int PLUGIN_PORT_NUMBER = 100;
    
    /**
     * 默认统计报表的天数：30
     */
    public static final int REPORT_DAYS = 30;
    
    /**
     * 默认统计的摄像机数：50
     */
    public static final int CAMERA_NUM = 50;
    
    /**
     * 默认统计的摄像机组数：50
     */
    public static final int CAMERA_GROUP_NUM = 50;
    
    /**
     * 更新用户行为日志时间 5分钟
     */
    public static final long ASALYSIS_SECONDS = 60 * 5;
    
    /**
     * 用户登录
     */
    public static final String USER_CONTORL_5 = "5";
    
    /**
     * 用户注销
     */
    public static final String USER_CONTORL_6 = "6";
    
    /**
     * 查看
     */
    public static final String USER_CONTORL_7 = "7";
    /**
     * 数据更新
     */
    public static final String USER_CONTORL_8 = "8";
   
    /**
     * 数据删除
     */
    public static final String USER_CONTORL_9 = "9";
    

    /**
     * 生成包含盐值md5密码
     * @param password
     * @return
     */
    public static String getPasswordByMd5(String password){
        return DigestUtil.md5Hex(password.trim() + MD5_SALT);
    }

    
    /**
     * 锁屏在redis中的前缀字符
     */
    public static final String LOCK_SCREEN_KEY = "LOCK_SCREEN_";
    
    /**
     * 用户播放key=invite_callid_${tokenid}
     */
    public static final String INVITE_CALLID = "invite_callid_";
    
    /**
     * 视频点播返回callID 最大存储时间
     */
    public static final long CALLID_EXPIRE_SECONDS = 60 * 60 * 12L;

    /**
     * websocket订阅路径
     */
    public static final String WEB_SC_TOPIC_NOTIFY = "/topic/notify";
   
    /**
     * 系统水印标识
     */
    public static final String WEB_SYS_WATE = "SHUIYIN";
    
    /**
     * 系统参数字典标识
     */
    public static final String WEB_SYS_CONFIG = "sysconfig";
    
    /**
     * 用户强制退出token
     */
    public static final String USER_LOGOUT_TOKEN= "user_logout_token";
    
    /**
     * 用户强制退出token失效
     */
    public static final long USER_LOGOUT_TIME = 30*10;
    
    /**
     * 系统功能菜单key
     */
    public static final String SYSMENU_METHOD ="sysmenu_method";
    
    /**
     * 系统功能菜单有效时长
     */
    public static final long KEY_SYSMENU_METHOD = 60 * 60 * 12L;
    
    /**
     * 字段逻辑删除 -已删除
     */
    public static final String FIELD_DETELEFLAG_YES="0";
    
    /**
     * 字段逻辑删除-正常
     */
    public static final String FIELD_DETELEFLAG_NO="1";
    
    
    
}
