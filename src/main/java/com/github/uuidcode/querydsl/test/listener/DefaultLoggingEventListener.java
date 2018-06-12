package com.github.uuidcode.querydsl.test.listener;

import java.sql.SQLException;

import com.p6spy.engine.common.ConnectionInformation;
import com.p6spy.engine.common.Loggable;
import com.p6spy.engine.common.P6LogQuery;
import com.p6spy.engine.logging.Category;
import com.p6spy.engine.logging.LoggingEventListener;

public class DefaultLoggingEventListener extends LoggingEventListener {
    public static ThreadLocal<ConnectionInformation> local = new ThreadLocal<>();

    @Override
    protected void logElapsed(Loggable loggable, long timeElapsedNanos, Category category, SQLException e) {
        local.set(loggable.getConnectionInformation());
        P6LogQuery.logElapsed(loggable.getConnectionInformation().getConnectionId(), timeElapsedNanos, category, loggable);
        local.set(null);
    }
}
