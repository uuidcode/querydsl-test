package com.github.uuidcode.querydsl.test.service;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.uuidcode.querydsl.test.entity.Book;
import com.github.uuidcode.querydsl.test.entity.QBook;
import com.github.uuidcode.querydsl.test.entity.User;

@Service
@Transactional
public class BookService extends EntityService<Book> {
    public List<Book> list(Long userId) {
        return this.createQuery()
            .select(QBook.book)
            .from(QBook.book)
            .where(QBook.book.userId.eq(userId))
            .fetch();
    }

    public List<Book> listAll() {
        return this.createQuery()
            .select(QBook.book)
            .from(QBook.book)
            .fetch();
    }

    public void manualJoin(List<User> userList)  {
        if (userList == null) {
            return;
        }

        List<Long> idList = userList.stream()
            .map(User::getUserId)
            .collect(toList());

        List<Book> bookList = this.createQuery()
            .select(QBook.book)
            .from(QBook.book)
            .where(QBook.book.userId.in(idList))
            .fetch();

        Map<Long, List<Book>> map = bookList.stream()
            .collect(groupingBy(Book::getUserId));

        userList.forEach(user -> {
            user.setBookList(map.get(user.getUserId()));
        });
    }
}
