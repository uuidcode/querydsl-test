package com.github.uuidcode.querydsl.test.generator;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;

import com.github.uuidcode.querydsl.test.util.CoreUtil;

public class JSonCacheKeyGenerator implements KeyGenerator {
    protected static Logger logger = LoggerFactory.getLogger(JSonCacheKeyGenerator.class);

    @Override
    public Object generate(Object target, Method method, Object... objects) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("declaringClass", target.getClass().getName());
        map.put("returnType", method.getReturnType().getName());
        map.put("methodName", method.getName());
        map.put("arguments", objects);
        String cacheKey = CoreUtil.toJson(map);

        if (logger.isDebugEnabled()) {
            logger.debug(">>> generate cacheKey: {}", cacheKey);
        }

        return cacheKey;
    }
}