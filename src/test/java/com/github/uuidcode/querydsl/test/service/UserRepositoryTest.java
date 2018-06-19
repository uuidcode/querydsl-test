package com.github.uuidcode.querydsl.test.service;

import java.util.stream.IntStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.uuidcode.querydsl.test.CoreTest;
import com.github.uuidcode.querydsl.test.entity.User;
import com.github.uuidcode.querydsl.test.util.CoreUtil;

public class UserRepositoryTest extends CoreTest {
    protected static Logger logger = LoggerFactory.getLogger(UserRepositoryTest.class);

    @Autowired
    private UserRepository userRepository;

    @Test
    public void test() {
        IntStream.range(0, 100)
            .boxed()
            .forEach(i -> {
                User user = User.of().setUsername(CoreUtil.createUUID());
                this.userRepository.save(user);
            });
    }
}