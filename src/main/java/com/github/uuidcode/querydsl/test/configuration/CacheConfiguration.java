package com.github.uuidcode.querydsl.test.configuration;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.config.JCacheConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.github.uuidcode.querydsl.test.serializer.JsonSerializer;

import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;

@EnableCaching
@Configuration
public class CacheConfiguration extends JCacheConfigurerSupport {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public CacheManager cacheManager() {
        JsonSerializer jsonSerializer = new JsonSerializer();

        RedisCacheConfiguration cacheConfiguration = defaultCacheConfig()
            .serializeValuesWith(jsonSerializer)
            .entryTtl(Duration.ofMinutes(20));

        return RedisCacheManager.builder(this.redisConnectionFactory)
            .cacheDefaults(cacheConfiguration)
            .build();
    }
}
