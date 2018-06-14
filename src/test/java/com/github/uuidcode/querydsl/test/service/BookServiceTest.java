package com.github.uuidcode.querydsl.test.service;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.uuidcode.querydsl.test.CoreTest;
import com.github.uuidcode.querydsl.test.entity.Book;
import com.github.uuidcode.querydsl.test.util.CoreUtil;

public class BookServiceTest extends CoreTest {
    protected static Logger logger = LoggerFactory.getLogger(BookServiceTest.class);

    @Autowired
    private BookService bookService;

    @Test
    public void insert() {
        Book book = Book.of()
            .setName(CoreUtil.createUUID())
            .setRegDatetime(new Date())
            .setModDatetime(new Date())
            .setUserId(14L);
        this.bookService.save(book);
    }

    @Test
    public void list() {
        this.bookService.save(Book.of().setName(CoreUtil.createUUID()));
        List<Book> bookList = this.bookService.listAll();
        CoreUtil.printJson(logger, bookList);
    }
    
    @Test
    public void audit() {
        Book book = Book.of()
            .setName(CoreUtil.createUUID())
            .setRegDatetime(new Date())
            .setModDatetime(new Date());
        this.bookService.save(book);
        this.bookService.update(book.setName(CoreUtil.createUUID()));
        this.bookService.remove(book);
    }
}