package com.hdvon.nmp.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdvon.nmp.common.WebConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * <br>
 * <b>功能：</b>Redis全局配置类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018/5/17<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
@Slf4j
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport{
	/**
     * redisTemplate 序列化使用的jdkSerializeable, 存储二进制字节码, 所以自定义序列化类
     * @param redisConnectionFactory
     * @return
     */
	@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		log.info("=========================== Redis redisTemplate starts ========================");
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
	
	/**
     * cacheManager 配置redis缓存策略
     * @param redisConnectionFactory
     * @return CacheManager
     */
	@Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		log.info("=========================== Redis cache manager starts ========================");
        RedisCacheManager redisCacheManager = new RedisCacheManager(this.redisTemplate(redisConnectionFactory));
        // 开启使用缓存名称作为key前缀
        redisCacheManager.setUsePrefix(true);
        
        //这里可以设置一个默认的过期时间 单位是秒
        redisCacheManager.setDefaultExpiration(WebConstant.REDIS_EXPIRE_SECONDS);

//        //设置部门key缓存的过期时间
//        Map<String, Long> expires = new HashMap<String, Long>();
//        expires.put(WebConstant.REDIS_DEPT_KEY, WebConstant.KEY_EXPIRE_SECONDS);
//        redisCacheManager.setExpires(expires);
//
//        //设置非虚拟组织(行政区域)key缓存的过期时间
//        expires.put(WebConstant.REDIS_ORG_KEY, WebConstant.KEY_EXPIRE_SECONDS);
//        redisCacheManager.setExpires(expires);
//
//        //设置虚拟组织key缓存的过期时间
//        expires.put(WebConstant.REDIS_VIRTUAL_ORG_KEY, WebConstant.KEY_EXPIRE_SECONDS);
//        redisCacheManager.setExpires(expires);

        return redisCacheManager;
    }
}
