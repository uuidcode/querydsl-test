package com.github.uuidcode.querydsl.test.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.uuidcode.querydsl.test.resolver.Query;
import com.github.uuidcode.querydsl.test.service.PostService;

@Configuration
public class GraphqlConfiguration {
    protected static Logger logger = LoggerFactory.getLogger(GraphqlConfiguration.class);

    @Autowired
    private PostService postService;

    @Bean
    public Query query() {
        if (logger.isDebugEnabled()) {
            logger.debug(">>> query");
        }

        return new Query(this.postService);
    }
 }
