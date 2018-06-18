package com.github.uuidcode.querydsl.test.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.uuidcode.querydsl.test.entity.Book;
import com.github.uuidcode.querydsl.test.entity.QBook;
import com.github.uuidcode.querydsl.test.entity.User;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

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
            List<Book> currentBookList = map.get(user.getUserId());
            user.setBookList(currentBookList);
        });
    }
}
