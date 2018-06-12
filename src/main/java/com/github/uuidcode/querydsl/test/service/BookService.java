package com.github.uuidcode.querydsl.test.service;

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
    public List<Book> listAll() {
        return this.createQuery()
            .select(QBook.book)
            .from(QBook.book)
            .fetch();
    }

    public void set(List<User> userList)  {
        if (userList == null) {
            return;
        }

        Map<Long, User> userMap = userList.stream()
            .collect(Collectors.toMap(User::getUserId, Function.identity()));

        List<Long> idList = userList.stream()
            .map(User::getUserId)
            .collect(Collectors.toList());

        List<Book> bookList = this.createQuery()
            .select(QBook.book)
            .from(QBook.book)
            .where(QBook.book.userId.in(idList))
            .fetch();

        bookList
            .forEach(book -> {
                User user = userMap.get(book.getUserId());

                if (user != null) {
                    user.addBook(book);
                }
            });
    }
}
