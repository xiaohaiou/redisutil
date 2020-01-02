package com.lc.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;
import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig {

    @Autowired
    private LettuceConnectionFactory lettuceConnectionFactoryPrivate;

    private Logger logger=LoggerFactory.getLogger(RedisConfig.class);

    //过期时间1天
    private Duration timeToLive = Duration.ofDays(1);

    @Bean
    public RedisCacheManager cacheManager() {

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(this.timeToLive)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
                .disableCachingNullValues();
        Map<String,RedisCacheConfiguration> cacheConfigurationMap=new HashMap<>();
        cacheConfigurationMap.put("users",config);
        cacheConfigurationMap.put("default",config);
        RedisCacheManager redisCacheManager = RedisCacheManager.builder(lettuceConnectionFactoryPrivate)
                .cacheDefaults(config)
                .transactionAware()
                .withInitialCacheConfigurations(cacheConfigurationMap)
                .build();
        logger.debug("自定义RedisCacheManager加载完成");
        return redisCacheManager;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String,Object> redisTemplate(){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactoryPrivate);
        redisTemplate.setKeySerializer(keySerializer());
        redisTemplate.setHashKeySerializer(keySerializer());
        redisTemplate.setValueSerializer(valueSerializer());
        redisTemplate.setHashValueSerializer(valueSerializer());
        logger.debug("自定义RedisTemplate加载完成");
        return redisTemplate;
    }
    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }
    private RedisSerializer<Object> valueSerializer() {
        FastJson2JsonRedisSerializer fastJson2JsonRedisSerializer = new FastJson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        fastJson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return fastJson2JsonRedisSerializer;
    }

    @Bean
    public KeyGenerator myKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }
}

