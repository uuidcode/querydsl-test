package com.github.uuidcode.querydsl.test;

import static org.assertj.core.api.Assertions.*;

import org.hibernate.boot.model.naming.Identifier;
import org.junit.Test;

import com.github.uuidcode.querydsl.test.strategy.DefaultPhysicalNamingStrategy;

public class DefaultPhysicalNamingStrategyTest {
    @Test
    public void test() {
        DefaultPhysicalNamingStrategy strategy = new DefaultPhysicalNamingStrategy();
        assertThat(strategy.toPhysicalColumnName(Identifier.toIdentifier("User"), null).getText()).isEqualTo("user");
    }
}