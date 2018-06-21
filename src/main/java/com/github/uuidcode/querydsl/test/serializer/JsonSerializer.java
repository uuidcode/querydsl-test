package com.github.uuidcode.querydsl.test.serializer;

import java.nio.ByteBuffer;

import org.springframework.data.redis.serializer.RedisElementReader;
import org.springframework.data.redis.serializer.RedisElementWriter;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import com.github.uuidcode.querydsl.test.entity.Payload;
import com.github.uuidcode.querydsl.test.util.CoreUtil;

public class JsonSerializer<T> implements SerializationPair<Payload> {
    public static JsonSerializer of() {
        return new JsonSerializer();
    }

    @Override
    public RedisElementReader<Payload> getReader() {
        return byteBuffer -> {
            String json = new String(byteBuffer.array());
            return CoreUtil.fromJson(json, Payload.class);
        };
    }

    @Override
    public RedisElementWriter<Payload> getWriter() {
        return payload -> {
            String json = CoreUtil.toJson(payload);
            return ByteBuffer.wrap(json.getBytes());
        };
    }
}
