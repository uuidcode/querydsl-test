package com.github.uuidcode.querydsl.test.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.uuidcode.querydsl.test.entity.Book;
import com.github.uuidcode.querydsl.test.entity.Cache;
import com.github.uuidcode.querydsl.test.entity.Payload;
import com.github.uuidcode.querydsl.test.entity.QBook;
import com.github.uuidcode.querydsl.test.entity.QUser;
import com.github.uuidcode.querydsl.test.entity.User;
import com.querydsl.core.types.Predicate;

import static com.github.uuidcode.querydsl.test.util.CoreUtil.fill;
import static com.github.uuidcode.querydsl.test.util.CoreUtil.map;

@Service
public class UserService extends QuerydslService<User, Long> {
    @Autowired
    private BookService bookService;

    @Autowired
    public UserService(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    @Cacheable(cacheNames = Cache.USER)
    public Payload findOne(Long userId) {
        User user = this.findOne(QUser.user.userId.eq(userId))
            .orElse(null);
        return Payload.of().setUser(user);
    }

    public User findOneWithJoin(Long id) {
        User user = this.findOne(QUser.user.userId.eq(id)).orElse(null);
        List<Book> bookList = this.bookService.findAll(QBook.book.userId.eq(id));
        return user.setBookList(bookList);
    }

    @Cacheable(cacheNames = Cache.USER)
    public Payload findAllWithJoin(Predicate predicate, Pageable pageable) {
        Page<User> userPage = this.findAll(predicate, pageable);
        List<User> userList = userPage.getContent();
        List<Long> userIdList = map(userList, User::getUserId);
        List<Book> bookList = this.bookService.findAll(QBook.book.userId.in(userIdList));
        fill(userList, bookList, Book::getUserId, User::setBookList);
        return Payload.of(userPage).setUserList(userList);
    }

    @CacheEvict(cacheNames = Cache.USER, allEntries = true)
    public Payload evict() {
        return Payload.of();
    }
}
