package com.github.uuidcode.querydsl.test.entity;

import java.lang.reflect.Field;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MetaEntityTest {
    @Test
    public void idField() {
        Field idField = MetaEntity.getIdField(User.class);
        assertThat(idField.getName()).isEqualTo("userId");
    }

}