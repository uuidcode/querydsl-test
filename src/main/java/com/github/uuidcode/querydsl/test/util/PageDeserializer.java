package com.github.uuidcode.querydsl.test.util;

import java.lang.reflect.Type;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class PageDeserializer implements JsonDeserializer<PageImpl> {
    Type contentType;

    public PageDeserializer(Type contentType) {
        this.contentType = contentType;
    }

    public PageImpl deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        JsonArray content = obj.getAsJsonArray("content");
        List contentList = context.deserialize(content, contentType);
        PageImpl page = new PageImpl(contentList);
        return page;
    }

}