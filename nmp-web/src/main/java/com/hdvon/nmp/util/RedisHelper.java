package com.hdvon.nmp.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.hdvon.nmp.common.WebConstant;
import com.hdvon.nmp.exception.UserTimeoutException;
import com.hdvon.nmp.vo.UserVo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

@Component
public class RedisHelper {

    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * 生成带前缀的token，组合方式：USER_{userId}_{tokenId}
     * @param token
     * @return
     */
    public static String getFullToken(String token){
        Object userId = null;
        try {
            Claims claims = JwtUtil.parseToken(token);
            userId = claims.get("userId");
            if(userId == null){
                throw new UserTimeoutException("tokenId无效");
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new UserTimeoutException("tokenId无效");
        }
        return WebConstant.REDIS_ONLINE_USER_PERFIX + "_" + userId + "_" + token;
    }

    /**
     * 保存登录用户到redis缓存
     * @param token
     * @param userVo
     */
    public void setUserByToken(String token , UserVo userVo){
        String tokenKey = getFullToken(token);
        String userVoStr = JSONObject.toJSONString(userVo);
        redisTemplate.opsForValue().set(tokenKey,userVoStr);
        redisTemplate.expire(tokenKey,WebConstant.TOKEN_EXPIRE_SECONDS, TimeUnit.SECONDS);
    }


    /**
     * 从redis获取登录用户信息
     * @param token
     * @return
     */
    public UserVo getUserByToken(String token){
        String tokenKey = getFullToken(token);
        //UserVo uservo = (UserVo) redisTemplate.opsForValue().get(tokenKey);
        String userVoStr = (String)redisTemplate.opsForValue().get(tokenKey);
        UserVo uservo = JSONObject.parseObject(userVoStr,UserVo.class);
        return uservo;
    }


    /**
     * 获取在线用户id列表
     * @return
     */
    public List<String> getOnlineUser(){
        List<String> userIds = new ArrayList<>();
        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
        ScanOptions options = ScanOptions.scanOptions().match(WebConstant.REDIS_ONLINE_USER_PERFIX +"*").count(Integer.MAX_VALUE).build();
        Cursor c = redisConnection.scan(options);
        while (c.hasNext()) {
            String key = new String((byte[]) c.next());
            if(key.indexOf("_") != -1){
                int index1 = key.indexOf("_") + 1;
                int index2 = key.indexOf("_",index1);
                String userId = key.substring(index1 ,index2);
                userIds.add(userId);
            }

        }
        redisConnection.close();
        return userIds;
    }

    /**
     * 获取在线摄像机列表
     * @return
     */
    public List<String> getOnlineCamera(){
        List<String> sbbmIds = new ArrayList<>();
        Map<String,Map<String,Object>> onlineMap= new HashMap<String,Map<String,Object>>();
        String onlineCamera = WebConstant.REDIS_ONLINE_CAMERA_PERFIX;
        if(redisTemplate.hasKey(onlineCamera)) {
	    	 onlineMap= redisTemplate.boundHashOps(onlineCamera).entries(); 
	    }
        if(! onlineMap.isEmpty()) {
        	for (Entry<String, Map<String,Object>> entry : onlineMap.entrySet()) {
        		Map<String, Object> mapValue= entry.getValue();
        		for (Entry<String, Object> value : mapValue.entrySet()) {
        			sbbmIds.add((String)value.getValue());
    		     }
		     }
        	
        }  
        
//        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
//        ScanOptions options = ScanOptions.scanOptions().match(WebConstant.INVITE_CALLID +"*").count(Integer.MAX_VALUE).build();
//        Cursor c = redisConnection.scan(options);
//        while (c.hasNext()) {
//            String key = new String((byte[]) c.next());
//            if(key.indexOf("_") != -1){
//                int index1 = key.indexOf("_") + 1;
//                int index2 = key.indexOf("_",index1);
//                String camerId = key.substring(index1 ,index2);
//                camerIds.add(key);
//            }
//        }
//        
//        redisConnection.close();
        return sbbmIds;
    }
    
    /**
     * 获取在线摄像机关联的用户列表
     * @param sbbm 设备编码
     * @return
     */
    public List<String> getOnlineUserByCamera(String sbbm){
        List<String> userIds = new ArrayList<>();
        Map<String,Object> onlineMap= new HashMap<String,Object>();
        String onlineCamera = WebConstant.REDIS_ONLINE_CAMERA_PERFIX;
        if(redisTemplate.hasKey(onlineCamera)) {
	    	 onlineMap= redisTemplate.boundHashOps(onlineCamera).entries(); 
	    }
        if(! onlineMap.isEmpty()) {
        	for (Entry<String, Object> entry : onlineMap.entrySet()) {
        		userIds.add(entry.getKey());
		     }
        }  
        
//        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
//        ScanOptions options = ScanOptions.scanOptions().match(WebConstant.REDIS_ONLINE_CAMERA_PERFIX+ "_" + sbbm + "_" +"*").count(Integer.MAX_VALUE).build();
//        Cursor c = redisConnection.scan(options);
//        while (c.hasNext()) {
//            String key = new String((byte[]) c.next());
//            if(key.indexOf("_") != -1){
//                int index1 = key.indexOf("_") + 1;
//                int index2 = key.indexOf("_",index1) + 1;
//                String userId = key.substring(index2);
//                userIds.add(userId);
//            }
//        }
//        redisConnection.close();
        return userIds;
    }

    /**
     * 根据userId获取在线用户的token列表
     * @return
     */
    public List<String> getOnlineTokenByUserId(List<String> userIds){
        ArrayList<String> tokenids = new ArrayList<>();
        for (int i = 0; i < userIds.size(); i++) {
            String userId = userIds.get(i);
            tokenids.addAll(getTokenByUserId(userId));
        }
        return tokenids;
    }

    /**
     * 设置用户失效
     * @param userIds
     * @param minutes
     * @return
     */
    public void setUserTimeout(List<String> userIds , Integer minutes){
        if(minutes == null){
            minutes = 0;
        }
        for (int j = 0; j < userIds.size(); j++) {
        	if(StrUtil.isNotBlank(userIds.get(j))) {
        		List<String> tokenIdList = getOnlineTokenByUserId(userIds);
    	        for (int i = 0; i < tokenIdList.size(); i++) {
    	            String key = WebConstant.REDIS_ONLINE_USER_PERFIX+"_"+userIds.get(j)+"_"+tokenIdList.get(i);
    	            redisTemplate.expire(key , minutes , TimeUnit.MINUTES);
    	      }
    	        // 用户在线列表一直强制下线的用户
    	        if(redisTemplate.hasKey(WebConstant.USER_LOGOUT_TOKEN)) {
    	        	List<String> listToken=(List<String>) redisTemplate.opsForValue().get(WebConstant.USER_LOGOUT_TOKEN);
    	        	tokenIdList.addAll(listToken);
    	        }
    	        redisTemplate.opsForValue().set(WebConstant.USER_LOGOUT_TOKEN, tokenIdList);
    	        redisTemplate.expire(WebConstant.USER_LOGOUT_TOKEN, WebConstant.USER_LOGOUT_TIME, TimeUnit.SECONDS);
    	        
        	}
	    	
        }
       
        //记录哪些用户设置了超时
        for (int i = 0; i < userIds.size(); i++) {
            String userid = userIds.get(i);
            String key = WebConstant.REDIS_TIMEOUT_USER_PERFIX + "_" + userid;
            redisTemplate.opsForValue().set(key , TimeUnit.MINUTES);
            redisTemplate.expire(key , minutes , TimeUnit.MINUTES);
        }
    }

    /**
     * 限制用户登录时长
     * @param userId
     * @param minutes
     * @return
     */
    public void setUserLimit(List<String> userId , Integer minutes){
        if(minutes == null){
            minutes = 0;
        }
        for (int i = 0; i < userId.size(); i++) {
            String key = WebConstant.REDIS_LIMIT_USER_PERFIX +"_"+userId.get(i);
            redisTemplate.opsForValue().set(key , minutes);
            redisTemplate.expire(key , minutes , TimeUnit.MINUTES);
        }

    }

    /**
     * 检查用户是否被限制登录REDIS_LIMIT_PERFIX
     * @param userId
     * @return
     */
    public Integer checkUserLimit(String userId){
        String key = WebConstant.REDIS_LIMIT_USER_PERFIX +"_"+userId;
        Integer minutes = (Integer) redisTemplate.opsForValue().get(key);
        return minutes;
    }

    /**
     * 检查用户是否被强制下线REDIS_USERTIMEOUT_PERFIX
     * @param userId
     * @return
     */
    public Long checkUserTimeout(String userId){
        String key = WebConstant.REDIS_TIMEOUT_USER_PERFIX +"_"+userId;
        Long expire = redisTemplate.getExpire(key,TimeUnit.MINUTES);
        return expire;
    }

    /**
     * 根据用户获取tokenid
     * @param curUserId
     * @return
     */
    public List<String> getTokenByUserId(String curUserId){
        List<String> userIds = new ArrayList<>();
        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
        ScanOptions options = ScanOptions.scanOptions().match(WebConstant.REDIS_ONLINE_USER_PERFIX + "_" + curUserId + "_" +"*").count(Integer.MAX_VALUE).build();
        Cursor c = redisConnection.scan(options);
        while (c.hasNext()) {
            String key = new String((byte[]) c.next());
            if(key.indexOf("_") != -1){
                int index1 = key.indexOf("_") + 1;
                int index2 = key.indexOf("_",index1) + 1;
                String tokenId = key.substring(index2);
                userIds.add(tokenId);
            }
        }
        redisConnection.close();
        return userIds;
    }
    
    /**
     * 判断用户是否锁屏
     * @param token
     * @return
     */
    public boolean existLockScerrn(String userId,String token){
    	boolean flag = false;
    	String key = WebConstant.LOCK_SCREEN_KEY+userId;
    	//判断是否存在锁屏
    	if(redisTemplate.hasKey(key)) {
    		String tokenVal = (String) redisTemplate.boundValueOps(key).get();
    		//token一直表示该用户已锁屏，必须解锁
    		if(token.equals(tokenVal)) {
    			return true;
    		}
    	}
    	return flag;
    }

    /**
     * 缓存用户最近登录的token
     * @param
     * @return
     */
    public void setCurrentToken(String userId , String tokenId){
        redisTemplate.opsForValue().set(userId,tokenId);
        redisTemplate.expire(userId,WebConstant.TOKEN_EXPIRE_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * 获取用户最近登录的token
     * @param
     * @return
     */
    public String getCurrentToken(String userId ){
       return (String)redisTemplate.opsForValue().get(userId);
    }
}
