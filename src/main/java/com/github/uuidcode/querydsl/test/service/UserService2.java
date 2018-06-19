package com.github.uuidcode.querydsl.test.service;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.uuidcode.querydsl.test.entity.Book;
import com.github.uuidcode.querydsl.test.entity.QBook;
import com.github.uuidcode.querydsl.test.entity.User;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

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

    public List<User> findAllWithJoin() {
        List<User> userList = this.findAll();
        List<Long> userIdList = userList.stream()
            .map(User::getUserId)
            .collect(toList());

        List<Book> bookList = this.bookService2.findAll(QBook.book.userId.in(userIdList));
        Map<Long, List<Book>> bookListMap = bookList.stream().collect(groupingBy(Book::getUserId));

        userList.forEach(user -> {
            List<Book> currentBookList = bookListMap.get(user.getUserId());
            user.setBookList(currentBookList);
        });

        return userList;
    }
}
