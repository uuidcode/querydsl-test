package com.github.uuidcode.querydsl.test.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.uuidcode.querydsl.test.entity.Book;
import com.github.uuidcode.querydsl.test.entity.Payload;
import com.github.uuidcode.querydsl.test.entity.QBook;
import com.github.uuidcode.querydsl.test.entity.User;
import com.github.uuidcode.querydsl.test.util.CoreUtil;
import com.querydsl.core.types.Predicate;

@Service
public class UserService2 extends QuerydslService<User, Long> {
    @Autowired
    private BookService2 bookService2;

    @Autowired
    public UserService2(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    public User findOneWithJoin(Long id) {
        User user = this.findOne(id);
        List<Book> bookList = this.bookService2.findAll(QBook.book.userId.eq(id));
        return user.setBookList(bookList);
    }

    public Payload findAllWithJoin(Predicate predicate, Pageable pageable) {
        Page<User> userPage = this.findAll(predicate, pageable);
        List<User> userList = userPage.getContent();
        List<Long> userIdList = CoreUtil.map(userList, User::getUserId);
        List<Book> bookList = this.bookService2.findAll(QBook.book.userId.in(userIdList));
        CoreUtil.fill(userList, bookList, Book::getUserId, User::setBookList);
        return Payload.of(userPage).setUserList(userList);
    }
}
