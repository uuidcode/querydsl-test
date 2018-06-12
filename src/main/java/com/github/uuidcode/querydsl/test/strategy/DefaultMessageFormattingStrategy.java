package com.github.uuidcode.querydsl.test.strategy;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.uuidcode.querydsl.test.util.CoreUtil;
import com.github.uuidcode.querydsl.test.util.StringStream;
import com.github.uuidcode.querydsl.test.listener.DefaultLoggingEventListener;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

public class DefaultMessageFormattingStrategy implements MessageFormattingStrategy {
    protected static Logger logger = LoggerFactory.getLogger(DefaultMessageFormattingStrategy.class);

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql) {
        String databaseUrl = null;
        try {
            databaseUrl = DefaultLoggingEventListener.local.get().getConnection().getMetaData().getURL();
        } catch (Throwable t) {
            if (logger.isErrorEnabled()) {
                this.logger.error("error PrettySqlFormat formatMessage", t);
            }
        }

        StackTraceElement stackTraceElement = this.getDatabaseStackTraceElement();

        return StringStream.of("")
            .add(this.stackTraceElementToString(stackTraceElement))
            .add("databaseUrl: " + databaseUrl)
            .add("category: " + category)
            .add("elapsed: " + elapsed  + "ms")
            .add("formatted query: ")
            .add(CoreUtil.getFormattedSQL(sql))
            .joiningWithNewLine();
    }

    private StackTraceElement getDatabaseStackTraceElement() {
        Exception exception = new Exception();
        StackTraceElement[] stackTraceElementArray = exception.getStackTrace();

        return Arrays.stream(stackTraceElementArray)
            .filter(s -> s.getClassName().startsWith("com.github.querydsl"))
            .findFirst()
            .orElse(null);
    }

    private String stackTraceElementToString(StackTraceElement stackTraceElement) {
        if (stackTraceElement == null) {
            return null;
        }

        String className = stackTraceElement.getClassName();
        String simpleClassName = className.substring(className.lastIndexOf(".") + 1);
        String methodName = stackTraceElement.getMethodName();
        int line = stackTraceElement.getLineNumber();

        return StringStream.of().add(className)
            .add(".")
            .add(methodName)
            .add("(")
            .add(simpleClassName)
            .add(".source")
            .add(":")
            .add(line)
            .add(")")
            .joining();
    }
}