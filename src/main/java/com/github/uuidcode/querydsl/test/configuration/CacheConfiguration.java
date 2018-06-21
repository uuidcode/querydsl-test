package com.github.uuidcode.querydsl.test.configuration;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.jcache.config.JCacheConfigurerSupport;
import org.springframework.context.annotation.Configuration;

import com.github.uuidcode.querydsl.test.generator.JSonCacheKeyGenerator;

@Configuration
public class CacheConfiguration extends JCacheConfigurerSupport {
    @Override
    public KeyGenerator keyGenerator() {
        return new JSonCacheKeyGenerator();
    }

}
