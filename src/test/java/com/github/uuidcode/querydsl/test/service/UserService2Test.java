package com.github.uuidcode.querydsl.test.service;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.uuidcode.querydsl.test.CoreTest;
import com.github.uuidcode.querydsl.test.entity.Book;
import com.github.uuidcode.querydsl.test.entity.QUser;
import com.github.uuidcode.querydsl.test.entity.User;
import com.github.uuidcode.querydsl.test.util.CoreUtil;

public class UserService2Test extends CoreTest {
    protected static Logger logger = LoggerFactory.getLogger(UserService2Test.class);

    @Autowired
    private UserService2 userService2;

    @Autowired
    private BookService2 bookService2;

    @Test
    public void findAll() {
        List<User> userList = this.userService2.findAll(QUser.user.userId.mod(2L).eq(0L));

        if (logger.isDebugEnabled()) {
            logger.debug(">>> test userList: {}", CoreUtil.toJson(userList));
        }
    }

    @Test
    public void findAllBySort() {
        List<User> userList = this.userService2.findAll(QUser.user.username.desc());

        if (logger.isDebugEnabled()) {
            logger.debug(">>> test userList: {}", CoreUtil.toJson(userList));
        }
    }

    @Test
    public void join() {
        List<User> userList = this.userService2.findAllWithJoin();

        if (logger.isDebugEnabled()) {
            logger.debug(">>> join userList: {}", CoreUtil.toJson(userList));
        }
    }

    @Test
    public void test() {
        User user = User.of().setUsername(CoreUtil.createUUID());
        this.userService2.save(user);

        if (logger.isDebugEnabled()) {
            logger.debug(">>> test user: {}", CoreUtil.toJson(user));
        }

        user = this.userService2.findOne(user.getUserId());

        if (logger.isDebugEnabled()) {
            logger.debug(">>> test user: {}", CoreUtil.toJson(user.getClass().getName()));
            logger.debug(">>> test user: {}", CoreUtil.toJson(user));
        }

        for (int i = 0; i < 3; i++) {
            this.bookService2.save(Book.of().setUserId(user.getUserId()).setName(CoreUtil.createUUID()));
        }

        user = this.userService2.findOneWithJoin(user.getUserId());

        if (logger.isDebugEnabled()) {
            logger.debug(">>> test user: {}", CoreUtil.toJson(user));
        }
    }
}