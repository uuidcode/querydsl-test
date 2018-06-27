package com.github.uuidcode.querydsl.test.configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.github.uuidcode.querydsl.test.util.StringStream;
import com.p6spy.engine.spy.P6SpyDriver;

@Configuration
public class DatabaseConfiguration {
    protected static Logger logger = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Bean
    public DataSource dataSource() {
        if (logger.isDebugEnabled()) {
            logger.debug(">>> dataSource");
        }

        String jdbcParameter = StringStream.of()
            .add("autoReconnect=true")
            .add("useUnicode=true")
            .add("characterEncoding=utf8")
            .add("mysqlEncoding=utf8")
            .joiningWithAmpersand();

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:p6spy:mysql://127.0.0.1:3306/querydsl?" + jdbcParameter);
        dataSource.setUsername("root");
        dataSource.setPassword("rootroot");
        dataSource.setDriverClassName(P6SpyDriver.class.getName());
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}