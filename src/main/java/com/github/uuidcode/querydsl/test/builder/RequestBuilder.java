package com.github.uuidcode.querydsl.test.builder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.uuidcode.querydsl.test.converter.GsonHttpMessageConverter;
import com.github.uuidcode.querydsl.test.entity.Payload;
import com.github.uuidcode.querydsl.test.util.CoreUtil;
import com.github.uuidcode.querydsl.test.util.GenericOf;
import com.google.gson.FieldNamingPolicy;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;

public class RequestBuilder {
    public enum AuthorizationScheme {
        KakaoAK, Bearer
    }

    public enum LatencyType {
        HIGH(1_000, 1_000),
        MIDDLE(3_000, 3_000),
        LOW(10_000, 10_000),
        VERY_LOW(30_000, 30_000),
        VERY_VERY_LOW(100_000, 100_000);;

        private Integer connectionTimeout;
        private Integer socketTimeout;

        LatencyType(Integer connectionTimeout, Integer socketTimeout) {
            this.connectionTimeout = connectionTimeout;
            this.socketTimeout = socketTimeout;
        }
    }

    protected static Logger logger = LoggerFactory.getLogger(RequestBuilder.class);
    private Request request;
    private LatencyType latencyType = LatencyType.MIDDLE;
    private FieldNamingPolicy fieldNamePolicy = FieldNamingPolicy.IDENTITY;
    private FieldNamingPolicy resultFieldNamePolicy;
    private List<Header> headerList = new ArrayList<>();

    public static RequestBuilder ofLowerCaseWithUnderscores() {
        return of().setFieldNamePolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
    }

    public static RequestBuilder of() {
        return new RequestBuilder();
    }

    public static RequestBuilder ofLowLatency() {
        return new RequestBuilder().setLatencyType(LatencyType.LOW);
    }

    public List<Header> getHeaderList() {
        return this.headerList;
    }

    public RequestBuilder setHeaderList(List<Header> headerList) {
        this.headerList = headerList;
        return this;
    }

    public FieldNamingPolicy getFieldNamePolicy() {
        return this.fieldNamePolicy;
    }

    public RequestBuilder setFieldNamePolicy(FieldNamingPolicy fieldNamePolicy) {
        this.fieldNamePolicy = fieldNamePolicy;
        return this;
    }

    public FieldNamingPolicy getResultFieldNamePolicy() {
        return this.resultFieldNamePolicy;
    }

    public RequestBuilder setResultFieldNamePolicy(FieldNamingPolicy resultFieldNamePolicy) {
        this.resultFieldNamePolicy = resultFieldNamePolicy;
        return this;
    }

    public RequestBuilder get(String url) {
        this.request = Request.Get(url);
        return this;
    }

    public RequestBuilder get(String url, Object object) {
        if (logger.isDebugEnabled()) {
            logger.debug(">>> get object: {}", CoreUtil.toJson(object));
        }

        return this.get(url, CoreUtil.toQueryString(this.fieldNamePolicy, object));
    }

    public RequestBuilder get(String url, String queryString) {
        this.request = Request.Get(url + "?" + queryString);
        return this;
    }

    public RequestBuilder post(String url) {
        this.request = Request.Post(url);
        return this;
    }

    public RequestBuilder put(String url) {
        this.request = Request.Put(url);
        return this;
    }

    public RequestBuilder delete(String url) {
        this.request = Request.Delete(url);
        return this;
    }

    public RequestBuilder setLatencyType(LatencyType latencyType) {
        this.latencyType = latencyType;
        return this;
    }

    public RequestBuilder addHeader(Header header) {
        this.headerList.add(header);
        return this;
    }

    public RequestBuilder addHeader(String name, String value) {
        return this.addHeader(new BasicHeader(name, value));
    }

    public RequestBuilder authorization(AuthorizationScheme authorizationScheme, String credentials) {
        return this.addHeader("Authorization", authorizationScheme.name() + " " + credentials);
    }

    public RequestBuilder addHeader(List<Header> headerList) {
        Optional.of(headerList)
            .ifPresent(list -> list.forEach(this::addHeader));
        return this;
    }

