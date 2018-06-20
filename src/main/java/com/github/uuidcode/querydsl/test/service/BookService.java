package com.github.uuidcode.querydsl.test.service;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.uuidcode.querydsl.test.entity.Book;

@Service
public class BookService extends QuerydslService<Book, Long> {
    @Autowired
    public BookService(EntityManager entityManager) {
        super(Book.class, entityManager);
    }
}
