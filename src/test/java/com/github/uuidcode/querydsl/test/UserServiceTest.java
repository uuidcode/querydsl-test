package com.github.uuidcode.querydsl.test;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.uuidcode.querydsl.test.entity.Book;
import com.github.uuidcode.querydsl.test.entity.User;
import com.github.uuidcode.querydsl.test.entity.UserAuthority;
import com.github.uuidcode.querydsl.test.service.BookService;
import com.github.uuidcode.querydsl.test.service.UserAuthorityService;
import com.github.uuidcode.querydsl.test.service.UserService;
import com.github.uuidcode.querydsl.test.util.CoreUtil;

public class UserServiceTest extends CoreTest {
    protected static Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthorityService userAuthorityService;

    @Autowired
    private BookService bookService;

    @Test
    public void nullTest() {
        User user = this.userService.get(null);

        if (logger.isDebugEnabled()) {
            logger.debug(">>> nullTest user: {}", CoreUtil.toJson(user));
        }
    }

    @Test
    public void notExistsTest() {
        User user = this.userService.get(-1L);

        if (logger.isDebugEnabled()) {
            logger.debug(">>> notExistsTest user: {}", CoreUtil.toJson(user));
        }
    }

    @Test
    public void existsTest() {
        User user = this.userService.get(14L);

        if (logger.isDebugEnabled()) {
            logger.debug(">>> existsTest user: {}", CoreUtil.toJson(user));
        }
    }

    @Test
    public void save() {
        User user = this.userService.save(User.of().setUsername("CCC"));

        if (logger.isDebugEnabled()) {
            logger.debug(">>> save user: {}", CoreUtil.toJson(user));
        }
    }

    @Test
    public void remove() {
        User user = this.userService.save(User.of().setUsername(CoreUtil.createUUID()));
        this.userService.remove(user.getUserId());

        if (logger.isDebugEnabled()) {
            logger.debug(">>> remove user: {}", CoreUtil.toJson(user));
        }
    }

    @Test
    public void update() {
        User user = User.of();
        this.userService.save(user);

        if (logger.isDebugEnabled()) {
            logger.debug(">>> update user: {}", CoreUtil.toJson(user));
        }

        this.userService.update(user.setUsername(CoreUtil.createUUID()));

        if (logger.isDebugEnabled()) {
            logger.debug(">>> update user: {}", CoreUtil.toJson(user));
        }
    }

    @Test
    public void update2() {
        User user = this.userService.get(26L);
        user.setUsername(String.valueOf(System.currentTimeMillis()));
        this.userService.update(user);
    }

    @Test
    public void list() {
        List<User> list = this.userService.list();
        CoreUtil.printJson(logger, list);
    }

    @Test
    public void join() {
        List<User> list = this.userService.list();
        this.userAuthorityService.join(list);
        this.bookService.join(list);
        CoreUtil.printJson(logger, list);
    }

    @Test
    public void manualJoin() {
        List<User> list = this.userService.list();
        this.userAuthorityService.manualJoin(list);
        this.bookService.manualJoin(list);
        CoreUtil.printJson(logger, list);
    }

    @Test
    public void join2() {
        List<User> userList = this.userService.list()
            .stream()
            .map(user -> {
                List<UserAuthority> userAuthorityList = this.userAuthorityService.list(user.getUserId());
                user.setUserAuthorityList(userAuthorityList);

                List<Book> bookList = this.bookService.list(user.getUserId());
                user.setBookList(bookList);
                return user;
            })
            .collect(Collectors.toList());

        if (logger.isDebugEnabled()) {
            logger.debug(">>> join2 userList: {}", CoreUtil.toJson(userList));
        }
    }
}