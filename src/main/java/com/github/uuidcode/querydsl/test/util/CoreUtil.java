package com.github.uuidcode.querydsl.test.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;

import com.github.uuidcode.querydsl.test.configuration.BinderConfiguration;
import com.github.uuidcode.querydsl.test.converter.GsonHttpMessageConverter;
import com.github.uuidcode.querydsl.test.entity.Book;
import com.google.common.base.CaseFormat;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
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

    private static DateTimeParser[] dateTimeParsers = {
        DateTimeFormat.forPattern("yyyyMMdd").getParser(),
        DateTimeFormat.forPattern("yyyyMMddHH").getParser(),
        DateTimeFormat.forPattern("yyyyMMddHHmm").getParser(),
        DateTimeFormat.forPattern("yyyyMMddHHmmss").getParser(),

        DateTimeFormat.forPattern("yyyy-MM-dd").getParser(),
        DateTimeFormat.forPattern("yyyy-MM-dd HH").getParser(),
        DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").getParser(),
        DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").getParser(),

        DateTimeFormat.forPattern("yyyy/MM/dd").getParser(),
        DateTimeFormat.forPattern("yyyy/MM/dd HH").getParser(),
        DateTimeFormat.forPattern("yyyy/MM/dd HH:mm").getParser(),
        DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss").getParser(),

        DateTimeFormat.forPattern("yyyy.MM.dd").getParser(),
        DateTimeFormat.forPattern("yyyy.MM.dd HH").getParser(),
        DateTimeFormat.forPattern("yyyy.MM.dd HH:mm").getParser(),
        DateTimeFormat.forPattern("yyyy.MM.dd HH:mm:ss").getParser(),

        DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").getParser(),
        DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ").getParser(),
        DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ").getParser(),
        DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS").getParser(),
        DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSSZ").getParser()
    };

    private static DateTimeFormatter dateTimeFormatter =
        new DateTimeFormatterBuilder()
            .append(null, dateTimeParsers)
            .toFormatter();

    public static Date parseDateTime(String text) {
        return dateTimeFormatter.parseDateTime(text).toDate();
    }

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
        return GsonHttpMessageConverter.gson.fromJson(json, clazz);
    }

    public static String toJson(Object object) {
        return GsonHttpMessageConverter.gson.toJson(object);
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

    public static String toQueryString(FieldNamingPolicy fieldNamePolicy, Object object) {
        if (object == null) {
            return "";
        }

        return toNameValuePairList(fieldNamePolicy, object)
            .stream()
            .map(p -> p.getName() + "=" + CoreUtil.urlEncode(p.getValue()))
            .collect(Collectors.joining("&"));
    }

    public static List<NameValuePair> toNameValuePairList(Object object) {
        return toNameValuePairList(FieldNamingPolicy.IDENTITY, object);
    }

    public static List<NameValuePair> toNameValuePairList(FieldNamingPolicy fieldNamePolicy, Object object) {
        List<NameValuePair> list = new ArrayList<>();

        if (object == null) {
            return list;
        }

        if (object instanceof Map) {
            return toNameValuePairList(fieldNamePolicy, (Map<?, ?>) object);
        }

        return Arrays
            .stream(FieldUtils.getAllFields(object.getClass()))
            .filter(field -> !Modifier.isStatic(field.getModifiers()))
            .filter(field -> !Modifier.isFinal(field.getModifiers()))
            .map(field -> {
                field.setAccessible(true);

                Object currentObject = null;

                try {
                    currentObject = field.get(object);
                } catch (Exception e) {
                }

                if (currentObject != null) {
                    String value = getValue(currentObject);
                    String name = fieldNamePolicy.translateName(field);
                    return new BasicNameValuePair(name, value);
                }

                return null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private static List<NameValuePair> toNameValuePairList(FieldNamingPolicy fieldNamePolicy, Map<?, ?> object) {
        Map<?, ?> map = object;

        return map.entrySet()
                .stream()
                .map(i -> {
                    try {
                        Field field = createField(i.getKey().toString());
                        String name = fieldNamePolicy.translateName(field);
                        String value = getValue(i.getValue());
                        return new BasicNameValuePair(name, value);
                    } catch (Exception e) {
                        logger.error("error", e);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static Field createField(String name) throws Exception {
        Constructor constructor = Field.class.getDeclaredConstructor(
            Class.class, String.class, Class.class, int.class, int.class, String.class, byte[].class);
        constructor.setAccessible(true);
        return (Field) constructor.newInstance(null, name, null, 0, 0, null, new byte[0]);
    }

    private static String getValue(Object value) {
        String text;
        if (value instanceof Date) {
            BinderConfiguration.DateEditor dateEditor = new BinderConfiguration.DateEditor();
            dateEditor.setValue(value);
            text = dateEditor.getAsText();
        } else if (value instanceof Integer) {
            BinderConfiguration.IntegerEditor integerEditor = new BinderConfiguration.IntegerEditor();
            integerEditor.setValue(value);
            text = integerEditor.getAsText();
        } else if (value instanceof Long) {
            BinderConfiguration.LongEditor longEditor = new BinderConfiguration.LongEditor();
            longEditor.setValue(value);
            text = longEditor.getAsText();
        } else {
            text = value.toString();
        }
        return text;
    }

    public static String urlEncode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (Exception e) {
            logger.error("error", e);
        }

        return "";
    }


    public static String urlDecode(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (Exception e) {
            logger.error("error", e);
        }

        return "";
    }

    public static String getFormatYyyyMMdd(Date date) {
        return getFormat(date, "yyyyMMdd");
    }

    public static String getFormatYyyyHyphenMM(Date date) {
        return getFormat(date, "yyyy-MM");
    }

    public static String getFormat(Date date, String formatString) {
        if (date == null) {
            return "";
        }

        FastDateFormat format = FastDateFormat.getInstance(formatString);
        return format.format(date);
    }

    public static Long parseLong(String value) {
        try {
            return Long.parseLong(value.trim().replaceAll("\\,", ""), 10);
        } catch (Throwable t) {
            if (logger.isErrorEnabled()) {
                logger.error(">>> error CoreUtil parseLong", t);
            }
        }

        return null;
    }

    public static String getFormat(Date date) {
        if (date == null) {
            return "";
        }

        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static <T, R> List<R> map(List<T> list, Function<T, R> mapper) {
        return list.stream()
            .map(mapper)
            .collect(Collectors.toList());
    }

    public static <T, R> Map<R, List<T>> groupBy(List<T> list, Function<T, R> mapper) {
        return list.stream()
            .collect(Collectors.groupingBy(mapper));
    }

    public static <T, R> List<T> fill(List<T> list, Map<Long, List<R>> map, BiFunction<T, List<R>, T> biFunction) {
        list.forEach(i -> {
            List<R> resultList = map.get(i);
            biFunction.apply(i, resultList);
        });

        return list;
    }

    public static <T, R> List<T> fill(List<T> parentList,
                                      List<R> childList,
                                      Function<R, Long> childMapper,
                                      BiFunction<T, List<R>, T> biFunction) {
        Map<Long, List<R>> map = CoreUtil.groupBy(childList, childMapper);
        return fill(parentList, map, biFunction);
    }
}