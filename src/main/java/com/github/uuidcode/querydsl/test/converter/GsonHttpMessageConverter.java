package com.github.uuidcode.querydsl.test.converter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.github.uuidcode.querydsl.test.util.CoreUtil;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class GsonHttpMessageConverter extends AbstractHttpMessageConverter<Object> {
    public static final String UTF_8 = "UTF-8";
    private Gson gson = null;

    public GsonHttpMessageConverter() {
        super(new MediaType("application", "json", Charset.forName(UTF_8)));
        this.gson = getDefaultGsonBuilder().create();
    }

    @Override
    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage)
        throws IOException, HttpMessageNotReadableException {
        return this.gson.fromJson(IOUtils.toString(inputMessage.getBody(), Charset.defaultCharset()), clazz);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    public static GsonBuilder getDefaultGsonBuilder() {
        return new GsonBuilder()
            .registerTypeAdapter(long.class, LongTypeAdapter)
            .registerTypeAdapter(Long.class, LongTypeAdapter)
            .registerTypeAdapter(String.class, StringTypeAdapter)
            .registerTypeAdapter(Date.class, DateTypeAdapter)
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
        return getDefaultGsonBuilder().create().fromJson(jsonString, clazz);
    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage)
        throws IOException, HttpMessageNotWritableException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
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

    private static TypeAdapter<Long> LongTypeAdapter = new TypeAdapter<Long>() {
        @Override
        public void write(JsonWriter out, Long value) throws IOException {
            out.value(value);
        }

        @Override
        public Long read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                String result = in.nextString();
                try {
                    return Long.parseLong(result.replaceAll(",", ""), 10);
                } catch (Exception e) {
                    return null;
                }
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }
    };

    private static TypeAdapter<String> StringTypeAdapter = new TypeAdapter<String>() {
        @Override
        public void write(JsonWriter out, String value) throws IOException {
            out.value(value);
        }

        @Override
        public String read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            try {
                String result = in.nextString();

                if (result == null || result.trim().length() == 0) {
                    return null;
                }

                return result.trim();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }
    };

    private static TypeAdapter<Date> DateTypeAdapter = new TypeAdapter<Date>() {
        @Override
        public void write(JsonWriter out, Date value) throws IOException {
            String date = null;
            try {
                FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
                date = format.format(value);
            } catch (Exception e) {
            }
            out.value(date);
        }

        @Override
        public Date read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            String result = in.nextString();
            try {
                FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
                return format.parse(result);
            } catch (Exception e) {
                try {
                    FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
                    return format.parse(result);
                } catch (Exception ex) {
                }
            }

            return null;
        }
    };
}