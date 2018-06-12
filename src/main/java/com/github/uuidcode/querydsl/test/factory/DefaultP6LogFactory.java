package com.github.uuidcode.querydsl.test.factory;

import com.github.uuidcode.querydsl.test.listener.DefaultLoggingEventListener;
import com.p6spy.engine.event.JdbcEventListener;
import com.p6spy.engine.logging.P6LogOptions;
import com.p6spy.engine.spy.P6Factory;
import com.p6spy.engine.spy.P6LoadableOptions;
import com.p6spy.engine.spy.option.P6OptionsRepository;

/**
 * spy.properties에 설정되어 있습니다.
 * https://github.com/p6spy/p6spy/blob/master/docs/configandusage.md
 */
public class DefaultP6LogFactory implements P6Factory {
    @Override
    public JdbcEventListener getJdbcEventListener() {
        return new DefaultLoggingEventListener();
    }

    @Override
    public P6LoadableOptions getOptions(P6OptionsRepository optionsRepository) {
        return new P6LogOptions(optionsRepository);
    }
}