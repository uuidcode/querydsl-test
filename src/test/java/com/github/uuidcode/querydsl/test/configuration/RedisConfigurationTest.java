package com.github.uuidcode.querydsl.test.configuration;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.github.uuidcode.querydsl.test.CoreTest;
import com.github.uuidcode.querydsl.test.util.CoreUtil;

public class RedisConfigurationTest extends CoreTest {
    protected static Logger logger = LoggerFactory.getLogger(RedisConfigurationTest.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("Integer", 1);
        map.put("String", "hello");
        map.put("Date", new Date());

        map.forEach((key, value) -> {
            this.redisTemplate.opsForValue().set(key, value);
            Object object = this.redisTemplate.opsForValue().get(key);

            if (logger.isDebugEnabled()) {
                logger.debug(">>> test object: {}", CoreUtil.toJson(object));
            }
        });

        Set keys = this.redisTemplate.keys("*");

        if (logger.isDebugEnabled()) {
            logger.debug(">>> test keys: {}", CoreUtil.toJson(keys));
        }
    }
}