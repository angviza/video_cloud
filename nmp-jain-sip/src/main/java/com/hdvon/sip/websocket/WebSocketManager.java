package com.hdvon.sip.websocket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 功能：管理每个连接，不是直接管理WebSocketSession
 * 作者：chenjiefeng
 * 日期：2018/10/19
 * 版权所有：广州弘度信息科技有限公司 版权所有(C) 2018
 */
public class WebSocketManager {

    //保存用户链接
    //private static ConcurrentHashMap<String, WebSocketSession> principalMap = new ConcurrentHashMap<String, WebSocketSession>();
    private static ConcurrentHashMap<String, WsCacheBean> Principal_Map = new ConcurrentHashMap();
    private static ConcurrentHashMap<String, WsCacheBean> No_Active_Map = new ConcurrentHashMap();//未激活ws
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


    public static ConcurrentHashMap<String, WsCacheBean> getNoActiveMap(){
        return No_Active_Map;
    }

    public static ConcurrentHashMap<String, WsCacheBean> getPrincipalMap(){
        return Principal_Map;
    }

    public static boolean put(String principal, WsCacheBean wsCacheBean){
        try {
            lock.writeLock().lock();
            if(lock.isWriteLocked()){
                if(!Principal_Map.containsKey(principal)){
                    Principal_Map.put(principal, wsCacheBean);
                    return true;
                }
            }
        }finally {
            lock.writeLock().unlock();
        }
        return false;
    }

    public static boolean putNoActive(String principal, WsCacheBean wsCacheBean){
        try {
            lock.writeLock().lock();
            if(lock.isWriteLocked()){
                if(!No_Active_Map.containsKey(principal)){
                    No_Active_Map.put(principal, wsCacheBean);
                    return true;
                }
            }
        }finally {
            lock.writeLock().unlock();
        }
        return false;
    }

    public static WsCacheBean get(String principal){
        return Principal_Map.get(principal);
    }

    public static void removePrincipal(String principal){
        Principal_Map.remove(principal);
    }

    public static void removeNoActive(String principal){
        No_Active_Map.remove(principal);
    }

    public static void clear(){
        if(No_Active_Map==null||No_Active_Map.size()==0) return;
        for (String key:No_Active_Map.keySet()){
            WsCacheBean wsCacheBean = No_Active_Map.get(key);
            if(System.currentTimeMillis()-wsCacheBean.getConnectTime()>60){
                WebSocketManager.removeNoActive(key);
                WebSocketManager.removePrincipal(key);
            }
        }
    }

}
