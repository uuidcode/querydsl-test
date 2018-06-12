package com.github.uuidcode.querydsl.test.service;

import org.junit.Test;

import static org.junit.Assert.*;

public class EntityServiceTest {
    @Test
    public void test() {
        UserService userService = new UserService();
        userService.get(1L);
    }
}