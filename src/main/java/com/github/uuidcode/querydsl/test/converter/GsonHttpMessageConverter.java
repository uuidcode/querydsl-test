package com.github.uuidcode.querydsl.test.converter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.github.uuidcode.querydsl.test.adapter.DateTypeAdapter;
import com.github.uuidcode.querydsl.test.adapter.LongTypeAdapter;
import com.github.uuidcode.querydsl.test.adapter.StringTypeAdapter;
import com.github.uuidcode.querydsl.test.util.CoreUtil;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHttpMessageConverter extends AbstractHttpMessageConverter<Object> {
    public static final String UTF_8 = "UTF-8";

    public GsonHttpMessageConverter() {
        super(new MediaType("application", "json", Charset.forName(UTF_8)));
    }

    @Override
    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage)
        throws IOException, HttpMessageNotReadableException {
        return gson.fromJson(IOUtils.toString(inputMessage.getBody(), Charset.defaultCharset()), clazz);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    public static Gson gson = createGsonBuilder().create();

    public static GsonBuilder createGsonBuilder() {
        return new GsonBuilder()
            .registerTypeAdapter(long.class, new LongTypeAdapter())
            .registerTypeAdapter(Long.class, new LongTypeAdapter())
            .registerTypeAdapter(String.class, new StringTypeAdapter())
            .registerTypeAdapter(Date.class, new DateTypeAdapter())
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .addSerializationExclusionStrategy(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                    if (fieldAttributes.getDeclaredClass().equals(Class.class)) {
                        return true;
                    }

                    return false;
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            });
    }

    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        return gson.fromJson(jsonString, clazz);
    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage)
        throws IOException, HttpMessageNotWritableException {
        ServletRequestAttributes requestAttributes =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String json = this.gson.toJson(object);
        boolean hasCallback = false;
        String callback = null;

        if (request != null) {
            callback = request.getParameter("callback");
            if (CoreUtil.isNotEmpty(callback)) {
                hasCallback = true;
            }
        }

        OutputStream out = outputMessage.getBody();

        if (hasCallback) {
            out.write(callback.getBytes());
            out.write("(".getBytes());
        }

        out.write(json.getBytes(UTF_8));

        if (hasCallback) {
            out.write(");".getBytes());
        }

        out.flush();
        out.close();
    }
}