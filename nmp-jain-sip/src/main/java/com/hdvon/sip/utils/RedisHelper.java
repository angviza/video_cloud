package com.hdvon.sip.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.hdvon.nmp.common.SipConstants;
import com.hdvon.sip.entity.UserVo;
import com.hdvon.sip.exception.UserTimeoutException;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
            throw new UserTimeoutException("tokenId无效");
        }
        return SipConstants.REDIS_ONLINE_USER_PERFIX + "_" + userId + "_" + token;
    }
    /**
     * 从redis获取登录用户信息
     * @param token
     * @return
     */
    public UserVo getUserByToken(String token){
        String tokenKey = getFullToken(token);
        String value = (String) redisTemplate.opsForValue().get(tokenKey);
        if(StringUtils.isEmpty(value)) {
        	return null;
        }
        UserVo uservo = JSON.parseObject(value, UserVo.class);
//      log.info(">>>>>>>>>>>>>>>>>>>>>>>>>tokenKey:{}",JSON.toJSONString(uservo));
        return uservo;
    }


 
}
