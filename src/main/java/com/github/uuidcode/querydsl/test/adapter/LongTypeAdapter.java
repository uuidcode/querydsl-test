package com.github.uuidcode.querydsl.test.adapter;

import java.io.IOException;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class LongTypeAdapter extends TypeAdapter<Long> {
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
