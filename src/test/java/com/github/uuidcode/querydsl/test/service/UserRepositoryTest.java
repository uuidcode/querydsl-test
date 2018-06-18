package com.github.uuidcode.querydsl.test.service;

import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.data.querydsl.QSort;

import com.github.uuidcode.querydsl.test.CoreTest;
import com.github.uuidcode.querydsl.test.entity.Payload;
import com.github.uuidcode.querydsl.test.entity.QUser;
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

    @Test
    public void sort() {
        Page<User> page= this.userRepository.findAll(
            QUser.user.userId.mod(2L).eq(0L),
            new QPageRequest(0, 10, new QSort(QUser.user.username.desc())));

        Payload payload = Payload.of()
            .paging(page.getNumber(), 10, 10, 100)
            .setUserList(page.getContent());

        if (logger.isDebugEnabled()) {
            logger.debug(">>> sort payload: {}", CoreUtil.toJson(payload));
        }
    }
}