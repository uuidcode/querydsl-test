package com.github.uuidcode.querydsl.test.service;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.data.querydsl.QSort;

import com.github.uuidcode.querydsl.test.CoreTest;
import com.github.uuidcode.querydsl.test.entity.Book;
import com.github.uuidcode.querydsl.test.entity.Payload;
import com.github.uuidcode.querydsl.test.entity.QUser;
import com.github.uuidcode.querydsl.test.entity.User;
import com.github.uuidcode.querydsl.test.util.CoreUtil;
import com.querydsl.core.types.dsl.BooleanExpression;

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
            Book book = Book.of().setUserId(user.getUserId()).setName(CoreUtil.createUUID());
            this.bookService2.save(book);
        }

        user = this.userService2.findOneWithJoin(user.getUserId());

        if (logger.isDebugEnabled()) {
            logger.debug(">>> test user: {}", CoreUtil.toJson(user));
        }
    }

    @Test
    public void getTest() {
        User user = this.userService2.getOne(1L);

        if (logger.isDebugEnabled()) {
            logger.debug(">>> getTest user: {}", CoreUtil.toJson(user));
        }

        Long userId = user.getUserId();

        if (logger.isDebugEnabled()) {
            logger.debug(">>> getTest userId: {}", CoreUtil.toJson(userId));
            logger.debug(">>> getTest user: {}", CoreUtil.toJson(user));
        }
    }

    @Test
    public void save() {
        for (int i = 0; i < 100; i++) {
            this.userService2.save(User.of().setUsername(CoreUtil.createUUID()));
        }
    }

    @Test
    public void sort() {
        int pageNumber = 1;
        QPageRequest pageable = new QPageRequest(pageNumber - 1, 10, new QSort(QUser.user.username.desc()));
        BooleanExpression predicate = QUser.user.userId.mod(2L).eq(0L);

        Page<User> page= this.userService2.findAll(predicate, pageable);

        Payload payload = Payload.of()
            .paging(page.getNumber() + 1, 10L, 10L, page.getTotalElements())
            .setUserList(page.getContent());


        if (logger.isDebugEnabled()) {
            logger.debug(">>> sort payload: {}", CoreUtil.toJson(payload));
        }
    }

    @Test
    public void payload() {
        QPageRequest pageable = new QPageRequest(0, 10, new QSort(QUser.user.userId.asc()));

        Payload payload = this.userService2.findAllWithJoin(null, pageable);

        if (logger.isDebugEnabled()) {
            logger.debug(">>> payload payload: {}", CoreUtil.toJson(payload));
        }
    }
}