    public RequestBuilder body(Object object) {
        List<NameValuePair> nameValuePairList = CoreUtil.toNameValuePairList(this.fieldNamePolicy, object);
        this.request = this.request.bodyForm(nameValuePairList, Consts.UTF_8);
        return this;
    }

    public RequestBuilder body(HttpEntity entity) {
        this.request = this.request.body(entity);
        return this;
    }

    public RequestBuilder body(String fileName, InputStream inputStream) {
        try {
            String urlEncodedFileName = CoreUtil.urlEncode(fileName);

            if (this.logger.isDebugEnabled()) {
                this.logger.debug("urlEncodedFileName:" + fileName);
            }

            ByteArrayBody fileBody = new ByteArrayBody(IOUtils.toByteArray(inputStream), urlEncodedFileName);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart("file", fileBody);

            return this.body(MultipartEntityBuilder
                .create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addPart("file", fileBody)
                .build());
        } catch (Exception e) {
            logger.error("body error", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public RequestBuilder json(Object object) {
        try {
            this.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            String json = CoreUtil.toJson(object);

            if (this.logger.isDebugEnabled()) {
                this.logger.debug(">>> json: " + json);
            }

            return this.body(new StringEntity(json, StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public RequestBuilder cookie(String cookie) {
        this.addHeader("Cookie", cookie);
        return this;
    }

    public RequestBuilder referer(String referer) {
        this.addHeader("Referer", referer);
        return this;
    }

    public RequestBuilder userAgent(String userAgent) {
        this.request.userAgent(userAgent);
        return this;
    }

    public Response execute() {
        try {
            this.request.connectTimeout(this.latencyType.connectionTimeout);
            this.request.socketTimeout(this.latencyType.socketTimeout);
            this.headerList.forEach(this.request::addHeader);

            return
                Executor
                    .newInstance()
                    .use(new BasicCookieStore())
                    .execute(this.request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String executeAndGetCookie()  {
        try {
            HttpResponse response = this.execute().returnResponse();

            if (this.logger.isDebugEnabled()) {
                this.logger.debug(">>> content: " + EntityUtils.toString(response.getEntity()));
            }

            return Stream.of(response.getHeaders("Set-Cookie"))
                .map(header -> header.getValue())
                .map(value -> value.split(";")[0])
                .collect(Collectors.joining("; "));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int executeAndGetCode() {
        try {
            return this.execute().returnResponse().getStatusLine().getStatusCode();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void assertExecuteAndGetCode(int code) {
        assertThat(this.executeAndGetCode()).isEqualTo(code);
    }

    public String executeAndGetContent() {
        try {
            String content = EntityUtils.toString(this.execute().returnResponse().getEntity(), "UTF-8");

            if (this.logger.isDebugEnabled()) {
                this.logger.debug(">>> content: " + content);
            }

            return content;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void executeAndDownload(File file) {
        try {
            HttpEntity entity = this.execute()
                .returnResponse()
                .getEntity();

            if (entity != null) {
                InputStream in = entity.getContent();
                FileOutputStream out = new FileOutputStream(file);
                IOUtils.copy(in, out);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Payload executeAndGetPayload() {
        return this.executeAndGetObject(Payload.class);
    }

    public <T> T executeAndGetObject(Class<T> tClass) {
        return this.executeAndGetObject(tClass, null);
    }

    public <T> T executeAndGetObject(Type typeofT) {
        return this.executeAndGetObject(null, typeofT);
    }

    private <T> T executeAndGetObject(Class<T> tClass, Type typeofT) {
        try {
            FieldNamingPolicy fieldNamingPolicy = ofNullable(this.resultFieldNamePolicy)
                .orElse(this.fieldNamePolicy);

            T object = GsonHttpMessageConverter.createGsonBuilder()
                .setFieldNamingPolicy(fieldNamingPolicy)
                .create()
                .fromJson(this.executeAndGetContent(), tClass != null ? tClass : typeofT );

            if (logger.isDebugEnabled()) {
                logger.debug(">>> executeAndGetObject object: " + CoreUtil.toJson(object));
            }

            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> executeAndGetList(Class<T> tClass) {
        return executeAndGetObject(new GenericOf<>(List.class, tClass));
    }
}
