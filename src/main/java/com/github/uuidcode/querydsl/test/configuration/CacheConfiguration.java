package com.github.uuidcode.querydsl.test.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.jcache.config.JCacheConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import com.github.uuidcode.querydsl.test.generator.JSonCacheKeyGenerator;
import com.github.uuidcode.querydsl.test.serializer.JsonSerializer;

import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;

@EnableCaching
@Configuration
public class CacheConfiguration extends JCacheConfigurerSupport {
    @Override
    public KeyGenerator keyGenerator() {
        return new JSonCacheKeyGenerator();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new JedisConnectionFactory(new RedisStandaloneConfiguration("10.198.20.108"));
    }

    @Bean
    public CacheManager cacheManager() {
        JsonSerializer jsonSerializer = new JsonSerializer();

        RedisCacheConfiguration cacheConfiguration = defaultCacheConfig()
            .serializeValuesWith(jsonSerializer);

        return RedisCacheManager.builder(this.redisConnectionFactory())
            .cacheDefaults(cacheConfiguration)
            .build();
    }
}
