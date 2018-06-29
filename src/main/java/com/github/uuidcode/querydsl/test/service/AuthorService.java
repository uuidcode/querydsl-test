package com.github.uuidcode.querydsl.test.service;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.uuidcode.querydsl.test.entity.Author;

@Service
public class AuthorService extends QuerydslService<Author, Long> {
    @Autowired
    public AuthorService(EntityManager entityManager) {
        super(Author.class, entityManager);
    }
}
