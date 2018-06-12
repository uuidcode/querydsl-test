package com.github.uuidcode.querydsl.test.adapter;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DateTypeAdapter extends TypeAdapter<Date> {
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
}
