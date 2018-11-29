package com.hdvon.nmp.config.redis;



import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.parser.ParserConfig;

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
    @Bean
    public RedisSerializer fastJson2JsonRedisSerializer() {     
    	ParserConfig.getGlobalInstance().setAutoTypeSupport(true); 
        return new FastJson2JsonRedisSerializer<Object>(Object.class);
    }
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

        //hash  使用jdk  的序列化
        redisTemplate.setHashValueSerializer(fastJson2JsonRedisSerializer());
        //StringRedisSerializer  key  序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //keySerializer  对key的默认序列化器。默认值是StringSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //  valueSerializer
        redisTemplate.setValueSerializer(fastJson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
	
	/**
     * cacheManager 配置redis缓存策略
     * @param redisConnectionFactory
     * @return CacheManager
     */
	@SuppressWarnings("rawtypes")
	@Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		log.info("=========================== Redis cache manager starts ========================");
        RedisCacheManager redisCacheManager = new RedisCacheManager(this.redisTemplate(redisConnectionFactory));
        return redisCacheManager;
    }
}
