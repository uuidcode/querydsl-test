package com.github.uuidcode.querydsl.test.adapter;

import java.io.IOException;

import com.github.uuidcode.querydsl.test.util.CoreUtil;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class StringTypeAdapter extends TypeAdapter<String> {
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
}
