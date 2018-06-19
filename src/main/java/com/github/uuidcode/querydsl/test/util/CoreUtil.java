package com.github.uuidcode.querydsl.test.util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;

import com.google.common.base.CaseFormat;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import javassist.util.proxy.MethodHandler;

public class CoreUtil {
    protected static Logger logger = LoggerFactory.getLogger(CoreUtil.class);

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
                    if (result.contains("E")) {
                        return new BigDecimal(result).longValue();
                    }

                    return Long.parseLong(result.replaceAll("\\,", "").trim(), 10);
                } catch (Exception e) {
                    return null;
                }
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }
    };

    @SuppressWarnings({"rawtypes" })
    private static TypeAdapter<Class> ClassTypeAdapter = new TypeAdapter<Class>() {
        @Override
        public void write(JsonWriter out, Class value) throws IOException {
            out.value(value.getName());
        }

        @Override
        public Class read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                String result = in.nextString();
                try {
                    return Class.forName(result);
                } catch (Exception e) {
                    return null;
                }
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
                return format.parse(result.trim());
            } catch (Exception e) {
                try {
                    FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
                    return format.parse(result.trim());
                } catch (Exception ex) {
                }
            }

            return null;
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

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(long.class, LongTypeAdapter)
            .registerTypeAdapter(Long.class, LongTypeAdapter)
            .registerTypeAdapter(String.class, StringTypeAdapter)
            .registerTypeAdapter(Class.class, ClassTypeAdapter)
            .registerTypeAdapter(Date.class, DateTypeAdapter)
            .registerTypeAdapter(PageImpl.class, new PageDeserializer(PageImpl.class))
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .addSerializationExclusionStrategy(new ExclusionStrategy() {
                Set<Class> excludedClassSet = new HashSet() {{
                    this.add(Class.class);
                    this.add(MethodHandler.class);
                }};

                @Override
                public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                    Class<?> declaredClass = fieldAttributes.getDeclaredClass();

                    if (excludedClassSet.contains(declaredClass)) {
                        return true;
                    }

                    return false;
                }

                @Override
                public boolean shouldSkipClass(Class<?> aClass) {
                    return false;
                }
            })
            .create();

    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static String wrapWithDoubleQuote(String text) {
        return wrap(text, "\"");
    }

    public static String wrapWithBrace(String text) {
        return wrap(text, leftBrace(), rightBrace());
    }

    public static String wrapWithAngleBracket(String text) {
        return wrap(text, leftAngleBracket(), rightAngleBracket());
    }

    public static String wrap(String text, String left, String right) {
        return left + text + right;
    }

    public static String wrap(String text, String left) {
        return wrap(text, left, left);
    }

    public static String toFirstCharUpperCase(String name) {
        if (isEmpty(name)) {
            return empty();
        }

        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static String toFirstCharLowerCase(String name) {
        if (isEmpty(name)) {
            return empty();
        }

        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public static List<String> splitWithUnderscore(String text) {
        return split(text, CoreUtil.underscore());
    }

    public static List<String> split(String text, String delimiter) {
        if (text == null) {
            return new ArrayList<>();
        }

        return Arrays.stream(text.split(delimiter))
            .collect(Collectors.toList());
    }

    public static String generate(String text, int size) {
        return IntStream.range(0, size)
            .mapToObj(i -> text)
            .collect(Collectors.joining());
    }

    public static String joining(List<String> list) {
        return CoreUtil.joining(list, CoreUtil.comma());
    }

    public static String joiningWithCommaAndSpace(List<String> list) {
        return CoreUtil.joining(list, CoreUtil.commaAndSpace());
    }

    public static String joining(List<String> list, String delimiter) {
        if (list == null) {
            return CoreUtil.empty();
        }

        return list.stream().collect(Collectors.joining(delimiter));
    }

    public static <E extends Enum<E>> List<E> toList(Class<E> enumClass) {
        return new ArrayList(EnumSet.allOf(enumClass));
    }

    public static void setContent(File file, String data) {
        try {
            FileUtils.writeStringToFile(file, data);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(">>> error setContent", e);
            }
        }
    }

    public static String getContent(File file) {
        try {
            return FileUtils.readFileToString(file);
        } catch (Exception e) {
            logger.error("error", e);
        }

        return empty();
    }

    public static String getContentFromResource(String name) {
        return getContent(new File(CoreUtil.class.getClassLoader().getResource(name).getFile()));
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> List<T> asList(T... array) {
        List<T> list = new ArrayList<>();

        for (T t : array) {
            list.add(t);
        }

        return list;
    }

    public static String lineSeparator() {
        return System.getProperty("line.separator");
    }

    public static String lineSeparator(int size) {
        return CoreUtil.generate(CoreUtil.lineSeparator(), size);
    }

    public static String indent(int size) {
        return CoreUtil.generate("    ", size);
    }

    public static String indent() {
        return indent(1);
    }

    public static String indent(String content) {
        if (CoreUtil.isEmpty(content)) {
            return CoreUtil.empty();
        }

        return indent() + content;
    }

    public static String commaAndSpace() {
        return CoreUtil.comma() + CoreUtil.space();
    }

    public static String comma() {
        return ",";
    }

    public static String space() {
        return " ";
    }

    public static String equal() {
        return "=";
    }

    public static String sharp() {
        return "#";
    }

    public static String underscore() {
        return "_";
    }

    public static String empty() {
        return "";
    }

    public static String semicolon() {
        return ";";
    }

    public static String slash() {
        return "/";
    }

    public static String backslash() {
        return "\\";
    }

    public static String dot() {
        return ".";
    }

    public static String leftBrace() {
        return "{";
    }

    public static String rightBrace() {
        return "}";
    }

    public static String leftParenthesis() {
        return "(";
    }

    public static String rightParenthesis() {
        return ")";
    }

    public static String leftAngleBracket() {
        return "<";
    }

    public static String rightAngleBracket() {
        return ">";
    }

    public static String underscoreToLowerCamel(String value) {
        if (value.contains(underscore())) {
            return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, value);
        }

        return CoreUtil.toFirstCharLowerCase(value);
    }

    public static String underscoreToUpperCamel(String value) {
        if (value.contains(underscore())) {
            return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, value);
        }

        return CoreUtil.toFirstCharUpperCase(value);
    }

    public static String dotToSlash(String value) {
        return value.replaceAll(backslash() + dot(), slash());
    }

    public static String getFormattedSQL(String sql) {
        String formattedSql = new BasicFormatterImpl().format(sql);
        formattedSql = formattedSql.replaceAll("\\s*LIMIT", " LIMIT");
        formattedSql = formattedSql.replaceAll("LIMIT\\s*(\\d),\\s*(\\d)", " LIMIT $1, $2");
        return formattedSql;
    }

    public static String createUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void printJson(Logger logger, Object object) {
        if (logger.isDebugEnabled()) {
            logger.debug(">>> printJson object: {}", CoreUtil.toJson(object));
        }
    }
}